package com.example.girass;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    public String group = "com.android.example.girass";
    private static SharedPreferences pref ;
    private NotificationManager mManager;
private static  String content;

    public NotificationHelper(Context base) {
        super(base);
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
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle( "لاتنس ذكر اللّه")
                .setContentText("اذكار")
                .setGroup(group)
                .setSmallIcon(R.drawable.logo);

                //.enableVibration(pref.getBoolean("generalVibrate", true));
    }
}
