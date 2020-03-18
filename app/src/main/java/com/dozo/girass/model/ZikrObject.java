package com.dozo.girass.model;

public class ZikrObject {


    public String ID, Details, Narriated;

    public int TimesToRepeat;

    public ZikrObject(String ID, String details, int timesToRepeat, String narriated) {
        this.ID = ID;
        Details = details;
        Narriated = narriated;
        TimesToRepeat = timesToRepeat;
    }
}


