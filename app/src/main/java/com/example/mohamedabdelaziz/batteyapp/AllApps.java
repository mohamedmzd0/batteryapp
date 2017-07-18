package com.example.mohamedabdelaziz.batteyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

public class AllApps extends Activity {
    RecyclerView recyclerView ;
    GridLayoutManager gridLayoutManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_apps);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview) ;
        Display display= getWindowManager().getDefaultDisplay() ;
        gridLayoutManager=new GridLayoutManager(getApplicationContext(),((int)display.getWidth()/300));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        Handler handler= new Handler() ;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new adapter());

            }
        }, 10);

    }

    public void back(View view) {
        onBackPressed();
    }

    class  adapter extends RecyclerView.Adapter<myholder>
    {
        @Override
        public myholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,null);
            myholder myhold= new myholder(view);
            return myhold;
        }

        @Override
        public void onBindViewHolder(myholder holder, final int position) {
            PackageManager packageManager= getApplicationContext().getPackageManager();
            try {
                holder.textView.setText(packageManager.getApplicationLabel(packageManager.getApplicationInfo(SlpashPage.packages.get(position)
                        .packageName, PackageManager.GET_META_DATA)));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                holder.textView.setText(SlpashPage.packages.get(position).packageName.replace("com", " ").replace(".", " ").replace("android", " ").replace("example", " ")
                        .replace("google","").trim());
            }
            final PackageManager pm = getPackageManager();
            if(pm.getLaunchIntentForPackage(SlpashPage.packages.get(position).packageName)==null)
                holder.statue.setText(" Not Running");
            else
                holder.statue.setText("Running");
            Drawable icon = null;
            try {
                icon = getPackageManager().getApplicationIcon(SlpashPage.packages.get(position).packageName);
                holder.imageView.setImageDrawable(icon);
            } catch (PackageManager.NameNotFoundException e) {
                FirebaseCrash.report(e);
                holder.imageView.setImageResource(R.mipmap.ic_launcher);
            }

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),Details.class) ;
                    intent.putExtra("index",position) ;
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_down,R.anim.push_up);
                    Log.d("item ",SlpashPage.packages.get(position).packageName) ;
                }
            });
        }

        @Override
        public int getItemCount() {
            return SlpashPage.packages.size();
        }
    }
    class  myholder extends RecyclerView.ViewHolder
    {
        ImageView imageView ;
        TextView textView;
        TextView statue ;
        View view ;

        public myholder(View itemView) {
            super(itemView);
            view=itemView;
            imageView= (ImageView) itemView.findViewById(R.id.item_image) ;
            textView= (TextView) itemView.findViewById(R.id.item_text) ;
            statue= (TextView) itemView.findViewById(R.id.statue) ;
        }
    }

}
