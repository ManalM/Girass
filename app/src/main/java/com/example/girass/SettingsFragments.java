package com.example.girass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.android.material.internal.CircularBorderDrawable;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragments extends Fragment implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView toolbarText;
    LinearLayout general, masbaha, notification, share, rate, about;
    private Dialog dialog, aboutDialog, detailsDialog;
    private CardView generalCard , masbahaCard;
    private Switch activate, morning, evening, sleep, wakeup, reminder ,masbahaSound,masbahaVibrate,generalSound,generalVibrate;
    private ImageButton aboutApp, close, aboutClose;
    private TextView www, phone, twitter, email, desc,
            morningTime, eveningTime, sleepTime, wakeTime, reminderTime,
            sound1 ,sound2,sound3,sound4,
    font1 , font2,font3,
    luncherMasbaha ,lucherfav,luncherZikr;
    private ImageView background_img, www_img, email_img, phone_img, twitter_img, dozo; // the image
    private ImageView generalArrow , masbahaArrow;
    public static MediaPlayer defualtSound;
    public  static  Boolean Masbahavibrate = true , Masbahasound=true
            ,GeneralSound= true , Generalvibrate= true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings_fragments, container, false);


        /////////////////////////////////////////////////
        /////////////  initiate variables  /////////////
        ///////////////////////////////////////////////


        general = (LinearLayout) rootView.findViewById(R.id.general_setting);
        masbaha = (LinearLayout) rootView.findViewById(R.id.masbaha_setting);
        notification = (LinearLayout) rootView.findViewById(R.id.notification);
        share = (LinearLayout) rootView.findViewById(R.id.share);
        rate = (LinearLayout) rootView.findViewById(R.id.rate);
        about = (LinearLayout) rootView.findViewById(R.id.about);
        generalCard = (CardView) rootView.findViewById(R.id.general_card);
        masbahaCard = (CardView) rootView.findViewById(R.id.masbaha_card);
        generalArrow = (ImageView) rootView.findViewById(R.id.general_arrow);
        masbahaArrow  = (ImageView) rootView.findViewById(R.id.masbaha_arrow);
        masbahaSound = (Switch) rootView.findViewById(R.id.masbaha_sounds);
        masbahaVibrate = (Switch) rootView.findViewById(R.id.masbaha_vibrate);
        generalSound = (Switch) rootView.findViewById(R.id.general_sounds);
        generalVibrate =  (Switch) rootView.findViewById(R.id.general_vibrate);
        font1= (TextView)   rootView.findViewById(R.id.font1);
        font2= (TextView)   rootView.findViewById(R.id.font2);
        font3= (TextView)   rootView.findViewById(R.id.font3);
        lucherfav = (TextView)   rootView.findViewById(R.id.luncher_fav);
        luncherMasbaha=(TextView)   rootView.findViewById(R.id.luncher_masbaha);

        luncherZikr
                =(TextView)   rootView.findViewById(R.id.luncher_zikr);
        sound1 = (TextView) rootView.findViewById(R.id.sound1);
        sound2 = (TextView) rootView.findViewById(R.id.sound2);
        sound3 = (TextView) rootView.findViewById(R.id.sound3);
        sound4 = (TextView) rootView.findViewById(R.id.sound4);
        defualtSound= MediaPlayer.create(getContext(),R.raw.click);

        /////////////////////////////////////////////
        /////////////     ToolBar       ////////////
        //////////////////////////////////////////


        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        toolbarText = rootView.findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText(R.string.settings);

        general.setOnClickListener(this);
        masbaha.setOnClickListener(this);
        notification.setOnClickListener(this);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);
        about.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {


        /*************************************
         ****        buttons  function    ****
         *************************************/


        switch (v.getId()) {

            case R.id.general_setting:
                if (generalCard.getVisibility() ==View.GONE){
                    generalCard.setVisibility(View.VISIBLE);
                      Glide.with(getContext()).load(R.drawable.up_arrow).into(generalArrow);
                      GenetalSetting();
                }else{
                    generalCard.setVisibility(View.GONE);
                       Glide.with(getContext()).load(R.drawable.down_arrow).into(generalArrow);
                }

                break;

            case R.id.masbaha_setting:
                if (masbahaCard.getVisibility() ==View.GONE){
                    masbahaCard.setVisibility(View.VISIBLE);
                      Glide.with(getContext()).load(R.drawable.up_arrow).into(masbahaArrow);
                      MasbahaSetting();
                }else{
                    masbahaCard.setVisibility(View.GONE);
                    Glide.with(getContext()).load(R.drawable.down_arrow).into(masbahaArrow);
                }
                break;
            case R.id.notification:
                showDialog();
                break;
            case R.id.share:
                shareApp();
                break;
            case R.id.rate:
                rateApp();
                break;
            case R.id.about:
                try {

                    // startActivity(new Intent(getContext(), Notes.class));
                    aboutApp();
                } catch (OutOfMemoryError e) {
                    Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private void GenetalSetting() {


        boolean VibrationChecked =generalVibrate.isChecked();
        boolean SoundsChecked = generalSound.isChecked();

        if(VibrationChecked == true){

           Generalvibrate= true;
        }else{
            Generalvibrate= false;
        }

        if(SoundsChecked == true){

            GeneralSound= true;
            defualtSound.start();
        }else {
            GeneralSound = false;
        }



    }

    private void MasbahaSetting() {

        boolean VibrationChecked = masbahaVibrate.isChecked();
        boolean SoundsChecked = masbahaSound.isChecked();

        if(VibrationChecked == true){

            Masbahavibrate = true;
        }else{
            Masbahavibrate = false;
        }

        if(SoundsChecked == true){

         Masbahasound= true;
        }else {
            Masbahasound = false;
        }

        final MediaPlayer mediaSound1 , mediaSound2 , mediaSound3,mediaSound4;
        mediaSound1 = MediaPlayer.create(getContext() , R.raw.click2);
        mediaSound2 = MediaPlayer.create(getContext() , R.raw.pop);
        mediaSound3 = MediaPlayer.create(getContext() , R.raw.click);
        mediaSound4 = MediaPlayer.create(getContext() , R.raw.menu2);



       sound1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(Masbahasound == true)
               mediaSound1.start();
               defualtSound= mediaSound1;

           }
       });

       sound2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(Masbahasound == true)
                   mediaSound2.start();
               defualtSound= mediaSound2;

           }
       });
        sound3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Masbahasound == true)
                    mediaSound3.start();
                defualtSound= mediaSound3;

            }
        });

        sound4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Masbahasound == true)
                    mediaSound4.start();
                defualtSound= mediaSound4;

            }
        });
    }



    private void showDialog() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.notification_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ///-----------TextView--------------
        morningTime = dialog.findViewById(R.id.morning_time);
        eveningTime = dialog.findViewById(R.id.evening_time);
        sleepTime = dialog.findViewById(R.id.sleep_time);
        wakeTime = dialog.findViewById(R.id.wakeup_time);
        reminderTime = dialog.findViewById(R.id.reminder_time);

        //--------------------Switch------------
        activate = (Switch) dialog.findViewById(R.id.activate);
        evening = (Switch) dialog.findViewById(R.id.evening);
        morning = (Switch) dialog.findViewById(R.id.morning);
        sleep = (Switch) dialog.findViewById(R.id.sleep);
        wakeup = (Switch) dialog.findViewById(R.id.wakeup);
        reminder = (Switch) dialog.findViewById(R.id.reminder);

        close = (ImageButton) dialog.findViewById(R.id.close);
        String[] VerityTimeArray= {

        "كل نصف ساعة",
                "كل ساعة",
                "كل ساعتين",
                "كل ثلاث ساعات",
                "كل أربع ساعات",
                "كل خمس ساعات",
                "كل ستة ساعات",
                "كل ١٢ ساعة",

    };


       int[] VerityIntervalArray  = {1800,
               3600,
               7200,
               10800,
               14400,
               18000,
               21600,
               43200} ;



        final int Minute= 60;
        final int HalfHour  = 1800;
        final int OneHoer = 3600;
        final int TowHoers = 7200;
        final int TreeHoers= 10800;
        final int FourHoers = 14400;
        final int FiveHoers  = 18000;
        final int SixHoers= 21600;
        final int HalfDay  = 43200;

        if (activate.isActivated()) {



        }
        try {
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dialog.show();
    }

    private void shareApp() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "تطبيق غراس الجنة ، حصن المسلم من أذكار اليوم و الليلة \n 🕌🕌🕌 " + "https://itunes.apple.com/us/app/girass/id1438981810?mt=8 \n 🕌🕌🕌 " + " \n www.dozo-apps.com ";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void rateApp() {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getContext().getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
        }
    }

    private void aboutApp() {


        aboutDialog = new Dialog(getContext());
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
        dozo = aboutDialog.findViewById(R.id.dozo);
        desc = aboutDialog.findViewById(R.id.desc);
        //-------------------------------------------------------

        www_img.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        twitter_img.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //  Glide.with(this).load(R.drawable.www).into(www_img);
        Glide.with(this).load(R.drawable.phone).into(phone_img);
        Glide.with(this).load(R.drawable.email1).into(email_img);
        // Glide.with(this).load(R.drawable.twitter).into(twitter_img);
        Glide.with(this).load(R.drawable.ic_cancel_black_24dp).into(aboutClose);
        Glide.with(this).load(R.drawable.i).into(aboutApp);
        //    Glide.with(this).load(R.drawable.dozo1).into(dozo);
        //     Glide.with(this).load(R.drawable.dark_back).into(background_img);

        twitter.setText("@dozo_apps");
        desc.setText("تمت برمجة هذا التطبيق في معامل دوزو \n \n إذا كان لديك اقتراح أو فكرة تطبيق تريد أن \n \n ننفذها لك فتواصل معنا");

        //------------------------------------------------------------------


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
                    Toast.makeText(getContext(), "لايوجد برنامج للإرسال الايميل", Toast.LENGTH_SHORT).show();
                }
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.call_title);

                builder.setMessage(R.string.call_message);
                builder.setPositiveButton("اتصال", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri number = Uri.parse("tel:" + phone.getText());
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        PackageManager packageManager = getContext().getPackageManager();
                        List activities = packageManager.queryIntentActivities(callIntent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                        boolean isIntentSafe = ((List) activities).size() > 0;

                        if (isIntentSafe)
                            try {
                                startActivity(callIntent);
                            } catch (ActivityNotFoundException e) {
                                String message = e.getMessage();
                                Toast.makeText(getContext(), "Error:" + message, Toast.LENGTH_LONG).show();
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
                        try {
                            startActivity(sendIntent);
                        } catch (ActivityNotFoundException e) {
                            String message = e.getMessage();

                            Toast.makeText(getContext(), "Error:" + message, Toast.LENGTH_LONG).show();


                        }
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
                try {
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    String message = e.getMessage();

                    Toast.makeText(getContext(), "Error:" + message, Toast.LENGTH_LONG).show();

                }

            }
        });

        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                detailsDialog = new Dialog(getContext());
                detailsDialog.setContentView(R.layout.details_dialog);
                //    CardView card =detailsDialog.findViewById(R.id.card);

                ///    card.setRadius(9);
                detailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                detailsDialog.show();
            }
        });
        aboutDialog.show();

    }


}
