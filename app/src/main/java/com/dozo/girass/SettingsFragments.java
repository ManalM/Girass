package com.dozo.girass;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.bumptech.glide.Glide;
import com.dozo.girass.Notification.Notify;
import com.dozo.girass.Notification.NotifyEvening;
import com.dozo.girass.Notification.NotifyReminder;
import com.dozo.girass.Notification.NotifySleep;
import com.dozo.girass.Notification.NotifyWakeup;
import com.dozo.girass.R;
import com.suke.widget.SwitchButton;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hotchemi.stringpicker.StringPicker;

import static android.content.Context.ALARM_SERVICE;
import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class SettingsFragments extends Fragment implements View.OnClickListener {

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
    private ImageView www_img, email_img, phone_img, twitter_img, dozo;
    private ImageView generalArrow, masbahaArrow;
    //
    private SeekBar seekBar;
    private RelativeLayout btns, declare;
    private LinearLayout listContact;
    private LinearLayout morningLayout, eveningLayout, wakeupLayout, sleepLayout, reminderLayout;
    //-------------------------------------------------------------
    private MediaPlayer mediaSound1, mediaSound2, mediaSound3, mediaSound4;
    //-------------------------------------------------------------

    public static MediaPlayer defualtSound, checkSound;
    public static Typeface defualtFont;
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

        if (pref.getString("generalCardVisibility", "gone").equals("visible")) {
            generalCard.setVisibility(View.VISIBLE);
        } else if (pref.getString("generalCardVisibility", "gone").equals("gone"))
            generalCard.setVisibility(View.GONE);

        if (pref.getString("masbahaCardVisibility", "gone").equals("visible")) {
            masbahaCard.setVisibility(View.VISIBLE);
        } else if (pref.getString("masbahaCardVisibility", "gone").equals("gone"))
            masbahaCard.setVisibility(View.GONE);
        //--------------------- Defaults ------------------------------


        if (pref.getString("defaultFont", "regular").equals("regular"))
            defualtFont = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/tajawal_regular.ttf");
        else if (pref.getString("defaultFont", "bold").equals("bold"))
            defualtFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
        else if (pref.getString("defaultFont", "light").equals("light"))
            defualtFont = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/sans-serif.ttf");

        //-------------------------------------------------------------
        textSize = (TextView) rootView.findViewById(R.id.text_size);
        fontType = (TextView) rootView.findViewById(R.id.FontType);
        seekBar = rootView.findViewById(R.id.seek_bar);

        fontType.setTypeface(defualtFont);
        seekBar.setProgress((int) pref.getInt("fontSize", 22));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(15);
            seekBar.setMinimumHeight(22);
        }


        textSize.setTextSize(pref.getInt("fontSize", 22));
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
                    editor.putString("launcher", "Fav");
                else if (position == 2)
                    editor.putString("launcher", "Masbaha");

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

                    defualtFont = Typeface.createFromAsset(getContext().getAssets(),
                            "fonts/tajawal_regular.ttf");

                    editor.putString("defaultFont", "regular");
                } else if (position == 1) {

                    defualtFont = Typeface.createFromAsset(getContext().getAssets(),
                            "fonts/sans-serif.ttf");
                    editor.putString("defaultFont", "light");
                } else if (position == 2) {

                    defualtFont = Typeface.createFromAsset(getContext().getAssets(),
                            "fonts/arial.ttf");
                    editor.putString("defaultFont", "bold");
                }


                fontType.setTypeface(defualtFont);

                editor.putInt("selectedFont", position);

                editor.commit();
            }

        });
        //----------------------------------------------------


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                textSize.setTextSize(progress);

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

                    aboutApp();
                } catch (OutOfMemoryError e) {
                    Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.reminder_time:
            case R.id.reminder_layout:
                chooseReminderTime();
                break;

            case R.id.evening_time:
            case R.id.evening_layout:
                pickerTime("evening");
                break;

            case R.id.sleep_time:
            case R.id.sleep_layout:
                pickerTime("sleep");
                break;

            case R.id.wakeup_time:
            case R.id.wakeup_layout:

                pickerTime("wakeup");
                break;

            case R.id.morning_layout:
            case R.id.morning_time:
                pickerTime("morning");
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
        Animation showList = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.notification_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
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
        morningLayout = dialog.findViewById(R.id.morning_layout);
        eveningLayout = dialog.findViewById(R.id.evening_layout);
        wakeupLayout = dialog.findViewById(R.id.wakeup_layout);
        sleepLayout = dialog.findViewById(R.id.sleep_layout);
        reminderLayout = dialog.findViewById(R.id.reminder_layout);
        //-------------------------control navigation bar---------------------
        View decor = dialog.getWindow().getDecorView();

        decor.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decor.setSystemUiVisibility(hideNavigation());
            }
        });

//-----------------------sharedPreference----------------------

        if (pref.getBoolean("NotificationLinearVisibility", false)) {
            checked = true;
            activate.setChecked(true);


            notificationLinear.setVisibility(View.VISIBLE);
            notificationLinear.startAnimation(showList);

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
        sleep.setChecked(pref.getBoolean("sleepSwitch", false));
        wakeup.setChecked(pref.getBoolean("wakeupSwitch", false));
        reminder.setChecked(pref.getBoolean("reminderSwitch", false));
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
                    notificationLinear.startAnimation(showList);

                    editor.putBoolean("NotificationLinearVisibility", checked);
                    editor.commit();
                } else {
                    Animation hideList = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

                    checked = isChecked;
                    notificationLinear.setVisibility(View.GONE);
                    notificationLinear.startAnimation(hideList);
                    closeSwitches();

                    for (int i = 0; i < 5; i++) {
                        cancelNotification(i);
                    }
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
                    morningNotification();
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

                    eveningNotification();
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

                    sleepNotification();
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

                    wakeupNotification();
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
                    cancelNotification(5);
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
        morningTime.setOnClickListener(this);

        eveningTime.setOnClickListener(this);
        sleepTime.setOnClickListener(this);
        wakeTime.setOnClickListener(this);
        reminderTime.setOnClickListener(this);
        morningLayout.setOnClickListener(this);
        eveningLayout.setOnClickListener(this);
        sleepLayout.setOnClickListener(this);
        wakeupLayout.setOnClickListener(this);
        reminderLayout.setOnClickListener(this);
        dialog.show();
    }

    private void closeSwitches() {
        morning.setChecked(false);
        evening.setChecked(false);
        sleep.setChecked(false);
        wakeup.setChecked(false);
        reminder.setChecked(false);
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
                reminder.setChecked(false);
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
                        morning.setChecked(false);
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
                        evening.setChecked(false);
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
                        sleep.setChecked(false);
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
                        wakeup.setChecked(false);
                        break;
                }

            }
        });

    }

    private Calendar getCalender(String s) {
        Calendar calendar = Calendar.getInstance();
        switch (s) {
            case "morning":

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

        return calendar;
    }

    private void morningNotification() {
        Calendar calendar = getCalender("morning");

        try {
            Intent myIntent = new Intent(getContext(), Notify.class);

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, myIntent, 0);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }


        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void eveningNotification() {
        Calendar calendar = getCalender("evening");

        try {
            Intent myIntent = new Intent(getContext(), NotifyEvening.class);

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 2, myIntent, 0);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void wakeupNotification() {
        Calendar calendar = getCalender("wakeup");

        try {
            Intent myIntent = new Intent(getContext(), NotifyWakeup.class);

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 3, myIntent, 0);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sleepNotification() {
        Calendar calendar = getCalender("sleep");

        try {
            Intent myIntent = new Intent(getContext(), NotifySleep.class);

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 4, myIntent, 0);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelNotification(int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        switch (requestCode) {
            case 1:
                Intent intent = new Intent(getContext(), Notify.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, intent, 0);
                alarmManager.cancel(pendingIntent);

                break;
            case 2:
                Intent intent1 = new Intent(getContext(), NotifyEvening.class);
                PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getContext(), requestCode, intent1, 0);
                alarmManager.cancel(pendingIntent1);

                break;
            case 3:
                Intent intent2 = new Intent(getContext(), NotifySleep.class);
                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getContext(), requestCode, intent2, 0);
                alarmManager.cancel(pendingIntent2);

                break;
            case 4:
                Intent intent3 = new Intent(getContext(), NotifyWakeup.class);
                PendingIntent pendingIntent3 = PendingIntent.getBroadcast(getContext(), requestCode, intent3, 0);
                alarmManager.cancel(pendingIntent3);

                break;
            case 5:
                Intent intent4 = new Intent(getContext(), NotifyReminder.class);
                PendingIntent pendingIntent4 = PendingIntent.getBroadcast(getContext(), requestCode, intent4, 0);
                alarmManager.cancel(pendingIntent4);

                break;
        }
        //   PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, intent, 0);

        // alarmManager.cancel(pendingIntent);
    }

    private void creatReminderNotification() {
        //-- take random zikr as a content

        Intent myIntent = new Intent(getContext(), NotifyReminder.class);

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
        //-------------------------control navigation bar---------------------

        View decorView = aboutDialog.getWindow().getDecorView();

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideNavigation());
            }
        });
//--------------------------------------------------------------------------------

        dozo = aboutDialog.findViewById(R.id.dozo);
        listContact = aboutDialog.findViewById(R.id.list_contact);
        declare = aboutDialog.findViewById(R.id.declare);
        btns = aboutDialog.findViewById(R.id.btns);
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
        desc = aboutDialog.findViewById(R.id.desc);
        //----------------------animation----------------------------
        aboutDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        a.setDuration(1000);
        Animation list = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        list.setDuration(1000);
        Animation btn = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        btn.setDuration(1000);


        declare.setVisibility(View.VISIBLE);
        declare.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listContact.setVisibility(View.VISIBLE);
                listContact.startAnimation(list);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        list.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btns.setVisibility(View.VISIBLE);

                btns.startAnimation(btn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //------------------------------------------------------------
        www_img.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        twitter_img.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dozo.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Glide.with(this).load(R.drawable.phone).into(phone_img);
        Glide.with(this).load(R.drawable.email1).into(email_img);
        // Glide.with(this).load(R.drawable.white_dozo).into(dozo);
        twitter.setText("@dozo_app");
        desc.setText("تمت برمجة هذا التطبيق في معامل دوزو إذا كان لديك اقتراح أو فكرة تطبيق تريد أن  ننفذها لك فتواصل معنا");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            desc.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog);
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
                        String text = URLEncoder.encode("السلام عليكم ...");
                        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=966567636391&text=" + text);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

                        sendIntent.setPackage("com.whatsapp");
                        try {
                            startActivity(sendIntent);
                        } catch (ActivityNotFoundException e) {

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
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=974020966971043841"));
                try {
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/dozo_app")));

                }

            }
        });

        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                detailsDialog = new Dialog(getContext());
                detailsDialog.setContentView(R.layout.details_dialog);
                detailsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);

                //-------------------------control navigation bar---------------------

                View decoreView1 = detailsDialog.getWindow().getDecorView();
                decoreView1.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if (visibility == 0)
                            decoreView1.setSystemUiVisibility(hideNavigation());
                    }
                });
                //----------------------------------------------------------------------
                TextView thanks = detailsDialog.findViewById(R.id.thanks);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    thanks.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
                }

                detailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                detailsDialog.show();
            }
        });
        aboutDialog.show();

    }

    private int hideNavigation() {
        return View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.

                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                ;
    }
}