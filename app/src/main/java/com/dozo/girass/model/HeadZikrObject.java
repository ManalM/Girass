package com.dozo.girass.model;

public class HeadZikrObject {


    public String ID, SearchTitle, TITLE;

    public ZikrObject[] AllAzkar;


    public HeadZikrObject(String ID, String searchTitle, String TITLE, ZikrObject[] allAzkar) {
        this.ID = ID;
        SearchTitle = searchTitle;
        this.TITLE = TITLE;
        AllAzkar = allAzkar;
    }
}
