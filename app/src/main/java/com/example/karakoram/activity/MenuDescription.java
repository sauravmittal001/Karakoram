package com.example.karakoram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karakoram.R;

import java.util.Objects;

public class MenuDescription extends AppCompatActivity {

    private String menu;
    private String day;
    private EditText breakfast;
    private EditText lunch;
    private EditText dinner;
    private TextView mDay;
    private Button mMenuChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_menu_description);
        initVariables();
        initViews();
        setViews();

    }

    private void initVariables() {
        //get individual meals
        menu = getIntent().getExtras().getString("menu");
        day = getIntent().getExtras().getString("day") + "'s Menu";
    }

    private void initViews() {
        mMenuChange = (Button) findViewById(R.id.button_change_menu);
        breakfast = (EditText) findViewById(R.id.et_breakfast_menu);
        lunch = (EditText) findViewById(R.id.et_lunch_menu);
        dinner = (EditText) findViewById(R.id.et_dinner_menu);
        mDay = (TextView) findViewById(R.id.tv_day);

    }

    private void setViews() {
        //set individual meals
        breakfast.setText(menu);
        lunch.setText(menu);
        dinner.setText(menu);
        mDay.setText(day);

        if (false) {    // if he is not admin
            mMenuChange.setVisibility(View.GONE);
            breakfast.setKeyListener(null);
            lunch.setKeyListener(null);
            dinner.setKeyListener(null);
        }

    }


    public void backPressed(View view) {
        super.onBackPressed();
    }
}