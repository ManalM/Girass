package com.example.girass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

class NotifyReminder extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationHelperReminder notificationHelper = new NotificationHelperReminder(context, intent);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(5, nb.build());

    }
}