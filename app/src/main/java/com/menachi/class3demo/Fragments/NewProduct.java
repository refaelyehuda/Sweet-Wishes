package com.menachi.class3demo.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.menachi.class3demo.Alerts.BasicAlertDialog;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.R;

public class NewProduct extends Fragment {

    private OnFragmentInteractionListener mListener;
    public interface Delegate{
        void cancel();
        void onReturnToList();
    }

    Delegate delegate;
    EditText productName;
    EditText productPrice;
    EditText productType;
    EditText imageName;


    public NewProduct() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    public void setDelegate(Delegate delegate){
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_product, container, false);
        productName = (EditText) view.findViewById(R.id.addNewProductName);
        productPrice = (EditText) view.findViewById(R.id.addNewProductPrice);
        productType = (EditText) view.findViewById(R.id.addNewProductTypeName);
        imageName = (EditText) view.findViewById(R.id.addNewProductImageName);
        Button saveBtn = (Button) view.findViewById(R.id.saveNewProductBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelNewProductBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product pr = new Product(productName.getText().toString(),productPrice.getText().toString(),imageName.getText().toString(),
                        productType.getText().toString());

                Model.instance().add(pr);
                BasicAlertDialog addStudentAlert = new BasicAlertDialog("OK", "" + productName.getText().toString() + " Was Added Successfully For transfer type OK",delegate);
                addStudentAlert.show(getFragmentManager(), "Tag");

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegate != null){
                    Log.d("TAG", "New student fragment - before cancel function a");
                    delegate.cancel();
                }else{
                    Log.d("TAG","New student fragment - without cancel function");
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
