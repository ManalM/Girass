package com.example.girass;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import android.widget.Toast;

public class AzkarFragment extends Fragment {
    private RecyclerView listView;
    private Toolbar toolbar;
    private TextView toolbarText;
    private ImageButton searchBtn;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
     /*   for (int i = 0; i < headZikrObjects.length; i++) {
            titles[i] = headZikrObjects[i].TITLE;
        }*/
        int i = 0;
        while (i < headZikrObjects.length) {
            titles[i] = headZikrObjects[i].TITLE;
            i++;
        }

        // ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.zikr_list_item, R.id.zikrText, titles);

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
           /*     Intent detailsIntent = new Intent(getContext(), ZikrDetails.class);

               detailsIntent.putExtra("array", titles[position]);*/

                // ZikrDetails sendZikrDetails = new ZikrDetails();
                //Bundle zikrBundle = new Bundle();
                // zikrBundle.putString("array", titles[position]);
                //   sendZikrDetails.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, allZikr).commit();

              /*  ZikrDetails zikrDetails = new ZikrDetails();
                Bundle bundle1 = new Bundle();
                bundle1.putString("array1", titles[position]);
                zikrDetails.setArguments(bundle1);

             /*  Intent intent = new Intent(getContext(), AllZikr.class);
                Intent detailsIntent = new Intent(getContext(), ZikrDetails.class);
              detailsIntent.putExtra("array", titles[position]);
                intent.putExtra("array", titles[position]);

                intent.putExtra("id", String.valueOf(position + 1));

                startActivity(intent);*/
            }
        });


        return rootView;


    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
