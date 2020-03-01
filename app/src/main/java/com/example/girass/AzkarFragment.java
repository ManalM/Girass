package com.example.girass;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;

import java.util.ArrayList;

public class AzkarFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView listView;
    private Toolbar toolbar;
    private TextView toolbarText;
    private ImageButton searchBtn;
    ArrayList<String> titles;
    LinearLayout searchBar;
    Adapter mAdapterAzkar;
    SearchView searchView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_azkar, container, false);

        // final Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_anim);
        /////////////////////////////////////////////
        /////////////     ToolBar       ////////////
        //////////////////////////////////////////

        searchBtn = rootView.findViewById(R.id.button);
        searchBar = rootView.findViewById(R.id.search_view1);
        searchView = rootView.findViewById(R.id.searchText);
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

        titles = new ArrayList<String>();

        int i = 0;
        while (i < headZikrObjects.length) {
            titles.add(headZikrObjects[i].TITLE);
            i++;
        }


        mAdapterAzkar = new Adapter(getContext(), titles);
        listView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        listView.setAdapter(mAdapterAzkar);


        clickItem();
        searchView.setOnQueryTextListener(this);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titles.clear();
                toolbar.setVisibility(View.GONE);
                searchBar.setVisibility(View.VISIBLE);
                clickItem();
/*
                mAdapterAzkar.setOnItemClickListener(new Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        AllZikr allZikr = new AllZikr();
                        Bundle bundle = new Bundle();
                        bundle.putString("array", titles.get(position));
                        bundle.putInt("tag", 1);
                        allZikr.setArguments(bundle);

                        //----------------------------------------------------------------


                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, allZikr).commit();

                    }
                });
*/
            }
        });

        return rootView;


    }


    private void clickItem() {
        mAdapterAzkar.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AllZikr allZikr = new AllZikr();
                Bundle bundle = new Bundle();
                bundle.putString("array", titles.get(position));
//                bundle.putString("id", String.valueOf(position + 1));
                bundle.putInt("tag", 1);
                allZikr.setArguments(bundle);

                //----------------------------------------------------------------


                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, allZikr).commit();

            }
        });

    }
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapterAzkar.getFilter().filter(newText);
        return false;
    }
}
