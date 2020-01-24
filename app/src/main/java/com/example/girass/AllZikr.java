package com.example.girass;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AllZikr extends FragmentActivity {
    private Toolbar toolbar;
    private TextView toolbarText;

    protected static int num_pages;
    private ViewPager mPager;

    private ImageView backBtn;
    private PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_zikr);

        backBtn = findViewById(R.id.button);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbarText = findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        backBtn.setImageResource(R.drawable.left_arrow);
        //--------------------------------------------------------
        mPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

        //------------------------------------------------------------------
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AzkarFragment()).commit();
            }
        });

        //------------------------------------------------------------------

        Intent intent = getIntent();
        String title = intent.getStringExtra("array");
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
        for (int i = 0; i < headZikrObjects.length; i++) {

            if (headZikrObjects[i].TITLE.equals(title)) {

                HeadZikrObject h = headZikrObjects[i];
                toolbarText.setText(h.TITLE);
                num_pages = h.AllAzkar.length;


            }
        }
    }

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

}


 class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new ZikrDetails();
    }

    @Override
    public int getCount() {
        return AllZikr.num_pages;
    }
}

