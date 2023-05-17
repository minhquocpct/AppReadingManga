package com.example.appreadingmangav2.Model;

public class Page {
    private String LinkImagePage,NumberPage;
    public Page(){
    }

    public String getNumberPage() {
        return NumberPage;
    }

    public void setNumberPage(String numberPage) {
        NumberPage = numberPage;
    }

    public Page(String linkImagePage, String numberPage) {
        LinkImagePage = linkImagePage;
        NumberPage = numberPage;
    }

    public String getLinkImagePage() {
        return LinkImagePage;
    }

    public void setLinkImagePage(String linkImagePage) {
        LinkImagePage = linkImagePage;
    }

}
