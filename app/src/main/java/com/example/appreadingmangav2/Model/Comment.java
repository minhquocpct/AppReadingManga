package com.example.appreadingmangav2.Model;

public class Comment {
    private String NameUser;
    private String Comment;
    public Comment(){
    }
    public Comment(String nameUser, String comment) {
        NameUser = nameUser;
        Comment = comment;
    }
    public String getNameUser() {
        return NameUser;
    }

    public void setNameUser(String nameUser) {
        this.NameUser = nameUser;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        this.Comment = comment;
    }
}
