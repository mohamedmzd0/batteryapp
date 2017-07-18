package com.example.mohamedabdelaziz.batteyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by Mohamed Abd ELaziz on 7/14/2017.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
            try {
                    myservices.change_happend(context, intent);
                Handler handler= new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myservices.change_happend(context,intent);
                    }
                },500);
                }
            catch (Exception e)
            {
                FirebaseCrash.report(e);
            }
    }

}