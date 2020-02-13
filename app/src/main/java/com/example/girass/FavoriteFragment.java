package com.example.girass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.girass.Preference.SharedPreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class FavoriteFragment extends Fragment {

    private Toolbar toolbar;
    private TextView toolbarText, editText;

    private String title;
    private RecyclerView list;
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    //---------------------
    SharedPreference sharedPreference;
    String[] favorites;
    final String mapKey = "map";
    String id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        //------------ToolBar----------------------

        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        toolbarText = rootView.findViewById(R.id.toolbar_title);
        editText = rootView.findViewById(R.id.edit);
        toolbar.setTitle("");
        toolbarText.setText(R.string.fav);
        editText.setText(R.string.Edit);

        //-------------------RecyclerView--------------
        list = rootView.findViewById(R.id.fav_list);
        list.setLayoutManager(new GridLayoutManager(getContext(), 1));

        //-------------------get Azkar------------------


        favorites = new String[0];
        HashMap<String, String> hashMap = loadMap();
        Collection<String> values = hashMap.values();
        favorites = values.toArray(new String[hashMap.size()]);
        FavAdapter adapter = new FavAdapter(getContext(), favorites);
        list.setAdapter(adapter);
//---------------------------------------------------------

        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
        final String[] IDs = new String[headZikrObjects.length];

        int i = 0;
        while (i < headZikrObjects.length) {
            if (headZikrObjects[i].TITLE.equals(title))

                id = headZikrObjects[i].ID;
            i++;
        }
        //-------------------On Item Clicked----------------------

        adapter.setOnItemClickListener(new FavAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), AllZikr.class);
                intent.putExtra("array", favorites[position]);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private HashMap<String, String> loadMap() {
        HashMap<String, String> outputMap = new HashMap<>();

        try {
            if (pref != null) {
                String jsonString = pref.getString(mapKey, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    outputMap.put(key, (String) jsonObject.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }
}
