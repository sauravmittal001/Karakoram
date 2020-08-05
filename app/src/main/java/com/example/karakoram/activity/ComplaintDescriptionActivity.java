package com.example.karakoram.activity;

import androidx.annotation.NonNull;
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
import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.Complaint;
import com.example.karakoram.resource.MaintComplaint;
import com.example.karakoram.resource.MessComplaint;
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

import static android.view.View.GONE;

public class ComplaintDescriptionActivity extends AppCompatActivity {

    /* Variables */
    private String category;
    private String time;
    private String date;
    private String status;
    private String initialStatus;
    private String area;
    private String description;
    private String entryNumber;
    private String name;
    private String room;
    private String wing;
    private String dbImageLocation;
    private UserType userType;
    private String key;
    private String complaintArea;
    private boolean isImageAttached;

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
    private ImageView mEdit;
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
        initVariables();
        initViews();
        setViews();
    }

    private void initVariables() {
        category = getIntent().getExtras().getString("category");
        time = getIntent().getExtras().getString("time");
        date = getIntent().getExtras().getString("date");
        initialStatus = getIntent().getExtras().getString("status");
        description = getIntent().getExtras().getString("description");
        entryNumber = getIntent().getExtras().getString("entryNumber");
        name = getIntent().getExtras().getString("name");
        isImageAttached = getIntent().getExtras().getBoolean("isImageAttached");

        if (category.equals(Category.Maintenance.name()) || category.equals(Category.Mess.name())) {
            area = getIntent().getExtras().getString("area");
            complaintArea = getIntent().getExtras().getString("complaintArea");
            if (category.equals(Category.Maintenance.name())) {
                room = getIntent().getExtras().getString("room");
                wing = getIntent().getExtras().getString("wing");
            }
        }

        key = getIntent().getExtras().getString("key");
        dbImageLocation = "complaintImages/" + key + ".png";
        Log.i("CRASHHH", dbImageLocation);
        userType = UserType.valueOf(getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE).getString("type", "Student"));
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
                mRoom.setVisibility(GONE);
            }
        } else {
            mArea.setVisibility(GONE);
        }

        mBack = (ImageView) findViewById(R.id.iv_back_button);
        mImage = (DynamicImageView) findViewById(R.id.div_complaint_description_image);
        mEdit = findViewById(R.id.iv_edit_button);
    }

    private void setViews() {
        mCategory.setText(category + " complaint");
        mDateTime.setText(time + ", " + date);
        mDescription.setText(description);
        mEntryNumber.setText(entryNumber);
        mName.setText(name);
        if (category.equals(Category.Maintenance.name()) || category.equals(Category.Mess.name())) {
            mArea.setText("Subject: " + area);
            if (category.equals(Category.Maintenance.name())) {
                mRoom.setText(room + ", " + wing + " wing");
            }
        }

        hideViewsUserType();
        // ImageView
        if(isImageAttached) {
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

        if(userType.equals(UserType.Admin)){
            mEdit.setVisibility(View.GONE);
        }
        else
            mEdit.setVisibility(View.VISIBLE);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {

        if(userType.equals(UserType.Admin)) {
            if (statusSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(ComplaintDescriptionActivity.this, "Select status", Toast.LENGTH_SHORT).show();
                return;
            } else if (initialStatus.equals(status)) {
                finish();
            } else {
                new AlertDialog.Builder(ComplaintDescriptionActivity.this, R.style.MyDialogTheme)
                        .setTitle("Please confirm")
                        .setMessage("Are you sure you want to change the status of this complaint to " + status + " ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseQuery.changeComplaintStatus(key, Status.valueOf(status));
                                Toast.makeText(ComplaintDescriptionActivity.this, "status updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        else
            finish();
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
        if (userType.equals(UserType.Admin)) {
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
            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    status = statusArray[i];
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            statusSpinner.setAdapter(statusAdapter);
            if (initialStatus == null) {
                Log.i("CRASHHH", "status");
            }

            if (initialStatus != null) {
                int spinnerPosition = statusAdapter.getPosition(initialStatus);
                statusSpinner.setSelection(spinnerPosition);
            }
        } else {
            findViewById(R.id.ll_complaint_status).setVisibility(View.GONE);
            mStatus.setText("Status : " + initialStatus);
        }
    }

    public void onClickOpenEditForm(View view) {
        Intent intent = new Intent(this, ComplaintFormActivity.class);
        intent.putExtra("editMode",true);
        intent.putExtra("key",key);
        intent.putExtra("description",description);
        intent.putExtra("isImageAttached",isImageAttached);
        intent.putExtra("category",category);
        if(category.equals("Mess")){
            intent.putExtra("messArea",complaintArea);
        }
        else if(category.equals("Maintenance")) {
            intent.putExtra("maintenanceArea", complaintArea);
            intent.putExtra("wing", wing);
            intent.putExtra("room", room);
        }
        startActivity(intent);
    }

}