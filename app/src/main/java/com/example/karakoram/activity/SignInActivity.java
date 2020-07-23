package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void onClickSignIn(View view){
        final String name = ((TextView)findViewById(R.id.name)).getText().toString();
        final String entryNumber = ((TextView)findViewById(R.id.entry_number)).getText().toString();
        final String department = ((TextView)findViewById(R.id.department)).getText().toString();
        final String program = ((TextView)findViewById(R.id.program)).getText().toString();
        final String room = ((TextView)findViewById(R.id.room)).getText().toString();
        Query query = FirebaseQuery.getUserByEntryNumber(entryNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> iterator =  snapshot.getChildren().iterator();
                if(iterator.hasNext()){
                    Toast.makeText(getApplicationContext(),"entry number already exists",Toast.LENGTH_SHORT).show();
                }
                else{
                    User user = new User(name,entryNumber,program,department,room);
                    String key = FirebaseQuery.addUser(user);
                    SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId",key);
                    editor.putString("userName",user.getName());
                    editor.putString("entryNumber",user.getEntryNumber());
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"logged in as "+name,Toast.LENGTH_SHORT).show();
                    SignInActivity.super.onBackPressed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClickLogIn(View view){
        String entryNumber = ((TextView)findViewById(R.id.login_entry_number)).getText().toString();
        Query query = FirebaseQuery.getUserByEntryNumber(entryNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> iterator =  snapshot.getChildren().iterator();
                if(iterator.hasNext()) {
                    DataSnapshot dataSnapshot = iterator.next();
                    User user = dataSnapshot.getValue(User.class);
                    String key = dataSnapshot.getKey();
                    SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId",key);
                    editor.putString("userName",user.getName());
                    editor.putString("entryNumber",user.getEntryNumber());
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"logged in as "+user.getName(),Toast.LENGTH_SHORT).show();
                    SignInActivity.super.onBackPressed();
                }
                else
                    Toast.makeText(getApplicationContext(),"user does not exist",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
