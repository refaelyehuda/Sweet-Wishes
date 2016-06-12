package com.menachi.class3demo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.menachi.class3demo.Fragments.ListProducts;
import com.menachi.class3demo.Fragments.NewComment;
import com.menachi.class3demo.Fragments.NewProduct;
import com.menachi.class3demo.Fragments.ProductComments;
import com.menachi.class3demo.Fragments.ProductDetails;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.ModelFirebase;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.Model.User;

import java.util.List;

public class ProductsActivity extends Activity implements ListProducts.Delegate,NewProduct.Delegate,ProductDetails.Delegate,NewComment.Delegate {

    String currentFragment = "listProducts";
    ListProducts listProductsFragment;
    NewProduct newProductFragment;
    ProductDetails productDetailsFragment;
    NewComment newCommentFragment;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_products_list);
        //The first fragment that need to be displayed is listStudentFragment
        listProductsFragment = new ListProducts();
        listProductsFragment.setDelegate(this);
        //get the current userID
        User currentUser = Model.instance().getUser();
        //
        Model.instance().initiateProducts(new ModelFirebase.ProductsDelegate() {
            @Override
            public void onProductList(List<Product> productsList) {
                Log.e("TAG", "data arrive from firebase");
                progressBar.setVisibility(View.GONE);
                //User currentUser = (User) getIntent().getSerializableExtra("User");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.main_frag_container, listProductsFragment, "y");
                transaction.addToBackStack("listProducts");
                transaction.show(listProductsFragment);
                transaction.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.addProductBtn);
        //check which object need to display in menu
        if (currentFragment.equals("details")) {
            item.setTitle("Add Comment");
        }else if(currentFragment.equals("listProducts")){
            setTitle("Products");
            item.setIcon(android.R.drawable.ic_input_add);
        }else{
            item.setTitle("");
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            String name = getFragmentManager().getBackStackEntryAt(count - 1).getName();
            if (name.equals("details")) {
                currentFragment = "details";
            }else if(name.equals("New Comment")){
                currentFragment = "New Comment";
            }
            else {
                currentFragment = "listProducts";
            }
            invalidateOptionsMenu();
        }
        else{
            finish();
        }
    }

    /**
     * this function defined to listen to user profile
     * @param item
     */
    public void onUserDetails(MenuItem item){
        switch (item.getItemId()) {
            case R.id.personal_info : {
                Log.d("TAG","personal_info selected");
                Intent intent = new Intent(getApplicationContext(),UserProfileActivity.class);
                //send to  UserProfileActivity the fragment to load
                intent.putExtra("fragment","personal_info");
                startActivity(intent);
                break;
            }
            case R.id.last_purchase : {
                Log.d("TAG","last_purchase selected");
                Intent intent = new Intent(getApplicationContext(),UserProfileActivity.class);
                //send to  UserProfileActivity the fragment to load
                intent.putExtra("fragment","last_purchase");
                startActivity(intent);
                break;
            }
            case R.id.reset_password : {
                Log.d("TAG","reset_password selected");
                Intent intent = new Intent(getApplicationContext(),UserProfileActivity.class);
                //send to  UserProfileActivity the fragment to load
                intent.putExtra("fragment","reset_password");
                startActivity(intent);
                break;
            }
            case R.id.billing_info : {
                Log.d("TAG","billing_info selected");
                Intent intent = new Intent(getApplicationContext(),UserProfileActivity.class);
                //send to  UserProfileActivity the fragment to load
                intent.putExtra("fragment","billing_info");
                startActivity(intent);
                break;
            }
        }
        Log.d("TAG", "onUserDetails");
    }

    @Override
    public void onProductSelected(Product product) {
        if (currentFragment.equals("listProducts")){
            setTitle("Product Details");
            Log.d("TAG", "Student selected " + product.getProductId());
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            productDetailsFragment = new ProductDetails();
            productDetailsFragment.setProduct(product);
            productDetailsFragment.setDelegate(this);
            ft.add(R.id.main_frag_container, productDetailsFragment);
            ft.hide(listProductsFragment);
            ft.addToBackStack("listProducts");
            ft.show(productDetailsFragment);
            ft.commit();
            currentFragment = "details";
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onNewProduct() {
        if (currentFragment.equals("listProducts")) {
            setTitle("New Product");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            newProductFragment = new NewProduct();
            newProductFragment.setDelegate(this);
            ft.add(R.id.main_frag_container, newProductFragment);
            ft.hide(listProductsFragment);
            ft.addToBackStack("listProducts");
            ft.show(newProductFragment);
            ft.commit();
            currentFragment = "add";
            invalidateOptionsMenu();
        }
    }

    @Override
    public void cancel() {
        int count = getFragmentManager().getBackStackEntryCount();
        if(getFragmentManager().getBackStackEntryAt(count - 1).getName().equals("details"))
            currentFragment = "details";
        else
            currentFragment = "listProducts";
        invalidateOptionsMenu();
        getFragmentManager().popBackStack();
    }

    @Override
    public void onReturnToList() {
        Log.d("TAG", "Returning to List Products");
        Intent back = new Intent(this, ProductsActivity.class);
        startActivity(back);
        this.finish();

    }

    @Override
    public void onNewComment(Product product) {
        if (currentFragment.equals("details")) {
            Log.d("TAG", "Add a new comment to : " + product.getProductId());
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            newCommentFragment = new NewComment();
            newCommentFragment.setProduct(product);
            newCommentFragment.setDelegate(this);
            ft.add(R.id.main_frag_container, newCommentFragment);
            ft.hide(productDetailsFragment);
            ft.addToBackStack("details");
            ft.show(newCommentFragment);
            ft.commit();
            currentFragment = "New Comment";
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onReturnToDetails(Product product) {
        if (currentFragment.equals("New Comment")) {
            Log.d("TAG", "Returning to product details");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            productDetailsFragment = new ProductDetails();
            productDetailsFragment.setProduct(product);
            productDetailsFragment.setDelegate(this);
            ft.add(R.id.main_frag_container, productDetailsFragment);
            ft.hide(newCommentFragment);
            ft.addToBackStack("New Comment");
            ft.show(productDetailsFragment);
            ft.commit();
            currentFragment = "details";
            invalidateOptionsMenu();
        }
    }
}
