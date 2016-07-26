package com.menachi.class3demo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;

import com.menachi.class3demo.Fragments.BillingInfo;
import com.menachi.class3demo.Fragments.LastPurchases;
import com.menachi.class3demo.Fragments.PersonalInfo;
import com.menachi.class3demo.Model.LastPurchase;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.ModelFirebase;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.Model.User;

import java.util.List;

public class UserProfileActivity extends Activity implements PersonalInfo.Delegate,BillingInfo.Delegate,LastPurchases.Delegate {
    String currentFragment;
    PersonalInfo personalInfoFragment;
    BillingInfo billingInfoFragment;
    LastPurchases lastPurchasesFragment;
    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        final String fragmentToLoad = getIntent().getStringExtra("fragment");
        currentUser = Model.instance().getUser();
        Model.instance().initiateLastPurchasesList(currentUser.getUserId(), new ModelFirebase.LastPurchasesEvents() {
            @Override
            public void onResult(List<LastPurchase> lastPurchases) {
                Model.instance().setLastPurchasesList(lastPurchases);
                Log.d("TAG", "The last purchases was get successfully");
                if (fragmentToLoad.equals("personal_info")) {
                    personalInfoFragment.setCurrentUser(currentUser);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.add(R.id.user_profile_main_frag, personalInfoFragment, "y");
                    transaction.addToBackStack("personal_info");
                    transaction.show(personalInfoFragment);
                    transaction.commit();
                } else if (fragmentToLoad.equals("billing_info")) {
                    currentFragment = "billing_info";
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.add(R.id.user_profile_main_frag, billingInfoFragment, "y");
                    transaction.addToBackStack("billing_info");
                    transaction.show(billingInfoFragment);
                    transaction.commit();
                } else if (fragmentToLoad.equals("last_purchase")) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.add(R.id.user_profile_main_frag, lastPurchasesFragment, "y");
                    transaction.addToBackStack("last_purchase");
                    transaction.show(lastPurchasesFragment);
                    transaction.commit();
                }
            }

            @Override
            public void onCancel(String error) {
                Log.d("TAG", "error with get  last purchases");
                Log.d("TAG", error);
            }
        });
        if(fragmentToLoad.equals("personal_info")){
            currentFragment = "personal_info";
            personalInfoFragment = new PersonalInfo();
            personalInfoFragment.setDelegate(this);
            personalInfoFragment.setCurrentUser(currentUser);
        }else if(fragmentToLoad.equals("billing_info")){
            currentFragment = "billing_info";
            billingInfoFragment = new BillingInfo();
            billingInfoFragment.setDelegate(this);
            billingInfoFragment.setCurrentUser(currentUser);
        }else if(fragmentToLoad.equals("last_purchase")){
            currentFragment = "last_purchase";
            lastPurchasesFragment = new LastPurchases();
            lastPurchasesFragment.setDelegate(this);
        }

    }

    public void onUserDetails(MenuItem item){
        switch (item.getItemId()) {
            case R.id.personal_info : {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                personalInfoFragment = new PersonalInfo();
                personalInfoFragment.setCurrentUser(currentUser);
                personalInfoFragment.setDelegate(this);
                ft.add(R.id.main_frag_container, personalInfoFragment);
                ft = hideCurrentFragment(ft);
                ft.show(personalInfoFragment);
                ft.commit();
                currentFragment = "personal_info";
                invalidateOptionsMenu();
                Log.d("TAG", "billing_info selected");
            }
            case R.id.last_purchase : {
                Log.d("TAG","last_purchase selected");
            }
            case R.id.reset_password : {
                Log.d("TAG","reset_password selected");
            }
            case R.id.billing_info : {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                billingInfoFragment = new BillingInfo();
                billingInfoFragment.setCurrentUser(currentUser);
                billingInfoFragment.setDelegate(this);
                ft.add(R.id.main_frag_container, billingInfoFragment);
                ft = hideCurrentFragment(ft);
                ft.show(billingInfoFragment);
                ft.commit();
                currentFragment = "billing_info";
                invalidateOptionsMenu();
                Log.d("TAG","billing_info selected");
            }
        }
        Log.d("TAG", "onUserDetails");
    }

    /**
     * hide the current fragment
     * @param ft
     * @return
     */
    public FragmentTransaction hideCurrentFragment(FragmentTransaction ft){
        if(currentFragment.equals("personal_info")) {
            ft.hide(personalInfoFragment);
            ft.addToBackStack("personal_info");
        }else if(currentFragment.equals("last_purchase")) {

        }else if(currentFragment.equals("reset_password")) {

        }else if(currentFragment.equals("billing_info")) {
            ft.hide(billingInfoFragment);
            ft.addToBackStack("billing_info");
        }

        return ft;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            String name = getFragmentManager().getBackStackEntryAt(count - 1).getName();
            if (name.equals("personal_info")) {
                currentFragment = "personal_info";
            } else {
                currentFragment = "billing_info";
            }
            invalidateOptionsMenu();
            //this.getFragmentManager().popBackStack();
        }
        else{
            finish();
        }
    }
    @Override
    public void cancel() {
        Log.d("TAG", "Returning to List");
        Intent back = new Intent(this, ProductsActivity.class);
        startActivity(back);
        this.finish();
    }

    @Override
    public void onReturnToList() {
        Log.d("TAG", "Returning to List");
        Intent back = new Intent(this, ProductsActivity.class);
        startActivity(back);
        this.finish();
    }

    @Override
    public void onProductSelected(Product st) {

    }

    @Override
    public void onNewProduct() {

    }
}
