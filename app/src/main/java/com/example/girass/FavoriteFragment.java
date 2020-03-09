package com.example.girass;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import com.example.girass.adapters.FavAdapter;
import com.example.girass.adapters.SimpleItemTouchHelperCallback;
import com.example.girass.adapters.SwipeAdapter;
import com.example.girass.helpers.OnStartDragListener;
import com.example.girass.model.PrefMethods;



import java.util.ArrayList;


public class FavoriteFragment extends Fragment implements OnStartDragListener {
    private Toolbar toolbar;
    private TextView toolbarText, editText;

    private RecyclerView list;
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    //---------------------

    private ArrayList<String> favorites;

    private ItemTouchHelper mItemTouchHelper;


    FavAdapter adapter;
    static int currentVisiblePosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        //------------ToolBar----------------------

        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        toolbarText = rootView.findViewById(R.id.toolbar_title1);
        editText = rootView.findViewById(R.id.edit);
        toolbar.setTitle("");
        toolbarText.setText(R.string.fav);

        //-------------------RecyclerView--------------
        list = rootView.findViewById(R.id.fav_list);
        list.setLayoutManager(new GridLayoutManager(getContext(), 1));
        //-------------------get Azkar------------------

        favorites = new PrefMethods(getContext()).getArray();
        adapter = new FavAdapter(getContext(), favorites);

        list.setAdapter(adapter);

        //----------------------subtitle of toolbar-----------------------

        if (favorites.size() == 0) {
            editText.setText(" ");

        } else {
            editText.setText(R.string.Edit);

        }

        //-------------------On Item Clicked----------------------


        adapter.setOnItemClickListener(new FavAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


                AllZikr allZikr = new AllZikr();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, allZikr).commit();
                Bundle bundle = new Bundle();
                bundle.putString("array", favorites.get(position));
                bundle.putInt("tag", 2);

                allZikr.setArguments(bundle);
            }
        });

        //----------------------edit text listener----------------------

        editText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (list.getAdapter() == adapter) {
                    editText.setText(R.string.agree);

                    SwipeAdapter swipeAdapter = new SwipeAdapter(getContext(), FavoriteFragment.this::onStartDrag);

                    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(swipeAdapter);
                    mItemTouchHelper = new ItemTouchHelper(callback);
                    mItemTouchHelper.attachToRecyclerView(list);
                    list.setAdapter(swipeAdapter);

                } else {

                    editText.setText(R.string.Edit);
                    favorites = new PrefMethods(getContext()).getArray();
                    adapter = new FavAdapter(getContext(), favorites);
                    list.setAdapter(adapter);
                }


            }
        });

        return rootView;
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);

    }

    //------- save state ---------------
    @Override
    public void onPause() {
        super.onPause();
        currentVisiblePosition = 0;
        currentVisiblePosition = ((LinearLayoutManager) list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((LinearLayoutManager) list.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        currentVisiblePosition = 0;
    }
}
