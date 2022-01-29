/*
 Copyright (C). Estep Software Forensics -- All Rights Reserved.
 Copyright Registration Number: TXU002159309.

 This file is part of the Tag My Code application.

 This application is protected under copyright laws and cannot be used, distributed, or copied without prior written
 consent from Estep Software Forensics.  Unauthorized distribution or use is strictly prohibited and punishable by
 domestic and international law.

 Proprietary and confidential.
 */
package com.estep.princeton.wordnet;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * <p>
 * An english word dictionary which allows for the retrieval of a word, its definition, and all synonyms
 * and any related words.
 * </p>
 * <p>
 * This implementation uses the data from the Princeton WordNet database as its primary source of the words. Taken
 * directly from their website, "WordNet is a large lexical database of English words. Nouns, verbs, adjectives
 * and adverbs are grouped into sets of cognitive synonyms (synsets), each expressing a distinct concept". The
 * WordNet web site exposes their internal data set files. Those data set files are contained internally within
 * this project (resources/data.adj, resources/data.adv, resources/data.noun, and resources/data.verb). This
 * implementation loads and parses the WordNet data into internal maps, keyed by the word itself and valued by the
 * metadata about the word. See the {@link Word} class for more details regarding the metadata.
 * </p>
 * <p>
 * This implementation supplements the data from the synonym WordNet data set with additional synonyms collected
 * from other online sources. Along with that, an additional data set containing related words to each word from
 * WordNet is also compiled. These additional files are contained internally within this project (resources/data.syns
 * and resources/data.related). Call the {@link #getAllSynonyms(String, boolean, DefinitionType...)} method passing
 * true to the boolean argument to retrieve all synonyms from both WordNet and the additional sources. Call the
 * {@link #getAllRelated(String, DefinitionType...)} method to get a set of related words associated with the supplied
 * word.
 * </p>
 * <p>
 * This class is a Spring bean class that is configured to only create one instance in the JVM. At first access, the
 * Spring libraries will create an instance of this class, and the WordNet and subsequent datasets are loaded and
 * cached into memory. The data loading process will result in slower instantiation time. Additional usages to
 * this class will access the instance already created and use the cached datasets.
 * </p>
 */
@Service
public class WordDictionaryImpl implements WordDictionary {
    private static final int WORD_COUNT = 3;
    private static final int WORD = 4;
    private Map<String, Word> dictionary;
    private Map<String, Set<String>> additionalSynonyms;
    private Map<String, Set<String>> additionalRelated;

    @Override
    public Set<String> getAllWords() {
        loadDictionary();
        return this.dictionary.keySet();
    }

    @Override
    public Word lookup(String word) {
        loadDictionary();

        Word wrd;
        if (word == null) {
            wrd = null;
        } else {
            word = word.toLowerCase(Locale.ROOT);
            wrd = dictionary.get(word);
        }
        return wrd;
    }

    @Override
    public Set<String> getAllSynonyms(String word, boolean includeSupplemental, DefinitionType... specificTypes) {
        loadDictionary();

        Set<String> synonyms = new TreeSet<>();
        word = word.toLowerCase(Locale.ROOT);
        Word entry = lookup(word);
        if (entry == null) {
            return synonyms;
        }

        List<Definition> definitions = entry.getDefinitions();
        if (definitions == null) {
            definitions = new ArrayList<>();
        }
        definitions.forEach(d -> synonyms.addAll(d.getSynonyms()));

        if (includeSupplemental) {
            Set<String> additionalSyns = additionalSynonyms.get(word);
            if (additionalSyns != null) {
                synonyms.addAll(additionalSyns);
            }
        }

        return reduceSetToSpecificTypes(synonyms, specificTypes);
    }

    /**
     * Reduces the supplied set of words to only those words that have a definition type matching the supplied types.
     *
     * @param words         the words to evaluate.
     * @param specificTypes the definition types to look for.
     * @return a new possibly reduced set of the words.
     */
    private Set<String> reduceSetToSpecificTypes(Set<String> words, DefinitionType... specificTypes) {
        if (specificTypes == null || specificTypes.length == 0) {
            return words;
        }

        Set<String> copyOfWords = new HashSet<>(words);
        Set<String> removeWords = new HashSet<>();
        for (String word : copyOfWords) {
            Word wrd = this.lookup(word);
            if (wrd == null) {
                removeWords.add(word);
            } else {
                for (DefinitionType definitionType : specificTypes) {
                    if (isMissingDefinitionType(wrd.getDefinitions(), definitionType)) {
                        removeWords.add(wrd.getWord());
                    }
                }
            }
        }
        copyOfWords.removeAll(removeWords);
        return copyOfWords;
    }

    /**
     * Returns true if the supplied list of definitions does NOT contain at least one type matching the supplied
     * definition type.
     *
     * @param definitions    the list of definitions.
     * @param definitionType the definition type to search for.
     * @return true if the supplied definition type is not found within the supplied list of definitions.
     */
    private boolean isMissingDefinitionType(List<Definition> definitions, DefinitionType definitionType) {
        return definitions.stream()
                .noneMatch(d -> d.getDefinitionType() == definitionType);
    }

    @Override
    public Set<String> getAllRelated(String word, DefinitionType... specificTypes) {
        loadDictionary();

        word = word.toLowerCase(Locale.ROOT);
        Set<String> words = additionalRelated.get(word);
        if (words == null) {
            words = new HashSet<>();
        } else {
            words = reduceSetToSpecificTypes(words, specificTypes);
        }
        return words;
    }

    @Override
    public Map<DefinitionType, Set<String>> getSynonyms(String word, boolean includeSupplemental,
                                                        DefinitionType... specificTypes) {
        Map<DefinitionType, Set<String>> synonyms = new HashMap<>();

        word = word.toLowerCase(Locale.ROOT);
        Word entry = lookup(word);
        if (entry == null) {
            return synonyms;
        }

        List<Definition> definitions = entry.getDefinitions();
        if (definitions == null) {
            definitions = new ArrayList<>();
        }

        for (Definition definition : definitions) {
            List<String> definitionSynonyms = definition.getSynonyms();
            if (definitionSynonyms.size() > 0) {
                // if the definition has synonyms defined, then add them to the returned map
                Set<String> syn = synonyms.computeIfAbsent(definition.getDefinitionType(), k -> new HashSet<>());
                syn.addAll(definitionSynonyms);
            }
        }

        if (includeSupplemental) {
            addSupplementalWordsToMap(word, synonyms);
        }

        if (specificTypes != null && specificTypes.length > 0) {
            // remove the types not asked for
            Set<DefinitionType> synonymTypes = new HashSet<>(synonyms.keySet());
            for (DefinitionType definitionType : synonymTypes) {
                if (Arrays.stream(specificTypes).noneMatch(t -> t == definitionType)) {
                    synonyms.remove(definitionType);
                }
            }
        }

        return synonyms;
    }

    /**
     * Retrieves the supplemental synonym words and adds them to the supplied map.
     *
     * @param word    the word to lookup.
     * @param wordMap the map to add to .
     */
    private void addSupplementalWordsToMap(String word, Map<DefinitionType, Set<String>> wordMap) {
        Set<String> words = this.additionalSynonyms.get(word);
        if (words == null) {
            return;
        }

        for (String wrd : words) {
            Word lookup = lookup(wrd);
            if (lookup == null) {
                continue;
            }

            for (Definition definition : lookup.getDefinitions()) {
                Set<String> syn = wordMap.computeIfAbsent(definition.getDefinitionType(),
                        k -> new HashSet<>());
                syn.add(lookup.getWord());
            }
        }
    }

    /**
     * Initializes this class by importing the data from the princeton dictionary files.
     */
    private void loadDictionary() {
        if (dictionary != null) {
            return;
        }

        synchronized (this) {
            if (dictionary != null) {
                return;
            }
            dictionary = new HashMap<>();

            load("/data.verb", DefinitionType.VERB);
            load("/data.noun", DefinitionType.NOUN);
            load("/data.adv", DefinitionType.ADVERB);
            load("/data.adj", DefinitionType.ADJECTIVE);

            additionalSynonyms = loadAdditional("/data.syns");
            additionalRelated = loadAdditional("/data.related");
        }
    }

    /**
     * Loads an internal map containing the words obtained from the additional sources other than wordnet.
     *
     * @param fileName the file containing the words to load.
     * @return a map keyed by the word and valued by a set containing the related words.
     */
    private Map<String, Set<String>> loadAdditional(String fileName) {
        URL u = getClass().getResource(fileName);
        if (u == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }

        try (InputStream in = u.openStream(); Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            Map<String, Set<String>> map = new TreeMap<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(line, "=");
                String word = tokenizer.nextToken().trim().toLowerCase(Locale.ROOT);

                String words = tokenizer.nextToken();
                Set<String> wordSet = new HashSet<>();
                StringTokenizer synTokens = new StringTokenizer(words, "\t");
                while (synTokens.hasMoreTokens()) {
                    String syn = synTokens.nextToken().trim();
                    wordSet.add(syn);
                }
                map.put(word, wordSet);
            }
            return map;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Imports the data from the supplied princeton dictionary file.
     *
     * @param fileName the file name of the file to import.
     * @param type     the type of definition data being imported.
     */
    private void load(String fileName, DefinitionType type) {
        URL u = getClass().getResource(fileName);
        if (u == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }

        try (InputStream in = u.openStream(); Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("  ")) {
                    addLineToDictionary(line, type);
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Parses the supplied line and extracts out the key information to store.
     *
     * @param line the line of data read from the princeton dictionary file.
     * @param type the type of definition data being added.
     */
    private void addLineToDictionary(String line, DefinitionType type) {
        // each part of the line is separated by a space
        String[] tokens = line.split(" ");

        // the word is found at the 4th index. spaces in the word are denoted by an underscore character.
        int idx = WORD;
        String word = tokens[idx].replace("_", " ");
        word = word.trim().toLowerCase(Locale.ROOT);

        // the word count is found at the 3rd index. it is a hexadecimal number that needs to be converted to a long.
        BigInteger bi = new BigInteger(tokens[WORD_COUNT], 16);
        int wordCount = bi.intValue();

        // the remaining words are the synonyms.
        List<String> syns = new ArrayList<>();
        for (int i = 1; i < wordCount; i++) {
            idx += 2;
            String synonym = tokens[idx].replace("_", " ").trim().toLowerCase(Locale.ROOT);
            syns.add(synonym);
        }

        // the word definitions are found after the pipe character
        idx = line.indexOf("|");
        String definitionLine = line.substring(idx).trim();
        List<String> usages = new ArrayList<>();

        String definition;
        idx = definitionLine.indexOf(";");
        if (idx < 0) {
            definition = definitionLine.substring(1);
        } else {
            definition = definitionLine.substring(1, idx).trim();
            populateUsages(definitionLine, idx, usages);
        }

        // add the word to the dictionary if not already in the dictionary
        Word wordEntry = dictionary.get(word);
        if (wordEntry == null) {
            wordEntry = new Word(word);
            dictionary.put(word, wordEntry);
        }

        // create the definition for the word/type and add the synonyms and usages to the definition
        Definition definitionEntry = new Definition(word, type);
        definitionEntry.setDefinition(definition);
        definitionEntry.getSynonyms().addAll(syns);
        definitionEntry.getUsages().addAll(usages);

        // associate the definition to the word in the dictionary
        wordEntry.getDefinitions().add(definitionEntry);
    }

    /**
     * Populates the list of usages from the supplied line of data read from the princeton file.
     *
     * @param definitionLine the line containing the usage data read from the princeton file.
     * @param lastIdx        the index of the last read usage statement.
     * @param usages         the list of usages to populate.
     */
    private void populateUsages(String definitionLine, int lastIdx, List<String> usages) {
        int startIdx = lastIdx + 1;
        int idx = definitionLine.indexOf(";", startIdx);
        while (idx >= 0) {
            String usage = definitionLine.substring(startIdx, idx).trim();
            usages.add(usage);

            startIdx = idx + 1;
            idx = definitionLine.indexOf(";", startIdx);
            String nextUsage = definitionLine.substring(startIdx);
            if (idx < 0 && nextUsage.length() > 0) {
                usages.add(nextUsage);
            }
        }
    }
}
