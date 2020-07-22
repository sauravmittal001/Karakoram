package com.example.karakoram.childFragment.mess;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.Meal;
import com.example.karakoram.resource.MessFeedback;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */

public class messFeedbackFragment extends Fragment {

    public messFeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mess_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String[] meal = {"Breakfast", "Lunch", "Snacks", "Dinner"};
        final String[] meal_category = new String[1];
        Spinner spin;
        spin = getView().findViewById(R.id.meal_input);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, meal);
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(dropDownAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                meal_category[0] = meal[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button button;
        button = getView().findViewById(R.id.upload_bill_form_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessFeedback messFeedback = new MessFeedback();
                messFeedback.setUserId("user");
                messFeedback.setTimestamp(new Date());
                messFeedback.setDescription(((EditText) getView().findViewById(R.id.description_input)).getText().toString());
                messFeedback.setRating(Integer.parseInt(((EditText) getView().findViewById(R.id.rating_input)).getText().toString()));
                messFeedback.setMeal(Meal.valueOf(meal_category[0]));
                FirebaseQuery.addMessFeedBack(messFeedback);
            }
        });
    }
}