package com.example.girass;

public class ZikrObject {


    String ID, Details, Narriated;

    int TimesToRepeat;

    public ZikrObject(String ID, String details, int timesToRepeat, String narriated) {
        this.ID = ID;
        Details = details;
        Narriated = narriated;
        TimesToRepeat = timesToRepeat;
    }
}


class HeadZikrObject {


    String ID, SearchTitle, TITLE;

    ZikrObject[] AllAzkar;


    public HeadZikrObject(String ID, String searchTitle, String TITLE, ZikrObject[] allAzkar) {
        this.ID = ID;
        SearchTitle = searchTitle;
        this.TITLE = TITLE;
        AllAzkar = allAzkar;
    }
}

