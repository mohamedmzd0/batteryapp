package com.example.mohamedabdelaziz.batteyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.google.firebase.crash.FirebaseCrash;


public class PowerConnectionReceiver extends BroadcastReceiver {
    public static int level ;
    public static boolean isCharging ;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            setstatue(level, isCharging);
        }catch (Exception e)
        {
            FirebaseCrash.report(e);
        }
    }
    public static void setstatue(int level , boolean isCharging)
    {
        MainActivity.level.setText(level+" % ");
        try {
            if (level <= 15 && !isCharging)
                MainActivity.imageView.setImageResource(R.drawable.b0);
            else if (level <= 30 && !isCharging && level > 15)
                MainActivity.imageView.setImageResource(R.drawable.b1);
            else if (level <= 60 && !isCharging && level > 30)
                MainActivity.imageView.setImageResource(R.drawable.b2);
            else if (level <= 90 && !isCharging && level > 60)
                MainActivity.imageView.setImageResource(R.drawable.b3);
            else if (level <= 100 && !isCharging && level > 90)
                MainActivity.imageView.setImageResource(R.drawable.b4);
            else if ( isCharging)
               MainActivity.imageView.setImageResource(R.drawable.ic);

        }catch (Exception e)
        {
            FirebaseCrash.report(e);
        }
    }
}