package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.User;
import com.example.karakoram.resource.UserType;
import com.example.karakoram.resource.Wing;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Objects;

public class UserInfoActivity extends AppCompatActivity {

    private CustomSpinner floorSpinner, roomNumberSpinner;
    private String[] floor, roomNumber, wing;
    private EditText mCurrent, mNew, mRetype;
    private TextView mName, mEntryNumber, mChangePassword;
    private String userId, userPassword, userEntryNumber, userName, userRoomNumber, userFloor;
    private boolean changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_user_info);
        initVariables();
        initViews();
        setViews();

    }

    private void initVariables() {
        floor = getResources().getStringArray(R.array.floor);
        roomNumber = getResources().getStringArray(R.array.room_number);
        changePassword = false;
        Wing[] wings = Wing.values();
        wing = new String[wings.length+1];
        wing[0] = "";
        for(int i=0;i<wings.length;i++)
            wing[i+1] = String.valueOf(wings[i]);

        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "loggedOut");
        userEntryNumber = sharedPreferences.getString("entryNumber", "NA");
        userName = sharedPreferences.getString("userName", "NA");
        String room = sharedPreferences.getString("room", "A01");
        userRoomNumber = room.substring(1);
        userFloor = room.substring(0,1);

        Query query = FirebaseQuery.getUserByEntryNumber(userEntryNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    userPassword = user.getPassword();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews() {
        floorSpinner = findViewById(R.id.spinner_floor);
        roomNumberSpinner = findViewById(R.id.spinner_room_number);

        mChangePassword = findViewById(R.id.tv_change_password);
        mCurrent = findViewById(R.id.et_current);
        mNew = findViewById(R.id.et_new);
        mRetype = findViewById(R.id.et_retype);

        mName = (TextView) findViewById(R.id.tv_user_name);
        mEntryNumber = (TextView) findViewById(R.id.tv_user_entryNumber);
    }

    private void setViews() {
        mName.setText(userName);
        mEntryNumber.setText(userEntryNumber);

        CustomSpinnerAdapter floorAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, floor);
        floorAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(floorAdapter);
        if (userFloor != null) {
            int spinnerPosition = floorAdapter.getPosition(userFloor);
            floorSpinner.setSelection(spinnerPosition);
        }

        CustomSpinnerAdapter roomNumberAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, roomNumber);
        roomNumberAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        roomNumberSpinner.setAdapter(roomNumberAdapter);
        if (userRoomNumber != null) {
            int spinnerPosition = roomNumberAdapter.getPosition(userRoomNumber);
            roomNumberSpinner.setSelection(spinnerPosition);
        }

        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword = !changePassword;
                if(!changePassword)
                    findViewById(R.id.ll_change_password).setVisibility(View.GONE);
                else
                    findViewById(R.id.ll_change_password).setVisibility(View.VISIBLE);
            }
        });
    }


    public void back(View view) {
        super.onBackPressed();
    }

    public void save(View view) {
        String location = floor[floorSpinner.getSelectedItemPosition()] + roomNumber[roomNumberSpinner.getSelectedItemPosition()];
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("room", location);
        editor.apply();

        if(changePassword) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (!mCurrent.getText().toString().equals(userPassword)) {
                    mCurrent.setBackground(getDrawable(R.drawable.background_rect_section_task_red));
                    Toast.makeText(this, "current password is incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mNew.getText().toString().equals(mRetype.getText().toString())) {
                    mRetype.setBackground(getDrawable(R.drawable.background_rect_section_task_red));
                    Toast.makeText(this, "both new passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            FirebaseQuery.setUserPassWord(userId, mNew.getText().toString(), false);
        }
        FirebaseQuery.setUserRoom(userId,location);
        super.onBackPressed();
    }
}
