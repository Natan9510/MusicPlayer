package com.example.musicplayer;

import static androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.ui.PlayerNotificationManager;

public class MyService extends Service {
    static final String ACTION_PLAY = "com.example.action.PLAY";
    static final String ACTION_STOP = "com.example.action.STOP";
    static final String ACTION_PAUSE = "com.example.action.PAUSE";
    String TAG = "zlo";

    MediaPlayer mediaPlayer = null;

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationChannel notificationChannel = new NotificationChannel("channel_id", "TestName",
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        mediaPlayer = MediaPlayer.create(this, R.raw.sche_ne_vmerla);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: ");
        if (intent.getAction().equals(ACTION_PLAY)) {
            Log.d(TAG, "onStartCommand: play");
            mediaPlayer.start();

            Intent intentA = new Intent("msg"); //action: "msg"
            intentA.putExtra("message", "Send data to broadcast");
            sendBroadcast(intentA);

            Intent intent1 = new Intent("a1");
            sendBroadcast(intent1);
            Intent intent2 = new Intent("a2");
            sendBroadcast(intent2);
            Intent intent3 = new Intent("a3");
            sendBroadcast(intent3);
            Intent intent4 = new Intent("a4");
            sendBroadcast(intent4);

        } else if (intent.getAction().equals(ACTION_STOP)) {
            Log.d(TAG, "onStartCommand: stop");
            mediaPlayer.stop();
            mediaPlayer = MediaPlayer.create(this, R.raw.sche_ne_vmerla);
            return START_NOT_STICKY;
        }else if(intent.getAction().equals(ACTION_PAUSE)){
            Log.d(TAG, "onStartCommand: pause");
            mediaPlayer.pause();
        }

        //intent for the service
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        //Pause
        Intent pauseActionIntent = new Intent(getApplicationContext(), MyService.class);
        pauseActionIntent.setAction(MyService.ACTION_PAUSE);

        PendingIntent pausePendingIntent = PendingIntent.getService(getApplicationContext(), 1,
                pauseActionIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Action pauseNotificationAction = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_media_ff, "Pause", pausePendingIntent
        ).build();

        //STOP
        Intent stopActionIntent = new Intent(getApplicationContext(), MyService.class);
        stopActionIntent.setAction(MyService.ACTION_STOP);

        PendingIntent stopPendingIntent = PendingIntent.getService(getApplicationContext(), 1,
                stopActionIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Action stopNotificationAction = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_media_ff, "Stop", stopPendingIntent
        ).build();

        //Start
        Intent startActionIntent = new Intent(getApplicationContext(), MyService.class);
        startActionIntent.setAction(MyService.ACTION_PLAY);

        PendingIntent startPendingIntent = PendingIntent.getService(getApplicationContext(), 1,
                startActionIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Action startNotificationAction = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_media_ff, "Play", startPendingIntent
        ).build();

        Notification notification =
                new NotificationCompat.Builder(this, "channel_id")
                        .setContentTitle("My Player Title")
                        .setContentText("My Player Content Text")
                        .setSmallIcon(R.drawable.small_notification_item)
                        .setContentIntent(pendingIntent)
                        .addAction(startNotificationAction)
                        .addAction(pauseNotificationAction)
                        .addAction(stopNotificationAction)
                        .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    

}

