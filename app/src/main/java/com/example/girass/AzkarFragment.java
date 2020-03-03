package com.example.girass;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;

import com.example.girass.Data.DataService;
import com.example.girass.adapters.Adapter;
import com.example.girass.model.HeadZikrObject;

import java.util.ArrayList;

public class AzkarFragment extends Fragment implements SearchView.OnQueryTextListener, Adapter.SelectedUser {
    private Toolbar toolbar;
    private ArrayList<String> titles;
    private LinearLayout searchBar;
    private ImageView arrow;
    private Adapter mAdapterAzkar;
    private RecyclerView listView;
    private final String KEY_RECYCLER_STATE = "list_state";
    private Bundle mBundleRecyclerViewState;
    private SavedState mListState;
    private LinearLayout linearLayout;
    GridLayoutManager linearLayoutManager;
    static int currentVisiblePosition;
    String LIST_STATE_KEY = "key";
    private AzkarFragment mLayoutManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_azkar, container, false);


        //---------------------------------------------------

        ImageButton searchBtn = rootView.findViewById(R.id.button);
        searchBar = rootView.findViewById(R.id.search_view1);
        SearchView searchView = rootView.findViewById(R.id.searchText);
        arrow = rootView.findViewById(R.id.search_arrow);
        /////////////////////////////////////////////
        /////////////     ToolBar       ////////////
        //////////////////////////////////////////
        linearLayout = rootView.findViewById(R.id.recyclerView_layout);
        linearLayoutManager = new GridLayoutManager(getContext(), 1);
        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        TextView toolbarText = rootView.findViewById(R.id.toolbar_title);

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



        mAdapterAzkar = new Adapter(getContext(), titles, this);
        listView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        final Parcelable state;
        state = listView.getLayoutManager().onSaveInstanceState();
        listView.setAdapter(mAdapterAzkar);

        //   listView.getLayoutManager().onRestoreInstanceState(state);
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

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.VISIBLE);
                searchBar.setVisibility(View.GONE);
                int i = 0;
                while (i < headZikrObjects.length) {
                    titles.add(headZikrObjects[i].TITLE);
                    i++;
                }
            }
        });

      /*  Fragment f = new Fragment();
        SavedState savestate = getFragmentManager().saveFragmentInstanceState(f);
        f.setInitialSavedState(savestate);*/
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

    //------- save state ---------------
    @Override
    public void onPause() {
        super.onPause();
        //mBundleRecyclerViewState = new Bundle();


        //   mBundleRecyclerViewState.putInt("selectedItem",mAdapterAzkar.selectedItem);
        currentVisiblePosition = 0;
        currentVisiblePosition = ((LinearLayoutManager) listView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    @Override
    public void onResume() {
        super.onResume();

  /*      if (mListState != null) {
            mLayoutManager.onActivityCreated(mListState);
        }*/

        ((LinearLayoutManager) listView.getLayoutManager()).scrollToPosition(currentVisiblePosition);

        currentVisiblePosition = 0;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle state) {
        super.onActivityCreated(state);
        // if(state != null)
        //      mListState = state.getParcelable(LIST_STATE_KEY);
    }
/*
    private void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        // Retrieve list state and list/item positions
        if(state != null)
            mListState = state.getParcelable(LIST_STATE_KEY);
    }*/

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
//        mListState =  getFragmentManager().saveFragmentInstanceState(mLayoutManager);
        //  mListState = mLayoutManager.onSaveInstanceState();
        //  savedInstanceState.putParcelable(LIST_STATE_KEY, mListState);

    }

}
