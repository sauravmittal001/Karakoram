package com.example.karakoram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karakoram.R;
import com.example.karakoram.resource.User;

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
    private ImageView mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_bill_description);
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
        mEdit = findViewById(R.id.iv_edit_button);
    }

    private void setViews() {
        mCategory.setText(category + " bill");
        mDateTime.setText(time + ", " + date);
        mAmount.setText("Rs. " + amount);
        mUserId.setText("Uploaded by " + userId);
        mDescription.setText(description);

        mButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillDescriptionActivity.this, BillImageActivity.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillDescriptionActivity.this,BillFormActivity.class);
                intent.putExtra("amount",amount);
                intent.putExtra("description",description);
                intent.putExtra("category",category);
                intent.putExtra("editMode",true);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString("userId","loggedOut");
        if(currentUserId.equals(userId))
            mEdit.setVisibility(View.VISIBLE);
        else
            mEdit.setVisibility(View.GONE);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}