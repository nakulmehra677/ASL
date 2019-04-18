package com.nakulmehra.hp.asl.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;

import com.nakulmehra.hp.asl.Activities.ChapterDetailActivity;

public class warningFragment extends DialogFragment {

    public static warningFragment newInstance(String message) {
        warningFragment frag = new warningFragment();
        Bundle args = new Bundle();
        args.putString("Message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString("Message");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Warning")
                .setMessage(message)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((ChapterDetailActivity) getActivity()).deleteChapterWarningFragmentDoPositiveClick();
                            }
                        }
                )
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                );
        Dialog dialog = alertDialog.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}