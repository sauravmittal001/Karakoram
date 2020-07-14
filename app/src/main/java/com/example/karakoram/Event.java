package com.example.karakoram;

public class Event {

    private String event_name;
    private String event_details;
    private String event_time;

    Event(String event_name,String event_details,String event_time)
    {
        this.event_name=event_name;
        this.event_details=event_details;
        this.event_time=event_time;
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

    public String getEvent_details() {
        return event_details;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_time() {
        return event_time;
    }
}
