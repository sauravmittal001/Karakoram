package com.example.karakoram.activity;

import androidx.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.Complaint;
import com.example.karakoram.resource.ComplaintArea;
import com.example.karakoram.resource.MaintComplaint;
import com.example.karakoram.resource.MessComplaint;
import com.example.karakoram.resource.Status;
import com.example.karakoram.resource.User;
import com.example.karakoram.resource.Wing;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class ComplaintFormActivity extends AppCompatActivity {

    private CustomSpinner categorySpinner, floorSpinner, roomNumberSpinner, wingSpinner, maintenanceAreaSpinner, messAreaSpinner;
    private String[] categoryArray, floorArray, roomNumberArray, wingArray, maintenanceAreaArrayEnums, messAreaArrayEnums;
    private Uri imageUri;
    private EditText mDescription;
    private String userRoomNumber, userFloor;
    private SharedPreferences sharedPreferences;
    private boolean isImageAttached = false;
    private boolean editMode;
    private Intent intent;
    private ImageView mImage;
    private ImageView mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_complaint_form);
        setVariables();
        setViews();
    }

    private void setVariables() {
        sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
        String room = sharedPreferences.getString("room", "A01");
        userRoomNumber = room.substring(1);
        userFloor = room.substring(0,1);
        maintenanceAreaArrayEnums = getResources().getStringArray(R.array.maintenance_area_enums);
        messAreaArrayEnums = getResources().getStringArray(R.array.mess_area_enums);
        intent = getIntent();
        editMode = intent.getBooleanExtra("editMode",false);
        mImage = findViewById(R.id.div_complaint_image);
        mDelete = findViewById(R.id.iv_delete);
    }

    private void setViews() {
        mDescription = findViewById(R.id.et_complaint_description);
        if(editMode){
            String description = intent.getStringExtra("description");
            mDescription.setText(description);
        }
        setSpinners();
        isImageAttached = intent.getBooleanExtra("isImageAttached",false);
        String key = intent.getStringExtra("key");
        String dbImageLocation = "complaintImages/" + key + ".png";
        if(isImageAttached && editMode) {
            StorageReference ref = FirebaseStorage.getInstance().getReference();
            ref.child(dbImageLocation).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUri = uri;
                            loadGlideImage(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // image not loading
                }
            });
            ((TextView)findViewById(R.id.tv_image)).setText("Selected Image:");
            mDelete.setVisibility(View.VISIBLE);
        }

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)findViewById(R.id.tv_image)).setText("No Image Selected");
                mDelete.setVisibility(View.GONE);
                mImage.setVisibility(View.GONE);
                isImageAttached = false;
            }
        });
    }

    private void setSpinners() {

        if(editMode){
            String category = intent.getStringExtra("category");
            categoryArray = new String[] {category};
        }
        else
            categoryArray = getResources().getStringArray(R.array.complaint_category);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, categoryArray);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        categorySpinner = findViewById(R.id.spinner_complaint_category);
        categorySpinner.setAdapter(adapter);

        Wing[] wings = Wing.values();
        wingArray = new String[wings.length+1];
        wingArray[0] = "";
        for(int i=0;i<wings.length;i++)
            wingArray[i+1] = String.valueOf(wings[i]);
        CustomSpinnerAdapter wingAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, wingArray);
        wingAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        wingSpinner = findViewById(R.id.spinner_complaint_wing);
        wingSpinner.setAdapter(wingAdapter);

        floorArray = getResources().getStringArray(R.array.floor);
        CustomSpinnerAdapter floorAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, floorArray);
        floorAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        floorSpinner = findViewById(R.id.spinner_complaint_floor);
        floorSpinner.setAdapter(floorAdapter);
        if (userFloor != null) {
            int spinnerPosition = floorAdapter.getPosition(userFloor);
            floorSpinner.setSelection(spinnerPosition);
        }

        roomNumberArray = getResources().getStringArray(R.array.room_number);
        CustomSpinnerAdapter roomNumberAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, roomNumberArray);
        roomNumberAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        roomNumberSpinner = findViewById(R.id.spinner_complaint_room_number);
        roomNumberSpinner.setAdapter(roomNumberAdapter);
        if (userRoomNumber != null) {
            int spinnerPosition = roomNumberAdapter.getPosition(userRoomNumber);
            roomNumberSpinner.setSelection(spinnerPosition);
        }

        String[] maintenanceAreaArray = getResources().getStringArray(R.array.maintenance_area);
        CustomSpinnerAdapter maintenanceAreaAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, maintenanceAreaArray);
        maintenanceAreaAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        maintenanceAreaSpinner = findViewById(R.id.spinner_complaint_maintenance_area);
        maintenanceAreaSpinner.setAdapter(maintenanceAreaAdapter);

        String[] messAreaArray = getResources().getStringArray(R.array.mess_area);
        CustomSpinnerAdapter messAreaAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, messAreaArray);
        messAreaAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        messAreaSpinner = findViewById(R.id.spinner_complaint_mess_area);
        messAreaSpinner.setAdapter(messAreaAdapter);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String category = categoryArray[categorySpinner.getSelectedItemPosition()];
                if (category.equals("Maintenance")) {
                    findViewById(R.id.ll_complaint_wing).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_complaint_room_number).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_complaint_maintenance_area).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_complaint_mess_area).setVisibility(View.GONE);
                } else if (category.equals("Mess")) {
                    findViewById(R.id.ll_complaint_wing).setVisibility(View.GONE);
                    findViewById(R.id.ll_complaint_room_number).setVisibility(View.GONE);
                    findViewById(R.id.ll_complaint_maintenance_area).setVisibility(View.GONE);
                    findViewById(R.id.ll_complaint_mess_area).setVisibility(View.VISIBLE);
                } else if (category.equals("Others")) {
                    findViewById(R.id.ll_complaint_wing).setVisibility(View.GONE);
                    findViewById(R.id.ll_complaint_room_number).setVisibility(View.GONE);
                    findViewById(R.id.ll_complaint_maintenance_area).setVisibility(View.GONE);
                    findViewById(R.id.ll_complaint_mess_area).setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        if(editMode){
            String category = intent.getStringExtra("category");
            if(category.equals("Mess")){
                String messArea = intent.getStringExtra("messArea");
                int position = ArrayUtils.toArrayList(messAreaArrayEnums).indexOf(messArea);
                messAreaSpinner.setSelection(position);
            }
            else if(category.equals("Maintenance")){
                String wing = intent.getStringExtra("wing");
                int position = ArrayUtils.toArrayList(wingArray).indexOf(wing);
                wingSpinner.setSelection(position);

                String room = intent.getStringExtra("room");

                Log.d("123hello",room);

                String roomNumber = room.substring(1);
                position = ArrayUtils.toArrayList(roomNumberArray).indexOf(roomNumber);
                roomNumberSpinner.setSelection(position);

                String floor = room.substring(0,1);
                position = ArrayUtils.toArrayList(floorArray).indexOf(floor);
                floorSpinner.setSelection(position);

                String maintenanceArea = intent.getStringExtra("maintenanceArea");
                position = ArrayUtils.toArrayList(maintenanceAreaArrayEnums).indexOf(maintenanceArea);
                maintenanceAreaSpinner.setSelection(position);
            }
        }
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
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //TODO: unchecked code
            ((TextView)findViewById(R.id.tv_image)).setText("Selected Image:");
            mDelete.setVisibility(View.VISIBLE);
            imageUri = data.getData();
            mImage.setImageURI(imageUri);
            isImageAttached = true;
        }
        else {
            ((TextView)findViewById(R.id.tv_image)).setText("No Image Selected");
            mDelete.setVisibility(View.GONE);
            mImage.setVisibility(View.GONE);
            isImageAttached = false;
            Log.d("123hello", "upload failure");
        }
    }


    public void backPressed(View view) {
        onBackPressed();
    }

    public void openWingMap(View view) {
        startActivity(new Intent(ComplaintFormActivity.this, WingMapActivity.class));
    }

    private void loadGlideImage(String url) {
        Log.i("CRASHHH", url); // not getting here
        RequestOptions requestOption = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter();
        Glide.with(ComplaintFormActivity.this)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .into(mImage);
    }

    public void onSubmitComplaint(View view) {
        String userId = sharedPreferences.getString("userId","loggedOut");
        if(userId.equals("loggedOut"))
            Toast.makeText(getApplicationContext(),"please login to continue", Toast.LENGTH_SHORT).show();
        else {
            Complaint complaint = null;
            String category = categoryArray[categorySpinner.getSelectedItemPosition()];
            ComplaintArea complaintArea;
            String description = String.valueOf(mDescription.getText());
            Wing wing;
            String room = floorArray[floorSpinner.getSelectedItemPosition()] + roomNumberArray[roomNumberSpinner.getSelectedItemPosition()];

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                if(!editMode) {
                    if (categorySpinner.getSelectedItemPosition() == 0) {
                        findViewById(R.id.ll_category_input).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                        return;
                    } else
                        findViewById(R.id.ll_category_input).setBackground(getDrawable(R.drawable.background_rounded_section_task));
                }

                if (category.equals("Maintenance")) {

                    if (maintenanceAreaSpinner.getSelectedItemPosition() == 0) {
                        findViewById(R.id.ll_complaint_maintenance_area_input).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                        return;
                    }
                    else {
                        findViewById(R.id.ll_complaint_maintenance_area_input).setBackground(getDrawable(R.drawable.background_rounded_section_task));
                        complaintArea = ComplaintArea.valueOf(maintenanceAreaArrayEnums[maintenanceAreaSpinner.getSelectedItemPosition()]);
                    }

                    if (wingSpinner.getSelectedItemPosition() == 0) {
                        findViewById(R.id.ll_complaint_wing_spinner).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                        return;
                    }
                    else {
                        wing = Wing.valueOf(wingArray[wingSpinner.getSelectedItemPosition()]);
                        findViewById(R.id.ll_complaint_wing_spinner).setBackground(getDrawable(R.drawable.background_rounded_section_task));
                    }

                    complaint = new MaintComplaint(sharedPreferences);
                    ((MaintComplaint)complaint).setComplaintArea(complaintArea);
                    ((MaintComplaint)complaint).setRoom(room);
                    ((MaintComplaint)complaint).setWing(wing);
                }
                else if (category.equals("Mess")) {

                    if (messAreaSpinner.getSelectedItemPosition() == 0) {
                        findViewById(R.id.ll_complaint_mess_area_input).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                        return;
                    }
                    else {
                        findViewById(R.id.ll_complaint_mess_area_input).setBackground(getDrawable(R.drawable.background_rounded_section_task));
                        complaintArea = ComplaintArea.valueOf(messAreaArrayEnums[messAreaSpinner.getSelectedItemPosition()]);
                    }

                    complaint = new MessComplaint(sharedPreferences);
                    ((MessComplaint)complaint).setComplaintArea(complaintArea);
                }
                else{
                    complaint = new Complaint(sharedPreferences);
                }
                if (description.equals("")) {
                    findViewById(R.id.et_complaint_description).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                    return;
                }
                else
                    mDescription.setBackground(getDrawable(R.drawable.background_rounded_section_task));
            }

            assert complaint != null;
            complaint.setDescription(description);
            complaint.setTimestamp(new Date());
            complaint.setIsImageAttached(isImageAttached);


            final Complaint finalComplaint = complaint;
            new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Please confirm")
                    .setMessage(editMode ? "Are you sure you want to make the changes ?" : isImageAttached ? "Are you sure you want to submit ?" : "Are you sure you want to submit without an image 3?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO image may or may not be sent
                            //TODO which category complaint to sent
                            if (!isImageAttached || imageUri == null || String.valueOf(imageUri).equals("")) {
                                if(editMode){
                                    boolean prevIsImageAttached = intent.getBooleanExtra("isImageAttached",false);
                                    String key = intent.getStringExtra("key");
                                    FirebaseQuery.updateComplaint(key,finalComplaint);
                                    if(prevIsImageAttached)
                                        FirebaseQuery.removeComplaintImage(key);
                                }
                                else
                                    FirebaseQuery.addCompliant(finalComplaint);
                            }
                            else {
                                if(editMode){
                                    String key = intent.getStringExtra("key");
                                    FirebaseQuery.updateComplaint(key,finalComplaint,imageUri);
                                }
                                else
                                    FirebaseQuery.addCompliant(finalComplaint, imageUri);
                            }

                            if(editMode)
                                Toast.makeText(getApplicationContext(),"complain updated", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(),"changes registered", Toast.LENGTH_SHORT).show();

                            ComplaintFormActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}