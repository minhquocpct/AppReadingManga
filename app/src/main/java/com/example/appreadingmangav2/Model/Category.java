package com.example.appreadingmangav2.Model;

public class Category {

    private int idCategory;
    private String nameCategory;
    public Category(){

    }
    public Category(int idCategory, String nameCategory) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
    }
    public int getIdCategory(int id) {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }


}
