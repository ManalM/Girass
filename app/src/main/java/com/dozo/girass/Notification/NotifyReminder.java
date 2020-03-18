package com.dozo.girass.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.dozo.girass.Data.DataService;

import java.util.Random;

public class NotifyReminder extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        DataService dataService = new DataService();
        String[] h = dataService.GetChosenAzkar();
        String randomReminderZikr = h[new Random().nextInt(h.length)];


        NotificationHelper notificationHelper = new NotificationHelper(context, randomReminderZikr);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();


        notificationHelper.getManager().notify(5, nb.build());


    }
}
