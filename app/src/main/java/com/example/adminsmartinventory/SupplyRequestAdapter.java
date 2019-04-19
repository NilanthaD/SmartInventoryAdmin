package com.example.adminsmartinventory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SupplyRequestAdapter extends RecyclerView.Adapter<SupplyRequestAdapter.SupplyRequestViewHolder> {

    private ArrayList<SupplyRequestContainer> supplyRequestList;
    private Context context;

    public SupplyRequestAdapter(ArrayList<SupplyRequestContainer> supplyRequestArray, Context context){
        this.supplyRequestList = supplyRequestArray;
        this.context = context;

    }
    public static class SupplyRequestViewHolder extends RecyclerView.ViewHolder{
        public TextView titleTV, itemIdTV, fromTV, statusTV, dateTV;
        private LinearLayout supplyRequestLL;


        public SupplyRequestViewHolder(@NonNull View supplyRequestView) {
            super(supplyRequestView);
            titleTV = supplyRequestView.findViewById(R.id.titleTV);
            itemIdTV = supplyRequestView.findViewById(R.id.itemIdTV);
            fromTV = supplyRequestView.findViewById(R.id.fromTV);
            dateTV = supplyRequestView.findViewById(R.id.dateTV);
            statusTV = supplyRequestView.findViewById(R.id.statusTV);
            supplyRequestLL = supplyRequestView.findViewById(R.id.supplyRequestLL);
        }
    }

    @NonNull
    @Override
    public SupplyRequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.supply_request_container, viewGroup, false);
        SupplyRequestViewHolder supplyRequestVH = new SupplyRequestViewHolder(v);
        return  supplyRequestVH;
    }

    @Override
    public void onBindViewHolder(@NonNull SupplyRequestViewHolder supplyRequestViewHolder, int i) {
        final SupplyRequestContainer currentRequest = supplyRequestList.get(i);
        final String dateCreated = FormatDate.getDate(currentRequest.getCreateDate());
//        if(currentRequest.getStatus() == "pending"){
//            supplyRequestViewHolder.titleTV.setBackgroundColor(Color.parseColor("#A2F292"));
//            Toast.makeText(context, "Status :", Toast.LENGTH_SHORT).show();
//        }
        supplyRequestViewHolder.titleTV.setText(currentRequest.getTitle());
        supplyRequestViewHolder.itemIdTV.setText(currentRequest.getItemId());
        supplyRequestViewHolder.fromTV.setText("From: "+currentRequest.getFrom());
        supplyRequestViewHolder.statusTV.setText("Status: "+currentRequest.getStatus());
        supplyRequestViewHolder.dateTV.setText(dateCreated);

        supplyRequestViewHolder.supplyRequestLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent requestActionIntent = new Intent(context, RequestAction.class);
                requestActionIntent.putExtra("supplyReqDocId", currentRequest.getSupplyReqDocId());
                requestActionIntent.putExtra("userEmail", currentRequest.getFrom());
                requestActionIntent.putExtra("status", currentRequest.getStatus());
                requestActionIntent.putExtra("docId", currentRequest.getDocId());
                context.startActivity(requestActionIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return supplyRequestList.size();
    }
}
