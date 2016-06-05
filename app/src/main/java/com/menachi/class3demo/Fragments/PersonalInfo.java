package com.menachi.class3demo.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.menachi.class3demo.DateAndPickers.Date.DateEditText;
import com.menachi.class3demo.Model.User;
import com.menachi.class3demo.R;

public class PersonalInfo extends Fragment {

    public interface Delegate{
        void onReturnToList();
    }
    EditText fName;
    EditText lName;
    EditText address;
    DateEditText birthDate;
    User user;
    Delegate delegate;

    public PersonalInfo() {
        // Required empty public constructor
    }

    public void setCurrentUser(User user){
        this.user = user;
    }
    public void setDelegate(Delegate delegate){
        this.delegate = delegate;
    }
    // TODO: Rename and change types and number of parameters
    public static PersonalInfo newInstance(String param1, String param2) {
        PersonalInfo fragment = new PersonalInfo();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    @Override
    public void onDetach() {
        super.onDetach();
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
