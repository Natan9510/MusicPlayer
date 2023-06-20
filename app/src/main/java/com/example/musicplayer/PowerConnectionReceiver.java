package com.example.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

public class PowerConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){
            Log.d("zlo", "onReceive: ACTION_POWER_CONNECTED");
        }else if(intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)){
            Log.d("zlo", "onReceive: ACTION_POWER_DISCONNECTED");
        }
    }
}
