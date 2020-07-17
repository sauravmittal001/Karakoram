package com.example.karakoram.firebase;

import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.HostelBill;
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

    public static void addEvent(Event event){
        String key = ref.child("events").push().getKey();
        ref.child("events").child(key).setValue(event);
    }

    public static Query getCategoryBills(Category category){
        return ref.child("hostelBills").orderByChild("category").equalTo(String.valueOf(category));
    }

    public static Query getBill(String billId){
        return ref.child("hostelBills").child(billId);
    }

    public static void addBill(HostelBill bill){
        String key = ref.child("hostelBills").push().getKey();
        ref.child("hostelBills").child(key).setValue(bill);
    }
}
