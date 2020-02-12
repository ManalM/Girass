package com.example.girass;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

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

import java.util.ArrayList;


public class FavoriteFragment extends Fragment {

    private Toolbar toolbar;
    private TextView toolbarText, editText;

    private String title;
    private RecyclerView list;
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_favorite,container,false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        //------------ToolBar----------------------

        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        toolbarText = rootView.findViewById(R.id.toolbar_title);
        editText = rootView.findViewById(R.id.edit);
        toolbar.setTitle("");
        toolbarText.setText(R.string.fav);
        editText.setText(R.string.Edit);

        //-------------------get Azkar------------------
        ArrayList<String> arrayList = new ArrayList<>();

        Bundle b = this.getArguments();
        if (b != null)
            title = b.getString("title");
        Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
        arrayList.add(title);
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
        final String[] titles = new String[headZikrObjects.length];


/*        for (int i =0 ; i< headZikrObjects.length;i++){
            if (headZikrObjects[i].TITLE.equals(title))
                arrayList.add(headZikrObjects[i].TITLE);
                titles [i]= headZikrObjects[i].TITLE;
        }*/
        //-------------------RecyclerView--------------

        list = rootView.findViewById(R.id.fav_list);
        list.setLayoutManager(new GridLayoutManager(getContext(), 1));
        FavAdapter adapter = new FavAdapter(getContext(), arrayList);

        list.setAdapter(adapter);
        return rootView;
    }

}
