package com.menachi.class3demo.Alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.menachi.class3demo.Fragments.NewProduct;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.Product;

public class BasicAlertDialog extends DialogFragment {

    Delegate delegate;
    public interface Delegate{
        void onReturnToDetails(Product product);
        void onReturnToList();
    }
    String title;
    String message;
    String functionToUse;
    Product product;

    public BasicAlertDialog(String title, String message, Delegate delegate , String functionToUse){
        this.title = title;
        this.message = message;
        this.delegate = delegate;
        this.functionToUse = functionToUse;
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
                    if (functionToUse.equals(Model.FunctionsToUse.RETURN_TO_LIST)) {
                        delegate.onReturnToList();
                    } else if(functionToUse.equals(Model.FunctionsToUse.PRODUCT_DETAILS)){
                        delegate.onReturnToDetails(product);
                    }
            }
        });
        return builder.create();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
