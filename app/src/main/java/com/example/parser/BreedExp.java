package com.example.parser;

/**
 * @author: Qianxuan Lin
 */

public class BreedExp extends Exp{
    // this breed expression connect the Token.Type.BNAME and Token.Type.BREED
    // show "breed=britishshorthair", for example
    private String breedName;
    private String breed;

    public BreedExp(String breedName, String breed) {
        this.breedName = breedName;
        this.breed = breed;
    }

    @Override
    public String show() {
        return breedName + "=" + breed;
    }



}
