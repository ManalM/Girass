package com.example.girass;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
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
    private RecyclerView  listView;
    private Toolbar toolbar;
    private TextView toolbarText;
    private ImageButton searchBtn;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_azkar,container,false);

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

     /*   for (int i = 0; i < headZikrObjects.length; i++) {
            titles[i] = headZikrObjects[i].TITLE;
        }*/
        int i = 0;
        while (i < headZikrObjects.length) {
            titles[i] = headZikrObjects[i].TITLE;
            i++;
        }

       // ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.zikr_list_item, R.id.zikrText, titles);

       AzkarAndFavAdapter mAdapterAzkar = new AzkarAndFavAdapter(getContext(),titles);
        listView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        listView.setAdapter(mAdapterAzkar);



        mAdapterAzkar.setOnItemClickListener(new AzkarAndFavAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext() , AllZikr.class);
                intent.putExtra("array", titles[position]);
                startActivity(intent);
            }
        });



        return rootView;




    }

}
