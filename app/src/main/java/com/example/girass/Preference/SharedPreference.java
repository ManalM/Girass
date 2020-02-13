package com.example.girass.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Context context;
    public static final String PREFS_NAME = "Girass_APP";
    public static final String FAVORITES = "Item_Favorite";

    public SharedPreference(Context context) {
        super();


        this.settings = context.getSharedPreferences(PREFS_NAME,
                0);
        this.editor = settings.edit();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(List<String> favorites) {


        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, String id) {
        List<String> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<String>();
        favorites.add(id);
        saveFavorites(favorites);
    }

    public void removeFavorite(Context context, String id) {
        ArrayList<String> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(id);
            saveFavorites(favorites);
        }
    }

    public ArrayList<String> getFavorites(Context context) {
        //     SharedPreferences settings;
        List<String> favorites;
/*

        settings =context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
*/

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites,
                    String[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<String>(favorites);
        } else
            return null;

        return (ArrayList<String>) favorites;
    }
}
