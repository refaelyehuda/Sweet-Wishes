package com.menachi.class3demo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.menachi.class3demo.Fragments.BillingInfo;
import com.menachi.class3demo.Fragments.LastPurchases;
import com.menachi.class3demo.Fragments.PersonalInfo;
import com.menachi.class3demo.Model.LastPurchase;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.ModelFirebase;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.Model.User;

import java.lang.reflect.Field;
import java.util.List;

public class UserProfileActivity extends Activity implements PersonalInfo.Delegate,BillingInfo.Delegate,LastPurchases.Delegate {
    String currentFragment;
    PersonalInfo personalInfoFragment;
    BillingInfo billingInfoFragment;
    LastPurchases lastPurchasesFragment;
    User currentUser;
    /**
     * Define all fragment that exist in this activity
     */
    public static class Fragments{
        public static final String PERSONAL_INFO = "personal_info";
        public static final String BILLING_INFO = "billing_info";
        public static final String LAST_PURCHASES = "last_purchase";
        public static final String LOG_OUT = "log_out";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        final String fragmentToLoad = getIntent().getStringExtra("fragment");
        currentUser = Model.instance().getUser();
        if (fragmentToLoad.equals(Fragments.PERSONAL_INFO)) {
            currentFragment = Fragments.PERSONAL_INFO;
            personalInfoFragment = new PersonalInfo();
            personalInfoFragment.setDelegate(this);
        } else if (fragmentToLoad.equals(Fragments.BILLING_INFO)) {
            currentFragment = Fragments.BILLING_INFO;
            billingInfoFragment = new BillingInfo();
            billingInfoFragment.setDelegate(this);
        } else if (fragmentToLoad.equals(Fragments.LAST_PURCHASES)) {
            currentFragment = Fragments.LAST_PURCHASES;
            lastPurchasesFragment = new LastPurchases();
            lastPurchasesFragment.setDelegate(this);
        }
        Model.instance().initiateLastPurchasesList(currentUser.getUserId(), new ModelFirebase.LastPurchasesEvents() {
            @Override
            public void onResult(List<LastPurchase> lastPurchases) {
                Model.instance().setLastPurchasesList(lastPurchases);
                Log.d("TAG", "The last purchases was get successfully");
                if (fragmentToLoad.equals(Fragments.PERSONAL_INFO)) {
                    personalInfoFragment.setCurrentUser(currentUser);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.add(R.id.user_profile_main_frag, personalInfoFragment, "y");
                    transaction.addToBackStack(Fragments.PERSONAL_INFO);
                    transaction.show(personalInfoFragment);
                    transaction.commit();
                } else if (fragmentToLoad.equals(Fragments.BILLING_INFO)) {
                    billingInfoFragment.setCurrentUser(currentUser);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.add(R.id.user_profile_main_frag, billingInfoFragment, "y");
                    transaction.addToBackStack(Fragments.BILLING_INFO);
                    transaction.show(billingInfoFragment);
                    transaction.commit();
                } else if (fragmentToLoad.equals(Fragments.LAST_PURCHASES)) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.add(R.id.user_profile_main_frag, lastPurchasesFragment, "y");
                    transaction.addToBackStack(Fragments.LAST_PURCHASES);
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
    }



    public void onUserDetails(MenuItem item){
        switch (item.getItemId()) {
            case R.id.personal_info : {
                if(!currentFragment.equals(Fragments.PERSONAL_INFO)){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    personalInfoFragment = new PersonalInfo();
                    personalInfoFragment.setCurrentUser(currentUser);
                    personalInfoFragment.setDelegate(this);
                    ft.add(R.id.user_profile_main_frag, personalInfoFragment);
                    ft = hideCurrentFragment(ft);
                    ft.show(personalInfoFragment);
                    ft.commit();
                    currentFragment = Fragments.PERSONAL_INFO;
                    invalidateOptionsMenu();
                    Log.d("TAG", "personal_info selected");
                }
                break;
            }
            case R.id.last_purchase : {
                if(!currentFragment.equals(Fragments.LAST_PURCHASES)){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    lastPurchasesFragment = new LastPurchases();
                    lastPurchasesFragment.setDelegate(this);
                    ft.add(R.id.user_profile_main_frag, lastPurchasesFragment);
                    ft = hideCurrentFragment(ft);
                    ft.show(lastPurchasesFragment);
                    ft.commit();
                    currentFragment = Fragments.LAST_PURCHASES;
                    invalidateOptionsMenu();
                    Log.d("TAG","last_purchase selected");
                }
                break;
            }
            case R.id.log_out : {
                Log.d("TAG", "Log Out selected");
                Model.instance().logOut();
                setResult(Model.Tools.LOG_OUT);
                finish();
                break;
            }
            case R.id.billing_info : {
                if(!currentFragment.equals(Fragments.BILLING_INFO)){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    billingInfoFragment = new BillingInfo();
                    billingInfoFragment.setCurrentUser(currentUser);
                    billingInfoFragment.setDelegate(this);
                    ft.add(R.id.user_profile_main_frag, billingInfoFragment);
                    ft = hideCurrentFragment(ft);
                    ft.show(billingInfoFragment);
                    ft.commit();
                    currentFragment = Fragments.BILLING_INFO;
                    invalidateOptionsMenu();
                    Log.d("TAG","billing_info selected");
                }
                break;
            }
        }
        Log.d("TAG", "onUserDetails");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.addProductBtn);
        item.setTitle("");
        item.setEnabled(false);
        setTitle("User Profile");
        return true;
    }

    /**
     * hide the current fragment
     * @param ft
     * @return
     */
    public FragmentTransaction hideCurrentFragment(FragmentTransaction ft){
        if(currentFragment.equals(Fragments.PERSONAL_INFO)) {
            ft.hide(personalInfoFragment);
        }else if(currentFragment.equals(Fragments.LAST_PURCHASES)) {
            ft.hide(lastPurchasesFragment);
        }else if(currentFragment.equals(Fragments.BILLING_INFO)) {
            ft.hide(billingInfoFragment);
        }

        return ft;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
        finish();
    }


    @Override
    public void onProductSelected(Product st) {

    }

    @Override
    public void onNewProduct() {

    }
}
