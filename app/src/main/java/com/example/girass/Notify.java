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



        NotificationHelper notificationHelper = new NotificationHelper(context, intent);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(300, nb.build());

    }
}
