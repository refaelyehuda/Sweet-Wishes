package com.menachi.class3demo.Fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetails extends Fragment implements ProductComments.Delegate{
    private OnFragmentInteractionListener mListener;
    ProductComments productCommentsFragment;

    public interface Delegate{
        void onProductEdit(Product st);
    }
    Delegate delegate;
    Product product;

    public void setDelegate(Delegate delegate){
        this.delegate = delegate;
    }
    public void setProduct(Product product){
        this.product = product;
    }

    public ProductDetails() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProductDetails newInstance(String param1, String param2) {
        ProductDetails fragment = new ProductDetails();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        if(product!=null){
            TextView id = (TextView) view.findViewById(R.id.ProductIDDetails);
            TextView name = (TextView) view.findViewById(R.id.ProductNameDetails);
            TextView price = (TextView) view.findViewById(R.id.ProducrPriceDetails);
            TextView imageName = (TextView) view.findViewById(R.id.ProductImageNameDetails);
            TextView type = (TextView) view.findViewById(R.id.ProducrTypeDetails);
            TextView createdDate = (TextView) view.findViewById(R.id.ProductCDateDetails);
            TextView lastUpdate = (TextView) view.findViewById(R.id.LastUpdateDateDetails);

            id.setText(this.product.getProductId());
            name.setText(this.product.getName());
            price.setText(this.product.getPrice());
            imageName.setText(this.product.getImageName());
            type.setText(this.product.getType());
            createdDate.setText(this.product.getCreateDate());
            lastUpdate.setText(this.product.getLastUpdate());

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            productCommentsFragment = new ProductComments();
            productCommentsFragment.setProduct(product);
            productCommentsFragment.setDelegate(this);
            ft.add(R.id.comments_frag_container, productCommentsFragment);
            ft.show(productCommentsFragment);
            ft.commit();
        }
        return view;
    }

    @Override
    public void onNewComment() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addProductBtn : {
                Log.i("TAG", "Editing Product: " + product.getProductId());
                if (this.delegate != null)
                    delegate.onProductEdit(product);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
