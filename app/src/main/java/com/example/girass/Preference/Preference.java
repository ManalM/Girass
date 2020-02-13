package com.example.girass.Preference;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Preference(Context context) {

        this.sharedPreferences = context.getSharedPreferences("MY_SHARED", 0);
        this.editor = context.getSharedPreferences("MY_SHARED", 0).edit();
    }


}
