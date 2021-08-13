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
 * Data container for the definition of a word.
 */
// CHECKSTYLE:OFF
@SuppressFBWarnings
public class Definition {
    private final String word;
    private final DefinitionType definitionType;
    private String definition;
    private List<String> synonyms;
    private List<String> usages;

    public Definition(String word, DefinitionType definitionType) {
        this.word = word;
        this.definitionType = definitionType;
        synonyms = new ArrayList<>();
        usages = new ArrayList<>();
    }

    public String getWord() {
        return word;
    }

    public DefinitionType getDefinitionType() {
        return definitionType;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getUsages() {
        return usages;
    }

    public void setUsages(List<String> usages) {
        this.usages = usages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("word", word)
                .append("definitionType", definitionType)
                .append("definition", definition)
                .append("synonyms", synonyms)
                .append("usages", usages)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Definition that = (Definition) o;
        return Objects.equal(word, that.word) && definitionType == that.definitionType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(word, definitionType);
    }
}
// CHECKSTYLE:ON
