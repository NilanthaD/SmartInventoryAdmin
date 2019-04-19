package com.example.adminsmartinventory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ItemsViewHolder> {
    private ArrayList<ItemViewContainer> itemListArray;
    private Context context;

    public ItemViewAdapter(ArrayList<ItemViewContainer> itemListArray, Context context) {
        this.itemListArray = itemListArray;
        this.context = context;
    }


    public static class ItemsViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemImage;
        public TextView itemID;
        public TextView itemName;
        public TextView unitPrice;
        public TextView quantityNeeded;
        public TextView requiredBy;
        public LinearLayout linearLayout2;

        public ItemsViewHolder(@NonNull View itemView){
            super(itemView);
            itemImage = itemView.findViewById(R.id.imageIV);
            itemID = itemView.findViewById(R.id.itemIdTV);
            itemName = itemView.findViewById(R.id.iNameTV);
            unitPrice = itemView.findViewById(R.id.priceTV);
            quantityNeeded = itemView.findViewById(R.id.qntyNeededTV);
            requiredBy = itemView.findViewById(R.id.requiredByTV);
            linearLayout2 = itemView.findViewById(R.id.linearLayout2);
        }
    }
    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemviewcontainer, viewGroup, false);
        ItemsViewHolder itemsVH = new ItemsViewHolder(v);
        return itemsVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder itemsViewHolder, int i) {
        final ItemViewContainer currentItem = itemListArray.get(i);
        final String requiredBy = FormatDate.getDate(currentItem.getRequiredBy());
        Picasso.get().load(currentItem.getImageURL()).into(itemsViewHolder.itemImage);
        itemsViewHolder.itemID.setText(currentItem.getItemID());
        itemsViewHolder.itemName.setText(currentItem.getItemName());
        itemsViewHolder.unitPrice.setText("Buying price :$" + currentItem.getUnitPrice());
        itemsViewHolder.quantityNeeded.setText("Quentity needed :" + currentItem.getQntyNeeded());
        itemsViewHolder.requiredBy.setText("Required by :" + requiredBy);

        itemsViewHolder.linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditItem.class);
                intent.putExtra("imageURL", currentItem.getImageURL());
                intent.putExtra("userEmail", currentItem.getUserEmail());
                intent.putExtra("documentId", currentItem.getDocumentId());
                intent.putExtra("itemId", currentItem.getItemID());
                intent.putExtra("itemName", currentItem.getItemName());
                intent.putExtra("unitPrice", currentItem.getUnitPrice());
                intent.putExtra("qntyRequired", currentItem.getQntyNeeded());
                intent.putExtra("requiredBy", requiredBy);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemListArray.size();
    }
}
