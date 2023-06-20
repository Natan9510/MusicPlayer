package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;

public class MainActivity extends AppCompatActivity {
    //відправити через бродкаст екшин в актівіті, наприклад ACTION_PLAY
    //і обробити в актівіті ці дані отримані
    //або вивести в текст вью
    //або зберегти в базу даних
    //або зберегти на сервак

    MediaPlayer music;
//    ExoPlayer player;
//    //is the act. bound?
//    boolean isBound = false;
    String TAG = "zlo";
    PowerConnectionReceiver powerConnectionReceiver = new PowerConnectionReceiver();
    TextView powerConnected, powerDisconnected;
    private BroadcastReceiver receiver  = new BroadcastReceiver() { //анонімний BroadcastReceiver
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent.getAction());

            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                Log.d(TAG, "onReceive: connected");
                powerConnected.setText(Intent.ACTION_POWER_CONNECTED);
                powerDisconnected.setText("");
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                Log.d(TAG, "onReceive: disconnected");
                powerDisconnected.setText(Intent.ACTION_POWER_DISCONNECTED);
                powerConnected.setText("");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerConnected = findViewById(R.id.power_connected_text_view);
        powerDisconnected = findViewById(R.id.power_disconnected_text_view);

        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction("a1");
        ifilter.addAction("a2");
        ifilter.addAction("a3");
        ifilter.addAction("a4");
        ifilter.addAction("msg");
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(receiver, ifilter);


        music = MediaPlayer.create(this, R.raw.sche_ne_vmerla);

        findViewById(R.id.pause_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //musicPause();
                Intent service = new Intent(getApplicationContext(), MyService.class);
                service.setAction(MyService.ACTION_PAUSE);
                startForegroundService(service);
            }
        });

        findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                musicPlay();
                Intent service = new Intent(getApplicationContext(), MyService.class);
                service.setAction(MyService.ACTION_PLAY);
                startForegroundService(service);
            }
        });

        findViewById(R.id.stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //musicStop();
                Intent service = new Intent(getApplicationContext(), MyService.class);
                service.setAction(MyService.ACTION_STOP);
                startForegroundService(service);
                music = MediaPlayer.create(getApplicationContext(), R.raw.sche_ne_vmerla);
            }
        });

    }

    public void musicPlay(){
        music.start();
    }

    public void musicPause(){
        music.pause();
    }

    public void musicStop(){
        music.stop();
        music = MediaPlayer.create(this, R.raw.sche_ne_vmerla);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerConnectionReceiver);
    }
}