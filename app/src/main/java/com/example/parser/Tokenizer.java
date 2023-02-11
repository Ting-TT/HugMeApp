package com.example.parser;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author: Qianxuan Lin
 */
public class Tokenizer {
    private String buffer;          // String to be transformed into tokens each time next() is called.
    private Token currentToken;     // The current token. The next token is extracted when next() is called.

    /**
     * the main() method is kept for testing purpose.
     * it is comment out because it's not directly used in this app
     */
//    public static void main(String[] args) {
//        // Create a scanner to get the user's input.
//        Scanner scanner = new Scanner(System.in);
//
//        /*
//         Continue to get the user's input until they exit.
//         To exit press: Control + D or providing the string 'q'
//         Example input you can try: ((1 + 2) * 5)/2
//         */
//        System.out.println("Provide a user input string to be tokenized:");
//        while (scanner.hasNext()) {
//            String input = scanner.nextLine();
//
//            // Check if 'quit' is provided.
//            if (input.equals("q"))
//                break;
//
//            // Create an instance of the tokenizer.
//            Tokenizer tokenizer = new Tokenizer(input);
//
//            // Print all the tokens.
//            while (tokenizer.hasNext()) {
//                System.out.print(tokenizer.current() + " ");
//                tokenizer.next();
//            }
//            System.out.println();
//        }
//    }

    /**
     * Tokenizer class constructor
     */
    public Tokenizer(String text) {
        buffer = text.toLowerCase();
        next();
    }

    /**
     * This function will find and extract a next token from buffer and
     * save the token to currentToken
     */
    public void next() {
        buffer = buffer.trim();     // remove whitespace

        if (buffer.isEmpty()) {
            currentToken = null;
            return;
        }

        // example: breed=dog;species=poodle
        char firstChar = buffer.charAt(0);
        if (firstChar == '=')
            currentToken = new Token("+", Token.Type.EQUAL);
        if (firstChar == ';')
            currentToken = new Token("-", Token.Type.SEMICOLON);

        if (Character.isLetter(firstChar)){
            String letters=firstChar+"";
            for(int i = 1; i<buffer.length()&&Character.isLetter(buffer.charAt(i));i++){
                letters=letters+buffer.charAt(i);
            }
            if(letters.equals("breed")){
                currentToken = new Token (letters, Token.Type.BNAME);
            }else if(letters.equals("species")){
                currentToken = new Token (letters, Token.Type.SNAME);
            }else if(letters.equals("cat")|letters.equals("dog")){
                currentToken = new Token (letters, Token.Type.SPECIES);
            }else if(letters.equals("samoyed")|letters.equals("poodle")|letters.equals("siamese")
                    |letters.equals("goldenretriever")|letters.equals("ragdoll")
                    |letters.equals("britishshorthair")){
                currentToken = new Token (letters, Token.Type.BREED);
            }else{
                throw new Token.IllegalTokenException("Invalid letter Token!");
            }

        }

        if(firstChar != ';'&& firstChar != '=' && !Character.isLetter(firstChar)){
            throw new Token.IllegalTokenException("Illegal Token!");}

        // Remove the extracted token from buffer
        int tokenLen = currentToken.getToken().length();
        buffer = buffer.substring(tokenLen);
    }

    //Returns the current token extracted by next()
    public Token current() {
        return currentToken;
    }

    //Check whether tokenizer still has tokens left
    public boolean hasNext() {
        return currentToken != null;
    }
}
