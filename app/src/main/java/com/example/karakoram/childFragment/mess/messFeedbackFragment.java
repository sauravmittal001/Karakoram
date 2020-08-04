package com.example.karakoram.childFragment.mess;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.activity.ComplaintFormActivity;
import com.example.karakoram.cache.mess.MessMenuCache;
import com.example.karakoram.resource.Anonymity;
import com.example.karakoram.resource.Meal;
import com.example.karakoram.resource.Menu;
import com.example.karakoram.resource.MessFeedback;
import com.example.karakoram.resource.User;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class messFeedbackFragment extends Fragment {

    private View view;
    private Context context;

    //views
    private CustomSpinner mealSpinner, anonymitySpinner;
    private SimpleRatingBar simpleRatingBar;
    private EditText mDescription;
    private TextView mMenu;
    //textview to set menu
    //button to submit, file complain

    private String currentMeal, selectedMeal;
    private ArrayList<String> allMealsOfToday;
    private ArrayList<String> eligibleMeals;
    private Anonymity anonymity;
    private int rating;
    private boolean editMode;
    private Intent intent;


    public messFeedbackFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mess_feedback, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshForm();
    }

    public void refreshForm(){
        allMealsOfToday = getTodayMenu();
        eligibleMeals = getMealsEligibleForRating();
        currentMeal = getTheCurrentMeal();
        intent = getActivity().getIntent();
        editMode = intent.getBooleanExtra("editMode",false);
        setMenuOfCurrentMeal(currentMeal);
        initAndSetAllTheViews();
    }

    private ArrayList<String> getTodayMenu() {
        MessMenuCache cache = new MessMenuCache(context);
        Calendar calendar = Calendar.getInstance();
        String dayNow = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        ArrayList<String> dayList = cache.getDayArray();
        ArrayList<Menu> menuList = cache.getMenuArray();
        Menu menuObject = menuList.get(dayList.indexOf(dayNow));
        ArrayList<String> todayMenu = new ArrayList<>();
        todayMenu.add(menuObject.getBreakFast());
        todayMenu.add(menuObject.getLunch());
        todayMenu.add(menuObject.getDinner());
        return todayMenu;
    }

    private ArrayList<String> getMealsEligibleForRating() {
        ArrayList<String> eligibleMeals = new ArrayList<>();
        String[] array = {"Breakfast", "Lunch", "Dinner"};
        for (String meal: array) {
            if (isTimeValidFor(meal) && !(isFeedbackAlreadyGivenFor(meal))) {
                eligibleMeals.add(meal);
            }
        }
        return eligibleMeals;
    }

    private String getTheCurrentMeal() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        boolean isTrue = false;
        // not between 00:00 and 06:59
        if (!(hour <= 6)) {
            // not between 07:00 and 07:15
            if (!(hour == 7 && minute < 15)) {
                if (hour < 12)
                    return "Breakfast";
                if (hour < 19)
                    return "Lunch";
                else
                    return "Dinner";
            }
        }
        return null;
    }

    private void setMenuOfCurrentMeal(String currentMeal) {
        mMenu = view.findViewById(R.id.tv_feedback_meal_menu);
        mDescription = view.findViewById(R.id.et_feedback_description);
            if (currentMeal != null && eligibleMeals.contains(currentMeal)) {
                if (currentMeal.equals("Breakfast")) {
                    mMenu.setText(allMealsOfToday.get(0));
                } else if (currentMeal.equals("Lunch")) {
                    mMenu.setText(allMealsOfToday.get(1));
                } else if (currentMeal.equals("Dinner")) {
                    mMenu.setText(allMealsOfToday.get(2));
                }
            }
    }

    private boolean isTimeValidFor(String meal) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // not between 00:00 and 06:59
        if (!(hour <= 6)) {
            // not between 07:00 and 07:15
            if (!(hour == 7 && minute < 15)) {
                if (meal.equals("Breakfast")) {
                    return true;
                }
                if (meal.equals("Lunch") && hour >= 12) {
                    return true;
                }
                if (meal.equals("Dinner") && hour >= 19)
                    return true;
            }
        }
        return false;
    }

    private boolean isFeedbackAlreadyGivenFor(String selectedMeal) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        String[] timestamp = sharedPreferences.getString(selectedMeal, "00-00-0000").split("-");
        int[] previousDate = new int[timestamp.length];
        for (int i = 0; i < timestamp.length; i++)
            previousDate[i]=Integer.parseInt(timestamp[i]);
        Calendar calendar = Calendar.getInstance();
        int[] currentDate= {calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)};
        Log.i("ARRAYS", String.valueOf(previousDate[0]));
        return Arrays.equals(previousDate, currentDate);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initAndSetAllTheViews() {
        simpleRatingBar = view.findViewById(R.id.srb_mess_feedback);
        if(editMode) {
            rating = intent.getIntExtra("rating", 0);
            simpleRatingBar.setRating(rating);
        }
        else
            simpleRatingBar.setRating(0);

        final TextView mStarText = view.findViewById(R.id.tv_star_text);
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

        mDescription = view.findViewById(R.id.et_feedback_description);
        if(editMode) {
            mDescription.setText(intent.getStringExtra("description"));
            view.findViewById(R.id.tv_feedback_complaint).setVisibility(View.GONE);
        }
        else
            mDescription.setText("");
        setSpinners();
        setButtons();
    }

    private void setSpinners() {
        String[] anonymityArray = getResources().getStringArray(R.array.feedback_anonymity);
        final String[] anonymityArrayEnum = getResources().getStringArray(R.array.feedback_anonymity_enum);
        final CustomSpinnerAdapter anonymityAdapter = new CustomSpinnerAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, anonymityArray);
        anonymityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        anonymitySpinner = view.findViewById(R.id.spinner_feedback_anonymity);

        if(editMode){
            String anonymity = intent.getExtras().getString("anonymity");
            int position = ArrayUtils.toArrayList(anonymityArrayEnum).indexOf(anonymity);
            anonymitySpinner.setSelection(position);
        }
        else
            anonymitySpinner.setSelection(0);

        anonymitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("123hello",anonymityArrayEnum[position]);
                anonymity = Anonymity.valueOf(anonymityArrayEnum[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                anonymity = Anonymity.Public;
            }
        });

        if(editMode){
            final String meal = intent.getStringExtra("meal");
            String[] mealArray = new String[] {meal};
            selectedMeal = meal;
            CustomSpinnerAdapter mealAdapter = new CustomSpinnerAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, mealArray);
            mealAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            mealSpinner = view.findViewById(R.id.spinner_feedback_meal);
            mealSpinner.setAdapter(mealAdapter);

            if (meal.equals("Breakfast")) {
                mMenu.setText(allMealsOfToday.get(0));
            }
            else if (meal.equals("Lunch")) {
                mMenu.setText(allMealsOfToday.get(1));
            }
            else{
                mMenu.setText(allMealsOfToday.get(2));
            }
        }
        else {
            final String[] mealArray = new String[eligibleMeals.size() + 1];
            mealArray[0] = "";
            for (int i = 0; i < eligibleMeals.size(); i++)
                mealArray[i + 1] = eligibleMeals.get(i);
            CustomSpinnerAdapter mealAdapter = new CustomSpinnerAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, mealArray);
            mealAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            mealSpinner = view.findViewById(R.id.spinner_feedback_meal);
            mealSpinner.setAdapter(mealAdapter);
            if (currentMeal != null && eligibleMeals.contains(currentMeal)) {
                int spinnerPosition = mealAdapter.getPosition(currentMeal);
                mealSpinner.setSelection(spinnerPosition);
                selectedMeal = currentMeal;
            }
            mealSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Log.i("MEAL", String.valueOf(mealArray.length));
                    for (String m : mealArray)
                        Log.i("MEAL", m);
                    if (mealSpinner.getSelectedItemPosition() > 0) {
                        String meal = mealArray[mealSpinner.getSelectedItemPosition()];
                        selectedMeal = meal;
                        if (eligibleMeals.size() != 0) {
                            if (meal.equals("Breakfast")) {
                                mMenu.setText(allMealsOfToday.get(0));
                            }
                            else if (meal.equals("Lunch")) {
                                mMenu.setText(allMealsOfToday.get(1));
                            }
                            else{
                                mMenu.setText(allMealsOfToday.get(2));
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }
    }

    private void setButtons() {
        Button submit = view.findViewById(R.id.button_feedback_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
                String userId = sharedPreferences.getString("userId","loggedOut");
                if(userId.equals("loggedOut"))
                    Toast.makeText(getActivity().getApplicationContext(),"please login to continue", Toast.LENGTH_SHORT).show();
                else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        View spinner = view.findViewById(R.id.ll_feedback_meal);
                        if (mealSpinner.getSelectedItemPosition() == 0 && !editMode) {
                            Toast.makeText(getActivity().getApplicationContext(), "Select meal", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (rating == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), "Select rating", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    MessFeedback messFeedback = new MessFeedback();
                    String description = mDescription.getText().toString();
                    messFeedback.setUserId(userId);
                    messFeedback.setDescription(description);
                    messFeedback.setMeal(Meal.valueOf(selectedMeal));
                    messFeedback.setRating(rating);
                    messFeedback.setTimestamp(new Date());
                    messFeedback.setUserName(sharedPreferences.getString("userName","NA"));
                    messFeedback.setAnonymity(anonymity);

                    if(editMode){
                        String key = intent.getStringExtra("key");
                        FirebaseQuery.updateMessFeedback(key,messFeedback);
                        Toast.makeText(getActivity().getApplicationContext(), "feedback updated", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                    else {
                        Calendar calendar = Calendar.getInstance();
                        int date = calendar.get(Calendar.DATE), month = calendar.get(Calendar.MONTH), year = calendar.get(Calendar.YEAR);
                        String currentDate = date + "-" + month + "-" + year;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(selectedMeal, currentDate);
                        editor.apply();

                        FirebaseQuery.addMessFeedback(messFeedback);
                        refreshForm();
                        Toast.makeText(getActivity().getApplicationContext(), "feedback submitted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        TextView complain = view.findViewById(R.id.tv_feedback_complaint);
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), ComplaintFormActivity.class));
            }
        });
    }

}