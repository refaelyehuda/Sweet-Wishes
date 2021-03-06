package com.menachi.class3demo.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.menachi.class3demo.Alerts.BasicAlertDialog;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.MyApplication;
import com.menachi.class3demo.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewProduct extends Fragment {

    private OnFragmentInteractionListener mListener;
    public interface Delegate extends BasicAlertDialog.Delegate{
        void cancel();
        void onReturnToList();
    }
    static final int IMAGE_CAPTURE = 1;
    static final int IMAGE_GALLERY = 2;

    Delegate delegate;
    EditText productName;
    EditText productPrice;
    EditText productType;
    String imageName;
    ImageView productImage;
    ProgressBar productProgressBar;
    ProgressBar mainProgressBar;
    Bitmap thumbnail;


    public NewProduct() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    public void setDelegate(Delegate delegate){
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_product, container, false);
        productName = (EditText) view.findViewById(R.id.addNewProductName);
        productPrice = (EditText) view.findViewById(R.id.addNewProductPrice);
        productType = (EditText) view.findViewById(R.id.addNewProductTypeName);
        productImage = (ImageView) view.findViewById(R.id.productImage);
        productProgressBar = (ProgressBar) view.findViewById(R.id.productImageProgressBar);
        mainProgressBar = (ProgressBar) view.findViewById(R.id.newProductProgressBar);
        final Button saveBtn = (Button) view.findViewById(R.id.saveNewProductBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelNewProductBtn);
        Button productImagePicbtn = (Button) view.findViewById(R.id.productImagePicbtn);

        productImagePicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productProgressBar.setVisibility(View.VISIBLE);
                selectImage();
                productProgressBar.setVisibility(View.GONE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!productName.getText().toString().equals("") && !productPrice.getText().toString().equals("") && !productType.getText().toString().equals("") && (imageName != null)){
                    final Product pr = new Product(productName.getText().toString(),productPrice.getText().toString(), productType.getText().toString(),imageName);
                    mainProgressBar.setVisibility(View.VISIBLE);
                    if(imageName != null){
                        Model.instance().saveImage(thumbnail, imageName, new Model.SaveImageListener() {
                            @Override
                            public void OnDone(Exception e) {
                                if (e == null) {
                                    Model.instance().addProduct(pr);
                                    mainProgressBar.setVisibility(View.GONE);
                                    //release the lock on the save button after save details
                                    BasicAlertDialog addProductAlert = new BasicAlertDialog("OK", "" + productName.getText().toString() + " Was Added Successfully For transfer type OK",delegate,Model.FunctionsToUse.RETURN_TO_LIST);
                                    addProductAlert.show(getFragmentManager(), "Tag");
                                } else {
                                    Log.d("TAG", "save image finished with error");
                                }

                            }
                        });
                    }
                }else{
                    Toast toast;
                    TextView textView;
                    toast = Toast.makeText(MyApplication.getContext(), "Please fill all fields (include image)", Toast.LENGTH_LONG);
                    textView = (TextView) toast.getView().findViewById(android.R.id.message);
                    textView.setTextColor(Color.RED);
                    toast.show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegate != null){
                    Log.d("TAG", "New student fragment - before cancel function a");
                    delegate.cancel();
                }else{
                    Log.d("TAG","New student fragment - without cancel function");
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {// if take a new picture
            Bundle extras = data.getExtras();
            thumbnail = (Bitmap) extras.get("data");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageName = "JPEG_" + timeStamp + ".jpeg";
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            productProgressBar.setVisibility(View.GONE);

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
            productImage.setImageBitmap(rotatebitmap);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageName = "JPEG_" + timeStamp + ".jpeg";
            productProgressBar.setVisibility(View.GONE);
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
        final CharSequence[] options;
        if(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            options =new CharSequence[3];
            options[0] = "Take Photo";
            options[1] = "Choose from Gallery";
            options[2] = "Cancel";
        }else{
            options= new CharSequence[2];
            options[0] = "Choose from Library";
            options[1] = "Cancel";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    takingPicture();
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
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
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
