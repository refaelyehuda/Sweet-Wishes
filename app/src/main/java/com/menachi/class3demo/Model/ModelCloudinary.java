package com.menachi.class3demo.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class ModelCloudinary {

    Cloudinary cloudinary;

    public ModelCloudinary(){
        cloudinary = new Cloudinary("cloudinary://273122758831428:uWIff4eRPhdKvo4bmIdfoTDL1N0@dlaok5vzh");
    }

    public void saveImage(Bitmap image , String imageName,Model.SaveImageListener listener) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        String name  =  imageName.substring(0,imageName.lastIndexOf("."));
        Map res = cloudinary.uploader().upload(bs,ObjectUtils.asMap("public_id",name));
        Log.d("TAG","save image to url " + res.get("url"));
    }

    public Bitmap getImage(String imageName){

        URL url = null;
        try {
            url = new URL(cloudinary.url().generate(imageName));
            Log.d("TAG","loa");
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bmp;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return  null;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public void deleteImage(){

    }
}
