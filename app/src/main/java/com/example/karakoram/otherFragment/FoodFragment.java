package com.example.karakoram.otherFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.childFragment.mess.MenuFragment;
import com.example.karakoram.resource.DayRating;
import com.example.karakoram.resource.Menu;
import com.example.karakoram.resource.User;
import com.example.karakoram.resource.UserType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import static android.content.Context.MODE_PRIVATE;

public class FoodFragment extends Fragment {

    View view;

    private String breakfastMenu, lunchMenu, dinnerMenu;
    private String day;
    private EditText breakfast, lunch, dinner;
    private TextView mDay, mRatingBreakfast, mRatingLunch, mRatingDinner;
    private Button mMenuChange;
    private SimpleRatingBar simpleRatingBarBreakfast, simpleRatingBarLunch, simpleRatingBarDinner;
    private MenuFragment menuFragment;
    private int dayIndex;
    private DayRating dayRating;
    private SharedPreferences sharedPreferences;

    public FoodFragment() {
    }

    public FoodFragment(Menu menu, MenuFragment menuFragment, int dayIndex) {
        this.day = menu.getDay();
        this.breakfastMenu = menu.getBreakFast();
        this.lunchMenu = menu.getLunch();
        this.dinnerMenu = menu.getDinner();
        this.menuFragment = menuFragment;
        this.dayIndex = dayIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food, container, false);
        initVariables();
        initViews();
        setViews();
        return view;
    }

    private void initVariables() {
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews() {
        mMenuChange = view.findViewById(R.id.button_change_menu_food_fragment);
        breakfast = view.findViewById(R.id.et_breakfast_menu);
        lunch = view.findViewById(R.id.et_lunch_menu);
        dinner = view.findViewById(R.id.et_dinner_menu);
        mDay = view.findViewById(R.id.tv_day);
        simpleRatingBarBreakfast = view.findViewById(R.id.srb_mess_menu_breakfast);
        simpleRatingBarLunch = view.findViewById(R.id.srb_mess_menu_lunch);
        simpleRatingBarDinner = view.findViewById(R.id.srb_mess_menu_dinner);
        mRatingBreakfast = view.findViewById(R.id.tv_rating_breakfast);
        mRatingLunch= view.findViewById(R.id.tv_rating_lunch);
        mRatingDinner = view.findViewById(R.id.tv_rating_dinner);
    }

    private void setViews() {
        //set individual meals
        breakfast.setText(breakfastMenu);
        lunch.setText(lunchMenu);
        dinner.setText(dinnerMenu);
        mDay.setText(day + "'s Menu");

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_menu);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                menuFragment.initVariables();
                setRating();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        String type = sharedPreferences.getString("type", "DEFAULT");
        if (!type.equals(UserType.Admin.name())) {
            mMenuChange.setVisibility(View.GONE);
            breakfast.setKeyListener(null);
            lunch.setKeyListener(null);
            dinner.setKeyListener(null);
        }

        mMenuChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
                String userId = sharedPreferences.getString("userId","loggedOut");
                String userType = sharedPreferences.getString("type","Student");
                if(userId.equals("loggedOut"))
                    Toast.makeText(getActivity().getApplicationContext(),"please login to continue",Toast.LENGTH_SHORT).show();
                else {
                    if(userType.equals("Student"))
                        Toast.makeText(getActivity().getApplicationContext(),"you are not authorised to perform this action",Toast.LENGTH_SHORT).show();
                    else {
                        Menu menuObject = new Menu();
                        menuObject.setBreakFast(breakfast.getText().toString());
                        menuObject.setLunch(lunch.getText().toString());
                        menuObject.setDinner(dinner.getText().toString());
                        menuObject.setDay(day);
                        FirebaseQuery.updateMenu(menuObject, dayIndex);
                        Toast.makeText(getActivity().getApplicationContext(), "menu updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        setRating();

    }

    private void setRating() {
        FirebaseQuery.getRating(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dayRating = snapshot.getValue(DayRating.class);
                assert dayRating != null;
                float ratingBreakfast = 0;
                if(dayRating.getBreakfast().getCount()!=0) {
                    ratingBreakfast = (float) dayRating.getBreakfast().getTotal() / dayRating.getBreakfast().getCount();
                    mRatingBreakfast.setText(String.format("%.2f",ratingBreakfast));
                }
                else{
                    mRatingBreakfast.setText("no rating yet");
                }
                float ratingLunch = 0;
                if(dayRating.getLunch().getCount()!=0) {
                    ratingLunch = (float) dayRating.getLunch().getTotal() / dayRating.getLunch().getCount();
                    mRatingLunch.setText(String.format("%.2f",ratingLunch));
                }
                else{
                    mRatingLunch.setText("no rating yet");
                }
                float ratingDinner = 0;
                if(dayRating.getDinner().getCount()!=0) {
                    ratingDinner = (float) dayRating.getDinner().getTotal() / dayRating.getDinner().getCount();
                    mRatingDinner.setText(String.format("%.2f",ratingDinner));
                }
                else{
                    mRatingDinner.setText("no rating yet");
                }

                simpleRatingBarBreakfast.setRating(ratingBreakfast);
                simpleRatingBarLunch.setRating(ratingLunch);
                simpleRatingBarDinner.setRating(ratingDinner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","Some error occurred");
            }
        });
    }
}