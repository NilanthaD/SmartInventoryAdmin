package com.example.adminsmartinventory;

import java.text.DateFormat;
import java.util.Date;

public class SupplyRequestContainer {
    private String title, itemId, from, supplyReqDocId, status, docId;
    private Date createDate;

    public SupplyRequestContainer(String title, String itemId, String from, Date requestCreatedDate, String supplyReqDocId, String status, String docId){
        this.title = title;
        this.itemId = itemId;
        this.from = from;
        this.createDate = requestCreatedDate;
        this.supplyReqDocId = supplyReqDocId;
        this.status = status;
        this.docId = docId;
    }

    public String getTitle() {
        return title;
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

    public Date getCreateDate() {
        return createDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDocId() {
        return docId;
    }
}
