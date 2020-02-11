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

        int count = intent.getIntExtra("count", 1);



        NotificationHelper notificationHelper = new NotificationHelper(context, intent);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();

        // for (int i =1;i<=count;i++)
        notificationHelper.getManager().notify(1, nb.build());

    }
}
