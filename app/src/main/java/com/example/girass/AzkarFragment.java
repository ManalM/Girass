package com.example.girass;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class AzkarFragment extends Fragment {
    private ListView listView;
    private Toolbar toolbar;
    private TextView toolbarText;
    private ImageButton searchBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_azkar,container,false);

       // final Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_anim);
        /////////////////////////////////////////////
        /////////////     ToolBar       ////////////
        //////////////////////////////////////////


        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        toolbarText = rootView.findViewById(R.id.toolbar_title);

        searchBtn = rootView.findViewById(R.id.search_btn);
        toolbar.setTitle("");
        toolbarText.setText(R.string.azkar);
        searchBtn.setImageResource(R.drawable.search_icon);


        /////////////////////////////////////////////
        /////////////  List Azkar //////////////////
        ///////////////////////////////////////////

        listView = rootView.findViewById(R.id.list);
        DataService dataService = new DataService();
        HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();

        String[] titles = new String[headZikrObjects.length];

     /*   for (int i = 0; i < headZikrObjects.length; i++) {
            titles[i] = headZikrObjects[i].TITLE;
        }*/
        int i = 0;
        while (i < headZikrObjects.length) {
            titles[i] = headZikrObjects[i].TITLE;
            i++;
        }

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.zikr_list_item, R.id.zikrText, titles);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);







        return rootView;




    }

}
