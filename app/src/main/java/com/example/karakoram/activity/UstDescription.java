package com.example.karakoram.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.R;
import com.example.karakoram.resource.Anonymity;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.Date;
import java.util.Objects;

public class UstDescription extends AppCompatActivity {

    /* Variables */
    private String key; //only passing to edit form
    private String userName;
    private String description;
    private String meal;
    private String time;
    private String date;
    private String day;
    private int rating;
    private Anonymity anonymity;

    private TextView mUserId;
    private TextView mDescription;
    private TextView mMealDay;
    private TextView mDateTime;
    private TextView mStarText;
    private SimpleRatingBar simpleRatingBar;

    private ImageView mBack;
    private ImageView mEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ust_description);
        initVariables();
        initViews();
        setViews();
    }

    public String monthName (int monthNumber) {
        String[] monthOfYear = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthOfYear[monthNumber-1];
    }

    private void initVariables() {
        key = getIntent().getExtras().getString("key");
        userName = getIntent().getExtras().getString("userName");
        description = getIntent().getExtras().getString("description");
        meal = getIntent().getExtras().getString("meal");
        time = getIntent().getExtras().getString("time");
        date = getIntent().getExtras().getString("date");
        day = getIntent().getExtras().getString("day");
        rating = Integer.parseInt(getIntent().getExtras().getString("rating"));
        anonymity = Anonymity.valueOf(getIntent().getExtras().getString("anonymity"));
    }

    private void initViews() {
        mUserId = findViewById(R.id.tv_mess_feedback_user_id);
        mDescription = findViewById(R.id.tv_mess_feedback_description);
        mMealDay = findViewById(R.id.tv_mess_feedback_day_meal);
        mDateTime = findViewById(R.id.tv_mess_feedback_time_date);
        mStarText = findViewById(R.id.tv_star_text_description);

        simpleRatingBar = findViewById(R.id.srb_mess_feedback_description);
        mBack = findViewById(R.id.iv_back_button);
        mEdit = findViewById(R.id.iv_edit_button);
    }

    private void setViews() {
        mUserId.setText("Feedback by " + userName);
        mDescription.setText(description);
        mMealDay.setText(day + " " + meal);
        mDateTime.setText(time + ", " + date);
        if (rating == 1) {
            mStarText.setText("Bad!");
        } else if (rating == 2) {
            mStarText.setText("Average");
        } else if (rating == 3) {
            mStarText.setText("Good");
        }

        simpleRatingBar.setRating((float) rating);
        simpleRatingBar.setIndicator(true);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UstDescription.this,MainActivity.class);
                intent.putExtra("editMode",true);
                intent.putExtra("rating",rating);
                intent.putExtra("description",description);
                intent.putExtra("anonymity",anonymity.toString());
                intent.putExtra("meal",meal);
                intent.putExtra("day",day);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });
        Date dateTime = new Date();
        String dateNow = (dateTime.getYear() + 1900) + " " + monthName(dateTime.getMonth() + 1) + " " + String.format("%02d",dateTime.getDate());
        if(dateNow.equals(date))
            mEdit.setVisibility(View.VISIBLE);
        else
            mEdit.setVisibility(View.GONE);
    }
}