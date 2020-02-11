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
/*        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");*/
       /* Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("fromNotification", true);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), context.getString(R.string.app_name));
        mBuilder.setContentTitle(title);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContentText(content);
        mBuilder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, mBuilder.build());*/
        //   id = PreferenceManager.getDefaultSharedPreferences(context).getInt("arrayListLength", 10);


        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(100, nb.build());

    }
}
