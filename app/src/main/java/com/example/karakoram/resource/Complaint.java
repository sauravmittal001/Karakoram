package com.example.karakoram.resource;

import android.content.SharedPreferences;

import java.util.Date;

public class Complaint {

    protected String userId;
    protected String entryNumber;
    protected String userName;
    protected Date timestamp;
    protected Status status;
    protected Category category;
    protected String description;
    protected boolean isImageAttached;

    public Complaint(){}

    public Complaint(SharedPreferences sharedPreferences){
        userId = sharedPreferences.getString("userId","loggedOut");
        userName = sharedPreferences.getString("userName","NA");
        entryNumber = sharedPreferences.getString("entryNumber","NA");
        status = Status.Fired;
        category = Category.Others;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean getIsImageAttached() {
        return isImageAttached;
    }

    public void setIsImageAttached(boolean imageAttached) {
        isImageAttached = imageAttached;
    }
}
