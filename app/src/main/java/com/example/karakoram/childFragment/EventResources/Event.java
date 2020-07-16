package com.example.karakoram.childFragment.EventResources;

public class Event {

    private String event_name;
    private String event_title;
    private String event_details;
    private String event_time;
    private int event_image=no_image_provided;

    private static final int no_image_provided=-1;

    public Event(String event_name, String event_title, String event_time, String event_details)
    {
        this.event_name=event_name;
        this.event_details=event_details;
        this.event_time=event_time;
        this.event_title=event_title;
    }

    public Event(String event_name, String event_title, String event_time, String event_details, int event_image)
    {
        this.event_name=event_name;
        this.event_details=event_details;
        this.event_time=event_time;
        this.event_title=event_title;
        this.event_image=event_image;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setEvent_details(String event_details) {
        this.event_details = event_details;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public void setEvent_image(int event_image) {
        this.event_image = event_image;
    }

    public String getEvent_details() {
        return event_details;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_time() {
        return event_time;
    }

    public String getEvent_title() {
        return event_title;
    }

    public int getEvent_image() {
        return event_image;
    }


    public boolean hasimage()
    {
        return event_image!=no_image_provided;
    }


}
