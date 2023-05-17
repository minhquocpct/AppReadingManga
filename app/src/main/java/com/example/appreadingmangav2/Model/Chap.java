package com.example.appreadingmangav2.Model;

public class Chap {
    private int idChap;
    private String NameChap;
    public Chap(){

    }
    public Chap(int idChap, String nameChap) {
        this.idChap = idChap;
        NameChap = nameChap;
    }

    public int getIdChap() {
        return idChap;
    }

    public void setIdChap(int idChap) {
        this.idChap = idChap;
    }

    public String getNameChap() {
        return NameChap;
    }

    public void setNameChap(String nameChap) {
        NameChap = nameChap;
    }
}
