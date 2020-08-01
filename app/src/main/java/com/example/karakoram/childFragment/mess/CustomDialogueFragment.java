package com.example.karakoram.childFragment.mess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.karakoram.R;
import com.example.karakoram.cache.MessMenuCache;
import com.example.karakoram.resource.User;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class CustomDialogueFragment extends DialogFragment {

    private ArrayList<String> buttonOrder = new ArrayList<>(Arrays.asList("breakFast", "lunch", "dinner"));
    private ArrayList<String> dayList = new ArrayList<>();
    private  ArrayList<String[]> menuList = new ArrayList<>();
    private View view;
    private Context context;
    private SharedPreferences sharedPreferences;
    private MessMenuCache cache;
    private SimpleRatingBar simpleRatingBar;
    private TextView mStarText, mOk, mReview;
    private int rating, hour, minute;
    private Button[] buttonArray;
    private String day, meal, menu;
    private String[] buttonState = {"", "", ""};

    public CustomDialogueFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_custom_dialogue, container, false);
//        context = container.getContext();
//        initVariables();
//        initViews();
//        setVariables();
//        setViews();
//        setListeners();
        return view;
    }

    private void initVariables() {
        sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        cache = new MessMenuCache(context);
    }

    private void initViews() {
        mStarText = view.findViewById(R.id.tv_star_text);
        Button mBreakfast = view.findViewById(R.id.button_dialogue_breakfast);
        Button mDinner = view.findViewById(R.id.button_dialogue_dinner);
        Button mLunch = view.findViewById(R.id.button_dialogue_lunch);
        buttonArray = new Button[]{mBreakfast, mLunch, mDinner};
        mOk = view.findViewById(R.id.tv_dialogue_ok);
        mReview = view.findViewById(R.id.tv_dialogue_review);
        mStarText = view.findViewById(R.id.tv_star_text);
    }

    private void setVariables() {
        setCalender();
        setTodayMealAndMenu();
        setSharedPref();
    }

    private void setViews() {
        setButtons();
        setRatingBar();
    }

    private void setListeners() {
        for (final Button button: buttonArray) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    meal = button.getText().toString();
                    for (int i = 0; i < 3; i++) {
                        if(!(buttonState[i].equals("gone"))) {
                            makeButtonUnselected(buttonArray[i]);
                        }
                    }
                    makeButtonSelected(button);
                }
            });
        }

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 3; i++) {
                    if (!buttonState[i].equals("gone")) {
                        if (buttonState[i].equals("selected")) {
                            makeButtonUnClick(buttonArray[i]);
                            // make firebase call
                        }
                    }
                }
            }
        });

        mReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 3; i++) {
                    if (!buttonState[i].equals("gone")) {
                        if (buttonState[i].equals("selected")) {
                            makeButtonUnClick(buttonArray[i]);
                            menu = menuList.get(dayList.indexOf(day))[buttonOrder.indexOf(meal)];
                            // send {menu, meal, rating} to review fragment
                        }
                    }
                }
            }
        });
    }

    /* VARIABLES HELPER ------ START */

    private void setCalender() {
        Calendar calendar = Calendar.getInstance();
        day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    private void setTodayMealAndMenu() {
        dayList = cache.getDayArray();
        menuList = cache.getMenuArray();
        if (hour >= 7 && hour < 12) {
            meal = "breakfast";
            if (hour == 7 && minute < 15)
                meal = "dinner";
        } else if (hour >= 12 && hour < 19) {
            meal = "lunch";
        } else {
            meal = "dinner";
        }
    }

    private void setSharedPref() {
        if ((hour >= 0 && hour < 7) || (hour == 7 && minute < 15)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("isBreakfastFeedback", "false");
            editor.putString("isLunchFeedback", "false");
            editor.putString("isDinnerFeedback", "false");
            editor.apply();
        }
    }

    /* VARIABLES HELPER ------ END*/

    /* VIEWS HELPER ------ START*/

    private void setButtons() {
        Boolean[] isFeedbackComplete = {
                Boolean.getBoolean(sharedPreferences.getString("isBreakfastFeedback", "true")),
                Boolean.getBoolean(sharedPreferences.getString("isLunchFeedback", "true")),
                Boolean.getBoolean(sharedPreferences.getString("isDinnerFeedback", "true"))
        };
        for (int i = 0; i < 3; i++) {
            Button button = buttonArray[i];
            if (isTimeValid(button)) {
                if (isFeedbackComplete[i])
                    makeButtonUnClick(button);
                else
                    makeButtonUnselected(button);
            }
            else
                makeButtonGone(button);
            if (button.getText().toString().equals(meal) && !isFeedbackComplete[i])
                makeButtonSelected(button);
        }


    }

    private Boolean isTimeValid(Button button) {
        Boolean isTrue = false;
        //not between 00:00 and 06:59
        if (!(hour >= 0 && hour <= 6)) {
            // not between 07:00 and 07:15
            if (!(hour == 7 && minute < 15)) {
                String s = button.getText().toString();
                // breakfast
                if (hour <= 23)
                    isTrue = s.equals("breakfast");
                if (hour >= 12 && hour <= 23)
                    isTrue = s.equals("lunch");
                if (hour >= 19 && hour <= 23)
                    isTrue = s.equals("dinner");
            }
        }
        return isTrue;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setRatingBar() {
        simpleRatingBar = view.findViewById(R.id.mess_feedback_rating_bar);
        SimpleRatingBar.AnimationBuilder builder = simpleRatingBar.getAnimationBuilder()
                .setDuration(1000)
                .setInterpolator(new BounceInterpolator());
        builder.start();
        simpleRatingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                rating = (int) simpleRatingBar.getRating();
                if (rating == 1) {
                    mStarText.setText("Bad!");
                    mStarText.setVisibility(View.VISIBLE);
                } else if (rating == 2) {
                    mStarText.setText("Average");
                    mStarText.setVisibility(View.VISIBLE);
                } else if (rating == 3) {
                    mStarText.setText("Great!");
                    mStarText.setVisibility(View.VISIBLE);
                } else {
                    mStarText.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    /* VIEWS HELPER ------ START*/


    private void makeButtonSelected(Button button) {
        button.setVisibility(View.VISIBLE);
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.meal_feedback_button_selected));
        buttonState[buttonOrder.indexOf(button.getText().toString())] = "selected";
    }

    private void makeButtonUnselected(Button button) {
        button.setVisibility(View.VISIBLE);
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.meal_feedback_button_unselected));
        buttonState[buttonOrder.indexOf(button.getText().toString())] = "unSelected";
    }

    private void makeButtonUnClick(Button button) {
        button.setVisibility(View.VISIBLE);
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.meal_feedback_button_unclick));
        buttonState[buttonOrder.indexOf(button.getText().toString())] = "unClick";
    }

    private void makeButtonGone(Button button) {
        button.setVisibility(View.GONE);
        buttonState[buttonOrder.indexOf(button.getText().toString())] = "gone";
    }
}