package com.menachi.class3demo.Fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.menachi.class3demo.Model.Comment;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.ModelFirebase;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.R;

import java.util.List;


public class ProductComments extends Fragment {

    ListView list;
    List<Comment> data;
    Product product;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public ProductComments() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductComments.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductComments newInstance(String param1, String param2) {
        ProductComments fragment = new ProductComments();
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
        //TODO need to check what is the  implification of this line
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_product_comments, container, false);
        list = (ListView) view.findViewById(R.id.comments_list);
        data =   Model.instance().getCommentsByProductId(product.getProductId());
        commentAddapter  adapter = new commentAddapter();
        list.setAdapter(adapter);
        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();

    }

    class commentAddapter extends BaseAdapter {

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
            Comment comment = data.get(position);
            UserName.setText(comment.getName());
            commentText.setText("\n\n " + comment.getText());
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
}
