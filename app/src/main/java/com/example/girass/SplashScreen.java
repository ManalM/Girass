package com.example.girass;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView star , name ;
    TextView label1 , label2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        star = findViewById(R.id.star);
        name = findViewById(R.id.name);
        label1 = findViewById(R.id.label1);
        label2 = findViewById(R.id.label2);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation a = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.txt_anim);
                a.setDuration(1000);
                star.startAnimation(a);

                name.startAnimation(a);
                label1.startAnimation(a);
                label2.startAnimation(a);


                startActivity(new Intent(SplashScreen.this,MainActivity.class));

                finish();
            }
        }, 4000);
    }
}
