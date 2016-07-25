package com.menachi.class3demo.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.menachi.class3demo.Alerts.BasicAlertDialog;
import com.menachi.class3demo.DateAndPickers.Date.DateEditText;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.User;
import com.menachi.class3demo.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonalInfo extends Fragment {

    public interface Delegate extends NewProduct.Delegate{
    }

    static final int IMAGE_CAPTURE = 1;
    static final int IMAGE_GALLERY = 2;
    EditText fName;
    EditText lName;
    EditText address;
    DateEditText birthDate;
    ImageView userImage;
    String imageName = null;
    ProgressBar imageProgressBar;
    Bitmap thumbnail;
    User user;
    Delegate delegate;

    public PersonalInfo() {
        // Required empty public constructor
    }

    public void setCurrentUser(User user){
        this.user = user;
    }
    public void setDelegate(Delegate delegate){
        this.delegate = delegate;
    }
    // TODO: Rename and change types and number of parameters
    public static PersonalInfo newInstance(String param1, String param2) {
        PersonalInfo fragment = new PersonalInfo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        if (user != null){
            fName = (EditText) view.findViewById(R.id.userFname);
            lName = (EditText) view.findViewById(R.id.userLname);
            address = (EditText) view.findViewById(R.id.usertAddress);
            birthDate = (DateEditText) view.findViewById(R.id.userBirthDate);
            userImage = (ImageView) view.findViewById(R.id.userImage);
            imageProgressBar = (ProgressBar) view.findViewById(R.id.userImageProgressBar);
            Button saveBtn = (Button) view.findViewById(R.id.saveUserPersonalInfo);
            Button cancelBtn = (Button) view.findViewById(R.id.cancelUserPersonalInfo);
            Button userImagePicbtn = (Button) view.findViewById(R.id.userImagePicbtn);

            fName.setText(user.getfName());
            lName.setText(user.getlName());
            address.setText(user.getAddress());
            birthDate.setText(user.getBirthDate());
            imageProgressBar.setVisibility(View.VISIBLE);

            Model.instance().getImage(user.getProfPicture(), new Model.GetImageListener() {
                    @Override
                    public void OnDone(Bitmap image, String imageName) {
                        if (image != null) {
                            Log.d("TAG","SUCCESS GET IMAGE");
                            imageProgressBar.setVisibility(View.GONE);
                            userImage.setImageBitmap(image);
                        }else{
                            imageProgressBar.setVisibility(View.GONE);
                            Log.d("TAG","ERROR GET Image");
                        }
                    }
                });

            userImagePicbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "Canceling Edit of user");
                    if (delegate != null) {
                        delegate.onReturnToList();
                    }
                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.setfName(fName.getText().toString());
                    user.setlName(lName.getText().toString());
                    user.setAddress(address.getText().toString());
                    user.setBirthDate(birthDate.getText().toString());
                    if(imageName != null){
                        user.setProfPicture(imageName);
                        Model.instance().saveImage(thumbnail, imageName, new Model.SaveImageListener() {
                            @Override
                            public void OnDone(Exception e) {
                                if (e == null) {
                                    Log.d("TAG", "save image finished");
                                    BasicAlertDialog alert;
                                    Model.instance().setCurrentUser(user);
                                    Model.instance().updateUser(user);
                                    alert = new BasicAlertDialog("OK", " The user updated successfully", delegate);
                                    alert.show(getFragmentManager(), "TAG");
                                    Log.d("TAG", "user created");
                                } else {
                                    Log.d("TAG", "save image finished with error");
                                }

                            }
                        });
                    }else{
                        BasicAlertDialog alert;
                        Model.instance().setCurrentUser(user);
                        alert = new BasicAlertDialog("OK", " The user updated successfully", delegate);
                        alert.show(getFragmentManager(), "TAG");
                        Log.d("TAG", "user saved without image change");
                    }
                }
            });
        }
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {// if take a new picture
            Bundle extras = data.getExtras();
            final Bitmap imageBitmap = (Bitmap) extras.get("data");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageName = "JPEG_" + timeStamp + ".jpeg";
            imageProgressBar.setVisibility(View.GONE);

        }
        if (requestCode == IMAGE_GALLERY && resultCode == Activity.RESULT_OK) { //if choose picture from gallery
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            thumbnail = (BitmapFactory.decodeFile(picturePath));
            Bitmap rotatebitmap = rotateImage(thumbnail, picturePath);
            userImage.setImageBitmap(rotatebitmap);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageName = "JPEG_" + timeStamp + ".jpeg";
            imageProgressBar.setVisibility(View.GONE);
//            Model.instance().saveImage(rotatebitmap, imageName, new Model.SaveImageListener() {
//                @Override
//                public void onResult(String pathImage) {
//                }
//            });
        }
    }

    private Bitmap rotateImage(Bitmap bitmap, String picturePath) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(picturePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
        }
        Bitmap Orientationbitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return Orientationbitmap1;
    }

    public void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    takingPicture();
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_GALLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void takingPicture() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent,IMAGE_CAPTURE);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
