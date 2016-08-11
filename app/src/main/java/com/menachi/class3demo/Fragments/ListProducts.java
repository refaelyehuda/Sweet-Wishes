package com.menachi.class3demo.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.ModelFirebase;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListProducts.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListProducts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListProducts extends Fragment {

    private OnFragmentInteractionListener mListener;
    public interface Delegate{
        void onProductSelected(Product st);
        void onNewProduct();
    }
    Delegate delegate;
    ListView list;
    SearchView searchItem;
    List<Product> data;
    myAddapter adapter;

    public void addProduct(Product product){
        data.add(product);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public ListProducts() {
        // Required empty public constructor
    }

    public static ListProducts newInstance(String param1, String param2) {
        ListProducts fragment = new ListProducts();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addProductBtn : {
                Log.d("TAG","moving to new product fragment");
                if (this.delegate != null)
                    delegate.onNewProduct();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
        //TODO need to check what is the  implification of this line
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_list_products, container, false);
        list = (ListView) view.findViewById(R.id.products_list);
        searchItem = (SearchView) view.findViewById(R.id.fragment_list_search_item);
        data =   Model.instance().getProductData();
        adapter = new myAddapter();
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //searchlist
            @Override
            public boolean onQueryTextSubmit(String query) {
                data = Model.instance().getProductByName(query);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                data = Model.instance().getProductByName(newText);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return view;
    }



    public void setDelegate(Delegate delegate){
        this.delegate = delegate;
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


    class myAddapter extends BaseAdapter{

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
            final ImageView productImage = (ImageView) convertView.findViewById(R.id.product_list_row_image);
            final ProgressBar productProgressBar = (ProgressBar) convertView.findViewById(R.id.profuct_row_progressBar);
            Product pr = data.get(position);
            productProgressBar.setVisibility(View.VISIBLE);
            Model.instance().getImage(pr.getImageName(), new Model.GetImageListener() {
                @Override
                public void OnDone(Bitmap image, String imageName) {
                    if (image != null) {
                        Log.d("TAG", "SUCCESS GET COMMENT IMAGE");
                        //check when the image is download if the current row is show in the screen
                        if (image != null && ((Integer) productName.getTag() == position)) {
                            productImage.setImageBitmap(image);
                            productProgressBar.setVisibility(View.GONE);
                        }
                    } else {
                        productProgressBar.setVisibility(View.GONE);
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
