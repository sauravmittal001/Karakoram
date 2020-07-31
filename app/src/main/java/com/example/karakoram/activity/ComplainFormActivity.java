package com.example.karakoram.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karakoram.R;
import com.example.karakoram.resource.Complain;
import com.example.karakoram.resource.Status;
import com.example.karakoram.resource.User;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;

import java.util.Objects;

public class ComplainFormActivity extends AppCompatActivity {

    private CustomSpinner categorySpinner, floorSpinner, roomNumberSpinner, wingSpinner, anonymitySpinner;
    private String[] categoryArray, floorArray, roomNumberArray, wingArray, anonymityArray;
    private Uri imageUri;
    private TextView mError;
    private EditText mDescription;
    private Button mButton;
    private String userRoomNumber, userFloor, userEntryNumber;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_complain_form);
        setVariables();
        setViews();
    }

    private void setVariables() {
        sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
        String room = sharedPreferences.getString("room", "A01");
        userRoomNumber = room.substring(1);
        userFloor = room.substring(0,1);
        userEntryNumber = sharedPreferences.getString("entryNumber", "DEFAULT");
    }

    private void setViews() {
        mError = (TextView) findViewById(R.id.tv_complain_details);
        mDescription = (EditText) findViewById(R.id.et_complain_description);
        mButton = findViewById(R.id.button_complain_submit);
        setSpinners();
    }

    private void setSpinners() {
        categoryArray = getResources().getStringArray(R.array.complaint_category);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, categoryArray);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        categorySpinner = findViewById(R.id.spinner_complain_category);
        categorySpinner.setAdapter(adapter);

        wingArray = getResources().getStringArray(R.array.wing);
        CustomSpinnerAdapter wingAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, wingArray);
        wingAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        wingSpinner = findViewById(R.id.spinner_complain_wing);
        wingSpinner.setAdapter(wingAdapter);

        floorArray = getResources().getStringArray(R.array.floor);
        CustomSpinnerAdapter floorAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, floorArray);
        floorAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        floorSpinner = findViewById(R.id.spinner_complain_floor);
        floorSpinner.setAdapter(floorAdapter);
        if (userFloor != null) {
            int spinnerPosition = floorAdapter.getPosition(userFloor);
            floorSpinner.setSelection(spinnerPosition);
        }

        roomNumberArray = getResources().getStringArray(R.array.room_number);
        CustomSpinnerAdapter roomNumberAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, roomNumberArray);
        roomNumberAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        roomNumberSpinner = findViewById(R.id.spinner_complain_room_number);
        roomNumberSpinner.setAdapter(roomNumberAdapter);
        if (userRoomNumber != null) {
            int spinnerPosition = roomNumberAdapter.getPosition(userRoomNumber);
            roomNumberSpinner.setSelection(spinnerPosition);
        }

        anonymityArray = getResources().getStringArray(R.array.complaint_anonymity);
        CustomSpinnerAdapter anonymityAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, anonymityArray);
        anonymityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        anonymitySpinner = findViewById(R.id.spinner_anonymity);
        anonymitySpinner.setAdapter(anonymityAdapter);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String category = categoryArray[categorySpinner.getSelectedItemPosition()];
                if (category.equals("Maintenance")) {
                    findViewById(R.id.ll_complaint_wing).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_complaint_room_number).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_anonymity).setVisibility(View.GONE);
                    mError.setVisibility(View.GONE);
                } else if (category.equals("Mess") || category.equals("Other")) {
                    findViewById(R.id.ll_complaint_wing).setVisibility(View.GONE);
                    findViewById(R.id.ll_complaint_room_number).setVisibility(View.GONE);
                    findViewById(R.id.ll_anonymity).setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        anonymitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String anonymity = anonymity = anonymityArray[anonymitySpinner.getSelectedItemPosition()];
                if (anonymity.equals("Public")) {
                    mError.setVisibility(View.VISIBLE);
                    mError.setText("Your details are visible to public");
                } else if (anonymity.equals("Only admin")) {
                    mError.setVisibility(View.VISIBLE);
                    mError.setText("Your details are visible only to admin");
                } else if (anonymity.equals("No one")) {
                    mError.setVisibility(View.VISIBLE);
                    mError.setText("Your details are not visible");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


    }

    public void onClickChooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        findViewById(R.id.tv_image).setVisibility(View.VISIBLE);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView complainImage = findViewById(R.id.div_complain_image);
            complainImage.setImageURI(imageUri);
        } else {
            Log.d("123hello", "upload failure");
        }
    }


    public void backPressed(View view) {
        onBackPressed();
    }

    public void openWingMap(View view) {
        startActivity(new Intent(ComplainFormActivity.this, WingMapActivity.class));
    }

    public void onSubmitComplaint(View view) {
        String userId = sharedPreferences.getString("userId","loggedOut");
        if(userId.equals("loggedOut"))
            Toast.makeText(getApplicationContext(),"please login to continue", Toast.LENGTH_SHORT).show();
        else {
            Complain complain = new Complain();
            String category = categoryArray[categorySpinner.getSelectedItemPosition()];
            String anonymity;
            String description = String.valueOf(mDescription.getText());
            String wing;
            String room = floorArray[floorSpinner.getSelectedItemPosition()] + "-" + roomNumberArray[roomNumberSpinner.getSelectedItemPosition()];

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if(categorySpinner.getSelectedItemPosition() == 0) {
                    findViewById(R.id.ll_category_input).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                    return;
                } else
                    findViewById(R.id.ll_category_input).setBackground(getDrawable(R.drawable.background_rounded_section_task));
                if (!category.equals("Maintenance")) {
                    if (anonymitySpinner.getSelectedItemPosition() == 0) {
                        findViewById(R.id.ll_anonymity_spinner).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                        return;
                    } else {
                        findViewById(R.id.ll_anonymity_spinner).setBackground(getDrawable(R.drawable.background_rounded_section_task));
                        anonymity = anonymityArray[anonymitySpinner.getSelectedItemPosition()];
                    }
                }
                if (description.equals("")) {
                    findViewById(R.id.et_complain_description).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                    return;
                } else
                    mDescription.setBackground(getDrawable(R.drawable.background_rounded_section_task));
                if(category.equals("Maintenance")) {
                    if (wingSpinner.getSelectedItemPosition() == 0) {
                        findViewById(R.id.ll_complaint_wing_spinner).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                        return;
                    } else {
                        wing = wingArray[wingSpinner.getSelectedItemPosition()];
                        findViewById(R.id.ll_complaint_wing_spinner).setBackground(getDrawable(R.drawable.background_rounded_section_task));
                    }
                }
            }

            complain.setUserId(userEntryNumber);
            complain.setStatus(Status.Fired);
//            complain.setCategory(Category.valueOf(category)); make enum
            complain.setDescription(description);
//            complain.setTimestamp(??);
//            complain.setWing(wing); make enum
//            complain.setRoomNumber(room);
//            complain.setAnonymity(anonymity); //make enum


            new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Please confirm")
                    .setMessage("Are you sure you want to submit")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO image may or may not be sent
                            //TODO which category complaint to sent
                            if (imageUri == null || String.valueOf(imageUri).equals("")) {
                                //firebase complain form without
                                return;
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}