package com.example.girass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotifyReminder extends BroadcastReceiver {

    int id;

    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationReminderHelper notificationHelper = new NotificationReminderHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();


        notificationHelper.getManager().notify(5, nb.build());


    }
}
