package com.example.girass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SplashScreen extends AppCompatActivity {

    ImageView star, name;
    TextView label1, label2;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Fragment selected = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
// TODO: background size of the splash screen
        star = findViewById(R.id.star);
        name = findViewById(R.id.name);
        label1 = findViewById(R.id.label1);
        label2 = findViewById(R.id.label2);


        pref = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);

        editor  = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this).edit();
        if (pref != null) {
            if (pref.getString("launcher", "Zikr").equals("Zikr"))
                selected = new AzkarFragment();
            else if (pref.getString("launcher", "Masbaha").equals("Masbaha"))

                selected = new MasbahaFragment();
            else if (pref.getString("launcher", "Fav").equals("Fav"))

                selected = new FavoriteFragment();
        } else {
            selected = new AzkarFragment();
            editor.putString("launcher", "ZIkr");
            editor.apply();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation a = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.txt_anim);
                a.setDuration(1000);
                star.startAnimation(a);

                name.startAnimation(a);
                label1.startAnimation(a);
                label2.startAnimation(a);


                startActivity(new Intent(SplashScreen.this, MainActivity.class));

                finish();
            }
        }, 4000);
    }
}
