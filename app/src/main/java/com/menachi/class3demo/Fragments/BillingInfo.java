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
import com.menachi.class3demo.DateAndPickers.Date.DateEditText;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.User;
import com.menachi.class3demo.R;

import java.util.HashMap;
import java.util.Map;

public class BillingInfo extends Fragment {
    private OnFragmentInteractionListener mListener;

    public interface Delegate extends NewProduct.Delegate{
    }
    Delegate delegate;
    User user;
    EditText billingName;
    EditText userCreditCard;
    DateEditText userCardExpiryDate;


    public BillingInfo() {
        // Required empty public constructor
    }

    public static BillingInfo newInstance(String param1, String param2) {
        BillingInfo fragment = new BillingInfo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

    public void setCurrentUser(User user){
        this.user = user;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_billing_info, container, false);
        if (user != null){
            billingName = (EditText) view.findViewById(R.id.billingName);
            userCreditCard = (EditText) view.findViewById(R.id.userCreditCard);
            userCardExpiryDate = (DateEditText) view.findViewById(R.id.userCardExpiryDate);

            Map<String, String> billingInfo = user.getBillingInfo();
            billingName.setText(billingInfo.get("billingName"));
            userCreditCard.setText(billingInfo.get("userCreditCard"));
            userCardExpiryDate.setText(billingInfo.get("userCardExpiryDate"));

            Button saveBtn = (Button) view.findViewById(R.id.saveUserBillingInfo);
            Button cancelBtn = (Button) view.findViewById(R.id.cancelUserBillingInfo);

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "Canceling Edit of user");
                    if (delegate != null) {
                        delegate.onReturnToList();
                    }
                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> billingInfo = new HashMap<String, String>();
                    billingInfo.put("billingName", billingName.getText().toString());
                    billingInfo.put("userCreditCard",userCreditCard.getText().toString());
                    billingInfo.put("userCardExpiryDate", userCardExpiryDate.getText().toString());
                    user.setBillingInfo(billingInfo);
                    Model.instance().setUser(user);
                    Log.d("TAG", "student created");
                    BasicAlertDialog alert;
                    Model.instance().setUser(user);
                    alert = new BasicAlertDialog("OK"," The user update with billing info  successfully", delegate);
                    alert.show(getFragmentManager(), "TAG");
                }
            });
        }
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
