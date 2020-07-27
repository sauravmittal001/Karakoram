package com.example.karakoram.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.R;
import com.example.karakoram.resource.User;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;

import java.util.Objects;

public class UserInfoActivity extends AppCompatActivity {

    private CustomSpinner floorSpinner, roomNumberSpinner, wingSpinner;
    private String[] floor, roomNumber, wing;

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
        wing = getResources().getStringArray(R.array.wing);
    }

    private void initViews() {
        floorSpinner = findViewById(R.id.spinner_floor);
        roomNumberSpinner = findViewById(R.id.spinner_room_number);
        wingSpinner = findViewById(R.id.spinner_wing);
    }

    private void setViews() {
        CustomSpinnerAdapter floorAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, floor);
        floorAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(floorAdapter);

        CustomSpinnerAdapter roomNumberAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, roomNumber);
        roomNumberAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        roomNumberSpinner.setAdapter(roomNumberAdapter);

        CustomSpinnerAdapter wingAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, wing);
        wingAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        wingSpinner.setAdapter(wingAdapter);
    }


    public void back(View view) {
        super.onBackPressed();
    }

    public void save(View view) {
        String selectedRoom = floor[floorSpinner.getSelectedItemPosition()] + "-" + roomNumber[roomNumberSpinner.getSelectedItemPosition()];
        String selectedWing = wing[wingSpinner.getSelectedItemPosition()];
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("room", selectedRoom);
        editor.putString("wing", selectedWing))
        editor.apply();
    }


}
