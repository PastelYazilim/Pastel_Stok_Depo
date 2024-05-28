package com.pastelyazilim.depo.zbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import static android.app.Activity.RESULT_OK;

public class MessageDialogFragment extends DialogFragment {
    public interface MessageDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    private String mTitle;
    private String mMessage;
    private MessageDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MessageDialogFragment newInstance(String title, String message, MessageDialogListener listener) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage).setTitle(mTitle);

        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Activity currentActivity = getActivity();
                currentActivity.setResult(RESULT_OK, currentActivity.getIntent());
                currentActivity.finish();


            }

        });
        builder.setNegativeButton("Tekrar Dene", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogPositiveClick(MessageDialogFragment.this);
                }


            }

        });

        return builder.create();
    }
}
