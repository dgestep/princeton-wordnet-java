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

/**
 * An enumeration representing the type for the word (noun, verb, adverb, or adjective).
 */
public enum DefinitionType {
    /**
     * Represents a word that is a noun.
     */
    NOUN("n"),
    /**
     * Represents a word that is a verb.
     */
    VERB("v"),
    /**
     * Represents a word that is an adverb.
     */
    ADVERB("r"),
    /**
     * Represents a word that is an adjective.
     */
    ADJECTIVE("a");

    private final String code;

    /**
     * Creates an instance of this enumeration.
     *
     * @param code sets the code.
     */
    DefinitionType(String code) {
        this.code = code;
    }

    /**
     * Returns the code associated with this DefinitionType.
     *
     * @return the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns a DefinitionType enumeration representing the supplied code.
     *
     * @param code the code.
     * @return the DefinitionType or null if no enumeration exists for the supplied code.
     */
    public static DefinitionType toEnum(String code) {
        DefinitionType[] values = DefinitionType.class.getEnumConstants();
        for (DefinitionType type : values) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
