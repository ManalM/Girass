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

    // minSdkVersion preferred to be 23
    // app should not supportsRtl
    LinearLayout bgimg;
    private Bitmap nav; // the image in the Bitmap format
    private Bitmap background; // background in the Bitmap format
    private BitmapDrawable bg; // background in the Drawable format
    Dialog aboutDialog;
    private TextView www, phone, twitter, email;
    private ImageButton aboutApp, aboutClose;
    private ImageView background_img, www_img, email_img, phone_img, twitter_img , dozo; // the image

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            aboutApp();
            //   aboutApp();
        }catch (OutOfMemoryError e ){
            Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void aboutApp() {
        aboutDialog = new Dialog(Notes.this);
        aboutDialog.setContentView(R.layout.about_dialog);
        aboutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        aboutClose = aboutDialog.findViewById(R.id.about_close);
        phone = aboutDialog.findViewById(R.id.phone);
        www = aboutDialog.findViewById(R.id.www);
        twitter = aboutDialog.findViewById(R.id.twitter);
        email = aboutDialog.findViewById(R.id.email);
        aboutApp = aboutDialog.findViewById(R.id.about_app);
        www_img = aboutDialog.findViewById(R.id.www_img);
        phone_img = aboutDialog.findViewById(R.id.phone_img);
        twitter_img = aboutDialog.findViewById(R.id.twitter_img);
        email_img = aboutDialog.findViewById(R.id.email_img);
        background_img = aboutDialog.findViewById(R.id.background);
        dozo = aboutDialog.findViewById(R.id.dozo);

        Glide.with(this).load(R.drawable.www).into(www_img);
        Glide.with(this).load(R.drawable.phone).into(phone_img);
        Glide.with(this).load(R.drawable.email).into(email_img);
        Glide.with(this).load(R.drawable.twitter).into(twitter_img);
        Glide.with(this).load(R.drawable.ic_cancel_black_24dp).into(aboutClose);
        Glide.with(this).load(R.drawable.i).into(aboutApp);
        Glide.with(this).load(R.drawable.dozo1).into(dozo);

        loadBitmap(R.id.www_img, www_img);
        loadBitmap(R.id.phone_img, phone_img);
        loadBitmap(R.id.twitter_img, twitter_img);
        loadBitmap(R.id.email_img, email_img);
        loadBitmap(R.id.about_app, aboutApp);
        loadBitmap(R.id.about_close, aboutClose);

        loadBackground(R.id.background, background_img);
        twitter.setText("@dozo_apps");

        aboutClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutDialog.dismiss();
            }
        });
        www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + www.getText()));
                startActivity(browserIntent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email.getText()));
                try {
                    startActivity(Intent.createChooser(emailIntent, "إرسال ايميل :"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Notes.this, "لايوجد برنامج للإرسال الايميل", Toast.LENGTH_SHORT).show();
                }
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Notes.this);
                builder.setTitle(R.string.call_title);

                builder.setMessage(R.string.call_message);
                builder.setPositiveButton("اتصال", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri number = Uri.parse("tel:" + phone.getText());
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        PackageManager packageManager = Notes.this.getPackageManager();
                        List activities = packageManager.queryIntentActivities(callIntent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                        boolean isIntentSafe = ((List) activities).size() > 0;

                        if (isIntentSafe)
                            try {
                                startActivity(callIntent);
                            } catch (ActivityNotFoundException e) {
                                String message = e.getMessage();
                                Toast.makeText(Notes.this, "Error:" + message, Toast.LENGTH_LONG).show();
                            }
                    }
                });
                builder.setNegativeButton("واتس أب", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setDataAndType(Uri.parse("https://api.whatsapp.com/send?phone=966567636391"), "text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "السلام عليكم ...");
                        //sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.whatsapp");
                        // sendIntent.setPackage("com.twitter");
                        startActivity(sendIntent);
                    }
                });
                builder.show();

            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter.getText().toString()));
                browserIntent.setPackage("com.twitter");
                startActivity(browserIntent);
            }
        });
        aboutDialog.show();

    }

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
