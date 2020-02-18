package com.example.girass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.girass.Preference.SharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.zip.Inflater;

public class AllZikr extends Fragment {
    private Toolbar toolbar;
    private TextView toolbarText;

    // protected static int num_pages=1;

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    private ImageView backBtn, like;
    private PagerAdapter pagerAdapter;
    private ViewPager mPager;
    public static String title;
    Boolean isLiked = false;
    String id;
    final String mapKey = "map";
    //SharedPreference sharedPreferences;
    private Context context;
    int tag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_all_zikr, container, false);
        //-----------------------------------------------------------
        backBtn = rootView.findViewById(R.id.button);
        toolbar = (Toolbar) rootView.findViewById(R.id.bar);
        toolbarText = rootView.findViewById(R.id.toolbar_title);
        like = rootView.findViewById(R.id.like);
        toolbar.setTitle("");
        backBtn.setImageResource(R.drawable.left_arrow);
        //--------------------------------------------------------

        title = getArguments().getString("array");
        id = getArguments().getString("id");
        tag = getArguments().getInt("tag");

        toolbarText.setText(title);


        //------------------SharedPreference---------------------------
        //sharedPreferences =  new SharedPreference(context);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();

        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
        HashMap<String, String> hashMap = loadMap();
//todo: make each item sparate from others , user can delete from the fav layout
        String[] IDs = new String[hashMap.size()];
        for (int i = 0; i < hashMap.size(); i++) {

            if (hashMap.containsKey(id + 1)) {
                if (pref.getBoolean("liked", true) == false) {
                    like.setImageResource(R.drawable.fav_heart);

                } else {
                    like.setImageResource(R.drawable.fill_heart);

                }
            } else {
                like.setImageResource(R.drawable.fav_heart);
                Toast.makeText(getContext(), "hash map doesn't contain ID" + id, Toast.LENGTH_SHORT).show();
            }

        }
      /*  for (String s: pref.getStringSet("set",set))
        {*/
        // if (pref.getStringSet("set", set).contains(id)) {

      /*  } else {
            like.setImageResource(R.drawable.fav_heart);
            Toast.makeText(this, "not equal", Toast.LENGTH_SHORT).show();
        }*/
        //   }

        //--------------------------------------------------------

        mPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        pagerAdapter.notifyDataSetChanged();


        //------------------------------------------------------------------
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // Objects.requireNonNull(getActivity()).onBackPressed();

            }
        });

        //------------------------------------------------------------------


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked == false) {
                    like.setImageResource(R.drawable.fill_heart);
                    Toast.makeText(getContext(), title + id, Toast.LENGTH_SHORT).show();

                    HashMap<String, String> hashMap = new HashMap<>();
                    for (int i = 0; i < headZikrObjects.length; i++) {
                        if (headZikrObjects[i].TITLE.equals(title) && headZikrObjects[i].ID.equals(id)) {
                            hashMap.put(headZikrObjects[i].ID, headZikrObjects[i].TITLE);

                            saveMap(hashMap);
                            Toast.makeText(getContext(), "added" + headZikrObjects[i].ID + headZikrObjects[i].TITLE, Toast.LENGTH_SHORT).show();
                        }
                    }
                    editor.putBoolean("liked", true);
                    isLiked = true;
                } else {
                    like.setImageResource(R.drawable.fav_heart);
                    HashMap<String, String> hashMap = new HashMap<>();
                    for (int i = 0; i < headZikrObjects.length; i++) {
                        if (headZikrObjects[i].TITLE.equals(title) && headZikrObjects[i].ID.equals(id)) {
                            hashMap.put(headZikrObjects[i].ID, headZikrObjects[i].TITLE);

                            deleteMapItem();
                            Toast.makeText(getContext(), "deleted" + headZikrObjects[i].ID + headZikrObjects[i].TITLE, Toast.LENGTH_SHORT).show();
                        }
                    }
                    isLiked = false;
                    editor.putBoolean("liked", false);
                }
                editor.putString("id", id);
                editor.apply();


            }
        });

        mPager.setAdapter(pagerAdapter);


        return rootView;
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

    private void deleteMapItem() {

        if (pref != null) {
            editor.remove(mapKey).apply();

            editor.clear();
            editor.commit();
        }

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void onBackPressed() {
  /*      if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.

            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }*/
        if (tag == 1)
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AzkarFragment()).addToBackStack(null).commit();
        else if (tag == 2)
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoriteFragment()).addToBackStack(null).commit();

    }

}


class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
    DataService dataService = new DataService();
    final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
    public static int num_pages;
    ZikrObject views;
    LinearLayout mLayoutInflater;
    ArrayList<ZikrObject> arrayList = new ArrayList<>();

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

/*     @NonNull
     @Override
     public Object instantiateItem(@NonNull ViewGroup container, int position) {

             View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);


         backBtn = itemView.findViewById(R.id.button);
         toolbar = (Toolbar) itemView.findViewById(R.id.main_toolbar);
         toolbarText = itemView.findViewById(R.id.toolbar_title);
         toolbar.setTitle("");
         backBtn.setImageResource(R.drawable.left_arrow);
         //--------------------------------------------------------

         Intent intent = null;
         try {
             intent = Intent.getIntent("");
         } catch (URISyntaxException e) {
             e.printStackTrace();
         }
         title = intent.getStringExtra("array");
         toolbarText.setText(title);
             container.addView(itemView);

             return itemView;

     }*/


    @Override
    public int getItemPosition(Object object) {
/*         for(int index = 0 ; index < getCount() ; index++){
             if(object.equals(views.ID.equals(index))) {
                 return index;
             }
         }
         return POSITION_NONE;*/
        arrayList = fetchArray();
        Fragment fragment = (Fragment) object;

        int position = arrayList.indexOf(fragment);
        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }

    }


    @Override
    public Fragment getItem(int position) {


        return new ZikrDetails();

    }


    @Override
    public int getCount() {

        for (int i = 0; i < headZikrObjects.length; i++) {

            if (headZikrObjects[i].TITLE.equals(AllZikr.title))
                num_pages = headZikrObjects[i].AllAzkar.length;

            //    break;
        }
        return num_pages;
    }

    public ArrayList<ZikrObject> fetchArray() {
        ArrayList<ZikrObject> a = new ArrayList<>();
        for (int i = 0; i < headZikrObjects.length; i++) {
            if (headZikrObjects[i].TITLE.equals(AllZikr.title)) {

                ZikrObject[] zikrObject = headZikrObjects[i].AllAzkar;
                for (int j = 0; j < zikrObject.length; j++) {


                    a.add(zikrObject[j]);

                }


            }
        }
        return a;
    }
}

