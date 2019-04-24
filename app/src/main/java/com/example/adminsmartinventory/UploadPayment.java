package com.example.adminsmartinventory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class UploadPayment extends AppCompatActivity {

    private  TextView itemIdTV, itemNameTV, noOfUnitsTV, totalAmtTV, unitPriceTV, accHolderNameTV,
    bankTV, accNoTv, routingTV, cancelTV;
    private ImageView imageIV;
    private Button paymentReceiptBTN;

    private String requestedBy, status, itemId, unitPrice, noOfunits, totalAmount, imageURL, supplyReqMsgId, paymentReqDocId;
    private  String supplyReqDocId,bankInfoDocId, userId;
    private URL imgURL;

    FirebaseFirestore db;
    DocumentReference supplyReqDocRef, bankInfoDocRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_payment);

        itemIdTV = findViewById(R.id.itemIdTV);
        itemNameTV = findViewById(R.id.itemNameTV);
        noOfUnitsTV = findViewById(R.id.noOfUnitsTV);
        totalAmtTV = findViewById(R.id.totalAmtTV);
        unitPriceTV = findViewById(R.id.unitPriceTV);
        accHolderNameTV = findViewById(R.id.accHoderNameET);
        bankTV = findViewById(R.id.bankNameTV);
        accNoTv = findViewById(R.id.accNoTV);
        routingTV = findViewById(R.id.routingTV);
        cancelTV = findViewById(R.id.cancelTV);
        imageIV = findViewById(R.id.imageIV);
        paymentReceiptBTN = findViewById(R.id.paymentReceiptBTN);

        Intent paymentIntent = getIntent();
        requestedBy = paymentIntent.getStringExtra("requestedBy");
        status = paymentIntent.getStringExtra("status");
        itemId = paymentIntent.getStringExtra("itemId");
        unitPrice = paymentIntent.getStringExtra("unitPrice");
        noOfunits = paymentIntent.getStringExtra("noOfUnits");
        totalAmount = paymentIntent.getStringExtra("totalAmount");
        supplyReqDocId = paymentIntent.getStringExtra("supplyReqDocId");
        bankInfoDocId = paymentIntent.getStringExtra("bankInfoDocId");
        userId = paymentIntent.getStringExtra("userId");
        paymentReqDocId = paymentIntent.getStringExtra("paymentRequestDocId");
        db= FirebaseFirestore.getInstance();
        supplyReqDocRef = db.collection("users").document(userId).collection("supplyList").document(supplyReqDocId);
        supplyReqDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    imageURL = doc.getString("imageURL");
                    Picasso.get().load(imageURL).into(imageIV);
                    itemIdTV.setText("Item Id :"+ itemId);
                    itemNameTV.setText("Item Name :"+doc.getString("itemName"));
                    noOfUnitsTV.setText("No of Units Supplied :"+noOfunits);
                    unitPriceTV.setText("Unit Price :$"+unitPrice);
                    totalAmtTV.setText("Total Amount :$"+totalAmount);
                    supplyReqMsgId = doc.getString("supplyReqMsgId");
                }
                else {
                    Toast.makeText(UploadPayment.this, "Read Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
        bankInfoDocRef = supplyReqDocRef.collection("BankInfo").document(bankInfoDocId);
        bankInfoDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    accHolderNameTV.setText("Acc Holder name :"+document.getString("name"));
                    bankTV.setText("Bank Name :"+document.getString("bankName"));
                    accNoTv.setText(document.getLong("accNo").toString());
                    routingTV.setText("Routing No :"+document.getLong("routingNo").toString());

                }
            }
        });

        paymentReceiptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("supplyRequests").document(supplyReqMsgId).update("status", "PaymentCompleted");
                db.collection("paymentRequests").document(paymentReqDocId).update("status","PaymentCompleted");
                Intent i = new Intent(UploadPayment.this, UploadPaymentLable.class);
                i.putExtra("userId", userId);
                i.putExtra("supplyReqDocId", supplyReqDocId);
                startActivity(i);

            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
