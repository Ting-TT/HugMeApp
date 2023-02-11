package com.example.parser;
import java.util.Scanner;

/**
 * @author: Qianxuan Lin
 */

/**
 * grammar rule:
 *
 * Start symbol: S
 * S → T=B | T=B;E=A | E=A;T=B | E=A
 * T → breed
 * A → cat | dog
 * E → species
 * B → samoyed | poodle | goldenretriever | britishshorthair | ragdoll | siamese
 *
 * examples:
 * species=dog;breed=goldenretriever
 * breed=poodle;species=dog
 * breed=poodle
 * species=dog
 *
 */

public class Parser {

    //thrown exception when there is not any possible production rule to parse tokens.
    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }

    // creat tokenizer class field for the parser to use.
    static Tokenizer tokenizer;


    /**
     * Parser class constructor
     * sets the tokenizer field.
     */
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * the main() method is kept for testing purpose.
     * it is comment out because it's not directly used in this app
     */
//    public static void main(String[] args) {
//        //String input = "species=dog;breed=goldenretriever"; // Exception: 1 parseSpecies
//        //String input = "species=dog"; //expression: species=dog
//        //String input = "breed=poodle"; //expression: breed=poodle
//        String input = "breed=poodle;species=dog;";//Exception: 1 parseBreed
//        Tokenizer tokenizer = new Tokenizer(input);
//        //System.out.println("tokenizer: " + tokenizer);
//        // Print out the expression from the parser.
//        Parser parser = new Parser(tokenizer);
//        //System.out.println("Parser: " + parser);
//        Exp expression = parser.parseExp();
//        System.out.println("expression: " + expression.show());
//
//     /*   // Create a scanner to get the user's input.
//        Scanner scanner = new Scanner(System.in);
//
//
//         //Continue to get the user's input until they exit.
//        System.out.println("Provide a mathematical string to be parsed:");
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
//            // Print out the expression from the parser.
//            Parser parser = new Parser(tokenizer);
//
//            Exp expression = parser.parseExp();
//            System.out.println("Parsing: " + expression.show());
//
//        }*/
//    }



    /**
     * return breed name for searching purpose
     * for example, return "poodle"
     */
    public static String getBreed(String userinput){
        String b ="";
        Tokenizer userTokenizer = new Tokenizer(userinput);

        while(userTokenizer.hasNext()){
            if(userTokenizer.current().getType()== Token.Type.BREED){
                b= userTokenizer.current().getToken();

            }
            userTokenizer.next();
        }
        return b;
    }

    /**
     * return species name for searching purpose
     * for example, return "dog"
     */
    public static String getSpecies(String userinput){//cat or dog
        String b ="";
        Tokenizer userTokenizer = new Tokenizer(userinput);

        while(userTokenizer.hasNext()){
            if(userTokenizer.current().getType()== Token.Type.SPECIES){
                b= userTokenizer.current().getToken();

            }
            userTokenizer.next();
        }
        return b;
    }



    /**
     * to check user input follows the grammar rule:
     * S → T=B | T=B;E=A | E=A;T=B | E=A
     * @return type: boolean
     */
    public static boolean parsing(String userinput){//changed to static
        Tokenizer userTokenizer = new Tokenizer(userinput);
        Exp parsed = new Parser(userTokenizer).parseExp();
        if(parsed.show().equals(userinput)){
            return true;
        }
        return false;
    }


    /**
     * paring tokens following the grammar rule:
     * S → T=B | T=B;E=A | E=A;T=B | E=A
     * @return type: Exp
     */
    public Exp parseExp() {

        if(tokenizer.hasNext()&&tokenizer.current().getType()==Token.Type.BNAME){
            Exp tb = parseBreed();
            System.out.println("Exp tb: " + tb.show());

            if(tokenizer.hasNext()&&tokenizer.current().getType()==Token.Type.SEMICOLON){
                tokenizer.next();
                if(tokenizer.hasNext()&&tokenizer.current().getType()==Token.Type.SNAME){
                    Exp ea = parseSpecies();
                    System.out.println("Exp ea: " + ea.show());
                    tokenizer.next();
                    if(!tokenizer.hasNext()){
                        return new ConcateExp(tb,ea);
                    }else {
                        throw new Parser.IllegalProductionException("1 parseExp: Provided with tokens not conforming to the grammar!");
                    }
                }else {
                    throw new Parser.IllegalProductionException("2 parseExp: Provided with tokens not conforming to the grammar!");
                }
            }else if(!tokenizer.hasNext()){
                return tb;
            }else {
                throw new Parser.IllegalProductionException("3 parseExp: Provided with tokens not conforming to the grammar!");
            }
            // end of: T=B | T=B;E=A
            // start of: E=A;T=B | E=A
        }else if(tokenizer.hasNext()&&tokenizer.current().getType()==Token.Type.SNAME){
            Exp ea = parseSpecies();

            if(tokenizer.hasNext()&&tokenizer.current().getType()==Token.Type.SEMICOLON){
                tokenizer.next();
                if(tokenizer.hasNext()&&tokenizer.current().getType()==Token.Type.BNAME){
                    Exp tb = parseBreed();
                    tokenizer.next();
                    if(!tokenizer.hasNext()){
                        return new ConcateExp(ea,tb);
                    }else {
                        throw new Parser.IllegalProductionException("4 parseExp: Provided with tokens not conforming to the grammar!");
                    }
                }else {
                    throw new Parser.IllegalProductionException("5 parseExp: Provided with tokens not conforming to the grammar!");
                }
            }else if(!tokenizer.hasNext()){
                return ea;
            }else {
                throw new Parser.IllegalProductionException("6 parseExp: Provided with tokens not conforming to the grammar!");
            }
            //end of: T=B | T=B;E=A
        }else {
            throw new Parser.IllegalProductionException("7 parseExp: Provided with tokens not conforming to the grammar!");
        }

    }

    /**
     * parsing tokens by this grammar rule:
     * S → E=A
     * @return type: Exp. (species expression)
     */
    public Exp parseSpecies() {

        if(tokenizer.hasNext()&&tokenizer.current().getType()==Token.Type.SNAME){
            String sname =tokenizer.current().getToken();
            tokenizer.next();

            if(tokenizer.hasNext() && tokenizer.current().getType()==Token.Type.EQUAL) {
                tokenizer.next();
                if (tokenizer.hasNext() && tokenizer.current().getType() == Token.Type.SPECIES) {

                    String species = tokenizer.current().getToken();
                    tokenizer.next();
                    return new BreedExp(sname, species);
                } else {
                    throw new Parser.IllegalProductionException("1 parseSpecies: Provided with tokens not conforming to the grammar!");
                }
            }else {
                throw new Parser.IllegalProductionException("2 parseSpecies: Provided with tokens not conforming to the grammar!");
            }

        }else {
            throw new Parser.IllegalProductionException("3 parseSpecies: Provided with tokens not conforming to the grammar!");
        }


    }


    /**
     * parsing tokens by this grammar rule:
     * S → T=B
     * @return type: Exp. (breed expression)
     */
    public Exp parseBreed() {

        if(tokenizer.hasNext()&&tokenizer.current().getType()==Token.Type.BNAME){
            String bname =tokenizer.current().getToken();
            tokenizer.next();

            if(tokenizer.hasNext() && tokenizer.current().getType()==Token.Type.EQUAL) {
                tokenizer.next();
                if (tokenizer.hasNext() && tokenizer.current().getType() == Token.Type.BREED) {

                    String breed = tokenizer.current().getToken();
                    tokenizer.next();
                    return new SpeciesExp(bname, breed);
                } else {
                    throw new Parser.IllegalProductionException("1 parseBreed: Provided with tokens not conforming to the grammar!");
                }
            }else {
                throw new Parser.IllegalProductionException("2 parseBreed: Provided with tokens not conforming to the grammar!");
            }

        }else {
            throw new Parser.IllegalProductionException("3 parseBreed: Provided with tokens not conforming to the grammar!");
        }


    }
}


