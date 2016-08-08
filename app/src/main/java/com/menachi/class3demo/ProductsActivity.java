package com.menachi.class3demo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.menachi.class3demo.Alerts.BasicAlertDialog;
import com.menachi.class3demo.Fragments.ListProducts;
import com.menachi.class3demo.Fragments.NewComment;
import com.menachi.class3demo.Fragments.NewProduct;
import com.menachi.class3demo.Fragments.ProductDetails;
import com.menachi.class3demo.Model.Comment;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.ModelFirebase;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.Model.User;

import java.util.List;

public class ProductsActivity extends Activity implements ListProducts.Delegate, BasicAlertDialog.Delegate,NewProduct.Delegate,ProductDetails.Delegate,NewComment.Delegate {

    String currentFragment;
    ListProducts listProductsFragment;
    NewProduct newProductFragment;
    ProductDetails productDetailsFragment;
    NewComment newCommentFragment;
    ProgressBar progressBar;

    /**
     * Define all fragment that exist in this activity
     */
    public static class Fragments{
        public static final String LIST_PRODUCT = "list_product";
        public static final String ADD_PRODUCT = "add_product";
        public static final String PRODUCT_DETAILS = "product_detais";
        public static final String NEW_COMMENT = "new_comment";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        currentFragment = Fragments.LIST_PRODUCT;
        progressBar = (ProgressBar) findViewById(R.id.progressBar_products_list);
        progressBar.setVisibility(View.VISIBLE);
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
                transaction.addToBackStack(Fragments.LIST_PRODUCT);
                transaction.add(R.id.main_frag_container, listProductsFragment, "y");
                transaction.addToBackStack(Fragments.LIST_PRODUCT);
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
        if (currentFragment.equals(Fragments.PRODUCT_DETAILS)) {
            item.setTitle("Add Comment");
        }else if(currentFragment.equals(Fragments.LIST_PRODUCT)){
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
        if (currentFragment.equals(Fragments.LIST_PRODUCT)) {
            finish();

        } else if (currentFragment.equals(Fragments.ADD_PRODUCT)) {
            currentFragment = Fragments.LIST_PRODUCT;
        } else if (currentFragment.equals(Fragments.NEW_COMMENT)) {
            currentFragment = Fragments.PRODUCT_DETAILS;

        } else if (currentFragment.equals(Fragments.PRODUCT_DETAILS)) {
            currentFragment = Fragments.LIST_PRODUCT;
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
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.last_purchase : {
                Log.d("TAG","last_purchase selected");
                Intent intent = new Intent(getApplicationContext(),UserProfileActivity.class);
                //send to  UserProfileActivity the fragment to load
                intent.putExtra("fragment", "last_purchase");
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.log_out : {
                Log.d("TAG", "log_out selected");
                Model.instance().logOut();
                setResult(Model.Tools.LOG_OUT);
                finish();
                break;
            }
            case R.id.billing_info : {
                Log.d("TAG","billing_info selected");
                Intent intent = new Intent(getApplicationContext(),UserProfileActivity.class);
                //send to  UserProfileActivity the fragment to load
                intent.putExtra("fragment","billing_info");
                startActivityForResult(intent, 0);
                break;
            }
        }
        Log.d("TAG", "onUserDetails");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG","On activity result " + resultCode);
        if(resultCode== Model.Tools.LOG_OUT) {
            setResult(Model.Tools.LOG_OUT);
            finish();
        }
    }

    @Override
    public void onProductSelected(Product product) {
        if (currentFragment.equals(Fragments.LIST_PRODUCT)) {
            setTitle("Product Details");
            Log.d("TAG", "Student selected " + product.getProductId());
            productDetailsFragment = new ProductDetails();
            productDetailsFragment.setProduct(product);
            productDetailsFragment.setDelegate(this);
            currentFragment = Fragments.PRODUCT_DETAILS;
            FragmentManager fm = getFragmentManager();
//            fm.popBackStack (Fragments.LIST_PRODUCT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(Fragments.PRODUCT_DETAILS);
            ft.add(R.id.main_frag_container, productDetailsFragment);
            ft.hide(listProductsFragment);
            ft.show(productDetailsFragment);
            ft.commit();
        }
    }

    @Override
    public void onNewProduct() {
        if (currentFragment.equals(Fragments.LIST_PRODUCT)) {
            setTitle("New Product");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            newProductFragment = new NewProduct();
            newProductFragment.setDelegate(this);
            ft.addToBackStack(Fragments.ADD_PRODUCT);
            ft.add(R.id.main_frag_container, newProductFragment);
            ft.hide(listProductsFragment);
            ft.show(newProductFragment);
            ft.commit();
            currentFragment = Fragments.ADD_PRODUCT;
            invalidateOptionsMenu();
        }
    }

    @Override
    public void cancel() {
        onBackPressed();
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
        if (currentFragment.equals(Fragments.PRODUCT_DETAILS)) {
            Log.d("TAG", "Add a new comment to : " + product.getProductId());
            FragmentManager fm = getFragmentManager();
//            fm.popBackStack (Fragments.PRODUCT_DETAILS, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fm.beginTransaction();
            newCommentFragment = new NewComment();
            newCommentFragment.setProduct(product);
            newCommentFragment.setDelegate(this);
            ft.add(R.id.main_frag_container, newCommentFragment);
            ft.addToBackStack(Fragments.NEW_COMMENT);
            ft.hide(productDetailsFragment);
            ft.show(newCommentFragment);
            ft.commit();
            currentFragment = Fragments.NEW_COMMENT;
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onReturnToDetails(Product product) {
        if (currentFragment.equals(Fragments.NEW_COMMENT)) {
            Log.d("TAG", "Returning to product details");
            productDetailsFragment.setProduct(product);
            Model.instance().getCommentsByProductId(product.getProductId(),new ModelFirebase.CommentDelegate() {
                @Override
                public void onCommentList(List<Comment> commentsList) {
                    productDetailsFragment.setComments(commentsList);
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack (Fragments.NEW_COMMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.show(productDetailsFragment);
                    ft.commit();
                    currentFragment = Fragments.PRODUCT_DETAILS;
                    invalidateOptionsMenu();
                }
            });
        }
    }
}
