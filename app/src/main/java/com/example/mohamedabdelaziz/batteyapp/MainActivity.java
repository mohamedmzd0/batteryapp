package com.example.mohamedabdelaziz.batteyapp;


import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ImageView imageView ;
    ArrayList<String>strings ;
    static TextView level ;
    Boolean optimized = false ;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView= (ImageView) findViewById(R.id.battery_statue) ;
        strings=new ArrayList<>() ;
        level= (TextView) findViewById(R.id.level);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            ComponentName receiver = new ComponentName(this, PowerConnectionReceiver.class);
            PackageManager pm1 = this.getPackageManager();
            pm1.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            PowerConnectionReceiver.setstatue(level, isCharging);
        }catch (Exception e)
        {
            FirebaseCrash.report(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            ComponentName receiver = new ComponentName(this, PowerConnectionReceiver.class);
            PackageManager pm = this.getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }catch (Exception e)
        {
            FirebaseCrash.report(e);
        }
    }

    public void optimmize(View view) {
        if(!optimized) {
            optimized = true;
            myservices.optimize(getApplicationContext(), false);
            findViewById(R.id.optim).setVisibility(View.INVISIBLE);
            findViewById(R.id.gif).setVisibility(View.VISIBLE);
            findViewById(R.id.optim).setEnabled(false);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.optim).setEnabled(true);
                    findViewById(R.id.optim).setBackgroundResource(R.drawable.currect);
                    findViewById(R.id.optim).setVisibility(View.VISIBLE);
                    findViewById(R.id.gif).setVisibility(View.INVISIBLE);
                }
            }, 2000);
        }
else
        Snackbar.make(view, R.string.optimized , 5000).show();
    }

    public void AllApps(View view) {
        startActivity(new Intent(getApplicationContext(),AllApps.class));
        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }


    public void startmanager(View view) {
        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_SETTINGS));
        overridePendingTransition(R.anim.push_down,R.anim.push_up);
    }

    public void gosettings(View view) {
        startActivity(new Intent(getApplicationContext(),Settings.class));
        overridePendingTransition(R.anim.fadout,R.anim.fadin);
    }
}
