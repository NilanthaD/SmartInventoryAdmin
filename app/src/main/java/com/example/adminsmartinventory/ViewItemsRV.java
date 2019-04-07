package com.example.adminsmartinventory;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import io.grpc.Context;

public class ViewItemsRV extends AppCompatActivity {

    private RecyclerView ViewItemsRV;
    private RecyclerView.Adapter ItemViewAdapter;
    private RecyclerView.LayoutManager itemsLayoutManager;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    CollectionReference itemCollection;

    private String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items_rv);

        db = FirebaseFirestore.getInstance();
//        storage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = mStorageRef.child("tempImages/1553180448816.jpg");

        itemCollection = db.collection("items");
        final Date today = new Date();
        final ArrayList<ItemViewContainer> itemListArray = new ArrayList<>();
        //itemCollection.get().
           itemCollection.orderBy("itemPostedDate", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc: task.getResult()){

                            itemListArray.add(new ItemViewContainer(userEmail, doc.getId(), doc.getString("imageURL"), doc.getString("itemId"), doc.getString("itemName"),
                                    doc.getLong("untPrice"), doc.getLong("unitRequired"), doc.getTimestamp("requiredBefore").toDate()));
                        }


                    ViewItemsRV = findViewById(R.id.ViewItemsRV);
                    ViewItemsRV.setHasFixedSize(true);
                    itemsLayoutManager = new LinearLayoutManager(ViewItemsRV.this);
                    ItemViewAdapter = new ItemViewAdapter(itemListArray, ViewItemsRV.this);
                    ViewItemsRV.setLayoutManager(itemsLayoutManager);
                    ViewItemsRV.setAdapter(ItemViewAdapter);
                }
            }
        });
    }
}
