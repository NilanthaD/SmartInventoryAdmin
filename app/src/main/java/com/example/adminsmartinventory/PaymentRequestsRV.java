package com.example.adminsmartinventory;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class PaymentRequestsRV extends AppCompatActivity {

    private RecyclerView paymentReqRV;
    private RecyclerView.Adapter paymentReqtAdapter;
    private RecyclerView.LayoutManager paymentReqLayoutManager;

    private FirebaseFirestore db;
    private DocumentReference paymentReqDocRef;
    private CollectionReference paymentRequestColRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_requests_rv);

        db = FirebaseFirestore.getInstance();
        paymentRequestColRef = db.collection("paymentRequests");

        final ArrayList<PaymentRequestsContainer> paymentRequestsList = new ArrayList<>();
        paymentRequestColRef.orderBy("requestedDate", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        paymentRequestsList.add(new PaymentRequestsContainer(doc.getString("status"), doc.getString("itemId"), doc.getString("userId"),
                               doc.getDate("requestedDate"), doc.getString("supplyReqDocId"), doc.getLong("noOfUnits"), doc.getLong("unitPrice"),
                                doc.getLong("totalValue"), doc.getString("bankInfoDocId")));
                    }
                    paymentReqRV = findViewById(R.id.paymentReqRV);
                    paymentReqRV.setHasFixedSize(true);
                    paymentReqLayoutManager= new LinearLayoutManager(PaymentRequestsRV.this);
                    paymentReqtAdapter = new PaymentRequestsAdapter(paymentRequestsList, PaymentRequestsRV.this);
                    paymentReqRV.setLayoutManager(paymentReqLayoutManager);
                    paymentReqRV.setAdapter(paymentReqtAdapter);
                }
            }
        });
    }
}
