package com.example.adminsmartinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadPaymentLable extends AppCompatActivity {
    Button uploadPaymentBTN;
    final int PICK_PDF_REQUEST = 5;
    private String userId, supplyReqDocId, pdfURL;

    private FirebaseFirestore db;
    private DocumentReference supplyReqDocRef;
    private StorageReference paymentReceiptRef;
    Uri pdfURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_payment_lable);

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        supplyReqDocId = intent.getStringExtra("supplyReqDocId");

        supplyReqDocRef = db.collection("users").document(userId).collection("supplyList").document(supplyReqDocId);
        paymentReceiptRef = FirebaseStorage.getInstance().getReference("payments");
        uploadPaymentBTN = findViewById(R.id.uploadPaymentBTN);
        uploadPaymentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_PDF_REQUEST);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK
                && data !=null && data.getData()!=null){
            pdfURI = data.getData();
            if(pdfURI != null){
                Long pdfName = System.currentTimeMillis();
                final StorageReference fileRef = paymentReceiptRef.child(pdfName + ".pdf" );
                fileRef.putFile(pdfURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                pdfURL = uri.toString();
                                supplyReqDocRef.update("paymentReceiptURL", pdfURL);
                                supplyReqDocRef.update("status","PaymentCompleted");
//                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.uploadShpCL), "Shipping label uploaded", 500);
//                                mySnackbar.show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(UploadPaymentLable.this);
                                builder.setMessage("You have completed the paying the customer").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent j = new Intent(UploadPaymentLable.this, PaymentRequestsRV.class);
                                        j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(j);
                                        UploadPaymentLable.this.finish();
                                    }
                                });
                                builder.create();
                                builder.show();

                            }
                        });
                    }
                });
            }
        }

    }
}
