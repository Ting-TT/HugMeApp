package com.example.parser;

/**
 * @author: Qianxuan Lin
 */
public class ConcateExp extends Exp{
    // this breed expression connect the Token.Type.BNAME and Token.Type.BREED
    // show "species=cat; breed=britishshorthair", for example
    private Exp bExp;
    private Exp sExp;

    // when concating expressions, the order doesn't matter
    // it can be "<species expression> ; <breed expression>" or  "<breed expression> ; <species expression>"
    public ConcateExp(Exp bExp, Exp sExp) {
        this.bExp = bExp;
        this.sExp =sExp;
    }

    @Override
    public String show() {
        return bExp.show() + ";" + sExp.show();
    }



}

