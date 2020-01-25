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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URISyntaxException;
import java.util.zip.Inflater;

public class AllZikr extends FragmentActivity {
    private Toolbar toolbar;
    private TextView toolbarText;

   // protected static int num_pages=1;


    private ImageView backBtn;
    private PagerAdapter pagerAdapter;
    private ViewPager mPager;
    public static String title;

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

        Intent intent = getIntent();
         title = intent.getStringExtra("array");
        toolbarText.setText(title);
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();

        //--------------------------------------------------------


        mPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        pagerAdapter.notifyDataSetChanged();


        //------------------------------------------------------------------
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AzkarFragment()).commit();
                //startActivity(new Intent(AllZikr.this,AzkarFragment.class));
            }
        });

        //------------------------------------------------------------------



        mPager.setAdapter(pagerAdapter);


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


 class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

int num_pages;
LinearLayout mLayoutInflater;
/*     private Toolbar toolbar;
     private TextView toolbarText;

     // protected static int num_pages=1;


     private ImageView backBtn;
     String title;*/
  //  int no_pages = AllZikr.num_pages;
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
     public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

       //  Fragment f = (Fragment) object;
    container.removeView((RelativeLayout) object);
         super.destroyItem(container, position, object);
     }

     @Override
    public Fragment getItem(int position) {

   return new ZikrDetails();

    }

    @Override
    public int getCount() {
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
     //       notifyDataSetChanged();
        for (int i = 0; i < headZikrObjects.length; i++) {

            if (headZikrObjects[i].TITLE.equals(AllZikr.title)) {

                HeadZikrObject h = headZikrObjects[i];
                // toolbarText.setText(h.TITLE);
                num_pages = h.AllAzkar.length;

                break;
            }
        }
        return num_pages;
    }

}

