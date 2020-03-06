package com.example.girass;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import androidx.core.app.NotificationCompat;


public class Notify extends BroadcastReceiver {

    int id;
    String content;

    @Override
    public void onReceive(Context context, Intent intent) {

        int requestCode = intent.getIntExtra("requestCode", 1);
        String content = intent.getStringExtra("content");
        NotificationHelper notificationHelper = new NotificationHelper(context, intent, content);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());



    }

}
