package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.User;
import com.example.karakoram.resource.UserType;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class SignInActivity extends AppCompatActivity {

    private CustomSpinner userInputSpinner;
    private String[] userInputArray;
    private EditText mPassword, mEntryNoEdit;
    private TextView mForgot, mEntryNoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initVariables();
        initViews();
        setViews();
    }

    private void initVariables() {

    }

    private void initViews() {
        mEntryNoTitle = findViewById(R.id.tv_login_entry_number);
        mEntryNoEdit = findViewById(R.id.et_login_entry_number);
        mPassword = findViewById(R.id.et_login_password);
        mForgot = findViewById(R.id.tv_login_forgot);
    }

    private void setViews() {
        userInputArray = getResources().getStringArray(R.array.user_type);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, userInputArray);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        userInputSpinner = findViewById(R.id.spinner_user_type);
        userInputSpinner.setAdapter(adapter);
        userInputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String userType = userInputArray[userInputSpinner.getSelectedItemPosition()];
                if (userType.equals("Student")) {
                    mEntryNoTitle.setText("Entry number");
                    mEntryNoTitle.setTextColor(Color.parseColor("#3E3E3E"));
                    mEntryNoEdit.setEnabled(true);
                } else {
                    mEntryNoTitle.setText("Entry number (not for admins)");
                    mEntryNoTitle.setTextColor(Color.parseColor("#d6d6d6"));
                    mEntryNoEdit.setEnabled(false);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public void onClickContinue(View view) {
        final String password = mPassword.getText().toString();
        String userInput = userInputArray[userInputSpinner.getSelectedItemPosition()];
        String entryNumber;
        if (!userInput.equals("Student")){
            entryNumber = userInput;
            Log.d("123hello",entryNumber);
        }
        else {
            entryNumber = mEntryNoEdit.getText().toString();
        }
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

    public void onForgotPassword(View view) {
        Toast.makeText(this, "error: contact developers", Toast.LENGTH_SHORT).show();
    }
}