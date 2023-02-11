package com.example.parser;
import java.util.Objects;

/**
 * @author: Qianxuan Lin
 */

public class Token {
    /** EQUAL-> =
     *  SEMICOLON -> ;
     *  BNAME -> breed
     *  SNAME -> species
     *  BREED -> samoyed | poodle | goldenretriever | britishshorthair | ragdoll | siamese
     *  SPECIES -> cat | dog
     */
    public enum Type {EQUAL, SEMICOLON, BNAME, SNAME, BREED, SPECIES}

    /**
     * thrown exception when a tokenizer attempts to tokenize something that is not of one
     * of the types of tokens.
     */
    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }

    // Fields of the class Token.
    private final String token; // Token representation in String form.
    private final Type type;    // Type of the token.

    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {

            return type + "";

    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true; // Same hashcode.
        if (!(other instanceof Token)) return false; // Null or not the same type.
        return this.type == ((Token) other).getType() && this.token.equals(((Token) other).getToken()); // Values are the same.
    }

    @Override
    public int hashCode() { return Objects.hash(token, type);}
}
