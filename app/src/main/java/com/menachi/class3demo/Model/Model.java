package com.menachi.class3demo.Model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.firebase.client.AuthData;
import com.menachi.class3demo.MyApplication;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Cloudinary example.
 */
public class Model {

    ModelCloudinary modelCloudinary;
    ModelFirebase modelFirebase;
    User user;
    List<Product> productData = new LinkedList<Product>();
    List<Comment> commentData = new LinkedList<Comment>();
    private static Model instance = new Model();

    public interface LoginStatus{
        /**
         * return true if user is loggedIn and false otherwise
         * @param status
         */
        public void isLoggedIn(boolean status,User user);
    }

    public interface SignupStatus{
        /**
         * return true if user is created and false otherwise
         * @param status
         */
        public void isSignup (boolean status,User user);
    }


    private Model(){
        modelCloudinary = new ModelCloudinary();
        modelFirebase = new ModelFirebase(MyApplication.getContext());
        initiateComments();
    }
    public void initiateProducts(final ModelFirebase.ProductsDelegate listener){
        modelFirebase.getProducts(new ModelFirebase.ProductsDelegate() {
            @Override
            public void onProductList(List<Product> productsList) {
                productData = productsList;
                listener.onProductList(productsList);
            }
        });
    }

    public List<Product> getProductData() {
        return productData;
    }


    public void initiateComments(){
        modelFirebase.getComments(new ModelFirebase.CommentDelegate() {
            @Override
            public void onCommentList(List<Comment> commentsList) {
                commentData = commentsList;
            }
        });

    }

    public List<Comment> getCommentsByProductId(String productId){
        List<Comment>commentList = new LinkedList<Comment>();
        for (Comment comment : commentData) {
           if(comment.getProductId().equals(productId) ){
               commentList.add(comment);
           }
        }
        return commentList;
    }



    public void setProductData(List<Product> productData) {
        this.productData = productData;
    }
    public void addProduct(Product product){
        productData.add(product);
        modelFirebase.addProduct(product);
    }

    public void addComment(Comment comment){
        modelFirebase.addComment(comment);
        commentData.add(comment);
    }

    /**
     * return the Model singleton
     * @return
     */
    public static Model instance (){
        return instance;
    }


    public void loginUser(String username , String password,LoginStatus listener){
        modelFirebase.Login(username, password, listener);
    }

    public void signup(User user,SignupStatus listener){
        modelFirebase.createUser(user, listener);

    }

    public AuthData getFirebaseAuth(){
        return modelFirebase.getAuthData();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        updateUser(user);
    }

    private void updateUser(User user){
        modelFirebase.updateUser(user.getUserId(), user);
    }

    /**
     * save image in cloudinary
     */
    public interface SaveImageListener{
        public void OnDone(Exception e);
    }

    /**
     * load image to cloudinary
     * @param image
     * @param imageName
     * @param listener
     */
    public void saveImage(final Bitmap image , final String imageName, final SaveImageListener listener){
        //open new thread for upload image to cloudinary
        AsyncTask<String,String,IOException> task = new AsyncTask<String, String, IOException>() {
            @Override
            protected IOException doInBackground(String... params) {
                try {
                    modelCloudinary.saveImage(image ,imageName,listener);
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return e;
                }
            }

            @Override
            protected void onPostExecute(IOException e) {
                super.onPostExecute(e);
                listener.OnDone(e);
            }
        };
        task.execute();
    }

    /**
     * get image from cloudinary
     */
    public interface GetImageListener{
        public void OnDone(Bitmap image,String imageName);
    }
    public void getImage(final String imageName, final GetImageListener listener){
        //open new thread for upload image to cloudinary
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap image =  modelCloudinary.getImage(imageName);
                return image;
            }

            @Override
            protected void onPostExecute(Bitmap image) {
                super.onPostExecute(image);
                listener.OnDone(image ,imageName);
            }
        };
        task.execute();

    }
}
