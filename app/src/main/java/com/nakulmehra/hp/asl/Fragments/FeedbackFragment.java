package com.nakulmehra.hp.asl.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nakulmehra.hp.asl.Managers.DatabaseManager;

public class FeedbackFragment extends DialogFragment {

    private EditText editText;
    private Context context;
    private DatabaseManager databaseManager;

    public FeedbackFragment() {
    }

    @SuppressLint("ValidFragment")
    public FeedbackFragment(Context context) {
        this.context = context;
    }

    public static FeedbackFragment newInstance(Context context) {
        FeedbackFragment frag = new FeedbackFragment(context);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        editText = new EditText(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        FrameLayout container = new FrameLayout(context);

        int margin = dpToPx(20, context.getResources());
        params.setMargins(margin, margin, margin, margin);

        editText.setLayoutParams(params);
        container.addView(editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Write feedback")
                .setView(container)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String feedbackMessage = editText.getText().toString();

                        if (!feedbackMessage.isEmpty()) {
                            databaseManager = new DatabaseManager(context);
                            databaseManager.sendFeedback(editText.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private int dpToPx(float dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
