package com.example.adminsmartinventory;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddItems extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView imageNameTV, dateTV, cancelTV;
    private EditText descriptionET, itemIDET, itemNameET, countET, unitPriceET;
    private static final int PICK_IMG_REQUEST = 1;
    private Button uploadeImageBTN, submitBTN;
    private Uri imageURI;
    private ImageButton dateBTN;
    private ImageView previewIV;

    private String itemId, itemName, imageURL, description;
    private Date requiredBefore;
    private int count, unitPrice;
    private Long imageName;
    private FirebaseFirestore db;
    private StorageReference storage, storg;
    private CollectionReference itemCollection;
    private DocumentReference itemDoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        itemIDET = findViewById(R.id.itemIDET);
        itemNameET = findViewById(R.id.itemNameET);
        countET = findViewById(R.id.countET);
        cancelTV = findViewById(R.id.CancelTV);
        uploadeImageBTN = findViewById(R.id.uploadImageBTN);
        previewIV = findViewById(R.id.previewIV);
        dateTV = findViewById(R.id.dateTV);
        dateBTN = findViewById(R.id.dateBTN);
        descriptionET = findViewById(R.id.descriptionET);
        submitBTN = findViewById(R.id.submitBTN);
        unitPriceET = findViewById(R.id.unitPriceET);

        storage = FirebaseStorage.getInstance().getReference("itemImages");
        storg = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        itemCollection = db.collection("items");


//      Click on upload image button to get a image
        uploadeImageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(AddItems.this, AdminView.class);
                startActivity(in);
            }
        });

//
        dateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemId = itemIDET.getText().toString();
                itemName = itemNameET.getText().toString();
                count = Integer.parseInt(countET.getText().toString());
                description = descriptionET.getText().toString();
                unitPrice = Integer.parseInt(unitPriceET.getText().toString());


                if(itemId.equals("")||itemName.equals("")||count<=0 || requiredBefore == null || imageURL.equals("") || description.equals("") || unitPrice<=0){
                    Toast.makeText(AddItems.this, "All the field needs to be completed ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Map<String, Object> addItem = new HashMap<>();
                    addItem.put("itemId", itemId);
                    addItem.put("imageURL", imageURL);
                    addItem.put("initialUnitsRequired", count);
                    addItem.put("itemDetails", description);
                    addItem.put("itemName", itemName);
                    addItem.put("itemPostedDate", new Timestamp(new Date()));
                    addItem.put("requiredBefore", new Timestamp(requiredBefore));
                    addItem.put("unitRequired", count);
                    addItem.put("untPrice", unitPrice);

                    itemCollection.document().set(addItem);
                    Toast.makeText(AddItems.this, "Item added to the list", Toast.LENGTH_SHORT).show();

                    finish();


                }

            }
        });

    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMG_REQUEST && resultCode == RESULT_OK
        && data !=null && data.getData()!=null){
            imageURI = data.getData();
            uploadImage();
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage() {
        if (imageURI != null) {
            imageName = System.currentTimeMillis();
            final StorageReference fileRef = storage.child(imageName + "." + getFileExtension(imageURI));
            fileRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageURL = uri.toString();
                            Picasso.get().load(imageURL).into(previewIV);
                        }
                    });
                }
            });

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        requiredBefore = c.getTime();

        String dateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dateTV.setText(dateString);
    }
}
