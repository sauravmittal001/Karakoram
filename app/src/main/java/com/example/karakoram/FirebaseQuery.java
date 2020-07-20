package com.example.karakoram;

import android.net.Uri;
import android.util.Log;

import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.HostelBill;
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

    public static Query getEvent(String key){
        return ref.child("events").child(key);
    }

    public static Query getAllEvents(){
        return ref.child("events");
    }

    public static void addEvent(Event event, Uri imageUri){
        String key = ref.child("events").push().getKey();
        if (key != null) {
            ref.child("events").child(key).setValue(event);
            storage.getReference("/eventImages/"+key+".png").putFile(imageUri);
        }
        else {
            Log.d("FireBaseQueryClass", "key is null");
        }
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
