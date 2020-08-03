package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class BillDescriptionActivity extends AppCompatActivity {

    /* Variables */
    private String category;
    private String time;
    private String date;
    private String amount;
    private String userId;
    private String description;
    private String key;

    /*Views*/
    private TextView mCategory;
    private TextView mDateTime;
    private TextView mAmount;
    private TextView mUserId;
    private TextView mDescription;

    private Button mButtonImage;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_bill_description);
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
        amount = getIntent().getExtras().getString("amount");
        userId = getIntent().getExtras().getString("userId");
        description = getIntent().getExtras().getString("description");
        key = getIntent().getExtras().getString("key");
    }

    private void initViews() {
        mCategory = (TextView) findViewById(R.id.tv_bill_description_category);
        mDateTime = (TextView) findViewById(R.id.tv_bill_time_date);
        mAmount = (TextView) findViewById(R.id.tv_bill_description_amount);
        mUserId = (TextView) findViewById(R.id.tv_bill_description_user_id);
        mDescription = (TextView) findViewById(R.id.tv_bill_description);

        mButtonImage = findViewById(R.id.button_bill_image);
        mBack = (ImageView) findViewById(R.id.iv_back_button);
    }

    private void setViews() {
        mCategory.setText(category + " bill");
        mDateTime.setText(time + ", " + date);
        mAmount.setText("Rs. " + amount);
        mUserId.setText("Uploaded by " + userId);
//        mDescription.setText(description);
        mDescription.setText("Let's create a function that will accept two parameters and will return the month of the given date. The first parameter will be the date and the second parameter will accept a boolean value which will be true or false. This boolean value will determine if the return month name wants to be shortened or not. If the value is set to true it will return full month name otherwise it will return an abbreviation of the first 3 characters of the month name. Here is the full javascript function code.");

        mButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillDescriptionActivity.this, BillImageActivity.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}