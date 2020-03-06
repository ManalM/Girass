package com.example.girass.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.girass.Notification.NotificationHelper;

public class NotifySleep extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context, "حان وقت أذكار النوم");
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(4, nb.build());

    }
}
