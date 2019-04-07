package com.example.adminsmartinventory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDate {

    public FormatDate() {
    }

    public static String getDate(Date date){
        Date createdDate = new Date();
        createdDate = date;
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        String formatedDate = sdf.format(createdDate);
        return formatedDate;
    }

}
