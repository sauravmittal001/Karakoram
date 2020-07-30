package com.example.karakoram;

import android.net.Uri;
import android.util.Log;

import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.Complain;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.HostelBill;
import com.example.karakoram.resource.MessFeedback;
import com.example.karakoram.resource.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseQuery {

    private static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();

    public static Query getUserDetail(String id){
        return ref.child("users").child(id);
    }

    public static String addUser(User user){
        String key = ref.child("users").push().getKey();
        ref.child("users").child(key).setValue(user);
        return key;
    }

    public static Query getUserByEntryNumber(String entryNumber){
        return ref.child("users").orderByChild("entryNumber").equalTo(entryNumber);
    }

    public static Query getUserByKey(String key){
        return ref.child("users").child(key);
    }

    public static Query getEvent(String key){
        return ref.child("events").child(key);
    }

    public static Query getAllEvents(){
        return ref.child("events").orderByChild("userId");
    }

    public static Query getUserEvents(String userId){
        return ref.child("events").orderByChild("userId").equalTo(userId);
    }

    public static void addEvent(Event event, Uri imageUri){
        String key = ref.child("events").push().getKey();
        ref.child("events").child(key).setValue(event);
        storage.getReference("/eventImages/"+key+".png").putFile(imageUri);
    }

    public static Query getCategoryBills(Category category){
        return ref.child("hostelBills").orderByChild("category").equalTo(String.valueOf(category));
    }

    public static Query getBill(String key){
        return ref.child("hostelBills").child(key);
    }

    public static void addBill(HostelBill bill, Uri imageUri){
        String key = ref.child("hostelBills").push().getKey();
        ref.child("hostelBills").child(key).setValue(bill);
        storage.getReference("/hostelBillImages/"+key+".png").putFile(imageUri);
    }

    public static void addMessFeedBack(MessFeedback messFeedback){
        String key = ref.child("messFeedback").push().getKey();
        ref.child("messFeedback").child(key).setValue(messFeedback);
    }

    public static Query getAllMessFeedback(){
        return ref.child("messFeedback");
    }

    public Query getUserMessFeedback(String userId){
        return ref.child("messFeedBack").orderByChild("userId").equalTo(userId);
    }

    public static Query getAllMenu(){
        return ref.child("messMenu");
    }

    public  static Query getMenu(String day){
        return ref.child("messMenu").child(day);
    }

    public static Query getAllComplains(){
        return ref.child("complains");
    }

    public static void addComplian(Complain complain){
        String key = ref.child("complains").push().getKey();
        ref.child("complains").child(key).setValue(complain);
    }

}