package com.example.girass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

public class Notes extends AppCompatActivity {


    //TODO: organise layout list_item

    //--------------------Drafts-----------------------------
    LinearLayout bgimg;
    private Bitmap nav; // the image in the Bitmap format
    private Bitmap background; // background in the Bitmap format
    private BitmapDrawable bg; // background in the Drawable format
    Dialog aboutDialog;
    private TextView www, phone, twitter, email,desc;
    private ImageButton aboutApp, aboutClose;
    private ImageView background_img, www_img, email_img, phone_img, twitter_img , dozo; // the image

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            //aboutApp();
        }catch (OutOfMemoryError e ){
            Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    //--------------------------------------------------------


    public void loadBitmap(int id, ImageView img) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        nav = BitmapFactory.decodeStream(getResources().openRawResource(id));
        img.setImageBitmap(nav);
    }

    public void loadBackground(int id, ImageView bgimg) {
        background = BitmapFactory.decodeStream(getResources().openRawResource(id));
        bg = new BitmapDrawable(background);
        bgimg.setBackgroundDrawable(bg);
    }
}
