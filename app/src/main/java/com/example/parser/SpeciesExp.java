package com.example.parser;
/**
 * @author: Qianxuan Lin
 */
public class SpeciesExp extends Exp{
    // this breed expression connect the Token.Type.SNAME and Token.Type.SPECIES
    // show "species=cat", for example
    private String speciesName;
    private String species;

    public SpeciesExp(String speciesName, String species) {
        this.speciesName = speciesName;
        this.species = species;
    }

    @Override
    public String show() {
        return  speciesName + "=" + species;
    }
}
