package com.menachi.class3demo;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;

import com.menachi.class3demo.Fragments.PersonalInfo;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.User;

public class UserProfileActivity extends Activity implements PersonalInfo.Delegate {
    String currentFragment;
    PersonalInfo personalInfoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        User currentUser = Model.instance().getUser();
        String fragmentToLoad = getIntent().getStringExtra("fragment");
        if(fragmentToLoad.equals("personal_info")){
            currentFragment = "personal_info";
            personalInfoFragment = new PersonalInfo();
            personalInfoFragment.setDelegate(this);
            personalInfoFragment.setCurrentUser(currentUser);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.user_profile_main_frag,personalInfoFragment, "y");
            transaction.addToBackStack("personal_info");
            transaction.show(personalInfoFragment);
            transaction.commit();
        }

    }


    public void onUserDetails(MenuItem item){
        switch (item.getItemId()) {
            case R.id.personal_info : {
                Log.d("TAG","personal_info selected");
            }
            case R.id.last_purchase : {
                Log.d("TAG","last_purchase selected");
            }
            case R.id.reset_password : {
                Log.d("TAG","reset_password selected");
            }
            case R.id.billing_info : {
                Log.d("TAG","billing_info selected");
            }
        }
        Log.d("TAG", "onUserDetails");
    }

    @Override
    public void onReturnToList() {
        Log.d("TAG", "Returning to List");
        Intent back = new Intent(this, ProductsActivity.class);
        startActivity(back);
        this.finish();
    }
}
