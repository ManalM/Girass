package com.example.girass;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageButton;

import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


public class MasbahaFragment extends Fragment implements View.OnClickListener {


    private ImageButton resetCount, chooseZikr, subhaBtn;
    private int count = 0, theCount = 0;
    private Boolean doIPlaySound = SettingsFragments.Masbahasound, selected = false;
    private Boolean doIVibrate = SettingsFragments.Masbahavibrate;
    private TextView noOfTasih, firstZikr, secZikr, thirdZikr;
    private final static String MY_PREFS = "MY_PREFS";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    String zikr, chozenZikr = "";
    private String[] headZikrObjects;

    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_masbaha, container, false);


        /////////////////////////////////////////////////
        /////////////  initiate variables  /////////////
        ///////////////////////////////////////////////


        resetCount = (ImageButton) rootView.findViewById(R.id.reset);
        chooseZikr = (ImageButton) rootView.findViewById(R.id.choose_btn);
        subhaBtn = (ImageButton) rootView.findViewById(R.id.subha_btn);

        noOfTasih = (TextView) rootView.findViewById(R.id.no_tasbih);
        firstZikr = (TextView) rootView.findViewById(R.id.fisrt_zikr);
        secZikr = (TextView) rootView.findViewById(R.id.sec_zikr);
        thirdZikr = (TextView) rootView.findViewById(R.id.third_zikr);
        editor = getContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        prefs = getContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        chozenZikr = prefs.getString("chooseZikr", null);


        ////////////////////////////////////////////////////////////////////
        /////////////  show List Azkar using ListDialog class /////////////
        //////////////////////////////////////////////////////////////////


        resetCount.setOnClickListener(this);
        chooseZikr.setOnClickListener(this);

        subhaBtn.setOnClickListener(this);


        return rootView;
    }

    ///////////////////////////////////////////////
    /////////////  Buttons Functions /////////////
    /////////////////////////////////////////////

    @Override
    public void onClick(View v) {

        /*************************************
         ****        buttons  function    ****
         *************************************/
        switch (v.getId()) {

            case R.id.reset:
                // do nt forget to play sound depends on settings
                setResetCount();
                break;
            case R.id.choose_btn:

                chooseZikr();
                break;
            case R.id.subha_btn:
                counting();
                break;


        }
    }

    private void chooseZikr() {
        if (doIPlaySound)
            SettingsFragments.defualtSound.start();
        showDialog();

    }

    public void counting() {


        /****************************************
         **** Subha button and animate the  *****
         **** text depending on the count   *****
         ****************************************/

        theCount++;
        count++;
        vibrateAndSounds();
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.txt_anim);
        subhaBtn.startAnimation(a);
        zikr = prefs.getString("chooseZikr", chozenZikr);

        noOfTasih.setText(String.valueOf(theCount));
        noOfTasih.setTextSize(40);
        noOfTasih.setTextColor(getResources().getColor(R.color.colorAccent));
        if (count == 0) {

            firstZikr.setText("");
            secZikr.setText("");
            thirdZikr.setText("");
            count++;

        } else if (count == 1) {
            firstZikr.startAnimation(a);

            firstZikr.setText(zikr);
            secZikr.setText("");
            thirdZikr.setText("");
        } else if (count == 2) {
            secZikr.startAnimation(a);

            secZikr.setText(zikr);

        } else if (count == 3) {

            thirdZikr.startAnimation(a);
            thirdZikr.setText(zikr);
            count = 0;

        }

    }


    private void vibrateAndSounds() {


        /*************************************
         ****  Vibration and sound      *****
         ****  depending on the count   *****
         *************************************/


        MediaPlayer pop = MediaPlayer.create(getContext(), R.raw.pop);
        MediaPlayer menu = MediaPlayer.create(getContext(), R.raw.menu);
        MediaPlayer click = MediaPlayer.create(getContext(), R.raw.click);


        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        if (!doIVibrate)
            System.out.println("No Vibrate");
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (doIVibrate && count == 33)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.EFFECT_HEAVY_CLICK));
            else if (doIVibrate && count == 66)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count == 99)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count == 100)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count == 200)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count == 300)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count == 400)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count == 500)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count == 600)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count == 700)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count == 800)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count == 900)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && count >= 1000)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else {
                v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.EFFECT_TICK));

            }

        } else {
            if (doIVibrate && count == 33)
                v.vibrate(500);
            else if (doIVibrate && count == 66)
                v.vibrate(500);
            else if (doIVibrate && count == 99)
                v.vibrate(500);
            else if (doIVibrate && count == 100)
                v.vibrate(500);
            else if (doIVibrate && count == 200)
                v.vibrate(500);
            else if (doIVibrate && count == 300)
                v.vibrate(500);
            else if (doIVibrate && count == 400)
                v.vibrate(500);
            else if (doIVibrate && count == 500)
                v.vibrate(500);
            else if (doIVibrate && count == 600)
                v.vibrate(500);
            else if (doIVibrate && count == 700)
                v.vibrate(500);
            else if (doIVibrate && count == 800)
                v.vibrate(500);
            else if (doIVibrate && count == 900)
                v.vibrate(500);
            else if (doIVibrate && count >= 1000)
                v.vibrate(500);
            else {
                v.vibrate(500);

            }
        }

        if (!doIPlaySound)
            System.out.println("No Sound");
        else if (doIPlaySound && count == 33)
            pop.start();
        else if (doIPlaySound && count == 66)
            pop.start();
        else if (doIPlaySound && count == 99)
            pop.start();
        else if (doIPlaySound && count == 100)
            menu.start();
        else if (doIPlaySound && count == 200)
            menu.start();
        else if (doIPlaySound && count == 300)
            menu.start();
        else if (doIPlaySound && count == 400)
            menu.start();
        else if (doIPlaySound && count == 500)
            menu.start();
        else if (doIPlaySound && count == 600)
            menu.start();
        else if (doIPlaySound && count == 700)
            menu.start();
        else if (doIPlaySound && count == 800)
            menu.start();
        else if (doIPlaySound && count == 900)
            menu.start();
        else if (doIPlaySound && count >= 1000)
            menu.start();
        else SettingsFragments.defualtSound.start(); /// change it after settings page < depends on user choice;


    }

    public void setResetCount() {

        /*************************************
         ****         reset button        *****
         ****  reset count and textViews  *****
         *************************************/


        if (doIPlaySound)
            SettingsFragments.defualtSound.start();
        // do nt forget to play sound depends on settings
        count = 0;
        theCount = 0;
        noOfTasih.setText(" ");

        firstZikr.setText("");
        secZikr.setText("");
        thirdZikr.setText("");
    }


    public void showDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog2);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView mRecyclerView;

        mRecyclerView = dialog.findViewById(R.id.dialog_list);
        DataService dataService = new DataService();
        headZikrObjects = dataService.GetChosenAzkar();
        AdapterAzkar mAdapterAzkar = new AdapterAzkar(getContext(), headZikrObjects);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setAdapter(mAdapterAzkar);
        mAdapterAzkar.setOnItemClickListener(new AdapterAzkar.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                chozenZikr = headZikrObjects[position];

                editor.putString("chooseZikr", chozenZikr);
                editor.apply();


                if (prefs.getString("chooseZikr", chozenZikr) != null) {
                    noOfTasih.setText(chozenZikr);
                    noOfTasih.setTextColor(getResources().getColor(R.color.firstText));
                    noOfTasih.setTextSize(15);// change with settings
                }
                firstZikr.setText("");
                secZikr.setText("");
                thirdZikr.setText("");
                count = 0;
                theCount = 0;
                dialog.dismiss();
                dialog.cancel();

            }
        });

        dialog.show();
    }


}
