package com.example.karakoram.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class EventFormActivity extends AppCompatActivity {

    private Uri imageUri;
    private TextView mError, dateView, timeView, mTitle, mDescription;
    private int year, month, day, hour, min;
    private boolean isTimeFilled, isDateFilled;
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            showDate(year, month + 1, day);
        }
    };


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
    }

    private void initViews() {
        mError = (TextView) findViewById(R.id.tv_error);
        dateView = (TextView) findViewById(R.id.et_date);
        timeView = (TextView) findViewById(R.id.et_time);
        mTitle = (EditText) findViewById(R.id.et_title);
        mDescription = (EditText) findViewById(R.id.et_description);
    }

    private void setViews() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);
    }


    public void onClickCreateEvent(View view) {
        Event event = new Event();
        String title = String.valueOf(mTitle.getText());
        String description = String.valueOf(mDescription.getText());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (title.equals("")) {
                mTitle.setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                return;
            } else {
                mTitle.setBackground(getDrawable(R.drawable.background_rounded_section_task));
            }
            if (description.equals("")) {
                mDescription.setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                return;
            } else {
                mDescription.setBackground(getDrawable(R.drawable.background_rounded_section_task));
            }
            if (!isDateFilled) {
                dateView.setBackground(getDrawable(R.drawable.background_rect_section_task_red));
                return;
            } else {
                dateView.setBackground(getDrawable(R.drawable.background_rect_section_task));
            }
            if (!isTimeFilled) {
                timeView.setBackground(getDrawable(R.drawable.background_rect_section_task_red));
                return;
            } else {
                timeView.setBackground(getDrawable(R.drawable.background_rect_section_task));
            }
        }
        if (imageUri == null || String.valueOf(imageUri).equals("")) {
            mError.setVisibility(View.VISIBLE);
            return;
        }
        event.setDateTime(new Date(year - 1900, month - 1, day, hour, min));
        event.setTitle(title);
        event.setDescription(description);
        Log.i("AAAA", description);
        FirebaseQuery.addEvent(event, imageUri);
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
        mError.setVisibility(View.INVISIBLE);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView eventImage = findViewById(R.id.div_event_image);
            eventImage.setImageURI(imageUri);
        } else {
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
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
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