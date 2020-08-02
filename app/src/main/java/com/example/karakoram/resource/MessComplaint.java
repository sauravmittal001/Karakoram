package com.example.karakoram.resource;

import android.content.SharedPreferences;

public class MessComplaint extends Complaint {

    private ComplaintArea complaintArea;

    public MessComplaint(){}

    public MessComplaint(SharedPreferences sharedPreferences) {
        super(sharedPreferences);
        this.category = Category.Mess;
    }

    public ComplaintArea getComplaintArea() {
        return complaintArea;
    }

    public void setComplaintArea(ComplaintArea complaintArea) {
        this.complaintArea = complaintArea;
    }
}
