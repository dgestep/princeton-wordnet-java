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

import java.util.Map;
import java.util.Set;

/**
 * Represents an english word dictionary which allows for the retrieval of a word, its definition, and all synonyms
 * and any related words.
 */
public interface WordDictionary {
    /**
     * Returns a list of all words in the dictionary.
     *
     * @return the set of words.
     */
    Set<String> getAllWords();

    /**
     * Returns the supplied word information from the dictionary.
     *
     * @param word the word to lookup.
     * @return the Word information or null if not found.
     */
    Word lookup(String word);

    /**
     * Returns a Set of synonyms associated with the supplied word.
     *
     * @param word                the word.
     * @param includeSupplemental supply true to include synonyms from additional sources other than wordnet.
     * @param specificTypes       an optional argument, that if supplied, will filter the returned words to only those that
     *                            match the supplied array of definition types.
     * @return the list or empty set if none exist.
     */
    Set<String> getAllSynonyms(String word, boolean includeSupplemental, DefinitionType... specificTypes);

    /**
     * Returns a Set of words considered "related to" the supplied word.
     *
     * @param word          the word.
     * @param specificTypes an optional argument, that if supplied, will filter the returned words to only those that
     *                      match the supplied array of definition types.
     * @return the list or empty set if none exist.
     */
    Set<String> getAllRelated(String word, DefinitionType... specificTypes);

    /**
     * Returns a Map of synonyms associated with the supplied word broken up by definition type.
     *
     * @param word                the word.
     * @param includeSupplemental supply true to include synonyms from additional sources other than wordnet.
     * @param specificTypes       an optional argument, that if supplied, will filter the returned words to only
     *                            those that match the supplied array of definition types.
     * @return the map or empty map if none exist.
     */
    Map<DefinitionType, Set<String>> getSynonyms(String word, boolean includeSupplemental,
                                                 DefinitionType... specificTypes);
}
