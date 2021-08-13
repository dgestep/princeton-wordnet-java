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

import com.estep.princeton.BaseTest;
import org.apache.commons.collections4.SetUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordDictionaryTest extends BaseTest {
    @Resource
    private WordDictionary wordDictionary;

    @Test
    public void testLookupFound() {
        Word word = wordDictionary.lookup("show");
        Assertions.assertNotNull(word);
        List<Definition> definitions = word.getDefinitions();
        Assertions.assertEquals(7, definitions.size());
        int verbCount = 0;
        int nounCount = 0;
        for (Definition definition : definitions) {
            if (definition.getDefinitionType() == DefinitionType.VERB) {
                verbCount++;
                if (definition.getDefinition().startsWith("be ore become")) {
                    Assertions.assertEquals(1, definition.getSynonyms().size());
                    Assertions.assertEquals(2, definition.getUsages().size());
                }
            }
            if (definition.getDefinitionType() == DefinitionType.NOUN) {
                nounCount++;
            }
        }
        Assertions.assertEquals(5, verbCount);
        Assertions.assertEquals(2, nounCount);
    }

    @Test
    public void testLookupNotFound() {
        Word word = wordDictionary.lookup("xxxx");
        Assertions.assertNull(word);
    }

    @Test
    public void testLookupNullWord() {
        Word word = wordDictionary.lookup(null);
        Assertions.assertNull(word);
    }

    @Test
    public void testLookupEmptyWord() {
        Word word = wordDictionary.lookup("");
        Assertions.assertNull(word);
    }

    @Test
    public void testGetSynonyms() {
        Map<DefinitionType, Set<String>> synonyms = wordDictionary.getSynonyms("show");
        Assertions.assertNotNull(synonyms);
        Assertions.assertEquals(1, synonyms.size());
        Set<String> syns = synonyms.get(DefinitionType.VERB);
        Assertions.assertEquals(5, syns.size());
        Assertions.assertEquals(syns, SetUtils.hashSet("show up", "demonstrate", "exhibit", "present", "demo"));
    }

    @Test
    public void testGetAllSynonyms() {
        Set<String> synonyms = wordDictionary.getAllSynonyms("show");
        Assertions.assertNotNull(synonyms);
        Assertions.assertEquals(5, synonyms.size());
        Assertions.assertEquals(synonyms, SetUtils.hashSet("show up", "demonstrate", "exhibit", "present", "demo"));
    }
}
