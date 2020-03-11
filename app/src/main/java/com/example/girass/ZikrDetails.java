package com.example.girass;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.girass.Data.DataService;
import com.example.girass.model.HeadZikrObject;
import com.example.girass.model.ObjectSerializer;
import com.example.girass.model.PrefMethods;
import com.example.girass.model.ZikrObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;
import static com.example.girass.AllZikr.title;


public class ZikrDetails extends Fragment {

    private TextView zikr, narriated, timeToRepeat, countingText;
    private LinearLayout click;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private MediaPlayer defualt, likeMedia;
    private Boolean doIPlaySound, doIVibrate, likeSound;
    private CircularProgressBar progressBar;
    private Typeface defaultFont;
    private int TextSize, countNumber = 1;
    private ZikrObject zikrObject;
    public static ImageView like;
    private Vibrator vibrator;
    String ZikrId = "";

    private Boolean isLiked = false;

    ImageView animeHeart;

    ArrayList<String> favArray;
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
        like = rootView.findViewById(R.id.like);
        zikr = rootView.findViewById(R.id.zikr);
        narriated = rootView.findViewById(R.id.narriated);
        timeToRepeat = rootView.findViewById(R.id.time_repeat);
        click = rootView.findViewById(R.id.click);
        countingText = rootView.findViewById(R.id.counting);
        progressBar = rootView.findViewById(R.id.progress);
        animeHeart = rootView.findViewById(R.id.anim_heart);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        likeMedia = MediaPlayer.create(getContext(), R.raw.pop);

        //----------------------------progress ---------------------------------

        progressBar.setVisibility(View.VISIBLE);


//--------------------SharedPreference-----------------------------


        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();

        if (pref != null) {
            defualt = MediaPlayer.create(getContext(), pref.getInt("defaultSound", R.raw.click));
            doIPlaySound = pref.getBoolean("masbahaSound", true);
            doIVibrate = pref.getBoolean("masbahaVibrate", true);
            likeSound = pref.getBoolean("generalSound", true);
            if (pref.getString("defaultFont", "regular").equals("regular"))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    defaultFont = getResources().getFont(R.font.tajawal_regular);
                } else if (pref.getString("defaultFont", "bold").equals("bold"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        defaultFont = getResources().getFont(R.font.tajawal_bold);
                    } else if (pref.getString("defaultFont", "light").equals("light"))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            defaultFont = getResources().getFont(R.font.tajawal_light);
                        }
            TextSize = pref.getInt("fontSize", 22);

        } else {
            likeSound = true;
            defualt = MediaPlayer.create(getContext(), R.raw.click);
            doIPlaySound = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                defaultFont = getResources().getFont(R.font.tajawal_regular);
            }
            TextSize = 22;
            editor.putInt("defaultSound", R.raw.click);
            editor.putBoolean("masbahaSound", true);
            editor.putBoolean("masbahaVibrate", true);
            editor.putString("defaultFont", "regular");
            editor.putInt("fontSize", TextSize);
            editor.commit();
        }


        //---------------------retrieve zikr------------------------


        favArray = new PrefMethods(getContext()).getArray();


        for (int i = 0; i < favArray.size(); i++) {

            if (favArray.contains(title)) {

                like.setImageResource(R.drawable.fill_heart);
                isLiked = true;
            } else {
                like.setImageResource(R.drawable.fav_heart);
                isLiked = false;
            }

        }
        editor.putBoolean("like", isLiked);
        //---------------------check if fav--------------------------
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeSound)
                    likeMedia.start();

                //-- animate heart ---------
                Animation likeAnim = AnimationUtils.loadAnimation(getContext(), R.anim.heart_beat);
                animeHeart.setVisibility(View.VISIBLE);
                animeHeart.startAnimation(likeAnim);
                likeAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animeHeart.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                //---- add to fav-----

                for (int i = 0; i < headZikrObjects.length; i++) {
                    if (headZikrObjects[i].TITLE.equals(title)) {
                        ZikrId = headZikrObjects[i].ID;

                        if (!favArray.contains(title)) {

                            like.setImageResource(R.drawable.fill_heart);
                            favArray.add(title);

                        } else {
                            like.setImageResource(R.drawable.fav_heart);
                            favArray.remove(title);


                        }
                    }
                }
                new PrefMethods(getContext()).saveArray(favArray);
            }
        });


        //---------------- change text view--------------------
        zikr.setText(zikrObject.Details);
        narriated.setText(zikrObject.Narriated);
        timeToRepeat.setText(Integer.valueOf(zikrObject.TimesToRepeat).toString());


        //---------------style of text depends on settings---------------
        textStyle();
        //--------------------------- counting button -------------
        int repeatingNumber = Integer.valueOf(zikrObject.TimesToRepeat);
        progressBar.setMax(repeatingNumber);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countNumber <= repeatingNumber) {
                    countingText.setText(String.valueOf(countNumber));
                    progressBar.setProgress(countNumber);
                    countNumber++;
                }
                if (doIPlaySound)
                    defualt.start();
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
