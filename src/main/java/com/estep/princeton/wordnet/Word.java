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
 * Represents a word in the {@link WordDictionary} and its definition data.
 */
public class Word {
    private final String word;
    private List<Definition> definitions;

    /**
     * Creates an instance of this Word.
     *
     * @param word the word.
     */
    public Word(String word) {
        this.word = word;
        definitions = new ArrayList<>();
    }

    /**
     * Returns this word.
     *
     * @return the word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns a list of definitions associated with this word.
     *
     * @return the list of definitions or empty list if none exist.
     */
    public List<Definition> getDefinitions() {
        return definitions == null ? new ArrayList<>() : definitions;
    }

    /**
     * Sets the list of definitions associated with this word.
     *
     * @param definitions the list.
     */
    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }


    @Override
    public String toString() {
        return "word=" + word
                + "definitions=" + definitions;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Word other = (Word) obj;
        if (word == null) {
            if (other.word != null)
                return false;
        } else if (!word.equals(other.word))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((word == null) ? 0 : word.hashCode());
        return result;
    }
}
