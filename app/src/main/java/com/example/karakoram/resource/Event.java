package com.example.karakoram.resource;

import java.util.Date;

public class Event {

    private String title;
    private String description;
    private Date dateTime;
    private String userId;
    private Date timeStamp;
    private boolean isImageAttached;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isImageAttached() {
        return isImageAttached;
    }

    public void setImageAttached(boolean imageAttached) {
        isImageAttached = imageAttached;
    }
}
