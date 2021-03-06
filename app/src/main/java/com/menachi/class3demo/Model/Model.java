package com.menachi.class3demo.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.client.AuthData;
import com.menachi.class3demo.Model.SQL.ModelSQL;
import com.menachi.class3demo.MyApplication;
import com.menachi.class3demo.ProductsActivity;

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
 * this class handle in all requests from control panel to backend
 */
public class Model {

    ModelCloudinary modelCloudinary;
    ModelFirebase modelFirebase;
    ModelSQL modelSql;
    User user = null;
    List<Product> productData = new LinkedList<Product>();
    List<LastPurchase> lastPurchasesList = new LinkedList<LastPurchase>();
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

    /**
     * constructor
     */
    private Model(){
        modelCloudinary = new ModelCloudinary();
        modelFirebase = new ModelFirebase(MyApplication.getContext());
        modelSql = new ModelSQL();
    }

    public void logOut(){
        user=null;
        productData=new LinkedList<Product>();
        lastPurchasesList = new LinkedList<LastPurchase>();
    }

    /**
     * Get all products and update the productData list on products
     * @param listener
     */
    public void initiateProducts(final ModelFirebase.ProductsDelegate listener){
        final Boolean[] isGetProducts = {false};
        final LastUpdates sqlProductLastUpdate = modelSql.getLastUpdate(Tabels.ProductTable);
        modelFirebase.getLastUpdateDate(Tabels.ProductTable, new ModelFirebase.LastUpdateEvents() {
            @Override
            public void onResult(final LastUpdates lastUpdates) {
                if(lastUpdates != null){
                    if(sqlProductLastUpdate != null){
                        if (!Tools.dateIsBigger(lastUpdates.getLastUpdate(), sqlProductLastUpdate.getLastUpdate())
                                && (sqlProductLastUpdate.getCountOfRecords() >= lastUpdates.getCountOfRecords() )) {
                            Log.e("TAG", "get all products from SQL");
                            isGetProducts[0] = true;
                            productData = modelSql.getAllProducts();
                            listener.onProductList(productData);
                        } else {
                            Log.e("TAG", "get all products from fireBase");
                            modelFirebase.getProducts(new ModelFirebase.ProductsDelegate() {
                                @Override
                                public void onProductList(List<Product> productsList) {
                                    if (isGetProducts[0].booleanValue() == false) {
                                        productData = productsList;
                                        for (Product product : productsList) {
                                            if (sqlProductLastUpdate != null) {
                                                if (Tools.dateIsBigger(product.getLastUpdate(), sqlProductLastUpdate.getLastUpdate())) {
                                                    modelSql.addProduct(product);
                                                }
                                            } else {
                                                modelSql.addProduct(product);
                                            }

                                        }
                                        isGetProducts[0] = true;
                                        //update SQL scheme on data updated
                                        modelSql.setLastUpdate(lastUpdates);
                                        listener.onProductList(productsList);
                                    }
                                }
                            });
                        }
                    //if data is not contains in SQL
                    }else{
                        Log.e("TAG", "get all products from fireBase");
                        modelFirebase.getProducts(new ModelFirebase.ProductsDelegate() {
                            @Override
                            public void onProductList(List<Product> productsList) {
                                if (isGetProducts[0].booleanValue() == false) {
                                    productData = productsList;
                                    for (Product product : productsList) {
                                        if (sqlProductLastUpdate != null) {
                                            if (Tools.dateIsBigger(product.getLastUpdate(), sqlProductLastUpdate.getLastUpdate())) {
                                                modelSql.addProduct(product);
                                            }
                                        } else {
                                            modelSql.addProduct(product);
                                        }

                                    }
                                    isGetProducts[0] = true;
                                    modelSql.setLastUpdate(lastUpdates);
                                    listener.onProductList(productsList);
                                }
                            }
                        });
                    }
                }else{
                    listener.onProductList(new LinkedList<Product>());
                }
            }

            @Override
            public void onCancel(String error) {
                Log.d("Error", "could not read products from firebase." + error);

                modelFirebase.getProducts(new ModelFirebase.ProductsDelegate() {
                    @Override
                    public void onProductList(List<Product> productsList) {
                        if (isGetProducts[0].booleanValue() == false) {
                            productData = productsList;
                            for (Product product : productsList) {
                                if (sqlProductLastUpdate != null) {
                                    if (Tools.dateIsBigger(product.getLastUpdate(), sqlProductLastUpdate.getLastUpdate())) {
                                        modelSql.addProduct(product);
                                        modelSql.setLastUpdateForSpecificRecord(Tabels.ProductTable, product.getLastUpdate());
                                    }
                                } else {
                                    modelSql.addProduct(product);
                                    modelSql.setLastUpdateForSpecificRecord(Tabels.ProductTable, product.getLastUpdate());
                                }

                            }
                            isGetProducts[0] = true;
                            listener.onProductList(productsList);
                        }
                    }
                });
            }
        });
    }

    /**
     * Get all products that purchased by curren user that logged in
     * @param userID
     * @param lastPurchasesEvents
     */
    public void initiateLastPurchasesList(String userID, final ModelFirebase.LastPurchasesEvents lastPurchasesEvents){
        final Boolean[] isGetLastPurchases = {false};
        //get last update object from SQL of table that we want to get data
        final LastUpdates sqlLastPurchasesLastUpdate = modelSql.getLastUpdate(Tabels.LastPurchasesTable);
        //get last update object from Firebase of table that we want to get data
        modelFirebase.getLastUpdateDate(Tabels.LastPurchasesTable, new ModelFirebase.LastUpdateEvents() {
            @Override
            public void onResult(final LastUpdates lastUpdates) {
                if(lastUpdates != null){
                    //check that this table was update before if not we need to get all data from Firebase
                    if(sqlLastPurchasesLastUpdate != null){
                        //check if the date that the SQL table is updated  old then Firebase  and the count of records is not less then Firebase
                        if (!Tools.dateIsBigger(lastUpdates.getLastUpdate(), sqlLastPurchasesLastUpdate.getLastUpdate())
                                && (sqlLastPurchasesLastUpdate.getCountOfRecords() >= lastUpdates.getCountOfRecords() )) {
                            Log.d("TAG", "get Last Purchases from SQL");
                            lastPurchasesList = modelSql.getLastPurchasesByUserId(user.getUserId());
                            isGetLastPurchases[0] = true;
                            lastPurchasesEvents.onResult(lastPurchasesList);

                        } else {
                            Log.d("TAG", "get Last Purchases  from firebase");
                            modelFirebase.getLastPurchases(user.getUserId(), new ModelFirebase.LastPurchasesEvents() {
                                @Override
                                public void onResult(List<LastPurchase> lastPurchases) {
                                    if (isGetLastPurchases[0].booleanValue() == false) {
                                        lastPurchasesList = lastPurchases;
                                        Log.d("TAG", "The last purchases was get successfully");
                                        for (LastPurchase lastPurchase : lastPurchases) {
                                            if (sqlLastPurchasesLastUpdate != null) {
                                                //check if the date that the SQL table updated is old then Firebase record
                                                if (Tools.dateIsBigger(lastPurchase.getLastUpdate(), sqlLastPurchasesLastUpdate.getLastUpdate())) {
                                                    modelSql.addLastPurchase(lastPurchase);
                                                }
                                            } else {
                                                modelSql.addLastPurchase(lastPurchase);
                                            }

                                        }
                                        isGetLastPurchases[0] = true;
                                        //Update the SQL on the date that we updates this table
                                        modelSql.setLastUpdate(lastUpdates);
                                        lastPurchasesEvents.onResult(lastPurchases);
                                    }
                                }

                                @Override
                                public void onCancel(String error) {
                                    Log.d("TAG", "error with get  last purchases");
                                    Log.d("TAG", error);
                                    lastPurchasesEvents.onCancel(error.toString());
                                }
                            });
                        }
                    } else {

                        Log.d("TAG", "get Last Purchases  from firebase");
                        modelFirebase.getLastPurchases(user.getUserId(), new ModelFirebase.LastPurchasesEvents() {
                            @Override
                            public void onResult(List<LastPurchase> lastPurchases) {
                                if (isGetLastPurchases[0].booleanValue() == false) {
                                    lastPurchasesList = lastPurchases;
                                    Log.d("TAG", "The last purchases was get successfully");
                                    for (LastPurchase lastPurchase : lastPurchases) {
                                        if (sqlLastPurchasesLastUpdate != null) {
                                            //check if the date that the SQL table updated is old then Firebase record
                                            if (Tools.dateIsBigger(lastPurchase.getLastUpdate(), sqlLastPurchasesLastUpdate.getLastUpdate())) {
                                                modelSql.addLastPurchase(lastPurchase);
                                            }
                                        } else {
                                            modelSql.addLastPurchase(lastPurchase);
                                        }

                                    }
                                    isGetLastPurchases[0] = true;
                                    //Update the SQL on the date that we updates this table
                                    modelSql.setLastUpdate(lastUpdates);
                                    lastPurchasesEvents.onResult(lastPurchases);
                                }
                            }

                            @Override
                            public void onCancel(String error) {
                                Log.d("TAG", "error with get  last purchases");
                                Log.d("TAG", error);
                                lastPurchasesEvents.onCancel(error.toString());
                            }
                        });
                    }

                } else {
                    lastPurchasesEvents.onResult(new LinkedList<LastPurchase>());
                }
            }

            @Override
            public void onCancel(String error) {
                Log.d("Error", "could not read last update date from firebase." + error);
                modelFirebase.getLastPurchases(user.getUserId(), new ModelFirebase.LastPurchasesEvents() {
                    @Override
                    public void onResult(List<LastPurchase> lastPurchases) {
                        if (isGetLastPurchases[0].booleanValue() == false) {
                            lastPurchasesList = lastPurchases;
                            Log.d("TAG", "The last purchases was get successfully from firebase");
                            lastPurchasesEvents.onResult(lastPurchases);

                            for (LastPurchase lastPurchase : lastPurchasesList) {
                                if (sqlLastPurchasesLastUpdate != null) {
                                    //check if the date that the SQL table updated is old then Firebase record
                                    if (Tools.dateIsBigger(lastPurchase.getLastUpdate(), sqlLastPurchasesLastUpdate.getLastUpdate())) {
                                        modelSql.addLastPurchase(lastPurchase);
                                        modelSql.setLastUpdateForSpecificRecord(Tabels.LastPurchasesTable, lastPurchase.getLastUpdate());
                                    }
                                } else {
                                    modelSql.addLastPurchase(lastPurchase);
                                    modelSql.setLastUpdateForSpecificRecord(Tabels.LastPurchasesTable, lastPurchase.getLastUpdate());
                                }

                            }
                            isGetLastPurchases[0] = true;
                            lastPurchasesEvents.onResult(lastPurchases);
                        }
                    }

                    @Override
                    public void onCancel(String error) {
                        Log.d("TAG", "error with get  last purchases from firebase");
                        Log.d("TAG", error);
                        lastPurchasesEvents.onCancel(error.toString());
                    }
                });
            }
        });
    }

    public List<Product> getProductByName(String textToSearch){
        List<Product> productToReturn = new LinkedList<Product>();
        if(productData != null){
            for (Product product : productData) {
                //Search just in lowercase
                if(product.getName().toLowerCase().contains(textToSearch.toLowerCase())){
                    productToReturn.add(product);
                }
            }
        }
        return productToReturn;
    }

    public List<Product> getProductData() {
        return productData;
    }
    public void getCommentsByProductId(final String productId,final ModelFirebase.CommentDelegate listener){

        final LastUpdates sqlCommentsLastUpdate = modelSql.getLastUpdate(Tabels.CommentsTable);
        final Boolean[] isGetComment = {false};
        modelFirebase.getLastUpdateDate(Tabels.CommentsTable, new ModelFirebase.LastUpdateEvents() {
            @Override
            public void onResult(final LastUpdates lastUpdates) {
                if(lastUpdates != null){
                    if(sqlCommentsLastUpdate != null){
                        //if date in firebase is not bigger then sqlCommentsLastUpdateDate
                        if (!Tools.dateIsBigger(lastUpdates.getLastUpdate(), sqlCommentsLastUpdate.getLastUpdate())
                               && (sqlCommentsLastUpdate.getCountOfRecords() >= lastUpdates.getCountOfRecords() ) ) {
                            Log.d("TAG", "get Comments from SQL");
                            isGetComment[0] = true;
                            listener.onCommentList(modelSql.getCommentsByProductId(productId));

                        } else {
                            Log.d("TAG", "get Comments  from firebase");
                            modelFirebase.getCommentsByProductId(productId, new ModelFirebase.CommentDelegate() {
                                @Override
                                public void onCommentList(List<Comment> commentsList) {
                                    if(isGetComment[0].booleanValue() == false){
                                        List<Comment> commentData = new LinkedList<Comment>();
                                        for (Comment comment : commentsList) {
                                            if(sqlCommentsLastUpdate != null){
                                                //check if the date that the SQL table updated is old then Firebase record
                                                if (Tools.dateIsBigger(comment.getLastUpdate(), sqlCommentsLastUpdate.getLastUpdate())) {
                                                    modelSql.addComment(comment);
                                                }
                                            }else{
                                                modelSql.addComment(comment);
                                            }
                                            commentData.add(comment);
                                        }
                                        isGetComment[0] = true;
                                        modelSql.setLastUpdate(lastUpdates);
                                        listener.onCommentList(commentData);
                                    }
                                }
                            });
                        }
                    }else{
                        Log.d("TAG", "get Comments  from firebase");
                        modelFirebase.getCommentsByProductId(productId, new ModelFirebase.CommentDelegate() {
                            @Override
                            public void onCommentList(List<Comment> commentsList) {
                                if(isGetComment[0].booleanValue() == false){
                                    List<Comment> commentData = new LinkedList<Comment>();
                                    for (Comment comment : commentsList) {
                                        if(sqlCommentsLastUpdate != null){
                                            //check if the date that the SQL table updated is old then Firebase record
                                            if (Tools.dateIsBigger(comment.getLastUpdate(), sqlCommentsLastUpdate.getLastUpdate())) {
                                                modelSql.addComment(comment);
                                            }
                                        }else{
                                            modelSql.addComment(comment);
                                        }
                                        commentData.add(comment);
                                    }
                                    isGetComment[0] = true;
                                    modelSql.setLastUpdate(lastUpdates);
                                    listener.onCommentList(commentData);
                                }
                            }
                        });
                    }
                }else{
                    listener.onCommentList(new LinkedList<Comment>());
                }
            }

            @Override
            public void onCancel(String error) {
                Log.d("Error", "could not read last update date from firebase." + error);
                modelFirebase.getCommentsByProductId(productId, new ModelFirebase.CommentDelegate() {
                    @Override
                    public void onCommentList(List<Comment> commentsList) {
                        if(isGetComment[0].booleanValue() == false){
                            Log.d("TAG", "The comments was get successfully from firebase");
                            List<Comment> commentData = new LinkedList<Comment>();
                            for (Comment comment : commentsList) {
                                if(sqlCommentsLastUpdate != null){
                                    //check if the date that the SQL table updated is old then Firebase record
                                    if (Tools.dateIsBigger(comment.getLastUpdate(), sqlCommentsLastUpdate.getLastUpdate())) {
                                        modelSql.addComment(comment);
                                        modelSql.setLastUpdateForSpecificRecord(Tabels.CommentsTable, comment.getLastUpdate());
                                    }
                                }else{
                                    modelSql.addComment(comment);
                                    modelSql.setLastUpdateForSpecificRecord(Tabels.CommentsTable, comment.getLastUpdate());
                                }

                            }
                            listener.onCommentList(commentData);
                        }
                    }
                });
            }
        });

    }
    public void setProductData(List<Product> productData) {
        this.productData = productData;
    }
    public void addProduct(Product product){
        Product updateProduct =  modelFirebase.addProduct(product);
        productData.add(updateProduct);
        //update the last update date of the specific collection that updated now
        modelFirebase.setLastUpdateDate(Tabels.ProductTable, Tools.getCurrentDate(), new ModelFirebase.SetLastUpdateEvent() {
            @Override
            public void onSuccess() {
                Log.e("TAG","setProductData  - SUCCESS to setLastUpdateDate");
            }

            @Override
            public void onError(String err) {
                Log.e("TAG","setProductData -  ERROR to setLastUpdateDate");
            }
        });
    }

    public void addComment(Comment comment) {
        Comment updateComment = modelFirebase.addComment(comment);
        //update the last update date of the specific collection that updated now
        modelFirebase.setLastUpdateDate(Tabels.CommentsTable, Tools.getCurrentDate(), new ModelFirebase.SetLastUpdateEvent() {
            @Override
            public void onSuccess() {
                Log.e("TAG", "addComment  - SUCCESS to setLastUpdateDate");
            }

            @Override
            public void onError(String err) {
                Log.e("TAG", "addComment -  ERROR to setLastUpdateDate");
            }
        });
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

    public void signup(User user, final SignupStatus listener){
        this.user = user;
        modelFirebase.createUser(user, new SignupStatus() {
            @Override
            public void isSignup(boolean status, User user) {
                if (status) {
                    listener.isSignup(status, user);
                    modelSql.addUser(user);
                    modelFirebase.setLastUpdateDate(Tabels.UsersTable, Tools.getCurrentDate(), new ModelFirebase.SetLastUpdateEvent() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(String err) {

                        }
                    });
                }else{
                    listener.isSignup(status, user);
                }
            }
        });

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
        modelFirebase.setLastUpdateDate(Tabels.UsersTable, user.getLastUpdate(), new ModelFirebase.SetLastUpdateEvent() {
            @Override
            public void onSuccess() {
                Log.e("TAG","updateUser  - SUCCESS to setLastUpdateDate");
            }

            @Override
            public void onError(String err) {
                Log.e("TAG","updateUser  - ERROR to setLastUpdateDate");
            }
        });
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
                    //save image in external storage
                    String imapgPath = saveImageToFile(image, imageName);
                    //save image in cloudinary
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
        //check if to user has a photo
        if(imageName.equals("None")){
            Bitmap image = null;
            listener.OnDone(image ,imageName);
        }else{
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


    public void addPurchaseToUser(LastPurchase lastPurchases){
        LastPurchase updaetLastPurchase = modelFirebase.addLastPurchases(lastPurchases);
        //update the last update date of the specific collection that updated now
        modelFirebase.setLastUpdateDate(Tabels.LastPurchasesTable, Tools.getCurrentDate(), new ModelFirebase.SetLastUpdateEvent() {
            @Override
            public void onSuccess() {
                Log.e("TAG","addPurchaseToUser  - SUCCESS to setLastUpdateDate");
            }

            @Override
            public void onError(String err) {
                Log.e("TAG","addPurchaseToUser  - ERROR to setLastUpdateDate");
            }
        });
    }

    /**
     * Contains the tables names
     * all app use in this constant
     */
    public static class Tabels {
        public static final String ProductTable = "Products";
        public static final String CommentsTable = "Comments";
        public static final String UsersTable = "Users";
        public static final String lastUpdateTable = "LastUpdates";
        public static final String LastPurchasesTable = "LastPurchase";
    }
    public static class Tools{
        public static int LOG_OUT = 401;
        public static int onBackPressed = 200;
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
        //check if date1 >  date2
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

    /**
     * define in alerts which function we need to use
     */
    public static class FunctionsToUse{
        public static final String PRODUCT_DETAILS = "product_details";
        public static final String RETURN_TO_LIST = "return_to_list";
    }
}
