package com.example.appreadingmangav2.Model;

public class Manga {
    private int idManga;
    private String NameManga, LinkImage;
public Manga(){
}
    public Manga(int idManga, String nameManga, String linkImage) {
        this.idManga = idManga;
        this.NameManga = nameManga;
        this.LinkImage = linkImage;
    }

    public int getIdManga() {
        return idManga;
    }

    public void setIdManga(int idManga) {
        this.idManga = idManga;
    }

    public String getNameManga() {
        return NameManga;
    }

    public void setNameManga(String nameManga) {
        NameManga = nameManga;
    }

    public String getLinkImage() {
        return LinkImage;
    }

    public void setLinkImage(String linkImage) {
        LinkImage = linkImage;
    }
}
