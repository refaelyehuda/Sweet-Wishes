package com.menachi.class3demo.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.client.AuthData;
import com.menachi.class3demo.Model.SQL.ModelSQL;
import com.menachi.class3demo.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Cloudinary example.
 */
public class Model {

    ModelCloudinary modelCloudinary;
    ModelFirebase modelFirebase;
    ModelSQL modelSql;
    User user;
    List<Product> productData = new LinkedList<Product>();
    List<LastPurchase> lastPurchasesList;
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
        modelSql = new ModelSQL();
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

    public void initiatelastPurchasesList(String userID,ModelFirebase.LastPurchasesEvents lastPurchasesEvents){
        modelFirebase.getLastPurchases(user.getUserId(), new ModelFirebase.LastPurchasesEvents() {
            @Override
            public void onResult(List<LastPurchase> lastPurchases) {
                lastPurchasesList = lastPurchases;
                Log.d("TAG", "The last purchases was get successfully");
            }

            @Override
            public void onCancel(String error) {
                Log.d("TAG", "error with get  last purchases");
                Log.d("TAG", error);
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
        modelFirebase.setLastUpdateDate(FirebaseTabels.lastUpdateTable,Tools.getCurrentDate());

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
        modelSql.addUser(user);

    }

    public AuthData getFirebaseAuth(){
        return modelFirebase.getAuthData();
    }

    public User getUser() {
        return user;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    public void updateUser(User user){
        modelFirebase.updateUser(user.getUserId(), user);
        modelFirebase.setLastUpdateDate(FirebaseTabels.UsersTable, user.getLastUpdate());
        modelSql.updateUserByID(user);
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


    /**
     * return all product the purchases by the user
      * @return
     */

    public List<Product> getLastPurchasesProductsList() {
        List<Product> products = new LinkedList<Product>();
        for (LastPurchase lastPurchase:lastPurchasesList) {
            products.add(lastPurchase.getProduct());
        }
        return products;
    }


    public void setLastPurchasesList(List<LastPurchase> lastPurchasesList) {
        this.lastPurchasesList = lastPurchasesList;
    }

    public void addPurchaseToUser(LastPurchase lastPurchases){
        lastPurchasesList.add(lastPurchases);
        modelFirebase.addLastPurchases(lastPurchases);
        modelFirebase.setLastUpdateDate(FirebaseTabels.lastUpdateTable,Tools.getCurrentDate());
    }


    public static class FirebaseTabels{
        public static final String ProductTable = "Products";
        public static final String CommentsTable = "Comments";
        public static final String UsersTable = "Users";
        public static final String lastUpdateTable = "lastUpdates";
        public static final String LastPurchasesTable = "LastPurchase";
    }
    public static class Tools{
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        private static final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        public static String getCurrentDate() {
            // Get the date today using Calendar object.
            Date today = Calendar.getInstance().getTime();
            // Using DateFormat format method we can create a string
            // representation of a date with the defined format.
            return df.format(today);
        }

        public static boolean dateIsBigger(String date1, String date2){
            if(date1==null&&date2==null) return true;
            if(date1==null ) return false;
            if(date2==null) return true;
            long firstDate = new Long(date1);
            Log.d("TAG", "after: " + firstDate);
            long lastDate = new Long(date2);
            return (firstDate>lastDate);
        }
    }
}
