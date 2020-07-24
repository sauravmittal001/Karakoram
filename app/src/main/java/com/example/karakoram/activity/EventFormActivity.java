package com.example.karakoram.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.R;
import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.User;

import java.util.Date;

public class EventFormActivity extends AppCompatActivity {

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
    }

    public void onClickUpdateEvent(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","loggedOut");
        if(userId.equals("loggedOut")) {
            Toast.makeText(getApplicationContext(),"please login to continue",Toast.LENGTH_SHORT).show();
        }
        else {
            Event event = new Event();
            String[] date = ((EditText) findViewById(R.id.date_input)).getText().toString().split("-");
            String[] time = ((EditText) findViewById(R.id.time_input)).getText().toString().split(":");
            event.setDateTime(new Date(Integer.parseInt(date[0]) - 1900, Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1])));
            event.setTitle(((EditText) findViewById(R.id.title_input)).getText().toString());
            event.setDescription(((EditText) findViewById(R.id.description_input)).getText().toString());
            event.setTimeStamp(new Date());
            event.setUserId(userId);
            FirebaseQuery.addEvent(event, imageUri);
            Toast.makeText(getApplicationContext(),"new event added",Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }

    public void onClickChooseImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            ImageView eventImage = findViewById(R.id.image_chosen);
            eventImage.setImageURI(imageUri);
        }
        else{
            Log.d("123hello","upload failure");
        }
    }
}