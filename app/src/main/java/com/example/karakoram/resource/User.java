package com.example.karakoram.resource;

import android.content.SharedPreferences;

import com.example.karakoram.activity.SignInActivity;

public class User {

    private String name;
    private String room;
    private String password;
    private String wing;
    private UserType type;
    private String entryNumber;
    public static final String SHARED_PREFS = "sharedPrefs";

    public User(){
        this.type = UserType.Admin;
    }

    public User(String name, String entryNumber, String password, String wing, String room){
        this.name = name;
        this.entryNumber = entryNumber;
        this.room = room;
        this.wing = wing;
        this.password = password;
        this.type = UserType.Student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
    }

    public String getWing() {
        return wing;
    }

    public void setWing(String wing) {
        this.wing = wing;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
