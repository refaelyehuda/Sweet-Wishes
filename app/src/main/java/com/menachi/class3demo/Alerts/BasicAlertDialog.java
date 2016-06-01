package com.menachi.class3demo.Alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.menachi.class3demo.Fragments.NewProduct;

/**
 * Created by refael yehuda on 4/14/2016.
 */
public class BasicAlertDialog extends DialogFragment {

    NewProduct.Delegate delegate;
    String title;
    String message;

    public BasicAlertDialog(String title, String message, NewProduct.Delegate delegate){
        this.title = title;
        this.message = message;
        this.delegate = delegate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title to our dialog
        builder.setTitle(title);
        //Set body message
        builder.setMessage(message);
        builder.setNeutralButton(this.title, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (delegate != null)
                    delegate.onReturnToList();
            }
        });
        return builder.create();
    }
}
