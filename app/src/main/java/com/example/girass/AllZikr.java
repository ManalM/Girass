package com.example.girass;

import androidx.annotation.NonNull;
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
import java.util.Set;
import java.util.zip.Inflater;

public class AllZikr extends FragmentActivity {
    private Toolbar toolbar;
    private TextView toolbarText;

    // protected static int num_pages=1;

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    private ImageView backBtn, like;
    private PagerAdapter pagerAdapter;
    private ViewPager mPager;
    public static String title;
    private BottomNavigationView bottomNavigationView;
    Boolean isLiked = false;
    String id;
    final String mapKey = "map";
    //SharedPreference sharedPreferences;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_zikr);

        //-----------------------------------------------------------
        backBtn = findViewById(R.id.button);
        toolbar = (Toolbar) findViewById(R.id.bar);
        toolbarText = findViewById(R.id.toolbar_title);
        like = findViewById(R.id.like);
        toolbar.setTitle("");
        backBtn.setImageResource(R.drawable.left_arrow);
       /* bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);*/
        //--------------------------------------------------------

        Intent intent = getIntent();
        title = intent.getStringExtra("array");
        id = intent.getStringExtra("id");
        toolbarText.setText(title);


        //------------------SharedPreference---------------------------
        //sharedPreferences =  new SharedPreference(context);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
      /*  for (String s: pref.getStringSet("set",set))
        {*/
        // if (pref.getStringSet("set", set).contains(id)) {
            if (pref.getBoolean("liked", true) == false) {
                like.setImageResource(R.drawable.fav_heart);

            } else {
                like.setImageResource(R.drawable.fill_heart);

            }
      /*  } else {
            like.setImageResource(R.drawable.fav_heart);
            Toast.makeText(this, "not equal", Toast.LENGTH_SHORT).show();
        }*/
        //   }

        //--------------------------------------------------------

        mPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        pagerAdapter.notifyDataSetChanged();


        //------------------------------------------------------------------
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //------------------------------------------------------------------


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked == false) {
                    like.setImageResource(R.drawable.fill_heart);
                    Toast.makeText(AllZikr.this, title + id, Toast.LENGTH_SHORT).show();

                    HashMap<String, String> hashMap = new HashMap<>();
                    for (int i = 0; i < headZikrObjects.length; i++) {
                        if (headZikrObjects[i].TITLE.equals(title) && headZikrObjects[i].ID.equals(id)) {
                            hashMap.put(headZikrObjects[i].ID, headZikrObjects[i].TITLE);

                            saveMap(hashMap);
                            Toast.makeText(AllZikr.this, "added" + headZikrObjects[i].ID + headZikrObjects[i].TITLE, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AllZikr.this, "deleted" + headZikrObjects[i].ID + headZikrObjects[i].TITLE, Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

/*
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
*/
/*
 private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;

        switch (menuItem.getItemId()) {
            case R.id.azkar:
                selectedFragment = new AzkarFragment();
                break;
            case R.id.favorite:
                selectedFragment = new FavoriteFragment();
                break;
            case R.id.settings:
                selectedFragment = new SettingsFragments();
                break;
            case R.id.masbaha:
                selectedFragment = new MasbahaFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
        return true;
    }
};
*/

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

            num_pages = headZikrObjects[i].AllAzkar.length;

            break;
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

