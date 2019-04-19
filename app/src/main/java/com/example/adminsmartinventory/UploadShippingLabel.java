package com.example.adminsmartinventory;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

public class UploadShippingLabel extends AppCompatActivity {

    Button uploadShippingBTN;
    final int PICK_PDF_REQUEST = 5;
    private String userId, supplyReqDocId, pdfURL;

    private FirebaseFirestore db;
    private DocumentReference supplyReqDocRef;
    private StorageReference shippingStorage;
    Uri pdfURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_shipping_label);

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        supplyReqDocId = intent.getStringExtra("supplyReqDocId");

        supplyReqDocRef = db.collection("users").document(userId).collection("supplyList").document(supplyReqDocId);
        shippingStorage = FirebaseStorage.getInstance().getReference("shippingLabels");


        uploadShippingBTN = findViewById(R.id.uploadShippingBTN);
        uploadShippingBTN.setOnClickListener(new View.OnClickListener() {
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
                final StorageReference fileRef = shippingStorage.child(pdfName + ".pdf" );
                fileRef.putFile(pdfURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                pdfURL = uri.toString();
                                supplyReqDocRef.update("shippingLabelURL", pdfURL);
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.uploadShpCL), "Shipping label uploaded", 500);
                                mySnackbar.show();
                            }
                        });
                    }
                });
            }
            Intent inte = new Intent(UploadShippingLabel.this, SupplyRequestRV.class);
            startActivity(inte);
            finish();
        }
    }

    }

