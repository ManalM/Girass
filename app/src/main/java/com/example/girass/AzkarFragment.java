package com.example.girass;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.*;

import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.LinearLayout;


import android.widget.TextView;

import com.example.girass.Data.DataService;
import com.example.girass.adapters.Adapter;
import com.example.girass.model.HeadZikrObject;

import java.util.ArrayList;

public class AzkarFragment extends Fragment implements Adapter.SelectedUser {
    private Toolbar toolbar;
    private ArrayList<String> titles;
    private LinearLayout searchBar;
    private ImageView arrow;
    private Adapter mAdapterAzkar;
    private RecyclerView listView;

    GridLayoutManager linearLayoutManager;
    static int currentVisiblePosition;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_azkar, container, false);


        //---------------------------------------------------

        ImageButton searchBtn = rootView.findViewById(R.id.button);
        searchBar = rootView.findViewById(R.id.search_view1);
        //  SearchView searchView = rootView.findViewById(R.id.searchText);
        EditText search = rootView.findViewById(R.id.search);
        arrow = rootView.findViewById(R.id.search_arrow);
        /////////////////////////////////////////////
        /////////////     ToolBar       ////////////
        //////////////////////////////////////////
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

        createArray();
        ///----------------search btn and process ----------

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mAdapterAzkar.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titles.clear();
                toolbar.setVisibility(View.GONE);
                search.setText(null);
                searchBar.setVisibility(View.VISIBLE);
                keyboardShowAndHide(getActivity(), search, 0);

            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.VISIBLE);
                searchBar.setVisibility(View.GONE);

                createArray();

                keyboardShowAndHide(getActivity(), search, 1);
            }
        });

        return rootView;


    }


    private void createArray() {
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
    }

    private void keyboardShowAndHide(Activity activity, EditText editText, int value) {
        editText.requestFocus();
        try {

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (value == 0)
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            else
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {

        }

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

        currentVisiblePosition = 0;
        currentVisiblePosition = ((LinearLayoutManager) listView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((LinearLayoutManager) listView.getLayoutManager()).scrollToPosition(currentVisiblePosition);

        currentVisiblePosition = 0;
    }


}
