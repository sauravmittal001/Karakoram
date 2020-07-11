package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karakoram.OnGetDataListener;
import com.example.karakoram.R;
import com.example.karakoram.resource.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void fillDetail(View view){
        EditText idInput = findViewById(R.id.id_input);
        String id = idInput.getText().toString();
        getData(ref.child("users").child(id));
    }

    public void readData(DatabaseReference ref, final OnGetDataListener listener) {
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError firebaseError) {
                listener.onFailure();
            }
        });
    }

    public void getData(DatabaseReference ref){
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(Object obj) {
                User user = (User)obj;
                Log.d("123",user.getName());
                if(user==null) {
                    Log.d("123", "yes");
                    user = new User();
                }
                else
                    Log.d("123","no");

                ((TextView)findViewById(R.id.name)).setText(user.getName());
                ((TextView)findViewById(R.id.entry_year)).setText(String.valueOf(user.getEntryYear()));
                ((TextView)findViewById(R.id.department)).setText(user.getDepartment());
                ((TextView)findViewById(R.id.program)).setText(user.getProgram());
            }

            @Override
            public void onStart() {
                Log.d("123","started");
            }

            @Override
            public void onFailure() {
                Log.d("123","failed");
            }
        });
    }
}