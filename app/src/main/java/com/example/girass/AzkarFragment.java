package com.example.girass;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import android.widget.ImageButton;

import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;

import java.util.ArrayList;

public class AzkarFragment extends Fragment implements SearchView.OnQueryTextListener, Adapter.SelectedUser {
    private Toolbar toolbar;
    private ArrayList<String> titles;
    private LinearLayout searchBar;
    private Adapter mAdapterAzkar;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_azkar, container, false);

        /////////////////////////////////////////////
        /////////////     ToolBar       ////////////
        //////////////////////////////////////////

        ImageButton searchBtn = rootView.findViewById(R.id.button);
        searchBar = rootView.findViewById(R.id.search_view1);
        SearchView searchView = rootView.findViewById(R.id.searchText);
        //---------------------------------------------------


        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        TextView toolbarText = rootView.findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText(R.string.azkar);

        searchBtn.setImageResource(R.drawable.search_icon);


        /////////////////////////////////////////////
        /////////////  List Azkar //////////////////
        ///////////////////////////////////////////

        RecyclerView listView = rootView.findViewById(R.id.list);
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();

        titles = new ArrayList<String>();

        int i = 0;
        while (i < headZikrObjects.length) {
            titles.add(headZikrObjects[i].TITLE);
            i++;
        }


        mAdapterAzkar = new Adapter(getContext(), titles, this);
        listView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        listView.setAdapter(mAdapterAzkar);


        ///----------------search btn and process ----------
        searchView.setOnQueryTextListener(this);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titles.clear();
                toolbar.setVisibility(View.GONE);
                searchBar.setVisibility(View.VISIBLE);

            }
        });

        return rootView;


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


    ///---------- send data to allZIkr fragment ------------
    @Override
    public void selectedUser(String s) {

        AllZikr allZikr = new AllZikr();
        Bundle bundle = new Bundle();
        bundle.putString("array", s);
        bundle.putInt("tag", 1);
        allZikr.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, allZikr).commit();


    }
}
