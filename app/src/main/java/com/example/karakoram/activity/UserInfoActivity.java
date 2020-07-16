package com.example.karakoram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karakoram.R;

public class UserInfoActivity extends AppCompatActivity {
    private EditText wing_edittext;
    private EditText room_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        wing_edittext=(EditText)findViewById(R.id.wing_edittext);
        room_edittext=(EditText)findViewById(R.id.room_edittext);
    }

    public void back(View view)
    {
        super.onBackPressed();
    }
    public void save(View view)
    {
        if(room_edittext.getText().toString().isEmpty()||wing_edittext.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Enter the data",Toast.LENGTH_SHORT).show();
        }
       else{
           Toast.makeText(getApplicationContext(),"Wing -  "+wing_edittext.getText().toString() +"\n"+"room no. -"+room_edittext.getText().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
