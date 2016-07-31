package com.menachi.class3demo.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.R;

import java.util.List;

public class LastPurchases extends Fragment {

    public interface Delegate extends ListProducts.Delegate{}

    Delegate delegate;
    ListView list;
    List<Product> data;

    public LastPurchases() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LastPurchase.
     */
    // TODO: Rename and change types and number of parameters
    public static LastPurchases newInstance(String param1, String param2) {
        LastPurchases fragment = new LastPurchases();
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

    public Delegate getDelegate() {
        return delegate;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_last_purchases, container, false);
        list = (ListView) view.findViewById(R.id.last_purchases_list);
        data =   Model.instance().getLastPurchasesProductsList();
        myAddapter adapter = new myAddapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "The row " + position + " selected");
                Product pr = data.get(position);
                if (delegate != null) {
                    delegate.onProductSelected(pr);
                }
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class myAddapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.product_list_row, null);
                Log.d("TAG", "create view:" + position);
            }else{
                Log.d("TAG", "use convert view:" + position);
            }

            final TextView productName = (TextView) convertView.findViewById(R.id.product_list_row_name);
            productName.setTag(new Integer(position));
            TextView productPrice = (TextView) convertView.findViewById(R.id.product_list_row_price);
            final ImageView lastPurchasesImage = (ImageView) convertView.findViewById(R.id.product_list_row_image);
            final ProgressBar lastPurchasesProgressBar  = (ProgressBar) convertView.findViewById(R.id.profuct_row_progressBar);
            Product pr = data.get(position);
            lastPurchasesProgressBar.setVisibility(View.VISIBLE);
            Model.instance().getImage(pr.getImageName(), new Model.GetImageListener() {
                @Override
                public void OnDone(Bitmap image, String imageName) {
                    if (image != null) {
                        Log.d("TAG", "SUCCESS GET COMMENT IMAGE");
                        //check when the image is download if the current row is show in the screen
                        if (image != null && ((Integer) productName.getTag() == position)) {
                            lastPurchasesImage.setImageBitmap(image);
                            lastPurchasesProgressBar.setVisibility(View.GONE);
                        }
                    } else {
                        lastPurchasesProgressBar.setVisibility(View.GONE);
                        Log.d("TAG", "ERROR GET COMMENT IMAGE");
                    }
                }
            });
            productName.setText(pr.getName());
            productPrice.setText(pr.getPrice() + "$");

            return convertView;
        }
    }
}
