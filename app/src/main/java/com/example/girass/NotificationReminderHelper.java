package com.example.girass;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.girass.Data.DataService;

import java.util.Random;

class NotificationReminderHelper extends ContextWrapper {
    public static final String channelID = "channelID5";
    public static final String channelName = "Channel Name5";
    private static SharedPreferences pref;
    private NotificationManager mManager;
    Boolean vibate, sound;
    String randomReminderZikr;
    Uri soundUri;

    public NotificationReminderHelper(Context base) {
        super(base);
        DataService dataService = new DataService();
        String[] h = dataService.GetChosenAzkar();
        randomReminderZikr = h[new Random().nextInt(h.length)];

        //content = message;
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
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {

        Intent intent = new Intent(NotificationReminderHelper.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationReminderHelper.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channelID)
                        .setContentTitle("لاتنس ذكر اللّه")
                        .setContentText(randomReminderZikr)
                        .setContentIntent(pendingIntent).setAutoCancel(true)
                        .setSmallIcon(R.drawable.logo);

        if (vibate)
            builder.setVibrate(new long[]{0, 250, 100, 250});
        if (sound)
            builder.setSound(soundUri);

        return builder;
    }

}
