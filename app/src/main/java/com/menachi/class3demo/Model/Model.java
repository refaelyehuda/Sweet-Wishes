package com.menachi.class3demo.Model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.menachi.class3demo.MyApplication;

import java.io.IOException;

/**
 * Cloudinary example.
 */
public class Model {

    ModelCloudinary modelCloudinary;
    ModelFirebase modelFirebase;
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

    /**
     * return the Model singleton
     * @return
     */
    public static Model instance (){
        return instance;
    }


    public void loginUser(String username , String password,LoginStatus listener){
        modelFirebase.Login(username, password,listener);
    }

    public void signup(User user,SignupStatus listener){
        modelFirebase.createUser(user,listener);

    }

    /**
     * save image in cloudinary
     */
    public interface SaveImageListener{
        public void OnDone(Exception e);
    }
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
