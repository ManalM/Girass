package com.example.girass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.fonts.FontFamily;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.android.material.internal.CircularBorderDrawable;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import hotchemi.stringpicker.StringPicker;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragments extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView toolbarText;
    LinearLayout general, masbaha, notification, share, rate, about,
            notificationLinear;
    private Dialog dialog, aboutDialog, detailsDialog;
    private CardView generalCard, masbahaCard;
    private Switch activate, morning, evening, sleep, wakeup, reminder, masbahaSound, masbahaVibrate, generalSound, generalVibrate;
    private ImageButton aboutApp, close, aboutClose;
    private TextView www, phone, twitter, email, desc,
            morningTime, eveningTime, sleepTime, wakeTime, reminderTime,
            sound1, sound2, sound3, sound4,
            font1, font2, font3,
            luncherMasbaha, lucherfav, luncherZikr,
            textSize, fontType;
    private ImageView background_img, www_img, email_img, phone_img, twitter_img, dozo; // the image
    private ImageView generalArrow, masbahaArrow;

    private SeekBar seekBar;
    //-------------------------------------------------------------
    private MediaPlayer mediaSound1, mediaSound2, mediaSound3, mediaSound4;
    //-------------------------------------------------------------

    public static MediaPlayer defualtSound;
    public static Typeface defualtFont;
    public static int TextSize;
    public static Boolean Masbahavibrate, Masbahasound, GeneralSound, Generalvibrate, checked;
    //-------------------------------------------------------------
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    //----------------------------------------
    String format;
    Calendar now;
    int hourOfDay, min;
    int hour, minute;
    final String[] VerityTimeArray = {

            "كل نصف ساعة",
            "كل ساعة",
            "كل ساعتين",
            "كل ثلاث ساعات",
            "كل أربع ساعات",
            "كل خمس ساعات",
            "كل ستة ساعات",
            "كل ١٢ ساعة",

    };

    static String chozenReminderTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings_fragments, container, false);

        Locale locale = new Locale("ar");
        Configuration configuration = getContext().getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);

            getContext().createConfigurationContext(configuration);
        }


        /////////////////////////////////////////////////
        /////////////  initiate variables  /////////////
        ///////////////////////////////////////////////


        //-------------------------------------------------------------

        general = (LinearLayout) rootView.findViewById(R.id.general_setting);
        masbaha = (LinearLayout) rootView.findViewById(R.id.masbaha_setting);
        notification = (LinearLayout) rootView.findViewById(R.id.notification);
        share = (LinearLayout) rootView.findViewById(R.id.share);
        rate = (LinearLayout) rootView.findViewById(R.id.rate);
        about = (LinearLayout) rootView.findViewById(R.id.about);
        //--------------------- Cards ---------------------------------

        //TODO:text of switches
        generalCard = (CardView) rootView.findViewById(R.id.general_card);
        masbahaCard = (CardView) rootView.findViewById(R.id.masbaha_card);
        generalArrow = (ImageView) rootView.findViewById(R.id.general_arrow);
        masbahaArrow = (ImageView) rootView.findViewById(R.id.masbaha_arrow);
        masbahaSound = (Switch) rootView.findViewById(R.id.masbaha_sounds);
        masbahaVibrate = (Switch) rootView.findViewById(R.id.masbaha_vibrate);
        generalSound = (Switch) rootView.findViewById(R.id.general_sounds);
        generalVibrate = (Switch) rootView.findViewById(R.id.general_vibrate);
        //------------------------ Fonts -------------------------------
// TODO: rectangle corner design
        font1 = (TextView) rootView.findViewById(R.id.font1);
        font2 = (TextView) rootView.findViewById(R.id.font2);
        font3 = (TextView) rootView.findViewById(R.id.font3);
        //-------------------------------------------------------------
        // TODO: rectangle corner design

        lucherfav = (TextView) rootView.findViewById(R.id.luncher_fav);
        luncherMasbaha = (TextView) rootView.findViewById(R.id.luncher_masbaha);

        luncherZikr = (TextView) rootView.findViewById(R.id.luncher_zikr);
        //------------------------- Sounds -------------------------------
// TODO: rectangle corner design

        sound1 = (TextView) rootView.findViewById(R.id.sound1);
        sound2 = (TextView) rootView.findViewById(R.id.sound2);
        sound3 = (TextView) rootView.findViewById(R.id.sound3);
        sound4 = (TextView) rootView.findViewById(R.id.sound4);

        //--------------------- Defaults ------------------------------

       /* defualtSound = MediaPlayer.create(getContext(), R.raw.click);
         defualtFont =Typeface.createFromAsset(getContext().getAssets(),"tajawal_regular.ttf");//tajawal_regular*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            defualtFont = getResources().getFont(R.font.tajawal_regular);
        }
        //  defualtFont= Typeface.createFromFile("font/tajawal_regular");
        //defualtFont =    Typeface.createFromFile("font/tajawal_regular");
        //-------------------------------------------------------------
        textSize = (TextView) rootView.findViewById(R.id.text_size);
        fontType = (TextView) rootView.findViewById(R.id.FontType);
        //TODO:the range of the textSize
        textSize.setTypeface(defualtFont);
        seekBar = rootView.findViewById(R.id.seek_bar);

        fontType.setTypeface(defualtFont);
        TextSize = 15;
        //TODO: color of the seekbar
        seekBar.setProgress((int) TextSize);
        textSize.setTextSize(TypedValue.COMPLEX_UNIT_PX, seekBar.getProgress());
        //-------------------------------------------------------------
        now = Calendar.getInstance();
        hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        min = now.get(Calendar.MINUTE);

        /////////////////////////////////////////////
        /////////////     ToolBar       ////////////
        //////////////////////////////////////////


        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        toolbarText = rootView.findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText(R.string.settings);

        //--------------------- SharedPreference ----------------------------
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();

        if (pref != null) {
            masbahaSound.setChecked(pref.getBoolean("masbahaSound", true));
            masbahaVibrate.setChecked(pref.getBoolean("masbahaVibrate", true));
            generalSound.setChecked(pref.getBoolean("generalSound", true));
            generalVibrate.setChecked(pref.getBoolean("generalVibrate", true));
            if (pref.getString("generalCardVisibility", "visible").equals("visible"))
                generalCard.setVisibility(View.VISIBLE);
            else if (pref.getString("generalCardVisibility", "gone").equals("gone"))
                generalCard.setVisibility(View.GONE);

            if (pref.getString("masbahaCardVisibility", "visible").equals("visible"))
                generalCard.setVisibility(View.VISIBLE);
            else if (pref.getString("masbahaCardVisibility", "gone").equals("gone"))
                generalCard.setVisibility(View.GONE);

            TextSize = pref.getInt("fontSize", 18);

   /*         if (pref.getBoolean("NotificationLinearVisibility", true)) {
                activate.setChecked(true);
                notificationLinear.setVisibility(View.VISIBLE);
            } else {
                activate.setChecked(false);
                notificationLinear.setVisibility(View.GONE);
            }*/
        } else {
            masbahaVibrate.setChecked(true);
            Masbahavibrate = true;

            masbahaSound.setChecked(true);
            Masbahasound = true;

            generalVibrate.setChecked(true);
            Generalvibrate = true;

            generalSound.setChecked(true);
            GeneralSound = true;

            generalCard.setVisibility(View.GONE);
            masbahaCard.setVisibility(View.GONE);

            TextSize = 18;

          /*  activate.setChecked(true);
            notificationLinear.setVisibility(View.VISIBLE);*/
            editor.putBoolean("masbahaSound", Masbahasound);
            editor.putBoolean("masbahaVibrate", Masbahavibrate);
            editor.putBoolean("generalSound", GeneralSound);
            editor.putBoolean("generalVibrate", Generalvibrate);
            editor.putString("generalCardVisibility", "gone");
            editor.putString("masbahaCardVisibility", "gone");
            editor.putInt("fontSize", TextSize);
            editor.apply();
        }

        changeTextViewsBackground();

        //-----------------------------------------------------------

        general.setOnClickListener(this);
        masbaha.setOnClickListener(this);
        notification.setOnClickListener(this);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);
        about.setOnClickListener(this);

        return rootView;
    }

    private void changeTextViewsBackground() {


        //----------------------- Masbaha--------------------
        if (pref.getInt("defaultSound", 0) == R.raw.click2)
            changeMasbahaTextView(sound1, sound2, sound3, sound4);
        else if (pref.getInt("defaultSound", 0) == R.raw.pop)
            changeMasbahaTextView(sound2, sound1, sound3, sound4);
        else if (pref.getInt("defaultSound", 0) == R.raw.click)
            changeMasbahaTextView(sound3, sound1, sound1, sound4);

        else if (pref.getInt("defaultSound", 0) == R.raw.menu2)
            changeMasbahaTextView(sound4, sound1, sound3, sound1);

        // -----------------------General fonts ---------------------------

        if (pref.getString("defaultFont", "bold").equals("bold"))
            changeGeneralTextView(font3, font2, font1);

        else if (pref.getString("defaultFont", "light").equals("light"))
            changeGeneralTextView(font2, font1, font3);

        else if (pref.getString("defaultFont", "regular").equals("regular"))
            changeGeneralTextView(font1, font2, font3);

        // -----------------------General launcher ---------------------------

        if (pref.getString("launcher", "Zikr").equals("Zikr"))
            changeGeneralTextView(luncherZikr, lucherfav, luncherMasbaha);
        else if (pref.getString("launcher", "Masbaha").equals("Masbaha"))
            changeGeneralTextView(luncherMasbaha, lucherfav, luncherZikr);

        else if (pref.getString("launcher", "Fav").equals("Fav"))
            changeGeneralTextView(lucherfav, luncherMasbaha, luncherZikr);

    }

    @Override
    public void onClick(View v) {


        /*************************************
         ****        buttons  function    ****
         *************************************/


        switch (v.getId()) {

            case R.id.general_setting:
                GenetalSetting();

                if (generalCard.getVisibility() == View.GONE) {
                    generalCard.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(R.drawable.up_arrow).into(generalArrow);
                    editor.putString("generalCardVisibility", "visible");
                    editor.commit();

                } else {
                    GenetalSetting();

                    generalCard.setVisibility(View.GONE);
                    Glide.with(getContext()).load(R.drawable.down_arrow).into(generalArrow);
                    editor.putString("generalCardVisibility", "gone");
                    editor.commit();
                }

                break;

            case R.id.masbaha_setting:
                if (masbahaCard.getVisibility() == View.GONE) {
                    masbahaCard.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(R.drawable.up_arrow).into(masbahaArrow);
                    editor.putString("masbahaCardVisibility", "visible");
                    editor.commit();
                    MasbahaSetting();

                } else {
                    masbahaCard.setVisibility(View.GONE);
                    Glide.with(getContext()).load(R.drawable.down_arrow).into(masbahaArrow);
                    editor.putString("masbahaCardVisibility", "gone");
                    editor.commit();
                    MasbahaSetting();

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


        generalVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Generalvibrate = true;
                    editor.putBoolean("generalVibrate", Generalvibrate);
                    editor.commit();
                } else {
                    Generalvibrate = false;
                    editor.putBoolean("generalVibrate", Generalvibrate);
                    editor.commit();
                }
            }
        });

        generalSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    GeneralSound = true;
                    editor.putBoolean("generalSound", GeneralSound);
                    editor.commit();
                } else {
                    GeneralSound = false;
                    editor.putBoolean("generalSound", GeneralSound);
                    editor.commit();
                }
            }
        });


        //----------------Launcher Listener-----------------------------
        luncherZikr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGeneralTextView(luncherZikr, lucherfav, luncherMasbaha);
                Toast.makeText(getContext(), "Zikr", Toast.LENGTH_SHORT).show();
                editor.putString("launcher", "Zikr");
                editor.commit();
            }
        });

        luncherMasbaha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGeneralTextView(luncherMasbaha, lucherfav, luncherZikr);
                editor.putString("launcher", "Masbaha");
                editor.commit();
            }
        });

        lucherfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGeneralTextView(lucherfav, luncherZikr, luncherMasbaha);
                editor.putString("launcher", "Fav");
                editor.commit();
            }
        });
        //---------------Fonts Listener---------------------------
        font1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  defualtFont = Typeface.createFromAsset(getContext().getAssets(),"font/tajawal_regular");
                changeGeneralTextView(font1, font2, font3);
                //   defualtFont = Typeface.createFromAsset(getContext().getAssets(),"tajawal_regular.ttf");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    defualtFont = getResources().getFont(R.font.tajawal_regular);
                } else {
                    //  defualtFont = Typeface.createFromAsset(getContext().getAssets(),"font/tajawal_regular.ttf");
                }
                fontType.setTypeface(defualtFont);
                editor.putString("defaultFont", "regular");
                editor.commit();
            }
        });
        font2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGeneralTextView(font2, font1, font3);

                //  defualtFont = Typeface.createFromAsset(getContext().getAssets(),"font/tajawal_light");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    defualtFont = getResources().getFont(R.font.tajawal_light);
                } else {
                    //       defualtFont = Typeface.createFromAsset(getContext().getAssets(),"font/tajawal_light.ttf");
                }
                fontType.setTypeface(defualtFont);

                editor.putString("defaultFont", "light");
                editor.commit();
            }
        });
        font3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGeneralTextView(font3, font2, font1);

                Toast.makeText(getContext(), "font3", Toast.LENGTH_SHORT).show();
                //     defualtFont = Typeface.createFromAsset(getContext().getAssets(),"font/tajwal_bold");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    defualtFont = getResources().getFont(R.font.tajwal_bold);
                } else {
                    //   defualtFont = Typeface.createFromAsset(getContext().getAssets(),"font/tajwal_bold.ttf");
                }
                fontType.setTypeface(defualtFont);

                editor.putString("defaultFont", "bold");
                editor.commit();

            }
        });
        //----------------------------------------------------

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSize.setTextSize(TypedValue.COMPLEX_UNIT_PX, progress);
                editor.putInt("fontSize", progress);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
    }

    private void MasbahaSetting() {
        mediaSound1 = MediaPlayer.create(getContext(), R.raw.click2);
        mediaSound2 = MediaPlayer.create(getContext(), R.raw.pop);
        mediaSound3 = MediaPlayer.create(getContext(), R.raw.click);
        mediaSound4 = MediaPlayer.create(getContext(), R.raw.menu2);
/*        boolean VibrationChecked = masbahaVibrate.isChecked();
        boolean SoundsChecked = masbahaSound.isChecked();*/

        masbahaVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Masbahavibrate = true;

                    editor.putBoolean("masbahaVibrate", Masbahavibrate);
                    editor.commit();
                } else {
                    Masbahavibrate = false;
                    editor.putBoolean("masbahaVibrate", Masbahavibrate);
                    editor.commit();
                }

            }
        });

        masbahaSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Masbahasound = true;

                    defualtSound.start();
                    editor.putBoolean("masbahaSound", Masbahasound);
                    editor.commit();
                } else {
                    Masbahasound = false;
                    defualtSound.start();
                    editor.putBoolean("masbahaSound", Masbahasound);
                    editor.commit();
                }
            }
        });


        sound1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMasbahaTextView(sound1, sound4, sound2, sound3);
                mediaSound1.start();

                if (pref.getBoolean("masbahaSound", true)) {
                    defualtSound = mediaSound1;
                    editor.putInt("defaultSound", R.raw.click2);
                    editor.commit();
                }

            }
        });

        sound2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMasbahaTextView(sound2, sound1, sound4, sound3);

                mediaSound2.start();
                if (pref.getBoolean("masbahaSound", true)) {
                    defualtSound = mediaSound2;
                    editor.putInt("defaultSound", R.raw.pop);
                    editor.commit();
                }
            }
        });
        sound3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMasbahaTextView(sound3, sound1, sound2, sound4);

                mediaSound3.start();
                if (pref.getBoolean("masbahaSound", true)) {
                    defualtSound = mediaSound3;
                    editor.putInt("defaultSound", R.raw.click);
                    editor.commit();
                }
            }
        });

        sound4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMasbahaTextView(sound4, sound1, sound2, sound3);

                mediaSound4.start();
                if (pref.getBoolean("masbahaSound", true)) {
                    defualtSound = mediaSound4;
                    editor.putInt("defaultSound", R.raw.menu2);
                    editor.commit();
                }
              /*  Bundle b = new Bundle();
                b.putInt();*/
            }
        });
    }


    private void showDialog() {
// notification part
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
        // TODO: text of switches and organise layout
        // TODO: text gravity right
        activate = (Switch) dialog.findViewById(R.id.activate);
        evening = (Switch) dialog.findViewById(R.id.evening);
        morning = (Switch) dialog.findViewById(R.id.morning);
        sleep = (Switch) dialog.findViewById(R.id.sleep);
        wakeup = (Switch) dialog.findViewById(R.id.wakeup);
        reminder = (Switch) dialog.findViewById(R.id.reminder);

        //--------------------general------------------
        notificationLinear = (LinearLayout) dialog.findViewById(R.id.linear_notification);
        close = (ImageButton) dialog.findViewById(R.id.close);

//-----------------------sharedPreference----------------------

        if (pref.getBoolean("NotificationLinearVisibility", true)) {
            activate.setChecked(true);

            checked = true;
            notificationLinear.setVisibility(View.VISIBLE);
            editor.putBoolean("NotificationLinearVisibility", checked);
            editor.commit();
        } else {
            activate.setChecked(false);

            checked = false;
            notificationLinear.setVisibility(View.GONE);
            editor.putBoolean("NotificationLinearVisibility", checked);
            editor.commit();
        }


        setTextOfTime("morning");
        setTextOfTime("evening");
        setTextOfTime("sleep");
        setTextOfTime("wakeup");
        setTextOfReminder();
//-------------------------------------------------------------------------


        int[] VerityIntervalArray = {1800,
                3600,
                7200,
                10800,
                14400,
                18000,
                21600,
                43200};


        final int Minute = 60;
        final int HalfHour = 1800;
        final int OneHoer = 3600;
        final int TowHoers = 7200;
        final int TreeHoers = 10800;
        final int FourHoers = 14400;
        final int FiveHoers = 18000;
        final int SixHoers = 21600;
        final int HalfDay = 43200;


        activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked = isChecked;
                    notificationLinear.setVisibility(View.VISIBLE);
                    setTextOfTime("morning");
                    setTextOfTime("evening");
                    setTextOfTime("sleep");
                    setTextOfTime("wakeup");
                    setTextOfReminder();
                    editor.putBoolean("NotificationLinearVisibility", checked);
                    editor.commit();
                } else {
                    checked = isChecked;
                    notificationLinear.setVisibility(View.GONE);
                    editor.putBoolean("NotificationLinearVisibility", checked);
                    editor.commit();
                }
            }
        });


        morning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            }
        });

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
//----------------------------show time dialog------------------
        morningTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerTime("morning");
            }
        });

        eveningTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerTime("evening");

            }
        });
        sleepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerTime("sleep");

            }
        });
        wakeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerTime("wakeup");

            }
        });
        reminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseReminderTime();
            }
        });
        dialog.show();
    }

    private void setTextOfReminder() {
        reminderTime.setText(pref.getString("chozenReminderTime", VerityTimeArray[0].toString()));
    }

    private void chooseReminderTime() {
        final Dialog reminderDialog = new Dialog(getContext());
        reminderDialog.setContentView(R.layout.reminder_time);
        reminderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ImageView cancel = reminderDialog.findViewById(R.id.cancel_reminder_picker);
        ImageView correct = reminderDialog.findViewById(R.id.correct_reminder_picker);
        final StringPicker stringPicker = (StringPicker) reminderDialog.findViewById(R.id.string_picker);

        stringPicker.setValues(VerityTimeArray);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reminderDialog.dismiss();
            }
        });

        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderDialog.dismiss();
                chozenReminderTime = stringPicker.getCurrentValue();
                reminderTime.setText(chozenReminderTime);
                editor.putString("chozenReminderTime", chozenReminderTime);
                editor.commit();
            }
        });
        reminderDialog.show();
    }

    private void setTextOfTime(String textView) {
        if (now.HOUR_OF_DAY == 0) {

            hourOfDay += 12;

            format = "AM";
        } else if (now.HOUR_OF_DAY == 12) {

            format = "PM";

        } else if (now.HOUR_OF_DAY > 12) {

            hourOfDay -= 12;

            format = "PM";

        } else {

            format = "AM";
        }
        min = now.MINUTE;

   /*     if (textView.equals("morning"))
            morningTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));
        else if (textView.equals("evening"))
            eveningTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));
        else if (textView.equals("sleep"))
            sleepTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));
        else if (textView.equals("wakeup"))
            wakeTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));
*/
        switch (textView) {
            case "morning":
                if (pref.getInt("minOf" + textView, min) <10 || pref.getInt("minOf" + textView, min) == 0 ) {
                    morningTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":0" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));

                } else
                    morningTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));

                break;
            case "evening":
                if (pref.getInt("minOf" + textView, min) <10 || pref.getInt("minOf" + textView, min) == 0 )
                    eveningTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":0" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));
                else
                    eveningTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));

                break;
            case "sleep":

                if (pref.getInt("minOf" + textView, min) <10 || pref.getInt("minOf" + textView, min) == 0 )
                    sleepTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":0" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));

                else
                    sleepTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));

                break;
            case "wakeup":
                if(pref.getInt("minOf" + textView, min) <10 || pref.getInt("minOf" + textView, min) == 0 )
                    wakeTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":0" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));
                else
                    wakeTime.setText((pref.getInt("hourOf" + textView, hourOfDay) + ":" + pref.getInt("minOf" + textView, min) + " " + pref.getString("formatOf" + textView, format)));
                break;
        }

    }

    private void pickerTime(String s) {
/// set the correct and cancel button
        //TODO: make dialog  Layout_alignParentBottom and width match_parent
        final Dialog timedialog;


        timedialog = new Dialog(getContext());
        timedialog.setContentView(R.layout.time_picker);

        timedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView cancel = timedialog.findViewById(R.id.cancel_time_picker);
        ImageView correct = timedialog.findViewById(R.id.correct_time_picker);
        final TimePicker timePicker = timedialog.findViewById(R.id.spinner_time_picker);
        timePicker.setIs24HourView(false);
        final String textView = s;


        //     TimePicker timePicker = Timedialog.findViewById(R.id.spinner_time_picker);

        timedialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timedialog.dismiss();
            }
        });
        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timedialog.dismiss();
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                if (hour > 12) {
                    format = "PM";
                    hour = hour - 12;
                } else if (hour == 0) {
                    hour += 12;
                    format = "AM";
                } else if (hour == 12) {
                    format = "PM";
                } else {
                    format = "AM";
                }


                editor.putInt("hourOf" + textView, hour);
                editor.putInt("minOf" + textView, minute);
                editor.putString("formatOf" + textView, format);
                editor.commit();

                switch (textView) {
                    case "morning":
                        if (minute < 10 || minute == 0) {
                            morningTime.setText((pref.getInt("hourOf" + textView, 00) + ":0" + pref.getInt("minOf" + textView, 00) + " " + pref.getString("formatOf" + textView, "AM")));

                        } else
                            morningTime.setText((pref.getInt("hourOf" + textView, 00) + ":" + pref.getInt("minOf" + textView, 00) + " " + pref.getString("formatOf" + textView, "AM")));

                        break;
                    case "evening":
                        if (minute < 10 || minute == 0)
                            eveningTime.setText((pref.getInt("hourOf" + textView, 00) + ":0" + pref.getInt("minOf" + textView, 00) + " " + pref.getString("formatOf" + textView, "AM")));
                        else
                            eveningTime.setText((pref.getInt("hourOf" + textView, 00) + ":" + pref.getInt("minOf" + textView, 00) + " " + pref.getString("formatOf" + textView, "AM")));

                        break;
                    case "sleep":

                        if (minute < 10 || minute == 0)
                            sleepTime.setText((pref.getInt("hourOf" + textView, 00) + ":0" + pref.getInt("minOf" + textView, 00) + " " + pref.getString("formatOf" + textView, "AM")));

                        else
                            sleepTime.setText((pref.getInt("hourOf" + textView, 00) + ":" + pref.getInt("minOf" + textView, 00) + " " + pref.getString("formatOf" + textView, "AM")));

                        break;
                    case "wakeup":
                        if (minute < 10 || minute == 0)
                            wakeTime.setText((pref.getInt("hourOf" + textView, 00) + ":0" + pref.getInt("minOf" + textView, 00) + " " + pref.getString("formatOf" + textView, "AM")));
                        else
                            wakeTime.setText((pref.getInt("hourOf" + textView, 00) + ":" + pref.getInt("minOf" + textView, 00) + " " + pref.getString("formatOf" + textView, "AM")));

                        break;
                }

            }
        });
        //   textView.setText(pref.getInt("hourOf" + textView.getText(), 00) + ":" + pref.getInt("minOf" + textView.getText(), 00) + " " + pref.getString("formatOf" + textView.getText(), "AM"));
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
        // TODo: make dialog fill page

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

                            Toast.makeText(getContext(), "لايوجد برنامج واتس اب ", Toast.LENGTH_LONG).show();


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

                    Toast.makeText(getContext(), "لايوجد برنامج تويتر", Toast.LENGTH_LONG).show();

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

    private void changeMasbahaTextView(TextView clickble, TextView other1, TextView other2, TextView other3) {
        clickble.setTextColor(getResources().getColor(R.color.colorAccent));
        clickble.setBackgroundColor(getResources().getColor(R.color.textColor));


        other1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        other1.setTextColor(getResources().getColor(R.color.textColor));

        other2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        other2.setTextColor(getResources().getColor(R.color.textColor));

        other3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        other3.setTextColor(getResources().getColor(R.color.textColor));
    }

    private void changeGeneralTextView(TextView clickble, TextView other1, TextView other2) {
        clickble.setTextColor(getResources().getColor(R.color.colorAccent));
        clickble.setBackgroundColor(getResources().getColor(R.color.textColor));

        other1.setTextColor(getResources().getColor(R.color.textColor));
        other1.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        other2.setTextColor(getResources().getColor(R.color.textColor));
        other2.setBackgroundColor(getResources().getColor(R.color.colorAccent));

    }
}
