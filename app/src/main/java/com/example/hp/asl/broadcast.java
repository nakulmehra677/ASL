package com.example.hp.asl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.constraint.ConstraintLayout;
import android.view.View;

//import static com.example.hp.asl.Activities.ChapterListActivity.getData;

public class broadcast extends BroadcastReceiver {

    ConstraintLayout constraintLayout, no_internet;

    @Override
    public void onReceive(Context context, Intent intent) {

        constraintLayout = ((Activity) context).findViewById(R.id.parent_layout);

        View childView = constraintLayout.findViewById(R.id.main_content);
        View childView2 = constraintLayout.findViewById(R.id.no_internet);

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if (noConnectivity) {
                childView.setVisibility(View.GONE);
                childView2.setVisibility(View.VISIBLE);
            } else {
                //getData();
                childView.setVisibility(View.VISIBLE);
                childView2.setVisibility(View.GONE);
            }
        }
    }
}
