/*
 Copyright (C) 2019. Doug Estep -- All Rights Reserved.
 Copyright Registration Number: TXU002159309.
 
 This file is part of the Tag My Code application.
  
 This application is protected under copyright laws and cannot be used, distributed, or copied without prior written 
 consent from Doug Estep.  Unauthorized distribution or use is strictly prohibited and punishable by domestic and 
 international law.
 
 Proprietary and confidential.
 */
package com.estep.princeton.wordnet;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents the dictionary.
 *
 * @author Doug Estep
 */
@Service
public class WordDictionaryImpl implements WordDictionary {
    private static final int WORD_COUNT = 3;
    private static final int WORD = 4;
    private Map<String, Word> dictionary;

    @Override
    public Word lookup(String word) {
        loadDictionary();
        return dictionary.get(word);
    }

    @Override
    public Set<String> getAllSynonyms(String word) {
        Set<String> synonyms = new TreeSet<>();
        Word entry = lookup(word);
        if (entry == null) {
            return synonyms;
        }
        List<Definition> definitions = entry.getDefinitions();
        if (CollectionUtils.isEmpty(definitions)) {
            return synonyms;
        }
        definitions.forEach(d -> synonyms.addAll(d.getSynonyms()));
        return synonyms;
    }

    @Override
    public Map<DefinitionType, Set<String>> getSynonyms(String word) {
        Map<DefinitionType, Set<String>> synonyms = new HashMap<>();
        Word entry = lookup(word);
        if (entry == null) {
            return synonyms;
        }
        List<Definition> definitions = entry.getDefinitions();
        if (CollectionUtils.isEmpty(definitions)) {
            return synonyms;
        }
        definitions.forEach(d -> {
            if (d.getSynonyms().size() > 0) {
                Set<String> syn = synonyms.computeIfAbsent(d.getDefinitionType(), k -> new HashSet<>());
                syn.addAll(d.getSynonyms());
            }
        });
        return synonyms;
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

        // the word count is found at the 3rd index. it is a hexadecimal number that needs to be converted to a long.
        BigInteger bi = new BigInteger(tokens[WORD_COUNT], 16);
        int wordCount = bi.intValue();

        // the remaining words are the synonyms.
        List<String> syns = new ArrayList<>();
        for (int i = 1; i < wordCount; i++) {
            idx += 2;
            syns.add(tokens[idx].replace("_", " "));
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
            if (idx < 0 && StringUtils.isNotEmpty(nextUsage)) {
                usages.add(nextUsage);
            }
        }
    }
}
