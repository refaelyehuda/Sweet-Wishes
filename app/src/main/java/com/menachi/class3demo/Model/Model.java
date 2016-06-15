package com.menachi.class3demo.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import com.firebase.client.AuthData;
import com.menachi.class3demo.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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


    public void getCommentsByProductId(final String productId,final ModelFirebase.CommentDelegate listener){
        modelFirebase.getComments(new ModelFirebase.CommentDelegate() {
            @Override
            public void onCommentList(List<Comment> commentsList) {
                List<Comment> commentData  = new LinkedList<Comment>();
                for (Comment comment : commentsList) {
                    if(comment.getProductId().equals(productId) ){
                        commentData.add(comment);
                    }
                }
                listener.onCommentList(commentData);
            }
        });

    }



    public void setProductData(List<Product> productData) {
        this.productData = productData;
    }
    public void addProduct(Product product){
        Product updateProduct =  modelFirebase.addProduct(product);
        productData.add(updateProduct);
    }

    public void addComment(Comment comment){
        Comment updateComment = modelFirebase.addComment(comment);
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
                    String imapgPath = saveImageToFile(image, imageName);
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
                //first try to find the image on the device
                Bitmap image = loadImageFromFile(imageName);
                //if image not found - try downloading it from parse
                if (image == null) {
                    image =  modelCloudinary.getImage(imageName);
                    if (image != null)
                        //save the image locally for next time
                        saveImageToFile(image, imageName);
                }
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

    private String saveImageToFile(Bitmap imageBitmap, String imageFileName) {
        File fos = null;
        OutputStream out = null;
        File dir = null;
        try {
            dir = MyApplication.getContext().getExternalFilesDir(null);
            fos = new File(dir, imageFileName);
            out = new FileOutputStream(fos);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fos.getAbsolutePath();
    }

    private Bitmap loadImageFromFile(String fileName) {
        String str = null;
        Bitmap bitmap = null;
        try {
            File dir = MyApplication.getContext().getExternalFilesDir(null);
            InputStream inputStream = new FileInputStream(new File(dir, fileName));
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
