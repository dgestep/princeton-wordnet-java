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

import java.util.Map;
import java.util.Set;

/**
 * Represents a class serving as a dictionary.
 */
public interface WordDictionary {
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
     * @param word the word.
     * @return the list or empty list if none exist.
     */
    Set<String> getAllSynonyms(String word);

    /**
     * Returns a Map of synonyms associated with the supplied word broken up by definition type.
     *
     * @param word the word.
     * @return the map or empty map if none exist.
     */
    Map<DefinitionType, Set<String>> getSynonyms(String word);
}
