package com.example.musicplayer;

import static androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.C;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import java.util.Objects;

public class PlayerService extends Service {
    //member
    private final IBinder serviceBinder = new ServiceBinder();
    //player
    ExoPlayer player;
    PlayerNotificationManager notificationManager;

    //class binder for clients
    public class ServiceBinder extends Binder{
        public PlayerService getPlayerService(){
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //assign variable
        player = new ExoPlayer.Builder(getApplicationContext()).build();

        //audio focus attributes

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                        .setContentType(AudioAttributes.DEFAULT.contentType) //C.CONTENT_TYPE_MUSIC
                                .build();

        player.setAudioAttributes(audioAttributes, true);

        //notification manager
        final String channelId = getResources().getString(R.string.app_name) + " Music Channel ";
        final int notificationId = 1111111;
        notificationManager = new PlayerNotificationManager.Builder(this, notificationId, channelId)
                .setNotificationListener(notificationListener)
                .setMediaDescriptionAdapter(descriptionAdapter)
                .setChannelImportance(IMPORTANCE_HIGH)
                .setSmallIconResourceId(R.drawable.small_notification_item)
                .setChannelDescriptionResourceId(R.string.app_name)
                .setPauseActionIconResourceId(R.drawable.pause)
                .setPlayActionIconResourceId(R.drawable.play)
                .setStopActionIconResourceId(R.drawable.stop)
                .build();

        //set player to notification manager
        notificationManager.setPlayer(player);
        notificationManager.setPriority(NotificationCompat.PRIORITY_MAX);
        notificationManager.setUseRewindAction(false);
        notificationManager.setUseFastForwardAction(false);
    }

    @Override
    public void onDestroy() {
        //release the player
        if(player.isPlaying()){
            player.stop();
        }
        notificationManager.setPlayer(null);
        player.release();
        player = null;
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    //notification listener
    PlayerNotificationManager.NotificationListener notificationListener =
            new PlayerNotificationManager.NotificationListener() {
                @Override
                public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                    PlayerNotificationManager.NotificationListener.super.onNotificationCancelled(notificationId, dismissedByUser);
                    stopForeground(true);
                    if(player.isPlaying()){
                        player.pause();
                    }
                }

                @Override
                public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                    PlayerNotificationManager.NotificationListener.super.onNotificationPosted(notificationId, notification, ongoing);
                    startForeground(notificationId, notification);
                }
            };

    //notification description adapter
    PlayerNotificationManager.MediaDescriptionAdapter descriptionAdapter =
            new PlayerNotificationManager.MediaDescriptionAdapter() {
                @Override
                public CharSequence getCurrentContentTitle(Player player) {
                    return Objects.requireNonNull(player.getCurrentMediaItem().mediaMetadata.title);
                }

                @Nullable
                @Override
                public PendingIntent createCurrentContentIntent(Player player) {
                    //intent to open the app when clicked
                    Intent openAppIntent = new Intent(getApplicationContext(), MainActivity.class);
                    return PendingIntent.getActivity(getApplicationContext(), 0, openAppIntent,
                            PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                }

                @Nullable
                @Override
                public CharSequence getCurrentContentText(Player player) {
                    return null;
                }

                @Nullable
                @Override
                public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                    //try creating Image view on the fly then get its drawable
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setImageURI(player.getCurrentMediaItem().mediaMetadata.artworkUri);

                    //get view drawable



                    return null;
                }
            };
}