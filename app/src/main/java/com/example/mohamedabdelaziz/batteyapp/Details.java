package com.example.mohamedabdelaziz.batteyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;


public class Details extends Activity {

    int index;
    TextView textView1, textView2;
    SharedPreferences sharedPreferences;
    Intent launchIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        index = getIntent().getIntExtra("index", -1);
        textView1 = (TextView) findViewById(R.id.app_name);
        textView2 = (TextView) findViewById(R.id.app_package);
        PackageManager packageManager = getApplicationContext().getPackageManager();
        try {
            textView1.setText(packageManager.getApplicationLabel(packageManager.getApplicationInfo(SlpashPage.packages.get(index)
                    .packageName, PackageManager.GET_META_DATA)));
        } catch (PackageManager.NameNotFoundException e) {
            FirebaseCrash.report(e);
            textView1.setText(SlpashPage.packages.get(index).packageName.replace("com", " ").replace(".", " ").replace("android", " ").replace("example", " ")
                    .replace("google", "").trim());
        }
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        final PackageManager pm = getPackageManager();
        if (pm.getLaunchIntentForPackage(SlpashPage.packages.get(index).packageName) == null)
            textView2.setText(" Not Running");
        else
            textView2.setText("Running");
        Drawable icon;
        try {
            icon = getPackageManager().getApplicationIcon(SlpashPage.packages.get(index).packageName);
            ((ImageView) findViewById(R.id.app_icon)).setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            FirebaseCrash.report(e);
            ((ImageView) findViewById(R.id.app_icon)).setImageResource(R.mipmap.ic_launcher);
        }

        if (!SlpashPage.packages.get(index).packageName.equalsIgnoreCase("com.example.mohamedabdelaziz.batteyapp") && !(SlpashPage.packages.get(index).packageName.contains("com.android") || SlpashPage.packages.get(index).packageName.equalsIgnoreCase("android")))
            ((CheckBox) findViewById(R.id.run_background)).setChecked(!sharedPreferences.getBoolean(SlpashPage.packages.get(index).packageName, true));
        else {
            ((CheckBox) findViewById(R.id.run_background)).setChecked(true);
            findViewById(R.id.run_background).setEnabled(false);
        }
        PackageManager pm2 = getApplicationContext().getPackageManager();
        launchIntent = pm2.getLaunchIntentForPackage(SlpashPage.packages.get(index).packageName);
        if (launchIntent == null) {
            findViewById(R.id.goapp).setEnabled(false);
        }
    }

    public void allowbackground(View view) {
        if (((CheckBox) findViewById(R.id.run_background)).isChecked()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SlpashPage.packages.get(index).packageName, false);
            editor.commit();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SlpashPage.packages.get(index).packageName, true);
            editor.commit();
        }
    }

    public void back(View view) {
        onBackPressed();
    }


    public void gotoapp(View view) {
        if (launchIntent != null)
            startActivity(launchIntent);
        else {

            Toast.makeText(getApplicationContext(), "System Apps", Toast.LENGTH_SHORT).show();
        }
    }
    }

