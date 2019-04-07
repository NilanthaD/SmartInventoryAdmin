package com.example.adminsmartinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminView extends AppCompatActivity {

    Button addItemBTN, messageBTN, ViewItemBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        addItemBTN = findViewById(R.id.addItemBTN);
        messageBTN = findViewById(R.id.messageBTN);
        ViewItemBTN = findViewById(R.id.ViewItemBTN);

        ViewItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AdminView.this,ViewItemsRV.class);
                startActivity(in);
            }
        });

        addItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AdminView.this, AddItems.class);
                startActivity(in);
            }
        });

    }


}
