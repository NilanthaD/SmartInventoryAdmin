package com.example.adminsmartinventory;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditItem extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private ImageView editImageIV;
    private EditText itemNameET, descriptionET, buyingPriceET, qntityNeededET, requiredByET;
    private Button saveChangesBTN;
    private TextView cancelTV, itemIdTV;
    private ImageButton dateBTN;
    private String imageURL, userEmail, documentId, itemId, itemName, requiredBy, description;
    private Date requiredBefore;
    private Long unitPrice, qntyRequired;

    private FirebaseFirestore db;
    private DocumentReference itemDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editImageIV = findViewById(R.id.editImgeIV);
        itemNameET = findViewById(R.id.itemNameET);
        descriptionET = findViewById(R.id.descriptionET);
        buyingPriceET = findViewById(R.id.buyingPriceET);
        qntityNeededET = findViewById(R.id.qntityNeededET);
        requiredByET = findViewById(R.id.requiredByET);
        saveChangesBTN = findViewById(R.id.saveChangesBTN);
        cancelTV = findViewById(R.id.cancelTV);
        dateBTN = findViewById(R.id.dateBTN);
        itemIdTV = findViewById(R.id.itemIdTV);



        Intent intent = getIntent();
        imageURL = intent.getStringExtra("imageURL");
        userEmail = intent.getStringExtra("userEmail");
        documentId = intent.getStringExtra("documentId");
        itemId = intent.getStringExtra("itemId");
        itemName = intent.getStringExtra("itemName");
        unitPrice = intent.getLongExtra("unitPrice", 0);
        qntyRequired = intent.getLongExtra("qntyRequired", 0);
        requiredBy = intent.getStringExtra("requiredBy");


        itemIdTV.setText(itemId + " - Edit");
        Picasso.get().load(imageURL).into(editImageIV);
        itemNameET.setText(itemName);
        descriptionET.setText("Description");
        buyingPriceET.setText(unitPrice.toString());
        qntityNeededET.setText(qntyRequired.toString());
        requiredByET.setText(requiredBy);

        db = FirebaseFirestore.getInstance();
        itemDoc = db.collection("items").document(documentId);
        dateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        itemDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                description = documentSnapshot.get("itemDetails").toString();
                descriptionET.setText(description);
            }
        });

        saveChangesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName = itemNameET.getText().toString();
                qntyRequired = Long.parseLong(qntityNeededET.getText().toString());
                description = descriptionET.getText().toString();
//                if(requiredBefore != null){
//                    requiredBy = FormatDate.getDate(requiredBefore);
//                }
                unitPrice = Long.parseLong(buyingPriceET.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(EditItem.this);
                builder.setMessage("Are you sure you want to save the changers").setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        itemDoc.update("itemName", itemName, "unitRequired", qntyRequired, "itemDetails", description, "untPrice", unitPrice);
                        if(requiredBefore != null){
                            itemDoc.update("requiredBefore", requiredBefore);
                        }
                        Intent j = new Intent(EditItem.this, ViewItemsRV.class);
                        j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(j);
                        EditItem.this.finish();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();


            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        requiredBefore = c.getTime();

        String dateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        requiredByET.setText(dateString);
    }
}
