package com.example.karakoram.resource;

import android.content.SharedPreferences;

import com.example.karakoram.activity.SignInActivity;

public class User {

    private String department;
    private String name;
    private String program;
    private String room;
    private UserType type;
    private String entryNumber;
    public static final String SHARED_PREFS = "sharedPrefs";

    public User(){
        this.type = UserType.Admin;
    }

    public User(String name, String entryNumber, String program, String department, String room){
        this.name = name;
        this.entryNumber = entryNumber;
        this.program = program;
        this.department = department;
        this.room = room;
        this.type = UserType.Admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
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
}
