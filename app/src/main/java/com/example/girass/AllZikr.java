package com.example.girass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import android.widget.TextView;


import com.example.girass.Data.DataService;

import com.example.girass.model.HeadZikrObject;
import com.example.girass.model.ZikrObject;



import java.util.ArrayList;


import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.Config;

public class AllZikr extends Fragment implements ViewPager.OnPageChangeListener {
    private Toolbar toolbar;
    private TextView toolbarText;


    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    private ImageView backBtn;
    private PagerAdapter pagerAdapter;
    private ViewPager mPager;
    private CircleIndicator circleIndicator;
    public static String title;
    Typeface defaultFont;


    private int tag;
    private int indicatorNumber;
    public static ArrayList<String> favoriteAzkar;


    private HorizontalScrollView hzScrollView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_all_zikr, container, false);
        //-----------------------------------------------------------
        backBtn = rootView.findViewById(R.id.button);
        toolbar = (Toolbar) rootView.findViewById(R.id.bar);
        toolbarText = rootView.findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        backBtn.setImageResource(R.drawable.left_arrow);
        //----------------------------------------------------------
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
        //--------------------------------------------------------

        title = getArguments().getString("array");
        tag = getArguments().getInt("tag");

        toolbarText.setText(title);


        //------------------SharedPreference---------------------------
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();

        if (pref != null) {


                if (pref.getString("defaultFont", "regular").equals("regular"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        defaultFont = getResources().getFont(R.font.tajawal_regular);
                    }

                else if (pref.getString("defaultFont", "bold").equals("bold"))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            defaultFont = getResources().getFont(R.font.tajawal_bold);
                        } else if (pref.getString("defaultFont", "light").equals("light"))
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                defaultFont = getResources().getFont(R.font.tajawal_light);
                            }


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                defaultFont = getResources().getFont(R.font.tajawal_regular);
            }
        }
        editor.putString("defaultFont", "regular");


        toolbarText.setTypeface(defaultFont);

        //----------------------viewPager and indicator---------------------

        hzScrollView = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);

        mPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        circleIndicator = rootView.findViewById(R.id.indicator);
        circleIndicator.setViewPager(mPager);


        pagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        Config config = new Config.Builder()
                .drawable(R.drawable.indicator_shape)
                .build();
        circleIndicator.initialize(config);
        for (int i = 0; i < headZikrObjects.length; i++) {

            if (headZikrObjects[i].TITLE.equals(AllZikr.title))
                indicatorNumber = headZikrObjects[i].AllAzkar.length;

        }
        if (indicatorNumber > 1)
            circleIndicator.createIndicators(indicatorNumber, 0);

        setHzScrollViewCenter(circleIndicator);
        //------------------------------------------------------------------
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //------------------------add to fav-----------------------------------


        favoriteAzkar = new ArrayList<>();

        mPager.setAdapter(pagerAdapter);
        mPager.addOnPageChangeListener(this);

        return rootView;
    }

/*
    todo:horizontal ScroolView
*/
private void setHzScrollViewCenter(View view) {
    //smoothly set horizontalScrollView to centerLock
    int screenWidth = mPager.getWidth();
    int scrollX = (view.getLeft() - (screenWidth / 2)) + (view.getWidth() / 2);
    hzScrollView.smoothScrollTo(scrollX, 0);

}


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void onBackPressed() {
        if (tag == 1)
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AzkarFragment()).addToBackStack(null).commit();
        else if (tag == 2)
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoriteFragment()).addToBackStack(null).commit();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        circleIndicator.animatePageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}


class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
    DataService dataService = new DataService();
    final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
    public static int num_pages;

    ZikrObject[] z;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {


        for (int i = 0; i < headZikrObjects.length; i++) {

            if (headZikrObjects[i].TITLE.equals(AllZikr.title))
                z = headZikrObjects[i].AllAzkar;

        }
        return new ZikrDetails(z[position]);

    }


    @Override
    public int getCount() {

        for (int i = 0; i < headZikrObjects.length; i++) {

            if (headZikrObjects[i].TITLE.equals(AllZikr.title))
                num_pages = headZikrObjects[i].AllAzkar.length;

        }
        return num_pages;
    }

}

