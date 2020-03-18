package com.dozo.girass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.dozo.girass.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    int id = 0;
    View decoreView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-------------------------control navigation bar---------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.textColor));
        }

        //--------------------------------------------------------------------
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
        if (pref != null) {
            if (pref.getString("launcher", "Zikr").equals("Zikr")) {
                selectedFragment = new AzkarFragment();
                id = R.id.azkar;
            } else if (pref.getString("launcher", "Masbaha").equals("Masbaha")) {
                selectedFragment = new MasbahaFragment();
                id = R.id.masbaha;
            } else if (pref.getString("launcher", "Fav").equals("Fav")) {
                selectedFragment = new FavoriteFragment();
                id = R.id.favorite;
            }
        } else {
            selectedFragment = new AzkarFragment();
            editor.putString("launcher", "ZIkr");
            editor.apply();
        }


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
        bottomNavigationView.setSelectedItemId(id);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


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



    }

