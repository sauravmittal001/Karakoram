package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.karakoram.DynamicImageView;
import com.example.karakoram.R;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.Status;
import com.example.karakoram.resource.User;
import com.example.karakoram.resource.UserType;
import com.example.karakoram.resource.Wing;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ComplaintDescriptionActivity extends AppCompatActivity {

    /* Variables */
    private String category;
    private String time;
    private String date;
    private String status;
    private String area;
    private String description;
    private String entryNumber;
    private String name;
    private String room;
    private String wing;
    private String dbImageLocation;
    private String userType;

    /*Views*/
    private TextView mCategory;
    private TextView mDateTime;
    private TextView mStatus;
    private TextView mArea;
    private TextView mDescription;
    private TextView mEntryNumber;
    private TextView mName;
    private TextView mRoom;

    private ImageView mImage;
    private Button mButtonSave;
    private ImageView mBack;
    private CustomSpinner statusSpinner;
    private String[] statusArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_complaint_description);
//        try {
//            initVariables();
//        } catch (Exception e) {
//            Log.i("CRASHHH1", String.valueOf(e));
//        }
//        try {
//            initViews();
//        } catch (Exception e) {
//            Log.i("CRASHHH2", String.valueOf(e));
//        }
//        try {
//            setViews();
//        } catch (Exception e) {
//            Log.i("CRASHHH3", String.valueOf(e));
//        }
        initVariables();
        initViews();
        setViews();
    }

    private void initVariables() {
        category = getIntent().getExtras().getString("category");
        time = getIntent().getExtras().getString("time");
        date = getIntent().getExtras().getString("date");
        status = getIntent().getExtras().getString("status");
        description = getIntent().getExtras().getString("description");
        entryNumber = getIntent().getExtras().getString("entryNumber");
        name = getIntent().getExtras().getString("name");

        if (category.equals(Category.Maintenance.name()) || category.equals(Category.Mess.name())) {
            area = getIntent().getExtras().getString("area");
            if (category.equals(Category.Maintenance.name())) {
                room = getIntent().getExtras().getString("room");
                wing = getIntent().getExtras().getString("wing");
            }
        }

        String key = getIntent().getExtras().getString("key");
        assert key != null;
        key = key.substring(1, key.length()-2);
        dbImageLocation = "complaintImages/" + key + ".png";
        Log.i("CRASHHH", dbImageLocation);
        userType = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE).getString("type", "Admin");
    }

    private void initViews() {
        mCategory = (TextView) findViewById(R.id.tv_complaint_description_category);
        mDateTime = (TextView) findViewById(R.id.tv_complaint_time_date);
        mStatus = (TextView) findViewById(R.id.tv_complaint_description_status);
        mDescription = (TextView) findViewById(R.id.tv_complaint_description);
        mEntryNumber = (TextView) findViewById(R.id.tv_complaint_description_entry_number);
        mName = (TextView) findViewById(R.id.tv_complaint_description_name);
        mArea = (TextView) findViewById(R.id.tv_complaint_description_area);
        mRoom = (TextView) findViewById(R.id.tv_complaint_description_room);
        if (category.equals(Category.Maintenance.name()) || category.equals(Category.Mess.name())) {


            mArea.setVisibility(View.VISIBLE);
            if (category.equals(Category.Maintenance.name())) {

                mRoom.setVisibility(View.VISIBLE);

            } else {
                mRoom.setVisibility(View.GONE);
            }
        } else {
            mArea.setVisibility(View.GONE);
        }

        mButtonSave = findViewById(R.id.button_complaint_submit);
        mBack = (ImageView) findViewById(R.id.iv_back_button);
        mImage = (DynamicImageView) findViewById(R.id.div_complaint_description_image);
    }

    private void setViews() {
        mCategory.setText(category + " complaint");
        mDateTime.setText(time + ", " + date);
        mDescription.setText(description);
//        mDescription.setText("Let's create a function that will accept two parameters and will return the month of the given date. The first parameter will be the date and the second parameter will accept a boolean value which will be true or false. This boolean value will determine if the return month name wants to be shortened or not. If the value is set to true it will return full month name otherwise it will return an abbreviation of the first 3 characters of the month name. Here is the full javascript function code.");
        mEntryNumber.setText(entryNumber);
        mName.setText(name);
        if (category.equals(Category.Maintenance.name()) || category.equals(Category.Mess.name())) {
            mArea.setText("Subject: " + area);
            if (category.equals(Category.Maintenance.name())) {
                mRoom.setText(room + ", " + wing + " wing");
            }
        }

        hideViewsUserType();

        // back button
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // ImageView
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child(dbImageLocation).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        loadGlideImage(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // image not loading
                    }
                 });
    }

    private void loadGlideImage(String url) {
        Log.i("CRASHHH", url); // not getting here
        RequestOptions requestOption = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter();
        Glide.with(ComplaintDescriptionActivity.this)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .into(mImage);
    }

    private void hideViewsUserType() {
        if (userType.equals(UserType.Admin.name())) {
            mStatus.setText("Status ");
            // status spinner
            findViewById(R.id.ll_complaint_status).setVisibility(View.VISIBLE);
            final Status[] statusList = Status.values();
            statusArray = new String[statusList.length+1];
            statusArray[0] = "";
            for(int i=0;i<statusList.length;i++)
                statusArray[i+1] = String.valueOf(statusList[i]);
            CustomSpinnerAdapter statusAdapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, statusArray);
            statusAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            statusSpinner = findViewById(R.id.spinner_complaint_status);
            statusSpinner.setAdapter(statusAdapter);
            if (status == null) {
                Log.i("CRASHHH", "status");
            }

            if (status != null) {
                int spinnerPosition = statusAdapter.getPosition(status);
                statusSpinner.setSelection(spinnerPosition);
            }
            // save button
            mButtonSave.setVisibility(View.VISIBLE);
            mButtonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (statusSpinner.getSelectedItemPosition() == 0) {
                        Toast.makeText(ComplaintDescriptionActivity.this, "Select status", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (statusArray[statusSpinner.getSelectedItemPosition()].equals(status)) {
                        finish();
                    } else {
                        //TODO make firebase call
                        finish();
                    }
                }
            });
        } else {
            mButtonSave.setVisibility(View.GONE);
            findViewById(R.id.ll_complaint_status).setVisibility(View.VISIBLE);
            mStatus.setText("Status : " + status);
        }
    }

}