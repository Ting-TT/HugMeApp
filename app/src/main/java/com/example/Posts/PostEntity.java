package com.example.Posts;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * @author: Xie Hu
 */

/**
 * This class is for all field of PostEntity
 */

public class PostEntity implements Serializable {
    public String getPetID() {
        return petID;
    }

    public void setPetID(String petID) {
        this.petID = petID;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public String getPetSpecies() {
        return petSpecies;
    }

    public void setPetSpecies(String petSpecies) {
        this.petSpecies = petSpecies;
    }

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public int getLikeTotal() {
        likeTotal = 0;
        return likeTotal;
    }

    public void setLikeTotal(int likeTotal) {
        this.likeTotal = likeTotal;
    }

    private String petID;
    private String petBreed;
    private String petSpecies;
    private String petAge;
    private String petGender;
    private List<String> emailList;
    private List<String> imageList;
    private List<String> stringList;
    private int likeTotal;

    public PostEntity() { }

    public PostEntity(String petID, String petBreed, String petSpecies, String petAge,
                      String petGender, List<String> imageList,
                      List<String> stringList, List<String> emailList, int likeTotal) {

        this.petID = petID;
        this.petBreed = petBreed;
        this.petSpecies = petSpecies;
        this.petAge = petAge;
        this.petGender = petGender;
        this.imageList = imageList;
        this.stringList = stringList;
        this.emailList = emailList;
        this.likeTotal = likeTotal;
    }
}
