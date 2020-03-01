package com.example.girass;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.girass.model.ZikrObject;


public class ZikrDetails extends Fragment {

    private TextView zikr, narriated, timeToRepeat, countingText;
    private LinearLayout click;
    private ImageView roundedButton;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private MediaPlayer defualt;
    private Boolean doIPlaySound, doIVibrate;
    private ProgressBar progressBar;
    private Typeface defaultFont;
    private int TextSize, countNumber = 1;
    private ZikrObject zikrObject;

    private Vibrator vibrator;

    //------------Constructor -----------------
    public ZikrDetails(ZikrObject zikrObject) {
        this.zikrObject = zikrObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_zikr_details, container, false);

        zikr = rootView.findViewById(R.id.zikr);
        narriated = rootView.findViewById(R.id.narriated);
        timeToRepeat = rootView.findViewById(R.id.time_repeat);
        click = rootView.findViewById(R.id.click);
        countingText = rootView.findViewById(R.id.counting);
        roundedButton = rootView.findViewById(R.id.rounded_button);
        progressBar = rootView.findViewById(R.id.progress);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        //----------------------------Click button---------------------------------
      /*  ShapeDrawable shapedrawable = new ShapeDrawable();
        shapedrawable.setShape(new OvalShape());
        shapedrawable.getPaint().setColor(Color.TRANSPARENT);
        shapedrawable.getPaint().setStrokeWidth(10);
        shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
        click.setBackground(shapedrawable);*/
     /* GradientDrawable gradientDrawable = (GradientDrawable) click.getDrawable();
      gradientDrawable.setStroke(10,Color.GRAY);*/
//--------------------SharedPreference-----------------------------


        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();


        if (pref != null) {
            defualt = MediaPlayer.create(getContext(), pref.getInt("defaultSound", R.raw.click));
            doIPlaySound = pref.getBoolean("masbahaSound", true);
            doIVibrate = pref.getBoolean("masbahaVibrate", true);
            if (pref.getString("defaultFont", "regular").equals("regular"))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    defaultFont = getResources().getFont(R.font.tajawal_regular);
                } else if (pref.getString("defaultFont", "bold").equals("bold"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        defaultFont = getResources().getFont(R.font.tajwal_bold);
                    } else if (pref.getString("defaultFont", "light").equals("light"))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            defaultFont = getResources().getFont(R.font.tajawal_light);
                        }
            TextSize = pref.getInt("fontSize", 18);

        } else {
            defualt = MediaPlayer.create(getContext(), R.raw.click);
            doIVibrate = true;
            doIPlaySound = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                defaultFont = getResources().getFont(R.font.tajawal_regular);
            }
            TextSize = 15;
            editor.putInt("defaultSound", R.raw.click);
            editor.putBoolean("masbahaSound", true);
            editor.putBoolean("masbahaVibrate", true);
            editor.putString("defaultFont", "regular");
            editor.putInt("fontSize", TextSize);
            editor.commit();
        }


        //---------------------retrieve zikr------------------------


        zikr.setText(zikrObject.Details);
        narriated.setText(zikrObject.Narriated);
        timeToRepeat.setText(Integer.valueOf(zikrObject.TimesToRepeat).toString());


        textStyle();
        int repeatingNumber = Integer.valueOf(zikrObject.TimesToRepeat);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countNumber <= repeatingNumber) {
                    countingText.setText(String.valueOf(countNumber));

                    /*    todo:use progress bar
                     */
                    progressBar.setProgress(10);
                    countNumber++;
                }
                if (doIPlaySound)
                    defualt.start();
                if (doIVibrate)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.EFFECT_TICK));
                    } else
                        vibrator.vibrate(500);

            }
        });
        return rootView;
    }

    private void textStyle() {
        zikr.setTypeface(defaultFont);
        narriated.setTypeface(defaultFont);

        zikr.setTextSize(TextSize);
        narriated.setTextSize(TextSize);
    }
}
