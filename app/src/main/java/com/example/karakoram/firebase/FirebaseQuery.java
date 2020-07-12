package com.example.karakoram.firebase;

import com.example.karakoram.resource.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FirebaseQuery {

    private static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public static Query getUserDetail(String id){
        return ref.child("users").child(id);
    }

    public static void setUser(String id, User user){
        ref.child("users").child(id).setValue(user);
    }

    public static Query getEvent(String title){
        return ref.child("events").orderByChild("title").equalTo(title);
    }

    public static Query getAllEvents(){
        return ref.child("events");
    }
}
