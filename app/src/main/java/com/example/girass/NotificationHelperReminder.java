package com.example.girass;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import com.example.girass.Data.DataService;

import java.util.Random;

class NotificationHelperReminder extends ContextWrapper {
    public static final String channelID = "channelID1";
    public static final String channelName = "Channel Name1";
    public String group = "com.android.example.girass";
    private static SharedPreferences pref;
    private NotificationManager mManager;
    private static String content;
    Boolean vibate, sound;

    Uri soundUri;

    public NotificationHelperReminder(Context base, Intent intent) {
        super(base);
        content = intent.getStringExtra("content");
        pref = PreferenceManager.getDefaultSharedPreferences(base);
        vibate = pref.getBoolean("generalVibrate", true);
        sound = pref.getBoolean("generalSound", true);

        soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.correct2);
        /* *//*    Activity activity = (Activity) base;
        Intent intent = activity.getIntent();*//*
        //content = intent.getStringExtra("content");
        pref= PreferenceManager.getDefaultSharedPreferences(base);
      //  content = pref.getString("contentOfMorning","لاتنسى ذكر الله");*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        //Context context;
       /* Activity activity = (Activity) context;
        Intent intent = activity.getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");*/
        DataService dataService = new DataService();
        String[] h = dataService.GetChosenAzkar();
        String randomReminderZikr = h[new Random().nextInt(h.length)];

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channelID)
                        .setContentTitle("لاتنس ذكر اللّه")
                        .setContentText(randomReminderZikr)
                        .setGroup(group)
                        .setSmallIcon(R.drawable.logo);

        if (vibate)
            builder.setVibrate(new long[]{0, 250, 100, 250});
        if (sound)
            builder.setSound(soundUri);

        return builder;
        //.enableVibration(pref.getBoolean("generalVibrate", true));
    }
}