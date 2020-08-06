package com.example.karakoram.childFragment.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.activity.MainActivity;
import com.example.karakoram.activity.SignInActivity;
import com.example.karakoram.resource.User;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    private View view;
    private CustomSpinner userInputSpinner;
    private String[] userInputArray;
    private EditText mPassword, mEntryNoEdit;
    private TextView mCreateAccount;

    public LoginFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEntryNoEdit = view.findViewById(R.id.et_login_entry_number);
        mPassword = view.findViewById(R.id.et_login_password);
        mCreateAccount = view.findViewById(R.id.tv_create_account);
        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignInActivity)getActivity()).updatePagerView();
            }
        });
        setSpinners();
        setButtons();
    }

    private void setSpinners() {
        userInputArray = getResources().getStringArray(R.array.user_type);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, userInputArray);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        userInputSpinner = view.findViewById(R.id.spinner_user_type);
        userInputSpinner.setAdapter(adapter);
        userInputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String userType = userInputArray[userInputSpinner.getSelectedItemPosition()];
                if (!userType.equals("Student")) {
                    view.findViewById(R.id.ll_entry_number).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.ll_entry_number).setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    private void setButtons() {
        Button submit = view.findViewById(R.id.button_sign_in);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = mPassword.getText().toString();
                String userInput = userInputArray[userInputSpinner.getSelectedItemPosition()];
                final String entryNumber;
                if (!userInput.equals("Student")){
                    entryNumber = userInput;
                }
                else {
                    entryNumber = mEntryNoEdit.getText().toString();
                }
                Query query = FirebaseQuery.getUserByEntryNumber(entryNumber);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            if(!user.isSignedIn()){
                                Toast.makeText(getActivity().getApplicationContext(),"please signin",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(password.equals(user.getPassword())) {
                                String key = entryNumber;
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", key);
                                editor.putString("userName", user.getName());
                                editor.putString("entryNumber", user.getEntryNumber());
                                editor.putString("room", user.getRoom());
                                editor.putString("type",user.getType().toString());
                                editor.apply();
                                Toast.makeText(getActivity().getApplicationContext(), "logged in as " + user.getName(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            else
                                Toast.makeText(getActivity().getApplicationContext(),"incorrect password",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getActivity().getApplicationContext(),"user does not exist",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        TextView forgot = view.findViewById(R.id.tv_login_forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "error: contact developers", Toast.LENGTH_SHORT).show();
            }
        });
    }


}