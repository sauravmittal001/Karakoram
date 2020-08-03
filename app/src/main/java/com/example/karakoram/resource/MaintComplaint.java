package com.example.karakoram.resource;

import android.content.SharedPreferences;

public class MaintComplaint extends Complaint {

    private ComplaintArea complaintArea;
    private String room;
    private Wing wing;

    public MaintComplaint(){}

    public MaintComplaint(SharedPreferences sharedPreferences) {
        super(sharedPreferences);
        this.category = Category.Maintenance;
    }

    public ComplaintArea getComplaintArea() {
        return complaintArea;
    }

    public void setComplaintArea(ComplaintArea complaintArea) {
        this.complaintArea = complaintArea;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Wing getWing() {
        return wing;
    }

    public void setWing(Wing wing) {
        this.wing = wing;
    }
}
