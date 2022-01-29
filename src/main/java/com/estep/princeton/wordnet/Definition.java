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

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the definition of a word in the {@link WordDictionary} along with the synonyms and usage examples for the
 * word.
 */
public class Definition implements Comparable<Definition> {
    private final String word;
    private final DefinitionType definitionType;
    private String definition;
    private List<String> synonyms;
    private List<String> usages;

    /**
     * Creates an instance of this class and sets the word and definition type.
     *
     * @param word           the word being defined.
     * @param definitionType an enumeration representing the type for the word (noun, verb, adverb, or adjective).
     */
    public Definition(String word, DefinitionType definitionType) {
        this.word = word;
        this.definitionType = definitionType;
        synonyms = new ArrayList<>();
        usages = new ArrayList<>();
    }

    /**
     * Returns the word being defined.
     *
     * @return the word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns the type for the word (noun, verb, adverb, or adjective).
     *
     * @return the definition type.
     */
    public DefinitionType getDefinitionType() {
        return definitionType;
    }

    /**
     * Returns the definition of the word.
     *
     * @return the definition.
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Sets the definition of the word.
     *
     * @param definition the definition.
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * Returns a list of synonyms associated with the word.
     *
     * @return the list.
     */
    public List<String> getSynonyms() {
        return synonyms;
    }

    /**
     * Sets the list of synonyms associated with the word.
     *
     * @param synonyms the list.
     */
    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    /**
     * Returns a list of examples on how the word is used.
     *
     * @return the list.
     */
    public List<String> getUsages() {
        return usages;
    }

    /**
     * Sets a list of examples on how the word is used.
     *
     * @param usages the list.
     */
    public void setUsages(List<String> usages) {
        this.usages = usages;
    }

    @Override
    public String toString() {
        return "word=" + word
                + "definitionType=" + definitionType
                + "definition=" + definition
                + "synonyms=" + synonyms
                + "usages=" + usages;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Definition other = (Definition) obj;
        if (word == null) {
            if (other.word != null)
                return false;
        } else if (!word.equals(other.word))
            return false;
        if (definitionType == null) {
            if (other.definitionType != null)
                return false;
        } else if (!definitionType.equals(other.definitionType))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((word == null) ? 0 : word.hashCode());
        result = prime * result + ((definitionType == null) ? 0 : definitionType.hashCode());
        return result;
    }

    @Override
    public int compareTo(Definition definition) {
        if (this.word == null || this.definitionType == null) {
            return -1;
        } else if (definition == null || definition.word == null || definition.definitionType == null) {
            return 1;
        }

        int rc = this.word.compareToIgnoreCase(definition.word);
        if (rc == 0) {
            rc = this.definitionType.compareTo(definition.definitionType);
        }
        return rc;
    }
}
