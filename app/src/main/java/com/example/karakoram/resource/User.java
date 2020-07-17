package com.example.karakoram.resource;

public class User {

    private String department;
    private int entryYear;
    private Boolean isLoggedin;
    private String name;
    private String program;
    private String room;
    private UserType type;

    public User(){
        department = "-NA-";
        name = "-NA-";
        room = "-NA-";
        program = "-NA-";
        isLoggedin = false;
        entryYear = -1;
        type = UserType.Student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        this.entryYear = entryYear;
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

    public Boolean getLoggedin() {
        return isLoggedin;
    }

    public void setLoggedin(Boolean loggedin) {
        isLoggedin = loggedin;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
