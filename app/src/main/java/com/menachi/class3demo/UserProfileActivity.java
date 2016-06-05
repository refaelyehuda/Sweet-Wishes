package com.menachi.class3demo;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        User currentUser = Model.instance().getUser();

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
