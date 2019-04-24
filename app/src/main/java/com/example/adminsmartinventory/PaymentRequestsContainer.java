package com.example.adminsmartinventory;

import java.util.Date;

public class PaymentRequestsContainer {

    private String paymentStatus, itemId, from, supplyReqDocId, bankInfoDocId, userId, paymetRequestDocId;
    private Long noOfUnits, unitPrice, totalCost;
    private Date requestedDate;

    public PaymentRequestsContainer(String paymentStatus, String itemId, String from, Date requestedDate, String supplyReqDocId,
                                  Long noOfUnits, Long unitPrice, Long totalCost, String bankInfoDocId, String userId, String paymetRequestDocId){
        this.paymentStatus = paymentStatus;
        this.itemId = itemId;
        this.noOfUnits = noOfUnits;
        this.unitPrice = unitPrice;
        this.totalCost = totalCost;
        this.from = from;
        this.requestedDate = requestedDate;
        this.supplyReqDocId = supplyReqDocId;
        this.bankInfoDocId = bankInfoDocId;
        this.userId = userId;
        this.paymetRequestDocId = paymetRequestDocId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getItemId() {
        return itemId;
    }

    public String getFrom() {
        return from;
    }

    public String getSupplyReqDocId() {
        return supplyReqDocId;
    }

    public String getBankInfoDocId() {
        return bankInfoDocId;
    }

    public Long getNoOfUnits() {
        return noOfUnits;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public Long getTotalCost() {
        return totalCost;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public String getUserId() { return userId;    }

    public String getPaymetRequestDocId() { return paymetRequestDocId; }
    //    public PaymentRequestsContainer(String title, String itemId, String from, Date requestCreatedDate, String supplyReqDocId, String status, String docId){
//        this.title = title;
//        this.itemId = itemId;
//        this.from = from;
//        this.createDate = requestCreatedDate;
//        this.supplyReqDocId = supplyReqDocId;
//        this.status = status;
//        this.docId = docId;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getItemId() {
//        return itemId;
//    }
//
//    public String getFrom() {
//        return from;
//    }
//
//    public String getSupplyReqDocId() {
//        return supplyReqDocId;
//    }
//
//    public Date getCreateDate() {
//        return createDate;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public String getDocId() {
//        return docId;
//    }
}
