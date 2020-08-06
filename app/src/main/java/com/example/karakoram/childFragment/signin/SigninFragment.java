package com.example.karakoram.childFragment.signin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.activity.ComplaintFormActivity;
import com.example.karakoram.activity.MainActivity;
import com.example.karakoram.activity.SignInActivity;
import com.example.karakoram.activity.SplashScreenActivity;
import com.example.karakoram.resource.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import static android.content.Context.MODE_PRIVATE;

public class SigninFragment extends Fragment {

    private View view;
    private EditText mPassword, mEntryNoEdit;
    private User user;
    private Context context;

    public SigninFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signin, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEntryNoEdit = view.findViewById(R.id.et_login_entry_number);
        mPassword = view.findViewById(R.id.et_login_password);
        setButtons();
    }

    private void setButtons() {
        Button submit = view.findViewById(R.id.button_sign_in);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = mPassword.getText().toString();
                final String entryNumber = mEntryNoEdit.getText().toString();

                if (password.equals("")) {
                    mPassword.setBackground(getActivity().getResources().getDrawable(R.drawable.background_rounded_section_task_red));
                    return;
                }
                else {
                    mPassword.setBackground(getActivity().getResources().getDrawable(R.drawable.background_rounded_section_task));
                }

                Query query = FirebaseQuery.getUserByEntryNumber(entryNumber);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            user = snapshot.getValue(User.class);
                            if(user.isSignedIn()){
                                Toast.makeText(getActivity().getApplicationContext(), "user already signed in", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else{
                                new AlertDialog.Builder(context, R.style.MyDialogTheme)
                                        .setTitle("Please confirm")
                                        .setMessage("Are you sure you want to create your account with this password ?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                String key = entryNumber;
                                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("userId", key);
                                                editor.putString("userName", user.getName());
                                                editor.putString("entryNumber", user.getEntryNumber());
                                                editor.putString("room", user.getRoom());
                                                editor.putString("type", user.getType().toString());
                                                editor.apply();
                                                Toast.makeText(getActivity().getApplicationContext(), "logged in as " + user.getName(), Toast.LENGTH_SHORT).show();
                                                FirebaseQuery.setUserPassWord(entryNumber,password,true);
                                                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                        else
                            Toast.makeText(getActivity().getApplicationContext(), "entry number does not exist", Toast.LENGTH_SHORT).show();
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