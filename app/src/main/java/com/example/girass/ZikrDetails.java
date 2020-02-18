package com.example.girass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ZikrDetails extends Fragment {

    TextView zikr, narriated, timeToRepeat;
    ImageView click, repeat;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    MediaPlayer defualt;
    Boolean doIPlaySound, doIVibrate;

    Typeface defaultFont;

    int TextSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_zikr_details, container, false);

        zikr = rootView.findViewById(R.id.zikr);
        narriated = rootView.findViewById(R.id.narriated);
        timeToRepeat = rootView.findViewById(R.id.time_repeat);
        ///-------------------------------------------
        click = rootView.findViewById(R.id.click);
        repeat = rootView.findViewById(R.id.repeat);

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
            if(pref.getString("defaultFont","regular").equals("regular"))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    defaultFont= getResources().getFont(R.font.tajawal_regular);
                }

            else if (pref.getString("defaultFont","bold").equals("bold"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        defaultFont= getResources().getFont(R.font.tajwal_bold);
                    }
            else if(pref.getString("defaultFont","light").equals("light"))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            defaultFont= getResources().getFont(R.font.tajawal_light);
                        }
            TextSize  = pref.getInt("fontSize",18);

        } else {
            defualt = MediaPlayer.create(getContext(), R.raw.click);
            doIVibrate = true;
            doIPlaySound = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                defaultFont= getResources().getFont(R.font.tajawal_regular);
            }
            TextSize = 15;
            editor.putInt("defaultSound", R.raw.click);
            editor.putBoolean("masbahaSound", true);
            editor.putBoolean("masbahaVibrate", true);
            editor.putString("defaultFont", "regular");
            editor.putInt("fontSize",TextSize);
            editor.commit();
        }


        //-------------------------------------------------------

        //---------------------------------------------------------
        String title;
    /*  Intent intent = getActivity().getIntent();
        if(intent !=null)
  title = intent.getStringExtra("array");*/
        //  title = getArguments() != null ? getArguments().getString("array") : null;
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
        for (int i = 0; i < headZikrObjects.length; i++) {

            if (headZikrObjects[i].TITLE.equals(AllZikr.title)) {

                //    HeadZikrObject h = headZikrObjects[i];
                ZikrObject[] zikrObject = headZikrObjects[i].AllAzkar;

                //      int index =new ZikrDetails().getId();
                for (int j = 0; j < zikrObject.length; j++) {


                    //Toast.makeText(getContext(), "this is "+ new ZikrDetails().getId(), Toast.LENGTH_SHORT).show();

                    zikr.setText(zikrObject[j].Details);
                    narriated.setText(zikrObject[j].Narriated);
                    timeToRepeat.setText(Integer.valueOf(zikrObject[j].TimesToRepeat).toString());

                    //------------------------Settings ------------------

                    textStyle();
                }


                break;
            } else
                zikr.setText("null");
        }

        return rootView;
    }

    private void textStyle(){
        zikr.setTypeface(defaultFont);
        narriated.setTypeface(defaultFont);

        zikr.setTextSize(TextSize);
        narriated.setTextSize(TextSize);
    }
}
