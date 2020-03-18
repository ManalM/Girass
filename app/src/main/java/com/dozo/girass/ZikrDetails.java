package com.dozo.girass;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dozo.girass.model.ZikrObject;
import com.dozo.girass.R;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class ZikrDetails extends Fragment {

    private TextView zikr, narriated, timeToRepeat, countingText;
    private LinearLayout click;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SoundPool defualt;
    private Boolean doIPlaySound, doIVibrate;
    private CircularProgressBar progressBar;
    private Typeface defaultFont;
    private int TextSize, countNumber = 0;
    private ZikrObject zikrObject;

    private Vibrator vibrator;

    private int load;

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
        progressBar = rootView.findViewById(R.id.progress);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        //----------------------------progress ---------------------------------

        progressBar.setVisibility(View.VISIBLE);


//--------------------SharedPreference-----------------------------


        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        defualt = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        if (pref != null) {
            load = defualt.load(getContext(), pref.getInt("defaultSound", R.raw.click), 1);
            doIPlaySound = pref.getBoolean("masbahaSound", true);
            doIVibrate = pref.getBoolean("masbahaVibrate", true);

            if (pref.getString("defaultFont", "regular").equals("regular"))
                defaultFont = Typeface.createFromAsset(getContext().getAssets(),
                        "fonts/tajawal_regular.ttf");
            else if (pref.getString("defaultFont", "bold").equals("bold"))
                defaultFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
            else if (pref.getString("defaultFont", "light").equals("light"))
                defaultFont = Typeface.createFromAsset(getContext().getAssets(),
                        "fonts/sans-serif.ttf");
            TextSize = pref.getInt("fontSize", 22);

        } else {
            load = defualt.load(getContext(), R.raw.click, 1);

            doIPlaySound = true;
            defaultFont = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/tajawal_regular.ttf");

            TextSize = 22;
            editor.putInt("defaultSound", R.raw.click);
            editor.putBoolean("masbahaSound", true);
            editor.putBoolean("masbahaVibrate", true);
            editor.putString("defaultFont", "regular");
            editor.putInt("fontSize", TextSize);
            editor.commit();
        }


        //---------------- change text view--------------------
        zikr.setText(zikrObject.Details);
        narriated.setText(zikrObject.Narriated);
        timeToRepeat.setText(Integer.valueOf(zikrObject.TimesToRepeat).toString());


        //---------------style of text depends on settings---------------
        textStyle();
        //--------------------------- counting button -------------
        int repeatingNumber = zikrObject.TimesToRepeat;
        progressBar.setMax(repeatingNumber);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countNumber < repeatingNumber) {
                    countingText.setText(String.valueOf(countNumber + 1));
                    progressBar.setProgress(countNumber + 1);

                }
                countNumber++;

                if (countNumber == repeatingNumber) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // move to next page
                            AllZikr.moveToNext();
                        }
                    }, 600);


                }

                if (doIPlaySound)
                    defualt.play(load, 1, 1, 0, 0, 1);
                if (doIVibrate)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(100, 1));
                    } else
                        vibrator.vibrate(100);


            }
        });

        return rootView;
    }


    //-----------------------methods--------------------------
    private void textStyle() {
        zikr.setTypeface(defaultFont);
        narriated.setTypeface(defaultFont);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            zikr.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            narriated.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        zikr.setTextSize(TextSize);
        narriated.setTextSize(TextSize);
    }


}
