package com.example.girass.model;

import android.content.Context;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;


import java.io.IOException;
import java.util.ArrayList;

public class PrefMethods {

    Context c;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public static final String ARRAY_KEY = "array_key";


    public PrefMethods(Context c) {
        this.c = c;
        editor = PreferenceManager.getDefaultSharedPreferences(c).edit();
        pref = PreferenceManager.getDefaultSharedPreferences(c);
        ;
    }

    public ArrayList<String> getArray() {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            if (pref != null) {

                arrayList = (ArrayList<String>) ObjectSerializer.deserialize(pref.getString(ARRAY_KEY, ObjectSerializer.serialize(new ArrayList<String>())));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public void saveArray(ArrayList<String> inputArray) {

        if (pref != null) {

            editor.remove(ARRAY_KEY).apply();
            try {
                editor.putString(ARRAY_KEY, ObjectSerializer.serialize(inputArray));
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
