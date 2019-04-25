package com.example.adminsmartinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class RequestAction extends AppCompatActivity {

    private ImageView imageIV;
    private TextView itemIDTV, requestedDateTV, itemNameTV, descriptionTV, statusTV, cancelTV, supplyAmountTV, userMsgTV, uploadPaymentTV;
    private TextView unitPriceTV, totalValueTV, newMsgTV, newAmountTV;
    private EditText messageET;
    private Button submitBTN;
    private Spinner approveSP, deliveredSP;
    private LinearLayout approvedLL, deliveredLL, statusChangeLL, messageLL, changeReqLL;

    String userId, supplyReqDocId, supplyReqMsgId, status, respondMessage, newMessage, chageReqDocId, itemId;
    String newAmount;
    Long unitPrice, newTotalAmount;

    FirebaseFirestore db;
    CollectionReference supplyRequestCol;
    DocumentReference supplyRequestDoc, itemDocRef, supplyMsgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_action);

        imageIV = findViewById(R.id.imageIV);
        itemIDTV = findViewById(R.id.itemIDTV);
        requestedDateTV = findViewById(R.id.requestDateTV);
        itemNameTV = findViewById(R.id.itemNameTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        supplyAmountTV = findViewById(R.id.supplyAmountTV);
        unitPriceTV = findViewById(R.id.unitPriceTV);
        totalValueTV = findViewById(R.id.totalValueTV);
        userMsgTV = findViewById(R.id.userMsgTV);
        statusTV = findViewById(R.id.statusTV);
        approveSP = findViewById(R.id.approveSP);
        cancelTV = findViewById(R.id.cancelTV);
        messageET = findViewById(R.id.messageET);
        submitBTN = findViewById(R.id.submitBTN);
        deliveredSP = findViewById(R.id.deliveredSP);
        deliveredLL = findViewById(R.id.deliveredLL);
        approvedLL = findViewById(R.id.approveLL);
        statusChangeLL = findViewById(R.id.statusChangeLL);
        messageLL = findViewById(R.id.messageLL);
        newAmountTV = findViewById(R.id.newAmountTV);
        newMsgTV = findViewById(R.id.newMsgTV);
        changeReqLL = findViewById(R.id.changeReqLL);
//        uploadPaymentTV = findViewById(R.id.uploadPaymentTV);
        newAmount="";

        db = FirebaseFirestore.getInstance();


        Intent requestIntent = getIntent();
        userId = requestIntent.getStringExtra("userEmail");
        supplyReqDocId = requestIntent.getStringExtra("supplyReqDocId");
//        supplyReqMsgId = requestIntent.getStringExtra("docId");
        status = requestIntent.getStringExtra("docId");

//        supplyMsgRef = db.collection("supplyRequests").document(supplyReqMsgId);
        supplyRequestDoc = db.collection("users").document(userId).collection("supplyList").document(supplyReqDocId);
        supplyRequestDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot supplyRequest = task.getResult();
                    Picasso.get().load(supplyRequest.getString("imageURL")).into(imageIV);
                    itemDocRef = db.collection("items").document(supplyRequest.getString("itemDocId"));
                    itemDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot item = task.getResult();
                                itemNameTV.setText("Item Name: " + item.getString("itemName"));
                                descriptionTV.setText("Description :\n" + item.getString("itemDetails"));
                            }
                        }
                    });
                    itemId = supplyRequest.getString("itemId");
                    itemIDTV.setText("Item Id: " + supplyRequest.getString("itemId"));
                    requestedDateTV.setText(FormatDate.getDate(supplyRequest.getDate("createdDate")));
                    supplyAmountTV.setText("Supply Amount: " + supplyRequest.getLong("supplyAmount").toString());
                    unitPrice = supplyRequest.getLong("unitPrice");
                    unitPriceTV.setText(("Unit price: $" + supplyRequest.getLong("unitPrice")));
                    totalValueTV.setText("Total value: $" + supplyRequest.getLong("totalValue"));
                    userMsgTV.setText("User Message\n" + supplyRequest.getString("message"));
                    status = supplyRequest.getString("status");
                    statusTV.setText("Status: " + status);
                    supplyReqMsgId = supplyRequest.getString("supplyReqMsgId");

                    if (status.equals("Pending")) {
                        changeReqLL.setVisibility(View.GONE);
                        deliveredLL.setVisibility(View.GONE);
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                                (RequestAction.this, R.array.approve, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        approveSP.setAdapter(adapter);

                        approveSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                status = parent.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                    if(status.equals("Pending for Change")){
                        deliveredLL.setVisibility(View.GONE);
                        supplyRequestDoc.collection("ChangeRequest").orderBy("requestDate", Query.Direction.DESCENDING).limit(1).get().
                                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot doc : task.getResult()){
                                                Long a = doc.getLong("newAmount");
                                                newAmount = a.toString();
                                                newTotalAmount = unitPrice*a;
                                                newMessage = doc.getString("changeMessage");
                                                chageReqDocId = doc.getId();
                                                newAmountTV.setText(newAmount);
                                                newMsgTV.setText(newMessage);
                                            }
                                        }
                                    }
                                });
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                                (RequestAction.this, R.array.approve, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        approveSP.setAdapter(adapter);

                        approveSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                status = parent.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    if (status.equals("Shipped")) {
                        approvedLL.setVisibility(View.GONE);
                        changeReqLL.setVisibility(View.GONE);
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                                (RequestAction.this, R.array.deliver, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        deliveredSP.setAdapter(adapter);
                        deliveredSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                status= parent.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
//

                    if(status.equals("TransactionCompleted")){
                        statusChangeLL.setVisibility(View.GONE);
                        messageLL.setVisibility(View.GONE);
                        submitBTN.setVisibility(View.GONE);
                        changeReqLL.setVisibility(View.GONE);
                    }
                    if(status.equals("Approved")||status.equals("ItemDelivered")||status.equals("PaymentRequested")){
                        approvedLL.setVisibility(View.GONE);
                        changeReqLL.setVisibility(View.GONE);
                        statusChangeLL.setVisibility(View.GONE);
                        messageLL.setVisibility(View.GONE);
                        submitBTN.setVisibility(View.GONE);
                    }
//                    if(status.equals("ItemDelivered")){
//                        changeReqLL.setVisibility(View.GONE);
//                        messageLL.setVisibility(View.GONE);
//                        submitBTN.setVisibility(View.GONE);
//                        statusChangeLL.setVisibility(View.GONE);
//                        approvedLL.setVisibility(View.GONE);
//                    }


                }
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("Approved")) {
                    respondMessage = "Are you sure you want to change the status to \"APPROVED\"";
                } else if (status.equals("Pending")) {
                    respondMessage = "Are you sure you don't want to respond to this request right now!";
                } else if(status.equals("Denied")){
                    respondMessage = "Are you sure you want to change the status to \"DENIED\"";
                }else if(status.equals("Shipped")){
                    respondMessage = "Are you sure you want to keep the status as \"Shipped\"";
                }else if(status.equals("ItemDelivered")){
                    respondMessage = "Are you sure you want conform that the item received as expected";
                }else{
                    respondMessage= "Are you sure you want to save the changes";
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(RequestAction.this);
                builder.setMessage(respondMessage).setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                supplyRequestDoc.update("status", status);
                                db.collection("supplyRequests").document(supplyReqMsgId).update("status", status);
//                                supplyRequestDoc.update("supplyReqMsgId", supplyReqMsgId);
//                                supplyMsgRef.update("status", status);
                                if (status.equals("Approved")) {
                                    if (!newAmount.equals("")) {
                                        supplyRequestDoc.update("supplyAmount", Long.parseLong(newAmount));
                                        supplyRequestDoc.update("totalValue", newTotalAmount);
                                    }

                                    Intent uploadShippingIntent = new Intent(RequestAction.this, UploadShippingLabel.class);
                                    uploadShippingIntent.putExtra("userId", userId);
                                    uploadShippingIntent.putExtra("supplyReqDocId", supplyReqDocId);
                                    startActivity(uploadShippingIntent);

                                }
                                if(status.equals("ItemDelivered")){
                                    changeReqLL.setVisibility(View.GONE);
                                    finish();
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestAction.this);
//                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                        }
//                                    });
//                                    builder.create();
//                                    builder.show();
//
//                                    Intent j = new Intent(RequestAction.this, SupplyRequestRV.class);
//                                    j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(j);
//                                    RequestAction.this.finish();

                                }
                                if(status.equals("PaymentRequested")){
                                    statusChangeLL.setVisibility(View.GONE);
                                }
                                if(status.equals("TransactionCompleted")){
                                    statusChangeLL.setVisibility(View.GONE);
                                    approvedLL.setVisibility(View.GONE);
                                    messageET.setVisibility(View.GONE);


                                }



                            }
                        }).setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
                                Intent j = new Intent(RequestAction.this, SupplyRequestRV.class);
                                j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(j);
                                RequestAction.this.finish();
                            }
                        });

                builder.create();
                builder.show();

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
