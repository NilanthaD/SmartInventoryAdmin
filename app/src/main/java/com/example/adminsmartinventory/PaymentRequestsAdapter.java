package com.example.adminsmartinventory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PaymentRequestsAdapter extends RecyclerView.Adapter<PaymentRequestsAdapter.PaymentRequestsViewHolder> {

    private ArrayList<PaymentRequestsContainer> paymentRequestList;
    private Context context;

    public PaymentRequestsAdapter(ArrayList<PaymentRequestsContainer> paymentRequestList, Context context){
        this.paymentRequestList = paymentRequestList;
        this.context = context;
    }
    public static class PaymentRequestsViewHolder extends RecyclerView.ViewHolder{
        public TextView paymentStatusTV, itemIdTV, noOfUnitsTV, unitPriceTV, totalCostTV, dateTV, requestedByTV;
        private LinearLayout paymentRequestLL;


        public PaymentRequestsViewHolder(@NonNull View paymentRequestView) {
            super(paymentRequestView);
            requestedByTV = paymentRequestView.findViewById(R.id.requestedByTV);
            paymentStatusTV = paymentRequestView.findViewById(R.id.paymentStatusTV);
            itemIdTV = paymentRequestView.findViewById(R.id.itemIdTV);
            noOfUnitsTV = paymentRequestView.findViewById(R.id.noOfUnitsTV);
            unitPriceTV = paymentRequestView.findViewById(R.id.unitPriceTV);
            totalCostTV = paymentRequestView.findViewById(R.id.totalCostTV);
            dateTV = paymentRequestView.findViewById(R.id.dateTV);
            paymentRequestLL = paymentRequestView.findViewById(R.id.paymentRequestLL);
        }
    }

    @NonNull
    @Override
    public PaymentRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_requests_container, viewGroup, false);
        PaymentRequestsViewHolder paymentRequestsViewHolder = new PaymentRequestsViewHolder(v);
        return paymentRequestsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentRequestsViewHolder paymentRequestsViewHolder, int i) {
        final PaymentRequestsContainer currentRequest = paymentRequestList.get(i);
        final String requestedDate = FormatDate.getDate(currentRequest.getRequestedDate());
        paymentRequestsViewHolder.requestedByTV.setText("Requested By : " + currentRequest.getFrom());
        paymentRequestsViewHolder.paymentStatusTV.setText("Status : " + currentRequest.getPaymentStatus());
        paymentRequestsViewHolder.itemIdTV.setText("Item Id : " + currentRequest.getItemId());
        paymentRequestsViewHolder.unitPriceTV.setText("Unit Price : $" + currentRequest.getUnitPrice());
        paymentRequestsViewHolder.noOfUnitsTV.setText("No of Units : " + currentRequest.getNoOfUnits().toString());
        paymentRequestsViewHolder.totalCostTV.setText("Total Amount : $" + currentRequest.getTotalCost().toString());
        paymentRequestsViewHolder.dateTV.setText("Requested Date : " + requestedDate);

        paymentRequestsViewHolder.paymentRequestLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(context, UploadPayment.class);
                paymentIntent.putExtra("requestedBy", currentRequest.getFrom());
                paymentIntent.putExtra("status", currentRequest.getPaymentStatus());
                paymentIntent.putExtra("itemId",currentRequest.getItemId());
                paymentIntent.putExtra("unitPrice",currentRequest.getUnitPrice());
                paymentIntent.putExtra("noOfUnits",currentRequest.getNoOfUnits().toString());
                paymentIntent.putExtra("totalAmount", currentRequest.getTotalCost().toString());
                paymentIntent.putExtra("requestedDate", requestedDate);
                paymentIntent.putExtra("bankInfoDocId", currentRequest.getBankInfoDocId());
                paymentIntent.putExtra("supplyReqDocId", currentRequest.getSupplyReqDocId());
                paymentIntent.putExtra("userId", currentRequest.getUserId());
                paymentIntent.putExtra("paymentRequestDocId", currentRequest.getPaymetRequestDocId());
                context.startActivity(paymentIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return paymentRequestList.size();
    }
}
