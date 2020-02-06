package com.example.girass;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

class Notify extends BroadcastReceiver {
    private final static String default_notification_channel_id = "default";
    public static final String CHANNEL_1_ID = "channel1";
    NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        Intent notificationIntent = new Intent(context, MainActivity.class);
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
        notificationManager.notify(200, mBuilder.build());


    }
}
