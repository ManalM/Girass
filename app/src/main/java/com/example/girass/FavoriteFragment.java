package com.example.girass;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class FavoriteFragment extends Fragment implements OnStartDragListener {

    private Toolbar toolbar;
    private TextView toolbarText, editText;

    private RecyclerView list;
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    //---------------------

    private String[] favorites;
    private final String mapKey = "map";
    String id = "";
    private ItemTouchHelper mItemTouchHelper;
    public static HashMap<String, String> hashMap;


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
        toolbarText = rootView.findViewById(R.id.toolbar_title);
        editText = rootView.findViewById(R.id.edit);
        toolbar.setTitle("");
        toolbarText.setText(R.string.fav);

        //-------------------RecyclerView--------------
        list = rootView.findViewById(R.id.fav_list);
        list.setLayoutManager(new GridLayoutManager(getContext(), 1));

        //-------------------get Azkar------------------

        hashMap = loadMap();
        Collection<String> values = hashMap.values();
        favorites = values.toArray(new String[hashMap.size()]);

        FavAdapter adapter = new FavAdapter(getContext(), favorites);
        list.setAdapter(adapter);


        //----------------------subtitle of toolbar-----------------------

        if (favorites.length == 0) {
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
                bundle.putString("array", favorites[position]);

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
                    ArrayList<String> arrayList = new ArrayList<>();

                    for (int i = 0; i < favorites.length; i++) {
                        arrayList.add(favorites[i]);
                    }

                    SwipeAdapter swipeAdapter = new SwipeAdapter(getContext(), arrayList, FavoriteFragment.this::onStartDrag);

                    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(swipeAdapter);
                    mItemTouchHelper = new ItemTouchHelper(callback);
                    mItemTouchHelper.attachToRecyclerView(list);
    /*                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                    itemTouchHelper.attachToRecyclerView(list);
                    SwipeAdapter swipeAdapter1 = new SwipeAdapter(getContext(), arrayList, null);

*/
                    list.setAdapter(swipeAdapter);
                } else {
                    editText.setText(R.string.Edit);
/*
                    DataService dataService = new DataService();
                    HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
                    ArrayList<String> ids = new ArrayList<>();
                    for (int i = 0; i < SwipeAdapter.mItems.size(); i++) {

                        if (SwipeAdapter.mItems.get(i).equals(headZikrObjects[i])) {
                            ids.add(headZikrObjects[i].ID);
                            Toast.makeText(getContext(), ids.get(i), Toast.LENGTH_SHORT).show();
                            hashMap.clear();
                            hashMap.put(ids.get(i), SwipeAdapter.mItems.get(i));
                            saveMap(hashMap);
                        }
                    }*/

                    list.setAdapter(adapter);
                }


            }
        });

        return rootView;
    }



    private HashMap<String, String> loadMap() {
        HashMap<String, String> outputMap = new HashMap<>();

        try {
            if (pref != null) {
                String jsonString = pref.getString(mapKey, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    outputMap.put(key, (String) jsonObject.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

    private void saveMap(HashMap<String, String> inputMap) {

        if (pref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            editor.remove(mapKey).apply();
            editor.putString(mapKey, jsonString);
            editor.commit();
        }
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
