package com.menachi.class3demo.Model;

import android.content.Context;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by refael yehuda on 5/29/2016.
 */
public class ModelFirebase {
    Firebase myFirebaseRef;

    public interface ProductsDelegate{
        void onProductList(List<Product> productsList);
    }

    public interface CommentDelegate{
        void onCommentList(List<Comment> commentsList);
    }
    ModelFirebase(Context context){
        Firebase.setAndroidContext(context);
        myFirebaseRef = new Firebase("https://dessers-project.firebaseio.com/");
    }

    public void Login(String username , String password, final Model.LoginStatus listener){
        myFirebaseRef.authWithPassword(username, password, new Firebase.AuthResultHandler() {
            boolean status = false;

            @Override
            public void onAuthenticated(AuthData authData) {
                String Uid = authData.getUid();
                getUserById(Uid, new GetUserById() {
                    @Override
                    public void onResult(User user) {
                        status = true;
                        listener.isLoggedIn(status, user);
                    }

                    @Override
                    public void onCancel() {
                        listener.isLoggedIn(status, null);
                    }
                });


            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listener.isLoggedIn(status, null);

            }
        });
    }

    /**
     * listener for return user by id
     */
    public interface GetUserById{
        public void onResult(User user);
        public void onCancel();
    }

    public void getUserById(String id , final GetUserById getUserById) {
        Firebase  stRef = myFirebaseRef.child("Users").child(id);
        stRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Log.d("TAG", "sucess to create user");
                getUserById.onResult(user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                getUserById.onCancel();
            }
        });
    }


    public void createUser(User user , Model.SignupStatus listener) {
        //create a new user in firebase regitration
        myFirebaseRef.createUser(user.getEmail(), user.getPassword(), new HandleUserCreatetion(user, listener));

    }
    /**
     * this class handle in the user creation response
     */
    class HandleUserCreatetion implements  Firebase.ValueResultHandler<Map<String, Object>>{
        boolean status;
        Model.SignupStatus listener;
        User user;
        //get the user to create in the DB after the registration
        public HandleUserCreatetion(User user, Model.SignupStatus listener){
            this.user = user;
            this.listener = listener;
        }
        @Override
        public void onSuccess(Map<String, Object> stringObjectMap) {
            status = true;
            String userId = (String) stringObjectMap.get("uid");
            user.setUserId(userId);
            Firebase userRef = myFirebaseRef.child("Users").child(userId);
            userRef.setValue(user);
            listener.isSignup(status,user);
        }

        @Override
        public void onError(FirebaseError firebaseError) {
            listener.isSignup(status,user);
        }
    }

    public String getUserId(){
        AuthData authData = myFirebaseRef.getAuth();
        if (authData != null) {
            return authData.getUid();
        }
        return null;
    }


    public void updateUser(String userId,User user){
        Firebase userRef = myFirebaseRef.child(Model.Tabels.UsersTable).child(userId);
        userRef.setValue(user);
    }

    public Product addProduct(Product product){
        Firebase productsRef = myFirebaseRef.child(Model.Tabels.ProductTable);
        Firebase newProductRef = productsRef.push();
        newProductRef.setValue(product);
        String productID = newProductRef.getKey();
        product.setProductId(productID);
        return product;
    }

    public void getProducts(final ProductsDelegate listener){
        Firebase productsRef = myFirebaseRef.child(Model.Tabels.ProductTable);
        final List<Product> data = new LinkedList<Product>();


        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count of products ", "" + snapshot.getChildrenCount());
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    data.add(product);
                    Log.e("Get Data", product.getName());
                }
                listener.onProductList(data);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });

    }

    public Comment addComment(Comment comment){
        Firebase commentsRef = myFirebaseRef.child(Model.Tabels.CommentsTable);
        Firebase newCommentsRef = commentsRef.push();
        newCommentsRef.setValue(comment);
        String commentID = newCommentsRef.getKey();
        comment.setCommentId(commentID);
        return comment;
    }

    public void getCommentsByProductId(String productId,final CommentDelegate listener){
        final List<Comment> data = new LinkedList<Comment>();
        Query qr  = myFirebaseRef.child(Model.Tabels.CommentsTable).orderByChild("productId").equalTo(productId);
        qr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count of products ", "" + snapshot.getChildrenCount());
                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    data.add(comment);
                    Log.e("Get Data", comment.getName());
                }
                listener.onCommentList(data);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("TAG", "The read of comments  failed: ");
            }
        });

    }


    /**
     * LastPurchase function
     */

    public interface LastPurchasesEvents{
        void onResult(List<LastPurchase> lastPurchases);
        void onCancel(String error);
    }

    public void getLastPurchases(String usersID , final LastPurchasesEvents lastPurchasesEvents ){
        Query qr  = myFirebaseRef.child(Model.Tabels.LastPurchasesTable).orderByChild("userID").equalTo(usersID);
        qr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<LastPurchase> lastPurchases = new LinkedList<LastPurchase>();
                Log.d("TAG","There are " + snapshot.getChildrenCount() + " obj");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    LastPurchase lastPurchas = postSnapshot.getValue(LastPurchase.class);
                    lastPurchases.add(lastPurchas);
                }
                lastPurchasesEvents.onResult(lastPurchases);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ERROR","The read comment was  failed: "+ firebaseError.getMessage());
                lastPurchasesEvents.onCancel(firebaseError.toString());
            }
        });
    }

    public LastPurchase addLastPurchases(LastPurchase lastPurchases){
        Firebase lastPurchaseRef = myFirebaseRef.child(Model.Tabels.LastPurchasesTable);
        Firebase newlastPurchaseRef = lastPurchaseRef.push();
        newlastPurchaseRef.setValue(lastPurchases);
        String lastPurchaseID = newlastPurchaseRef.getKey();
        lastPurchases.setLastPurchaseID(lastPurchaseID);
        return lastPurchases;
    }


    public interface LastUpdateEvents{
        void onResult(LastUpdates lastUpdates);
        void onCancel(String error);
    }

    public void getLastUpdateDate(String tableName, final LastUpdateEvents lastUpdateEvents){
        Firebase lastUpdateRef = myFirebaseRef.child(Model.Tabels.lastUpdateTable).child(tableName);
        lastUpdateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LastUpdates lastUpdates = null;
                if (dataSnapshot.getValue() != null) {
                    lastUpdates = dataSnapshot.getValue(LastUpdates.class);
                    if (lastUpdates.getLastUpdate() == null || lastUpdates.getLastUpdate().equals(""))
                        lastUpdates.setLastUpdate("1");
                    lastUpdateEvents.onResult(lastUpdates);
                } else {
                    lastUpdateEvents.onResult(lastUpdates);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                lastUpdateEvents.onCancel(firebaseError.toString());
            }
        });


    }

    public interface LastUpdateEvent{
        void onResult(LastUpdates lastUpdates);
        void onError(String err);
    }

    public void setLastUpdateDate(final String tableName, final String updatedDate , final LastUpdateEvent lastUpdateEvent)
    {
        myFirebaseRef.child(Model.Tabels.lastUpdateTable).child(tableName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LastUpdates lastUpdates;
                if(dataSnapshot.getValue() != null){
                    lastUpdates = dataSnapshot.getValue(LastUpdates.class);
                    lastUpdates.setCountOfRecords(lastUpdates.getCountOfRecords() + 1);
                    lastUpdates.setTabelName(tableName);
                    lastUpdates.setLastUpdate(updatedDate);
                }else{
                    lastUpdates = new LastUpdates(tableName,1,updatedDate);
                }
                myFirebaseRef.child(Model.Tabels.lastUpdateTable).child(tableName).setValue(lastUpdates);
                lastUpdateEvent.onResult(lastUpdates);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                lastUpdateEvent.onError(firebaseError.toString());
            }
        });

    }

    public AuthData getAuthData(){
        return myFirebaseRef.getAuth();
    }



}

