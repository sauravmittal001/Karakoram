package com.example.karakoram.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.R;

import java.util.Objects;

public class ustDescription extends AppCompatActivity {

    /* Variables */
    private String meal;
    private String date;
    private String description;
    private int rating;
    private String userName;

    /*Views*/
    private TextView mMeal;
    private TextView mDate;
    private TextView mDescription;
    private RatingBar mRating;
    private TextView mUserid;
    private Button mButtonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ust_description);
        initVariables();
        initViews();
        setViews();
    }

    private void initVariables() {
        meal = Objects.requireNonNull(getIntent().getExtras()).getString("Meal");
        date = getIntent().getExtras().getString("date");
        rating=getIntent().getExtras().getInt("rating");
        userName = getIntent().getExtras().getString("userName");
        description = getIntent().getExtras().getString("description");
        mButtonDone = findViewById(R.id.button_event_description_done);

        String key = getIntent().getExtras().getString("key");

    }

    private void initViews() {


        mRating = findViewById(R.id.feedback_star);
        mDescription = findViewById(R.id.feedback_details);
        mMeal = findViewById(R.id.meal_type);
        mDate = findViewById(R.id.feedback_date);
        mUserid = findViewById(R.id.feedback_user_details);
    }

    private void setViews() {

        mMeal.setText(meal);
        mDate.setText(date);
        mDescription.setText(description);
        mUserid.setText(userName);
        mRating.setNumStars(3);
        mRating.setStepSize(1);
        mRating.setRating(rating);



        //Button
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}