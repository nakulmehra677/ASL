package com.example.hp.asl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class broadcast extends BroadcastReceiver {

    ConstraintLayout constraintLayout, no_internet;


    @Override
    public void onReceive(Context context, Intent intent) {

        constraintLayout = ((Activity) context).findViewById(R.id.chlist);
        no_internet = ((Activity) context).findViewById(R.id.image);

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if (noConnectivity) {
                constraintLayout.setVisibility(View.GONE);
                no_internet.setVisibility(View.VISIBLE);

            } else {

                chlist.getData();
                constraintLayout.setVisibility(View.VISIBLE);
                no_internet.setVisibility(View.GONE);
            }
        }
    }
}
