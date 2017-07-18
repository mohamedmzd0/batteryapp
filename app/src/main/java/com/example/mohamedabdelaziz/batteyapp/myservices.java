package com.example.mohamedabdelaziz.batteyapp;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.crash.FirebaseCrash;

import java.util.List;

/**
 * Created by Mohamed Abd ELaziz on 7/15/2017.
 */

public class myservices extends Service {
    public static boolean started =false;
    static NetworkInfo.State wifiState = null;
    static NetworkInfo.State mobileState = null;
    static ConnectivityManager cm ;
    static NotificationManager manager = null;
    static NotificationCompat.Builder builder;

    @Override
    public void onCreate() {
        super.onCreate();
    started=true;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void change_happend(Context context, Intent intent)
    {
        try{
            if (null != intent) {
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context);
                if (wifiState != null && mobileState != null
                                && NetworkInfo.State.CONNECTED != wifiState
                                && NetworkInfo.State.CONNECTED == mobileState) {
                                    builder.setContentText("data saver")
                                    .setOngoing(true)
                                    .setContentTitle("Data Connected")
                                    .setSmallIcon(R.drawable.data);
                            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(0, builder.build());
                            myservices.optimize(context,false);
                        } else if (wifiState != null && mobileState != null
                                && NetworkInfo.State.CONNECTED != wifiState
                                && NetworkInfo.State.CONNECTED != mobileState) {
                            // no network
                        int icon=R.drawable.notofication_50 ;
                    try {
                        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                        Intent batteryStatus = context.registerReceiver(null, ifilter);
                        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                                status == BatteryManager.BATTERY_STATUS_FULL;
                        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                       if (level<25 && !isCharging)
                           icon= R.drawable.notification_0 ;
                        else if (level >=25 && level<=70 && !isCharging)
                           icon=R.drawable.notofication_50 ;
                        else if (level>70 && !isCharging)
                           icon=R.drawable.notification_100 ;
                        else if (isCharging)
                           icon=R.drawable.notofication_ic ;
                    }catch (Exception e)
                    {
                        FirebaseCrash.report(e);
                    }

                            builder.setContentText("battery saver")
                                    .setOngoing(true)
                                    .setContentTitle("no Internet")
                                    .setSmallIcon(icon);
                            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(0, builder.build());
                            myservices.optimize(context,false);
                        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
                                    builder.setContentText("Optimized")
                                    .setOngoing(true)
                                    .setContentTitle("Wifi Connected")
                                    .setSmallIcon(R.drawable.wifi_icon);
                            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(0, builder.build());
                            myservices.optimize(context,false);
                        }
            }

        }catch (Exception e)
        {
            FirebaseCrash.report(e);
        }

    }
    public static void optimize(Context context, boolean all ) {
        try {
            SharedPreferences sharedPreferences=context.getSharedPreferences("data",MODE_PRIVATE) ;
            ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            final PackageManager pm = context.getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            for (ApplicationInfo packageInfo : packages) {
                if(! (packageInfo.packageName.contains("com.android") ||packageInfo.packageName.equalsIgnoreCase("android") ))
                if (packageInfo.packageName != context.getPackageName() && sharedPreferences.getBoolean(packageInfo.packageName,true)) {
                    if (all) {
                        manager.killBackgroundProcesses(packageInfo.packageName);
                    }
                    else if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                        manager.killBackgroundProcesses(packageInfo.packageName);
                    }
                }
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }
}
