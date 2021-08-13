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

import com.google.common.base.Objects;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for a word in the dictionary.
 */
// CHECKSTYLE:OFF
@SuppressFBWarnings
public class Word {
    private final String word;
    private List<Definition> definitions;

    /**
     * Creates this word.
     *
     * @param word the word.
     */
    public Word(String word) {
        this.word = word;
        definitions = new ArrayList<>();
    }

    public String getWord() {
        return word;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("word", word)
                .append("definitions", definitions)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equal(word, word1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(word);
    }
}
// CHECKSTYLE:ON
