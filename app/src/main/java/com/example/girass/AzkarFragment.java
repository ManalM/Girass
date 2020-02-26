package com.example.girass;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;

import android.widget.TextView;

public class AzkarFragment extends Fragment {
    private RecyclerView listView;
    private Toolbar toolbar;
    private TextView toolbarText;
    private ImageButton searchBtn;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_azkar, container, false);

        // final Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_anim);
        /////////////////////////////////////////////
        /////////////     ToolBar       ////////////
        //////////////////////////////////////////

        searchBtn = rootView.findViewById(R.id.button);
        //---------------------------------------------------


        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        toolbarText = rootView.findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText(R.string.azkar);

        searchBtn.setImageResource(R.drawable.search_icon);


        /////////////////////////////////////////////
        /////////////  List Azkar //////////////////
        ///////////////////////////////////////////

        listView = rootView.findViewById(R.id.list);
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();

        final String[] titles = new String[headZikrObjects.length];

        int i = 0;
        while (i < headZikrObjects.length) {
            titles[i] = headZikrObjects[i].TITLE;
            i++;
        }


        Adapter mAdapterAzkar = new Adapter(getContext(), titles);
        listView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        listView.setAdapter(mAdapterAzkar);


        mAdapterAzkar.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AllZikr allZikr = new AllZikr();
                Bundle bundle = new Bundle();
                bundle.putString("array", titles[position]);
                bundle.putString("id", String.valueOf(position + 1));
                bundle.putInt("tag", 1);
                allZikr.setArguments(bundle);

                //----------------------------------------------------------------


                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, allZikr).commit();

            }
        });


        return rootView;


    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
