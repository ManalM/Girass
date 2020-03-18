package com.example.girass;


import android.app.Dialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageButton;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.girass.Data.DataService;
import com.example.girass.adapters.AdapterAzkar;

import static android.content.Context.MODE_PRIVATE;


public class MasbahaFragment extends Fragment implements View.OnClickListener {


    private ImageButton resetCount, chooseZikr, subhaBtn;
    private LinearLayout tasbihLayout;
    private int count = 0, theCount = 0;
    private Boolean doIPlaySound;
    private Boolean doIVibrate;
    private TextView noOfTasih, firstZikr, secZikr, thirdZikr;
    private final static String MY_PREFS = "MY_PREFS";
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private String zikr, chozenZikr = "";
    private String[] headZikrObjects;
    private Dialog dialog;
    private Vibrator v;
    private MediaPlayer pop, menu;
    private SoundPool soundPool;
    private int load;

    private SharedPreferences.Editor temporarySharedEditor;
    private SharedPreferences temporaryShared;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_masbaha, container, false);
        /////////////////////////////////////////////////
        /////////////  initiate variables  /////////////
        ///////////////////////////////////////////////
        //---------------- SharedPreference settings -------------------------------------------
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        temporaryShared = getContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        temporarySharedEditor = getContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        if (prefs != null) {

            load = soundPool.load(getContext(), prefs.getInt("defaultSound", R.raw.click), 1);
            doIPlaySound = prefs.getBoolean("masbahaSound", true);
            doIVibrate = prefs.getBoolean("masbahaVibrate", true);

        } else {
            load = soundPool.load(getContext(), R.raw.click, 1);

            doIVibrate = true;
            doIPlaySound = true;
            editor.putInt("defaultSound", R.raw.click);
            editor.putBoolean("masbahaSound", true);
            editor.putBoolean("masbahaVibrate", true);
            editor.apply();
        }

//-------------------------------------------------------------------
        pop = MediaPlayer.create(getContext(), R.raw.pop);
        menu = MediaPlayer.create(getContext(), R.raw.menu);
        //click = MediaPlayer.create(getContext(), R.raw.click);


        v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        resetCount = (ImageButton) rootView.findViewById(R.id.reset);
        chooseZikr = (ImageButton) rootView.findViewById(R.id.choose_btn);
        subhaBtn = (ImageButton) rootView.findViewById(R.id.subha_btn);
        tasbihLayout = rootView.findViewById(R.id.tasbih_layout);
        noOfTasih = (TextView) rootView.findViewById(R.id.no_tasbih);
        firstZikr = (TextView) rootView.findViewById(R.id.fisrt_zikr);
        secZikr = (TextView) rootView.findViewById(R.id.sec_zikr);
        thirdZikr = (TextView) rootView.findViewById(R.id.third_zikr);

        chozenZikr = prefs.getString("chooseZikr", null);


        ////////////////////////////////////////////////////////////////////
        /////////////  show List Azkar using ListDialog class /////////////
        //////////////////////////////////////////////////////////////////

        resetCount.setOnClickListener(this);
        chooseZikr.setOnClickListener(this);

        subhaBtn.setOnClickListener(this);
        tasbihLayout.setOnClickListener(this);


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
                setResetCount();
                break;
            case R.id.choose_btn:

                chooseZikr();
                break;
            case R.id.subha_btn:
                counting();
                break;
            case R.id.tasbih_layout:
                counting();
                break;

        }
    }

    private void chooseZikr() {
        if (doIPlaySound)
            soundPool.play(load, 1, 1, 0, 0, 1);

        if (doIVibrate)
            v.vibrate(100);
        showDialog();

    }

    private void counting() {


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

        editor.putInt("theCount", theCount);
        editor.putInt("count", count);
        editor.apply();
    }


    private void vibrateAndSounds() {


        /*************************************
         ****  Vibration and sound      *****
         ****  depending on the count   *****
         *************************************/


        if (doIVibrate == false)
            System.out.println("No Vibrate");
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (doIVibrate && theCount == 33)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.EFFECT_HEAVY_CLICK));
            else if (doIVibrate && theCount == 66)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount == 99)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount == 100)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount == 200)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount == 300)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount == 400)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount == 500)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount == 600)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount == 700)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount == 800)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount == 900)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else if (doIVibrate && theCount >= 1000)
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            else {
                v.vibrate(VibrationEffect.createOneShot(100, 1));

            }

        } else {
            if (doIVibrate && theCount == 33)
                v.vibrate(500);
            else if (doIVibrate && theCount == 66)
                v.vibrate(500);
            else if (doIVibrate && theCount == 99)
                v.vibrate(500);
            else if (doIVibrate && theCount == 100)
                v.vibrate(500);
            else if (doIVibrate && theCount == 200)
                v.vibrate(500);
            else if (doIVibrate && theCount == 300)
                v.vibrate(500);
            else if (doIVibrate && theCount == 400)
                v.vibrate(500);
            else if (doIVibrate && theCount == 500)
                v.vibrate(500);
            else if (doIVibrate && theCount == 600)
                v.vibrate(500);
            else if (doIVibrate && theCount == 700)
                v.vibrate(500);
            else if (doIVibrate && theCount == 800)
                v.vibrate(500);
            else if (doIVibrate && theCount == 900)
                v.vibrate(500);
            else if (doIVibrate && theCount >= 1000)
                v.vibrate(500);
            else {
                v.vibrate(100);

            }
        }

        if (doIPlaySound == false)
            System.out.println("No Sound");
        else if (doIPlaySound && theCount == 33) {
            pop.start();
        } else if (doIPlaySound && theCount == 66) {
            pop.start();
        } else if (doIPlaySound && theCount == 99) {
            pop.start();
        } else if (doIPlaySound && theCount == 100)
            menu.start();
        else if (doIPlaySound && theCount == 200)
            menu.start();
        else if (doIPlaySound && theCount == 300)
            menu.start();
        else if (doIPlaySound && theCount == 400)
            menu.start();
        else if (doIPlaySound && theCount == 500)
            menu.start();
        else if (doIPlaySound && theCount == 600)
            menu.start();
        else if (doIPlaySound && theCount == 700)
            menu.start();
        else if (doIPlaySound && theCount == 800)
            menu.start();
        else if (doIPlaySound && theCount == 900)
            menu.start();
        else if (doIPlaySound && theCount >= 1000) {
            menu.start();
        } else {

            soundPool.play(load, 1, 1, 0, 0, 1);

        }

    }

    private void setResetCount() {

        /*************************************
         ****         reset button        *****
         ****  reset count and textViews  *****
         *************************************/

        if (doIVibrate)
            v.vibrate(100);
        if (doIPlaySound)
            soundPool.play(load, 1, 1, 0, 0, 1);
        // do nt forget to play sound depends on settings
        count = 0;
        theCount = 0;
        noOfTasih.setText(" ");

        firstZikr.setText("");
        secZikr.setText("");
        thirdZikr.setText("");
    }


    private void showDialog() {
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
                    noOfTasih.setTextColor(getResources().getColor(R.color.secondText));
                    noOfTasih.setTextSize(20);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        temporarySharedEditor.putInt("theCount", theCount);
        temporarySharedEditor.putString("first", firstZikr.getText().toString());
        temporarySharedEditor.putString("sec", secZikr.getText().toString());
        temporarySharedEditor.putString("third", thirdZikr.getText().toString());
        temporarySharedEditor.putString("text", noOfTasih.getText().toString());
        temporarySharedEditor.putInt("count", count);
        temporarySharedEditor.apply();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (temporaryShared != null) {
            count = temporaryShared.getInt("count", 0);
            theCount = temporaryShared.getInt("theCount", 0);


            if (theCount > 0) {

                noOfTasih.setTextSize(40);
                noOfTasih.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            if (count == 0) {
                if (theCount == 0) {
                    noOfTasih.setText(temporaryShared.getString("text", ""));
                } else

                    noOfTasih.setText(String.valueOf(theCount));

                firstZikr.setText(temporaryShared.getString("first", ""));

                secZikr.setText(temporaryShared.getString("sec", ""));
                thirdZikr.setText(temporaryShared.getString("third", ""));

            } else if (count == 3) {

                noOfTasih.setText(String.valueOf(theCount));

                firstZikr.setText(temporaryShared.getString("first", ""));

                secZikr.setText(temporaryShared.getString("sec", ""));
                thirdZikr.setText(temporaryShared.getString("third", ""));

                count = 0;

            } else {
                noOfTasih.setText(String.valueOf(theCount));

                firstZikr.setText(temporaryShared.getString("first", ""));

                secZikr.setText(temporaryShared.getString("sec", ""));
                thirdZikr.setText(temporaryShared.getString("third", ""));
            }

        }
    }
}

