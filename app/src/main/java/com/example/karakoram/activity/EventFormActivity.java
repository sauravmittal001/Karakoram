package com.example.karakoram.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.User;
import com.example.karakoram.resource.UserType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class EventFormActivity extends AppCompatActivity {

    private Uri imageUri;
    private TextView dateView, timeView, mTitle, mDescription;
    private int year, month, day, hour, min;
    private boolean isTimeFilled, isDateFilled, isImageAttached, editMode;
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2 + 1, arg3);
            year = arg1;
            month = arg2 + 1;
            day = arg3;
        }
    };
    private Intent intent;
    private ImageView mImage, mDelete;
    private Date dateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_event_form);
        initVariables();
        initViews();
        setViews();
    }

    private void initVariables() {
        isDateFilled = false;
        isTimeFilled = false;
        intent = getIntent();
        isImageAttached = intent.getBooleanExtra("isImageAttached",false);
        editMode = intent.getBooleanExtra("editMode",false);
        if(editMode)
            dateTime = new Date(Date.parse (getIntent().getStringExtra("dateTime")));
    }

    private void initViews() {
        dateView = findViewById(R.id.et_date);
        timeView = findViewById(R.id.et_time);
        mTitle = findViewById(R.id.et_title);
        mDescription = findViewById(R.id.et_description);
        mImage = findViewById(R.id.div_event_image);
        mDelete = findViewById(R.id.iv_delete);
    }

    private void setViews() {
        Calendar calendar = Calendar.getInstance();

        if(editMode){
            year = dateTime.getYear()+1900;
            month = dateTime.getMonth()+1;
            day = dateTime.getDate();
            hour = dateTime.getHours();
            min = dateTime.getMinutes();
        }
        else {
            year = calendar.get(Calendar.YEAR);
            month = 1;
            day = 1;
            hour = 23;
            min = 59;
        }

        showDate(year, month, day);
        showTime(hour, min);

        if(editMode){
            Log.d("123hello","hi");
            mTitle.setText(intent.getStringExtra("title"));
            mDescription.setText(intent.getStringExtra("description"));
            if(isImageAttached) {
                String key = intent.getStringExtra("key");
                StorageReference ref = FirebaseQuery.getEventImageRef(key);
                ref.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUri = uri;
                                loadGlideImage(uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // image not loading
                    }
                });
                ((TextView)findViewById(R.id.tv_image)).setText("Selected Image:");
                mDelete.setVisibility(View.VISIBLE);
                mImage.setVisibility(View.VISIBLE);
            }
        }

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)findViewById(R.id.tv_image)).setText("No Image Selected");
                mDelete.setVisibility(View.GONE);
                mImage.setVisibility(View.GONE);
                isImageAttached = false;
            }
        });
    }

    private void loadGlideImage(String url) {
        Log.i("CRASHHH", url); // not getting here
        RequestOptions requestOption = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter();
        Glide.with(EventFormActivity.this)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .into(mImage);
    }

    public void onClickCreateEvent(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","loggedOut");
        UserType userType = UserType.valueOf(sharedPreferences.getString("type","Student"));
        if(userId.equals("loggedOut"))
            Toast.makeText(getApplicationContext(),"please login to continue", Toast.LENGTH_SHORT).show();
        else {
            if(userType.equals(UserType.Student))
                Toast.makeText(getApplicationContext(),"you are not authorised to perform this action", Toast.LENGTH_SHORT).show();
            else {
                Event event = new Event();
                String title = String.valueOf(mTitle.getText());
                String description = String.valueOf(mDescription.getText());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (title.equals("")) {
                        mTitle.setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                        return;
                    }
                    else {
                        mTitle.setBackground(getDrawable(R.drawable.background_rounded_section_task));
                    }

                    if (description.equals("")) {
                        mDescription.setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                        return;
                    }
                    else {
                        mDescription.setBackground(getDrawable(R.drawable.background_rounded_section_task));
                    }

                }

                event.setDateTime(new Date(year - 1900, month - 1, day, hour, min));
                event.setTitle(title);
                event.setDescription(description);
                event.setUserId(userId);
                event.setTimeStamp(new Date());
                event.setImageAttached(isImageAttached);

                if (!isImageAttached || imageUri == null || String.valueOf(imageUri).equals("")) {
                    if(editMode){
                        boolean prevIsImageAttached = intent.getBooleanExtra("isImageAttached",false);
                        String key = intent.getStringExtra("key");
                        FirebaseQuery.updateEvent(key,event);
                        if(prevIsImageAttached)
                            FirebaseQuery.removeEventImage(key);
                    }
                    else
                        FirebaseQuery.addEvent(event);
                }
                else {
                    if(editMode){
                        String key = intent.getStringExtra("key");
                        FirebaseQuery.updateEvent(key,event,imageUri);
                    }
                    else
                        FirebaseQuery.addEvent(event, imageUri);
                }

                if(editMode)
                    Toast.makeText(getApplicationContext(),"event updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"new event added", Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }

    // upload image
    public void onClickChooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        findViewById(R.id.tv_image).setVisibility(View.VISIBLE);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ((TextView)findViewById(R.id.tv_image)).setText("Selected Image:");
            imageUri = data.getData();
            mImage.setImageURI(imageUri);
            mDelete.setVisibility(View.VISIBLE);
            mImage.setVisibility(View.VISIBLE);
            isImageAttached = true;
        }
        else {
            ((TextView)findViewById(R.id.tv_image)).setText("No Image Selected");
            mDelete.setVisibility(View.GONE);
            mImage.setVisibility(View.GONE);
            isImageAttached = false;
            Log.d("123hello", "upload failure");
        }
    }

    //Date picker
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        isDateFilled = true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999)
            return new DatePickerDialog(this, myDateListener, year, month, day);
        return null;
    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(year));
    }


    //Time picker
    public void openClock(View view) {
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;
                        timeView.setText(time);
                        showTime(hourOfDay, minute);
                        isTimeFilled = true;
                    }
                }, hour, min, false);
        timePickerDialog.show();
    }

    public void showTime(int hour, int min) {
        String format;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        timeView.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }

    public void backPressed(View view) {
        onBackPressed();
    }
}