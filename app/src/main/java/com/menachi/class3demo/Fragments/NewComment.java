package com.menachi.class3demo.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.menachi.class3demo.Alerts.BasicAlertDialog;
import com.menachi.class3demo.Model.Comment;
import com.menachi.class3demo.Model.Model;
import com.menachi.class3demo.Model.Product;
import com.menachi.class3demo.R;

public class NewComment extends Fragment {

    public interface Delegate extends BasicAlertDialog.Delegate{
        void onReturnToDetails(Product product);
        void cancel();
    }
    Delegate delegate;
    Product product;
    Comment comment;
    EditText commentText;
    EditText commentGrade;

    public void setDelegate(Delegate delegate){
        this.delegate = delegate;
    }
    public void setProduct(Product product){this.product = product;}

    public NewComment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewComment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewComment newInstance(String param1, String param2) {
        NewComment fragment = new NewComment();
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
        View view = inflater.inflate(R.layout.fragment_new_comment, container, false);
        commentText = (EditText) view.findViewById(R.id.comment_text);
        commentGrade = (EditText) view.findViewById(R.id.comment_grade);
        Button saveBtn = (Button) view.findViewById(R.id.saveNewCommentBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelNewCommentBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Model.instance().getUser().getfName();
                String userID = Model.instance().getUser().getUserId();
                String userImageName = Model.instance().getUser().getProfPicture();
                comment = new Comment(product.getProductId(),userID,name,userImageName,commentText.getText().toString(),commentGrade.getText().toString());
                Model.instance().addComment(comment);
                Log.d("TAG", "Comment was added successfully");
                BasicAlertDialog Alert = new BasicAlertDialog("OK", "The comment for " + product.getName() + " Was Added Successfully For transfer type OK",delegate,Model.FunctionsToUse.PRODUCT_DETAILS);
                Alert.setProduct(product);
                Alert.show(getFragmentManager(), "Tag");
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegate != null){
                    Log.d("TAG", "New Comment  fragment - cancel");
                    delegate.cancel();
                }else{
                    Log.d("TAG","New Comment fragment - without cancel function");
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
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
