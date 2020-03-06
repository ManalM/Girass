package com.example.girass;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private static SharedPreferences pref;
    private NotificationManager mManager;
    private static String content;
    Boolean vibate, sound;
    int code;
    Uri soundUri;

    public NotificationHelper(Context base, Intent intent, String message) {
        super(base);
        //  content = intent.getStringExtra("message");

        content = message;
        pref = PreferenceManager.getDefaultSharedPreferences(base);
        vibate = pref.getBoolean("generalVibrate", true);
        sound = pref.getBoolean("generalSound", true);

        soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.correct2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel;

        channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {

        Intent intent = new Intent(NotificationHelper.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationHelper.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channelID)
                        .setContentTitle("لاتنس ذكر اللّه")
                        .setContentText(content)
                        .setContentIntent(pendingIntent).setAutoCancel(true)
                        .setSmallIcon(R.drawable.logo);

        if (vibate)
            builder.setVibrate(new long[]{0, 250, 100, 250});
        if (sound)
            builder.setSound(soundUri);

        return builder;
    }
}