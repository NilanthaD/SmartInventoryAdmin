package com.example.adminsmartinventory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SupplyRequestRV extends AppCompatActivity {

    private RecyclerView supplyRequestRV;
    private RecyclerView.Adapter supplyRequestAdapter;
    private RecyclerView.LayoutManager supplyRequestLayoutManager;

    private FirebaseFirestore db;
    private DocumentReference supplyReqDocRef;
    private CollectionReference supplyRequestColRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_request_rv);

        db = FirebaseFirestore.getInstance();
        supplyRequestColRef = db.collection("supplyRequests");

        final ArrayList<SupplyRequestContainer> supplyRequestList = new ArrayList<>();
        supplyRequestColRef.orderBy("requestDate", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        supplyRequestList.add(new SupplyRequestContainer("New supply request", doc.getString("itemId"), doc.getString("user"),
                                doc.getDate("requestDate"), doc.getString("supplyReqDocId"),doc.getString("status"), doc.getId()));

                    }
                    supplyRequestRV = findViewById(R.id.supplyRequestRV);
                    supplyRequestRV.setHasFixedSize(true);
                    supplyRequestLayoutManager = new LinearLayoutManager(SupplyRequestRV.this);
                    supplyRequestAdapter = new SupplyRequestAdapter(supplyRequestList, SupplyRequestRV.this);
                    supplyRequestRV.setLayoutManager(supplyRequestLayoutManager);
                    supplyRequestRV.setAdapter(supplyRequestAdapter);

                }
            }
        });
    }
}
