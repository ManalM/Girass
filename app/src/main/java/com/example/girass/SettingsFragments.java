package com.example.girass;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
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
import android.media.AudioAttributes;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import com.addisonelliott.segmentedbutton.SegmentedButton;
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.android.material.internal.CircularBorderDrawable;
import com.suke.widget.SwitchButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.zip.Inflater;

import hotchemi.stringpicker.StringPicker;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class SettingsFragments extends Fragment implements View.OnClickListener, TimePicker.OnTimeChangedListener {

    private Toolbar toolbar;
    private TextView toolbarText;
    LinearLayout general, masbaha, notification, share, rate, about,
            notificationLinear;
    private Dialog dialog, aboutDialog, detailsDialog;
    private CardView generalCard, masbahaCard;
    private SwitchButton activate, morning, evening, sleep, wakeup, reminder, masbahaSound, masbahaVibrate;
    private ImageButton aboutApp, close, aboutClose;
    private SwitchButton generalSound, generalVibrate;
    private TextView www, phone, twitter, email, desc,
            morningTime, eveningTime, sleepTime, wakeTime, reminderTime,

            textSize, fontType;
    private ImageView background_img, www_img, email_img, phone_img, twitter_img, dozo; // the image
    private ImageView generalArrow, masbahaArrow;
    //
    private SeekBar seekBar;
    //-------------------------------------------------------------
    private MediaPlayer mediaSound1, mediaSound2, mediaSound3, mediaSound4;
    //-------------------------------------------------------------

    public static MediaPlayer defualtSound, checkSound;
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
    final int[] VerityIntervalArray = {1800,
            3600,
            7200,
            10800,
            14400,
            18000,
            21600,
            43200};

    //---------------------------------------------------------------------------
    public static final String CHANNEL_1_ID = "channel1";

    static String chozenReminderTime;

    SegmentedButtonGroup soundsGroup;
    SegmentedButtonGroup fontsGroup, launcherGroup;
    int count = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings_fragments, container, false);

        /*****************************************
         ****   make time picker in arabic   ****
         **************************************/


        Locale locale = new Locale("ar");
        Configuration configuration = getContext().getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);

            getContext().createConfigurationContext(configuration);
        }


        /////-------------------initiate variables---------------


        general = (LinearLayout) rootView.findViewById(R.id.general_setting);
        masbaha = (LinearLayout) rootView.findViewById(R.id.masbaha_setting);
        notification = (LinearLayout) rootView.findViewById(R.id.notification);
        share = (LinearLayout) rootView.findViewById(R.id.share);
        rate = (LinearLayout) rootView.findViewById(R.id.rate);
        about = (LinearLayout) rootView.findViewById(R.id.about);
        //--------------------- Cards ---------------------------------

        generalCard = (CardView) rootView.findViewById(R.id.general_card);
        masbahaCard = (CardView) rootView.findViewById(R.id.masbaha_card);
        generalArrow = (ImageView) rootView.findViewById(R.id.general_arrow);
        masbahaArrow = (ImageView) rootView.findViewById(R.id.masbaha_arrow);
        masbahaSound = rootView.findViewById(R.id.masbaha_sounds);
        masbahaVibrate = rootView.findViewById(R.id.masbaha_vibrate);
        generalSound = rootView.findViewById(R.id.general_sounds);
        generalVibrate = rootView.findViewById(R.id.general_vibrate);

        //--------------------- Defaults ------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            defualtFont = getResources().getFont(R.font.tajawal_regular);
        }


        //-------------------------------------------------------------
        now = Calendar.getInstance();
        hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        min = now.get(Calendar.MINUTE);

        //-------------------------ToolBar--------------------


        toolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        toolbarText = rootView.findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText(R.string.settings);

        //--------------------- SharedPreference ----------------------------
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();

        masbahaSound.setChecked(pref.getBoolean("masbahaSound", true));
        masbahaVibrate.setChecked(pref.getBoolean("masbahaVibrate", true));
        generalSound.setChecked(pref.getBoolean("generalSound", true));
        generalVibrate.setChecked(pref.getBoolean("generalVibrate", true));

        if (pref.getString("generalCardVisibility", "visible").equals("visible")) {
            generalCard.setVisibility(View.VISIBLE);
        } else if (pref.getString("generalCardVisibility", "gone").equals("gone"))
            generalCard.setVisibility(View.GONE);

        if (pref.getString("masbahaCardVisibility", "visible").equals("visible")) {
            masbahaCard.setVisibility(View.VISIBLE);
        } else if (pref.getString("masbahaCardVisibility", "gone").equals("gone"))
            masbahaCard.setVisibility(View.GONE);


        //-------------------------------------------------------------
        textSize = (TextView) rootView.findViewById(R.id.text_size);
        fontType = (TextView) rootView.findViewById(R.id.FontType);
        //TODO:the range of the textSize seekbar value !=0
        textSize.setTypeface(defualtFont);
        seekBar = rootView.findViewById(R.id.seek_bar);

        fontType.setTypeface(defualtFont);
        seekBar.setProgress((int) pref.getInt("fontSize", 18));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(18);
            seekBar.setMinimumHeight(22);
        }
        textSize.setTextSize(TypedValue.COMPLEX_UNIT_PX, pref.getInt("fontSize", 18));
        checkSound = MediaPlayer.create(getContext(), R.raw.correct);

        //--------------------Listeners------------------------------

        general.setOnClickListener(this);
        masbaha.setOnClickListener(this);
        notification.setOnClickListener(this);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);
        about.setOnClickListener(this);
        /*****************************************
         ****                                 ****
         *             General Settings          *
         *                                       *
         ****                                 ****
         *****************************************/


        generalVibrate.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
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

        generalSound.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {

                    GeneralSound = true;
                    checkSound.start();

                    editor.putBoolean("generalSound", GeneralSound);
                    editor.commit();
                } else {
                    GeneralSound = false;
                    checkSound.start();

                    editor.putBoolean("generalSound", GeneralSound);
                    editor.commit();
                }

            }
        });


        //----------------Launcher Listener-----------------------------

        launcherGroup = (SegmentedButtonGroup) rootView.findViewById(R.id.segment_luncher);
        launcherGroup.setPosition(pref.getInt("selectedLauncher", 0), false);
        launcherGroup.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                if (position == 0)
                    editor.putString("launcher", "Zikr");
                else if (position == 1)
                    editor.putString("launcher", "Masbaha");
                else if (position == 2)
                    editor.putString("launcher", "Fav");

                editor.putInt("selectedLauncher", position);
                editor.commit();
            }
        });

        //---------------Fonts Listener---------------------------
        fontsGroup = (SegmentedButtonGroup) rootView.findViewById(R.id.segment_font);
        fontsGroup.setPosition(pref.getInt("selectedFont", 0), false);
        fontsGroup.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                if (position == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        defualtFont = getResources().getFont(R.font.tajawal_regular);
                    }
                    fontType.setTypeface(defualtFont);
                    editor.putString("defaultFont", "regular");
                } else if (position == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        defualtFont = getResources().getFont(R.font.tajawal_light);
                    }
                    fontType.setTypeface(defualtFont);

                    editor.putString("defaultFont", "light");
                } else if (position == 2) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        defualtFont = getResources().getFont(R.font.tajwal_bold);
                    }
                    fontType.setTypeface(defualtFont);

                    editor.putString("defaultFont", "bold");
                }
                editor.putInt("selectedFont", position);

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

        /*****************************************
         ****                                 ****
         *             Masbaha Settings          *
         *                                       *
         ****                                 ****
         *****************************************/
        masbahaVibrate.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
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


        masbahaSound.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {

                    Masbahasound = true;

                    checkSound.start();
                    editor.putBoolean("masbahaSound", Masbahasound);
                    editor.commit();
                } else {
                    Masbahasound = false;
                    checkSound.start();
                    editor.putBoolean("masbahaSound", Masbahasound);
                    editor.commit();
                }
            }
        });

        mediaSound1 = MediaPlayer.create(getContext(), R.raw.click2);
        mediaSound2 = MediaPlayer.create(getContext(), R.raw.pop);
        mediaSound3 = MediaPlayer.create(getContext(), R.raw.click);
        mediaSound4 = MediaPlayer.create(getContext(), R.raw.menu2);

        soundsGroup = (SegmentedButtonGroup) rootView.findViewById(R.id.segment_sound);
        soundsGroup.setPosition(pref.getInt("selectedSound", 0), false);
        soundsGroup.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {

                if (position == 0) {
                    mediaSound1.start();


                    if (pref.getBoolean("masbahaSound", true)) {
                        defualtSound = mediaSound1;
                        editor.putInt("defaultSound", R.raw.click2);
                    }

                } else if (position == 1) {
                    mediaSound2.start();

                    if (pref.getBoolean("masbahaSound", true)) {
                        defualtSound = mediaSound2;
                        editor.putInt("defaultSound", R.raw.pop);

                    }

                } else if (position == 2) {
                    editor.putInt("selectedSound", position);

                    mediaSound3.start();
                    if (pref.getBoolean("masbahaSound", true)) {
                        defualtSound = mediaSound3;
                        editor.putInt("defaultSound", R.raw.click);

                    }

                } else if (position == 3) {


                    mediaSound4.start();
                    if (pref.getBoolean("masbahaSound", true)) {
                        defualtSound = mediaSound4;
                        editor.putInt("defaultSound", R.raw.menu2);

                    }

                }
                editor.putInt("selectedSound", position);
                editor.commit();
            }
        });

        return rootView;
    }


    @Override
    public void onClick(View v) {


        /*************************************
         ****        buttons  function    ****
         *************************************/


        switch (v.getId()) {

            case R.id.general_setting:

                if (generalCard.getVisibility() == View.GONE) {
                    generalCard.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(R.drawable.up_arrow).into(generalArrow);
                    editor.putString("generalCardVisibility", "visible");
                    editor.commit();

                } else {

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


                } else {
                    masbahaCard.setVisibility(View.GONE);
                    Glide.with(getContext()).load(R.drawable.down_arrow).into(masbahaArrow);
                    editor.putString("masbahaCardVisibility", "gone");
                    editor.commit();


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


    /*************************************
     ****    General settings funcs   ****
     *************************************/




    /******************************************
     ****    notification settings funcs   ****
     ******************************************/
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

        activate = dialog.findViewById(R.id.activate);
        evening = dialog.findViewById(R.id.evening);
        morning = dialog.findViewById(R.id.morning);
        sleep = dialog.findViewById(R.id.sleep);
        wakeup = dialog.findViewById(R.id.wakeup);
        reminder = dialog.findViewById(R.id.reminder);

        //--------------------general------------------
        notificationLinear = (LinearLayout) dialog.findViewById(R.id.linear_notification);
        close = (ImageButton) dialog.findViewById(R.id.close);

//-----------------------sharedPreference----------------------

        if (pref.getBoolean("NotificationLinearVisibility", true)) {
            checked = true;
            activate.setChecked(true);


            notificationLinear.setVisibility(View.VISIBLE);
            editor.putBoolean("NotificationLinearVisibility", checked);
            editor.commit();
        } else {
            checked = false;
            activate.setChecked(false);


            notificationLinear.setVisibility(View.GONE);
            editor.putBoolean("NotificationLinearVisibility", checked);
            editor.commit();
        }

        morning.setChecked(pref.getBoolean("morningSwitch", true));

        evening.setChecked(pref.getBoolean("eveningSwitch", true));
        sleep.setChecked(pref.getBoolean("sleepSwitch", true));
        wakeup.setChecked(pref.getBoolean("wakeupSwitch", true));
        reminder.setChecked(pref.getBoolean("reminderSwitch", true));
        setTextOfTime("morning");
        setTextOfTime("evening");
        setTextOfTime("sleep");
        setTextOfTime("wakeup");
        setTextOfReminder();
//-------------------------------------------------------------------------


        activate.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked = isChecked;
                    notificationLinear.setVisibility(View.VISIBLE);
                    editor.putBoolean("NotificationLinearVisibility", checked);
                    editor.commit();
                } else {
                    checked = isChecked;
                    notificationLinear.setVisibility(View.GONE);
                    for (int i = 1; i <= 4; i++)
                        cancelNotification(i);
                    editor.putBoolean("NotificationLinearVisibility", checked);
                    editor.commit();
                }
            }
        });


        //-----------------------------Switched checked--------------------------
        morning.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {

                if (isChecked) {


                    createTimeNotification("morning", "حان وقت أذكار الصباح", 1);

                    editor.putBoolean("morningSwitch", true);
                    editor.commit();
                } else {
                    cancelNotification(1);
                    editor.putBoolean("morningSwitch", false);
                    editor.commit();
                }
            }
        });
        evening.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                if (isChecked) {
                    createTimeNotification("evening", "حان وقت أذكار المساء", 2);

                    editor.putBoolean("eveningSwitch", true);
                    editor.commit();
                } else {
                    cancelNotification(2);
                    editor.putBoolean("eveningSwitch", false);
                    editor.commit();
                }
            }
        });
        sleep.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                if (isChecked) {
                    createTimeNotification("sleep", "حان وقت أذكار النوم", 3);

                    editor.putBoolean("sleepSwitch", true);
                    editor.commit();
                } else {
                    cancelNotification(3);
                    editor.putBoolean("sleepSwitch", false);
                    editor.commit();
                }
            }
        });
        wakeup.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                if (isChecked) {
                    createTimeNotification("wakeup", "حان وقت أذكار الاستيقاظ من النوم", 4);

                    editor.putBoolean("wakeupSwitch", true);
                    editor.commit();
                } else {
                    cancelNotification(4);
                    editor.putBoolean("wakeupSwitch", false);
                    editor.commit();
                }
            }
        });
        reminder.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {

                if (isChecked) {
                    creatReminderNotification();
                    editor.putBoolean("reminderSwitch", true);
                    editor.commit();
                } else {
                    editor.putBoolean("reminderSwitch", false);
                    editor.commit();
                }
            }
        });
        //---------------------------------close button--------------------------------------
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

        Window window = reminderDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        window.setAttributes(wlp);
        ImageView cancel = reminderDialog.findViewById(R.id.cancel_reminder_picker);
        ImageView correct = reminderDialog.findViewById(R.id.correct_reminder_picker);
        final StringPicker stringPicker = (StringPicker) reminderDialog.findViewById(R.id.string_picker);

        stringPicker.setValues(VerityTimeArray);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkSound.start();
                reminderDialog.dismiss();
            }
        });

        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSound.start();
                reminderDialog.dismiss();
                chozenReminderTime = stringPicker.getCurrentValue();
                reminderTime.setText(chozenReminderTime);
                editor.putString("chozenReminderTime", chozenReminderTime);
                editor.commit();
            }
        });
        reminderDialog.show();
    }

    private void setTextOfTime(String s) {
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
        switch (s) {
            case "morning":
                if (pref.getInt("minOfMorning", min) < 10 || pref.getInt("minOfMorning", min) == 0) {
                    morningTime.setText((pref.getInt("hourOfMorning", hourOfDay) + ":0" + pref.getInt("minOfMorning", min) + " " + pref.getString("formatOfMorning", format)));

                } else
                    morningTime.setText((pref.getInt("hourOfMorning", hourOfDay) + ":" + pref.getInt("minOfMorning", min) + " " + pref.getString("formatOfMorning", format)));

                break;
            case "evening":
                if (pref.getInt("minOfEvening", min) < 10 || pref.getInt("minOfEvening", min) == 0)
                    eveningTime.setText((pref.getInt("hourOfEvening", hourOfDay) + ":0" + pref.getInt("minOfEvening", min) + " " + pref.getString("formatOfEvening", format)));
                else
                    eveningTime.setText((pref.getInt("hourOfEvening", hourOfDay) + ":" + pref.getInt("minOfEvening", min) + " " + pref.getString("formatOfEvening", format)));

                break;
            case "sleep":

                if (pref.getInt("minOfSleep", min) < 10 || pref.getInt("minOfSleep", min) == 0)
                    sleepTime.setText((pref.getInt("hourOfSleep", hourOfDay) + ":0" + pref.getInt("minOfSleep", min) + " " + pref.getString("formatOfSleep", format)));

                else
                    sleepTime.setText((pref.getInt("hourOfSleep", hourOfDay) + ":" + pref.getInt("minOfSleep", min) + " " + pref.getString("formatOfSleep", format)));

                break;
            case "wakeup":
                if (pref.getInt("minOfWakeup", min) < 10 || pref.getInt("minOfWakeup", min) == 0)
                    wakeTime.setText((pref.getInt("hourOfWakeup", hourOfDay) + ":0" + pref.getInt("minOfWakeup", min) + " " + pref.getString("formatOfWakeup", format)));
                else
                    wakeTime.setText((pref.getInt("hourOfWakeup", hourOfDay) + ":" + pref.getInt("minOfWakeup", min) + " " + pref.getString("formatOfWakeup", format)));
                break;
        }

    }

    private void pickerTime(String s) {
        final Dialog timedialog;


        timedialog = new Dialog(getContext());
        timedialog.setContentView(R.layout.time_picker);

        Window window = timedialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        window.setAttributes(wlp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ImageView cancel = timedialog.findViewById(R.id.cancel_time_picker);
        ImageView correct = timedialog.findViewById(R.id.correct_time_picker);
        final TimePicker timePicker = timedialog.findViewById(R.id.spinner_time_picker);
        timePicker.setIs24HourView(false);
        final String textview = s;


        timedialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkSound.start();
                timedialog.dismiss();
            }
        });
        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSound.start();
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

/*
                editor.putInt("hourOf" + textView, hour);
                editor.putInt("minOf" + textView, minute);
                editor.putString("formatOf" + textView, format);
                editor.commit();*/

                switch (textview) {
                    case "morning":
                        editor.putInt("hourOfMorning", hour);
                        editor.putInt("minOfMorning", minute);
                        editor.putString("formatOfMorning", format);
                        editor.commit();
                        if (minute < 10 || minute == 0) {
                            morningTime.setText((pref.getInt("hourOfMorning", 00) + ":0" + pref.getInt("minOfMorning", 00) + " " + pref.getString("formatOfMorning", "AM")));

                        } else
                            morningTime.setText((pref.getInt("hourOfMorning", 00) + ":" + pref.getInt("minOfMorning", 00) + " " + pref.getString("formatOfMorning", "AM")));

                        break;
                    case "evening":
                        editor.putInt("hourOfEvening", hour);
                        editor.putInt("minOfEvening", minute);
                        editor.putString("formatOfEvening", format);
                        editor.commit();
                        if (minute < 10 || minute == 0)
                            eveningTime.setText((pref.getInt("hourOfEvening", 00) + ":0" + pref.getInt("minOfEvening", 00) + " " + pref.getString("formatOfEvening", "AM")));
                        else
                            eveningTime.setText((pref.getInt("hourOfEvening", 00) + ":" + pref.getInt("minOfEvening", 00) + " " + pref.getString("formatOfEvening", "AM")));

                        break;
                    case "sleep":
                        editor.putInt("hourOfSleep", hour);
                        editor.putInt("minOfSleep", minute);
                        editor.putString("formatOfSleep", format);
                        editor.commit();
                        if (minute < 10 || minute == 0)
                            sleepTime.setText((pref.getInt("hourOfSleep", 00) + ":0" + pref.getInt("minOfSleep", 00) + " " + pref.getString("formatOfSleep", "AM")));

                        else
                            sleepTime.setText((pref.getInt("hourOfSleep", 00) + ":" + pref.getInt("minOfSleep", 00) + " " + pref.getString("formatOfSleep", "AM")));

                        break;
                    case "wakeup":
                        editor.putInt("hourOfWakeup", hour);
                        editor.putInt("minOfWakeup", minute);
                        editor.putString("formatOfWakeup", format);
                        editor.commit();
                        if (minute < 10 || minute == 0)
                            wakeTime.setText((pref.getInt("hourOfWakeup", 00) + ":0" + pref.getInt("minOfWakeup", 00) + " " + pref.getString("formatOfWakeup", "AM")));
                        else
                            wakeTime.setText((pref.getInt("hourOfWakeup", 00) + ":" + pref.getInt("minOfWakeup", 00) + " " + pref.getString("formatOfWakeup", "AM")));

                        break;
                }

            }
        });

        //   textView.setText(pref.getInt("hourOf" + textView.getText(), 00) + ":" + pref.getInt("minOf" + textView.getText(), 00) + " " + pref.getString("formatOf" + textView.getText(), "AM"));
    }

    public void createTimeNotification(String s, String content, int requestCode) {
        // String s , int content

        count++;

        Calendar calendar = Calendar.getInstance();
        switch (s) {
            case "morning":

        /*        editor.putString("contentOfMorning",content);
                editor.apply();*/

                calendar.set(Calendar.HOUR, pref.getInt("hourOfMorning", hourOfDay));
                calendar.set(Calendar.MINUTE, pref.getInt("minOfMorning", min));
                if (pref.getString("formatOfMorning", format).equals("AM"))
                    calendar.set(Calendar.AM_PM, Calendar.AM);
                else if (pref.getString("formatOfMorning", format).equals("PM"))
                    calendar.set(Calendar.AM_PM, Calendar.PM);

                break;
            case "evening":

                calendar.set(Calendar.HOUR, pref.getInt("hourOfEvening", hourOfDay));
                calendar.set(Calendar.MINUTE, pref.getInt("minOfEvening", min));
                if (pref.getString("formatOfEvening", format).equals("AM"))
                    calendar.set(Calendar.AM_PM, Calendar.AM);
                else if (pref.getString("formatOfEvening", format).equals("PM"))
                    calendar.set(Calendar.AM_PM, Calendar.PM);

                break;
            case "sleep":
                calendar.set(Calendar.HOUR, pref.getInt("hourOfSleep", hourOfDay));
                calendar.set(Calendar.MINUTE, pref.getInt("minOfSleep", min));
                if (pref.getString("formatOfSleep", format).equals("AM"))
                    calendar.set(Calendar.AM_PM, Calendar.AM);
                else if (pref.getString("formatOfSleep", format).equals("PM"))
                    calendar.set(Calendar.AM_PM, Calendar.PM);

                break;
            case "wakeup":
                calendar.set(Calendar.HOUR, pref.getInt("hourOfWakeup", hourOfDay));
                calendar.set(Calendar.MINUTE, pref.getInt("minOfWakeup", min));
                if (pref.getString("formatOfWakeup", format).equals("AM"))
                    calendar.set(Calendar.AM_PM, Calendar.AM);
                else if (pref.getString("formatOfWakeup", format).equals("PM"))
                    calendar.set(Calendar.AM_PM, Calendar.PM);

                break;
        }

        calendar.set(Calendar.SECOND, 0);
        try {
            //   ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

            //  for (int i = 0; i <= 4; i++) {
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                Intent myIntent = new Intent(getContext(), Notify.class);
                myIntent.putExtra("content", content);
            myIntent.putExtra("count", count);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, myIntent, 0);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    ///TODO: try with Repeating func.
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            // intentArray.add(pendingIntent);

                /*editor.putInt("arrayListLength", intentArray.size());
                editor.commit();*/
            //  }


        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void cancelNotification(int requestCode) {
        count--;
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), Notify.class);
        intent.putExtra("count", count);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    private void creatReminderNotification() {
        //-- take random zikr as a content
        DataService dataService = new DataService();
        String[] h = dataService.GetChosenAzkar();
        String randomReminderZikr = h[new Random().nextInt(h.length)];
        Toast.makeText(getContext(), randomReminderZikr, Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(getContext(), Notify.class);
        myIntent.putExtra("content", randomReminderZikr);

        try {
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 5, myIntent, 0);

            long interval = VerityIntervalArray[0];
            for (int i = 0; i < VerityTimeArray.length; i++) {
                if (VerityTimeArray[i].equals(pref.getString("chozenReminderTime", VerityTimeArray[i]))) {
                    // convert time to milles
                    interval = VerityIntervalArray[i] * 1000;
                }
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 15000, interval, pendingIntent);
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*************************************
     ****    share settings funcs   *****
     ***********************************/
    private void shareApp() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "تطبيق غراس الجنة ، حصن المسلم من أذكار اليوم و الليلة \n 🕌🕌🕌 " + "https://itunes.apple.com/us/app/girass/id1438981810?mt=8 \n 🕌🕌🕌 " + " \n www.dozo-apps.com ";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    /**********************************
     ****    Rate settings funcs   ****
     **********************************/
    private void rateApp() {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getContext().getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
        }
    }

    /****************************************
     ****    about app settings funcs   ****
     *************************************/
    private void aboutApp() {


        aboutDialog = new Dialog(getContext());

        aboutDialog.setContentView(R.layout.about_dialog);
        aboutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aboutDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);


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
//TODO: test

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

//TODO: test
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


    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
      /*  Calendar calendar = Calendar.getInstance();
        // calendar.set(Calendar. SECOND , 0 ) ;
        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
       *//* if (pref.getString("formatOf" + s, format).equals("AM"))
            calendar.set(Calendar.AM_PM, Calendar.AM);
        else if (pref.getString("formatOf" + s, format).equals("PM"))
            calendar.set(Calendar.AM_PM, Calendar.PM);*//*

        createTimeNotification(calendar);*/
    }
}