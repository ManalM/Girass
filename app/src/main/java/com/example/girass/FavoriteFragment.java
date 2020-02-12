package com.example.girass;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FavoriteFragment extends Fragment {

    private Toolbar toolbar;
    private TextView toolbarText, editText;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_favorite,container,false);



        /////////////////////////////////////////////
        /////////////     ToolBar       ////////////
        //////////////////////////////////////////


        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        toolbarText = rootView.findViewById(R.id.toolbar_title);
        editText = rootView.findViewById(R.id.edit);
        toolbar.setTitle("");
        toolbarText.setText(R.string.fav);
        editText.setText(R.string.Edit);
        return rootView;
    }

}
