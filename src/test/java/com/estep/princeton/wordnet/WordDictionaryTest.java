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

import com.estep.princeton.BaseTest;
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
    public void testLookupFoundCaseNoMatter() {
        Word word = wordDictionary.lookup("ShOW");
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
    public void testGetSynonymsNoSpecificTypes() {
        Map<DefinitionType, Set<String>> synonyms = wordDictionary.getSynonyms("show", true);
        Assertions.assertNotNull(synonyms);
        Assertions.assertEquals(3, synonyms.size());
        Assertions.assertEquals(14, synonyms.get(DefinitionType.VERB).size());
        Assertions.assertEquals(22, synonyms.get(DefinitionType.NOUN).size());
        Assertions.assertEquals(2, synonyms.get(DefinitionType.ADJECTIVE).size());
    }

    @Test
    public void testGetSynonymsNoSpecificTypesCaseNoMatter() {
        Map<DefinitionType, Set<String>> synonyms = wordDictionary.getSynonyms("SHOw", true);
        Assertions.assertNotNull(synonyms);
        Assertions.assertEquals(3, synonyms.size());
        Assertions.assertEquals(14, synonyms.get(DefinitionType.VERB).size());
        Assertions.assertEquals(22, synonyms.get(DefinitionType.NOUN).size());
        Assertions.assertEquals(2, synonyms.get(DefinitionType.ADJECTIVE).size());
    }

    @Test
    public void testGetSynonymsNounAndVerbTypes() {
        Map<DefinitionType, Set<String>> synonyms = wordDictionary.getSynonyms("show", true,
                DefinitionType.NOUN, DefinitionType.VERB);
        Assertions.assertNotNull(synonyms);
        Assertions.assertEquals(2, synonyms.size());
        Assertions.assertEquals(14, synonyms.get(DefinitionType.VERB).size());
        Assertions.assertEquals(22, synonyms.get(DefinitionType.NOUN).size());
    }

    @Test
    public void testGetNounVerbRelated() {
        Set<String> related = wordDictionary.getAllRelated("show", DefinitionType.NOUN, DefinitionType.VERB);
        Assertions.assertNotNull(related);
        Assertions.assertEquals(21, related.size());
    }

    @Test
    public void testGetNounVerbRelatedCaseNoMatter() {
        Set<String> related = wordDictionary.getAllRelated("shOW", DefinitionType.NOUN, DefinitionType.VERB);
        Assertions.assertNotNull(related);
        Assertions.assertEquals(21, related.size());
    }

    @Test
    public void testGetAllRelated() {
        Set<String> related = wordDictionary.getAllRelated("show");
        Assertions.assertNotNull(related);
        Assertions.assertEquals(77, related.size());
    }

    @Test
    public void testGetNounAndVerbSynonymsWithSupplemental() {
        Set<String> synonyms = wordDictionary.getAllSynonyms("show", true,
                DefinitionType.NOUN, DefinitionType.VERB);
        Assertions.assertNotNull(synonyms);
        Assertions.assertEquals(10, synonyms.size());
    }

    @Test
    public void testGetNounAndVerbSynonymsWithSupplementalCaseNoMatter() {
        Set<String> synonyms = wordDictionary.getAllSynonyms("SHOW", true,
                DefinitionType.NOUN, DefinitionType.VERB);
        Assertions.assertNotNull(synonyms);
        Assertions.assertEquals(10, synonyms.size());
    }

    @Test
    public void testGetAllSynonymsWithSupplemental() {
        Set<String> synonyms = wordDictionary.getAllSynonyms("show", true);
        Assertions.assertNotNull(synonyms);
        Assertions.assertEquals(31, synonyms.size());
    }

    @Test
    public void testGetAllSynonymsNoSupplemental() {
        Set<String> synonyms = wordDictionary.getAllSynonyms("show", false);
        Assertions.assertNotNull(synonyms);
        Assertions.assertEquals(5, synonyms.size());
    }
}
