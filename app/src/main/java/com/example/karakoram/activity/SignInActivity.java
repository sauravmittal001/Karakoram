package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class SignInActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] adminUsers = {"Developer", "Mess Secy", "Mess Supervisor", "Maint Secy", "Sports Secy", "Cult secy", "Warden"};
    String adminUser;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        spin = findViewById(R.id.category_input);
        spin.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, adminUsers);
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(dropDownAdapter);
    }

    public void onClickSignIn(View view){
        final String name = "User Name";
        final String entryNumber = ((TextView)findViewById(R.id.entry_number)).getText().toString();
        final String password = ((TextView)findViewById(R.id.password)).getText().toString();
        final String wing = ((TextView)findViewById(R.id.wing)).getText().toString();
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
                    User user = new User(name,entryNumber,password,wing,room);
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
        String entryNumber = ((TextView)findViewById(R.id.entry_number_login)).getText().toString();
        final String password = ((TextView)findViewById(R.id.password_login)).getText().toString();
        Query query = FirebaseQuery.getUserByEntryNumber(entryNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> iterator =  snapshot.getChildren().iterator();
                if(iterator.hasNext()) {
                    DataSnapshot dataSnapshot = iterator.next();
                    User user = dataSnapshot.getValue(User.class);
                    if(password.equals(user.getPassword())) {
                        String key = dataSnapshot.getKey();
                        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId", key);
                        editor.putString("userName", user.getName());
                        editor.putString("entryNumber", user.getEntryNumber());
                        editor.putString("room", user.getRoom());
                        editor.putString("wing", user.getWing());
                        editor.putString("type",user.getType().toString());
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "logged in as " + user.getName(), Toast.LENGTH_SHORT).show();
                        SignInActivity.super.onBackPressed();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"incorrect password",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"user does not exist",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClickLogInAdmin(View view){
        String entryNumber = adminUser;
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

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        adminUser = adminUsers[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        adminUser = "Others";
    }

}
