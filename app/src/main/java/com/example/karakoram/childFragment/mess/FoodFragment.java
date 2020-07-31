package com.example.karakoram.childFragment.mess;

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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.Menu;
import com.example.karakoram.resource.User;
import com.example.karakoram.resource.UserType;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

public class FoodFragment extends Fragment {

    View view;

    private String breakfastMenu, lunchMenu, dinnerMenu;
    private String day;
    private EditText breakfast, lunch, dinner;
    private TextView mDay;
    private Button mMenuChange;

    private SharedPreferences sharedPreferences;

    public FoodFragment() {
    }

    public FoodFragment(String day, Menu menu) {
        this.day = day;
        this.breakfastMenu = menu.getBreakFast();
        this.lunchMenu = menu.getLunch();
        this.dinnerMenu = menu.getDinner();
        if(day.equals("Friday"))
            Log.d("123hello",breakfastMenu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food, container, false);
        if(day.equals("Friday"))
            Log.d("123hello",this.breakfastMenu);
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
        mMenuChange = (Button) view.findViewById(R.id.button_change_menu_food_fragment);
        breakfast = (EditText) view.findViewById(R.id.et_breakfast_menu);
        lunch = (EditText) view.findViewById(R.id.et_lunch_menu);
        dinner = (EditText) view.findViewById(R.id.et_dinner_menu);
        mDay = (TextView) view.findViewById(R.id.tv_day);
    }

    private void setViews() {
        //set individual meals
        breakfast.setText(breakfastMenu);
        lunch.setText(lunchMenu);
        dinner.setText(dinnerMenu);
        mDay.setText(day + "'s Menu");

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
                        FirebaseQuery.updateMenu(menuObject, day);
                        Toast.makeText(getActivity().getApplicationContext(), "menu updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}