package com.example.karakoram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karakoram.R;
import com.example.karakoram.resource.User;

public class UserInfoActivity extends AppCompatActivity {
    private EditText wingEditText;
    private EditText roomEditText;
    private EditText nameEditText;
    private EditText entryNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        wingEditText = findViewById(R.id.wing_edittext);
        roomEditText = findViewById(R.id.room_edittext);
        nameEditText = findViewById(R.id.name_edittext);
        entryNumberEditText = findViewById(R.id.entry_number_edittext);
        loadData();
    }

    public void saveData(View view)
    {
        if(roomEditText.getText().toString().isEmpty()||wingEditText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter the data",Toast.LENGTH_SHORT).show();
        }
        else{
           Toast.makeText(getApplicationContext(),"Wing -  "+wingEditText.getText().toString() +"\n"+"room no. -"+roomEditText.getText().toString(),Toast.LENGTH_SHORT).show();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName",nameEditText.getText().toString());
        editor.putString("entryNumber",entryNumberEditText.getText().toString());
        editor.putString("room",roomEditText.getText().toString());
        editor.putString("wing",wingEditText.getText().toString());
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
        nameEditText.setText(sharedPreferences.getString("userName",""));
        entryNumberEditText.setText(sharedPreferences.getString("entryNumber",""));
        roomEditText.setText(sharedPreferences.getString("room",""));
        wingEditText.setText(sharedPreferences.getString("wing",""));

    }

}
