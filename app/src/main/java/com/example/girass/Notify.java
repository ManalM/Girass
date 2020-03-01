package com.example.girass;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import androidx.core.app.NotificationCompat;


public class Notify extends BroadcastReceiver {

    int id;

    @Override
    public void onReceive(Context context, Intent intent) {

        int requestCode = intent.getIntExtra("requestCode", 1);

        String content = intent.getStringExtra("content");

        NotificationHelper notificationHelper = new NotificationHelper(context, intent, content);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();

        // for (int i =1;i<=count;i++)

        notificationHelper.getManager().notify(requestCode, nb.build());

    }
}
