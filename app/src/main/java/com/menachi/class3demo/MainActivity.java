package com.menachi.class3demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.menachi.class3demo.Model.Model;

import java.net.URL;

public class MainActivity extends Activity {
    EditText fname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = (Button) findViewById(R.id.login);
        Button signup = (Button) findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });



        //get image into Bitmap
        //Bitmap image = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        //upload image to cloudinary
        /*Model.instance().saveImage(image, "add.jpg", new Model.SaveImageListener() {
            @Override
            public void OnDone(Exception e) {
                if (e == null) {
                    Log.d("TAG", "save image finished");
                } else {
                    Log.d("TAG", "save image finished with error");
                }

            }
        });*/
//        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        final EditText imageName = (EditText) findViewById(R.id.main_name);
//        Button save = (Button) findViewById(R.id.main_save);
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//                Bitmap bitmap = drawable.getBitmap();
//                Model.instance().saveImage(bitmap,imageName.getText().toString() + ".jpg", new Model.SaveImageListener() {
//                    @Override
//                    public void OnDone(Exception e) {
//                        if (e == null) {
//                            Log.d("TAG", "save image finished");
//                        } else {
//                            Log.d("TAG", "save image finished with error");
//                        }
//
//                    }
//                });
//
//            }
//        });
//
//        Button load = (Button) findViewById(R.id.main_get);
//        load.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Model.instance().getImage(imageName.getText().toString(), new Model.GetImageListener() {
//                    @Override
//                    public void OnDone(Bitmap image, String imageName) {
//                        if (image != null) {
//                            Log.d("TAG","SUCCESS GET IMAGE");
//                            imageView.setImageBitmap(image);
//
//
//                        }else{
//                            Log.d("TAG","ERROR");
//                        }
//                    }
//                });
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
