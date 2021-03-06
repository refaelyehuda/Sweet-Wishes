package com.menachi.class3demo.Fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.menachi.class3demo.Alerts.BasicAlertDialog;
import com.menachi.class3demo.Model.Comment;
import com.menachi.class3demo.Model.LastPurchase;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.ModelFirebase;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.R;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetails extends Fragment{
    private OnFragmentInteractionListener mListener;

    public interface Delegate extends NewProduct.Delegate{
        void onNewComment(Product product);
    }
    Delegate delegate;
    Product product;
    ListView list;
    commentAddapter adapter;
    List<Comment> comments = new LinkedList<Comment>();


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        adapter.notifyDataSetChanged();
    }

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
        setHasOptionsMenu(true);
        if(product!=null){
            TextView name = (TextView) view.findViewById(R.id.ProductNameDetails);
            TextView price = (TextView) view.findViewById(R.id.ProducrPriceDetails);
            TextView type = (TextView) view.findViewById(R.id.ProducrTypeDetails);
            final ImageView productImage = (ImageView) view.findViewById(R.id.imageDetails);
            final ProgressBar imageProgressBar = (ProgressBar) view.findViewById(R.id.product_details_image_progressBar);
            imageProgressBar.setVisibility(View.VISIBLE);
            Model.instance().getImage(this.product.getImageName(), new Model.GetImageListener() {
                @Override
                public void OnDone(Bitmap image, String imageName) {
                    imageProgressBar.setVisibility(View.GONE);
                    productImage.setImageBitmap(image);
                }
            });
            Button buyProduct = (Button) view.findViewById(R.id.userBuyProductBth);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.product_details_progressBar);

            buyProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Model.instance().getUser().getBillingInfo() != null){
                        progressBar.setVisibility(View.VISIBLE);
                        LastPurchase lastPurchase = new LastPurchase(Model.instance().getUser().getUserId(), product);
                        Model.instance().addPurchaseToUser(lastPurchase);
                        progressBar.setVisibility(View.GONE);
                        BasicAlertDialog addProductAlert = new BasicAlertDialog("OK", "" + product.getName().toString() + " was purchased successfully", delegate, Model.FunctionsToUse.RETURN_TO_LIST);
                        addProductAlert.show(getFragmentManager(), "Tag");
                    }else{
                        BasicAlertDialog addProductAlert = new BasicAlertDialog("OK", "To buy this product you need to add your credit card.", delegate, Model.FunctionsToUse.RETURN_TO_LIST);
                        addProductAlert.show(getFragmentManager(), "Tag");
                    }

                }
            });

            name.setText(this.product.getName());
            price.setText(this.product.getPrice());
            type.setText(this.product.getType());

            list = (ListView) view.findViewById(R.id.comments_list);
            Model.instance().getCommentsByProductId(product.getProductId(), new ModelFirebase.CommentDelegate() {
                @Override
                public void onCommentList(List<Comment> commentsList) {
                    comments = commentsList;
                    adapter = new commentAddapter();
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addProductBtn : {
                Log.d("TAG", "Add comment to : " + product.getProductId());
                if (this.delegate != null){
                 delegate.onNewComment(product);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();

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

    class commentAddapter extends BaseAdapter {

        @Override
        public int getCount() {
            return comments.size();
        }

        @Override
        public Object getItem(int position) {
            return comments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.comment_list_row, null);
                Log.d("TAG", "create view:" + position);

            }else{
                Log.d("TAG", "use convert view:" + position);
            }

            final TextView UserName = (TextView) convertView.findViewById(R.id.user_list_row_name);
            TextView commentText = (TextView) convertView.findViewById(R.id.comment_list_row_text);
            final ImageView commentImage = (ImageView) convertView.findViewById(R.id.comment_list_row_image);
            final ProgressBar commentProgressBar = (ProgressBar) convertView.findViewById(R.id.commentProgressBar);
            //save the current position to check when the image is download if the current row is show in the screen
            UserName.setTag(new Integer(position));
            Comment comment = comments.get(position);
            UserName.setText("User : " + comment.getName());
            commentText.setText("\n " + comment.getText());
            commentProgressBar.setVisibility(View.VISIBLE);
            Model.instance().getImage(comment.getImageName(), new Model.GetImageListener() {
                @Override
                public void OnDone(Bitmap image, String imageName) {
                    if (image != null) {
                        Log.d("TAG", "SUCCESS GET COMMENT IMAGE");
                        //check when the image is download if the current row is show in the screen
                        if (image != null && ((Integer)UserName.getTag() == position)) {
                            commentImage.setImageBitmap(image);
                            commentProgressBar.setVisibility(View.GONE);
                        }
                    }else{
                        commentProgressBar.setVisibility(View.GONE);
                        Log.d("TAG","ERROR GET COMMENT IMAGE");
                    }
                }
            });
            return convertView;
        }
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
