package com.example.karakoram.childFragment.mess;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.karakoram.R;
import com.example.karakoram.activity.ComplainFormActivity;
import com.example.karakoram.cache.MessMenuCache;
import com.example.karakoram.resource.Meal;
import com.example.karakoram.resource.MessFeedback;
import com.example.karakoram.resource.User;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class messFeedbackFragment extends Fragment {

    private View view;
    private Context context;
    private MessMenuCache cache;
    private RatingBar ratingBar;
    private String anonymity = "I want it to be completely anonymous", userId, userName, meal, menu;
    private int rating;
    private TextView mStarText;
    private EditText mDescription;

    public messFeedbackFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mess_feedback, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new CustomDialogueFragment();
        dialogFragment.show(ft, "dialog");
//        SimpleRatingBar myRatingBar = view.findViewById(R.id.aaaa);
//        SimpleRatingBar.AnimationBuilder builder = myRatingBar.getAnimationBuilder()
//                .setDuration(1000)
//                .setInterpolator(new BounceInterpolator());
//        builder.start();
//        setVariables();
//        setViews();
    }

    private void setVariables() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        userId = sharedPreferences.getString("entryNumber", "DEFAULT");
        userName = sharedPreferences.getString("userName", "DEFAULT");
        cache = new MessMenuCache(context);
        setTodayMealAndMenu();
    }

    private void setViews() {
        mDescription = view.findViewById(R.id.et_feedback_description);
//        mStarText = view.findViewById(R.id.tv_star_text);
        TextView mMenu = view.findViewById(R.id.tv_feedback_meal_menu);
        mMenu.setText(menu);

        Button mSubmit = view.findViewById(R.id.button_feedback_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        TextView mComplaint = view.findViewById(R.id.tv_feedback_complaint);
        mComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ComplainFormActivity.class));
            }
        });

        setSpinners();
    }



    private void setSpinners() {
        String[] anonymityArray = getResources().getStringArray(R.array.feedback_anonymity);
        CustomSpinnerAdapter anonymityAdapter = new CustomSpinnerAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, anonymityArray);
        anonymityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        CustomSpinner anonymitySpinner = view.findViewById(R.id.spinner_feedback_anonymity);
        anonymitySpinner.setAdapter(anonymityAdapter);
        if (anonymity != null) {
            int spinnerPosition = anonymityAdapter.getPosition(anonymity);
            anonymitySpinner.setSelection(spinnerPosition);
        }

        String[] mealArray = getResources().getStringArray(R.array.meal_time);
        CustomSpinnerAdapter mealAdapter = new CustomSpinnerAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, mealArray);
        mealAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        CustomSpinner mealSpinner = view.findViewById(R.id.spinner_feedback_meal);
        mealSpinner.setAdapter(mealAdapter);
        if (meal != null) {
            int spinnerPosition = mealAdapter.getPosition(meal);
            mealSpinner.setSelection(spinnerPosition);
        }
    }

    private void setTodayMealAndMenu() {
        Calendar calendar = Calendar.getInstance();
        String dayNow = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
        int minNow = calendar.get(Calendar.MINUTE);
        ArrayList<String> dayList = cache.getDayArray();
        ArrayList<String[]> menuList = cache.getMenuArray();
        ArrayList<String> mealList = new ArrayList<>(Arrays.asList("breakFast", "lunch", "dinner"));

        if (hourNow >= 7 && hourNow < 12) {
            meal = "breakfast";
            if (hourNow == 7 && minNow < 15)
                meal = "dinner";
        } else if (hourNow >= 12 && hourNow < 19) {
            meal = "lunch";
        } else {
            meal = "dinner";
        }
        menu = menuList.get(dayList.indexOf(dayNow))[mealList.indexOf(meal)];
    }

    public void submit() {
        String description = mDescription.getText().toString();
        if (userId.equals("loggedOut"))
            Toast.makeText(getActivity().getApplicationContext(), "please login to continue", Toast.LENGTH_SHORT).show();
        else {
            if (!(rating == 1 || rating == 2 || rating == 3)) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    mStarText.setText("Please rate the food");
                    mStarText.setTextColor(Color.RED);
                    mStarText.setVisibility(View.VISIBLE);
                    return;
                }
            } else {
                mStarText.setTextColor(Color.BLACK);
                mStarText.setVisibility(View.INVISIBLE);
            }
        }
        MessFeedback messFeedback = new MessFeedback();
//        messFeedback.setUserId(userId);
//        messFeedback.setDescription(description);
//        messFeedback.setRating(rating);
//        messFeedback.setMeal(Meal.valueOf(meal));
//        messFeedback.setTimestamp(??);
//        messFeedback.setName(userName);
//        messFeedback.setAnonymity(anonymity);
    }

}