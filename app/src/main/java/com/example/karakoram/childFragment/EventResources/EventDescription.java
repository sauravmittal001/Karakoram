package com.example.karakoram.childFragment.EventResources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class EventDescription extends AppCompatActivity {

    /* Variables */
    String title;
    String time;
    String description;
    String key;
    String date;
    String dbImageLocation;

    /*Views*/
    TextView mTitle;
    TextView mDate;
    TextView mTime;
    TextView mDescription;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        initVariables();
        initViews();
        setViews();
    }

    private void initVariables() {
        title = Objects.requireNonNull(getIntent().getExtras()).getString("title");
        time = getIntent().getExtras().getString("time");
        description = getIntent().getExtras().getString("description");
        key = getIntent().getExtras().getString("key");
        date = getIntent().getExtras().getString("date");
        dbImageLocation = "eventImages/" + key + ".png";
    }

    private void initViews() {
        mTitle = findViewById(R.id.tv_event_title);
        mDate = findViewById(R.id.tv_event_date);
        mTime = findViewById(R.id.tv_event_time);
        mDescription = findViewById(R.id.tv_event_description);
        image = findViewById(R.id.iv_event_image);
    }

    private void setViews() {
        // TextViews
        mTitle.setText(title);
        mTime.setText(time);
        mDate.setText(date);
        mDescription.setText(description);

        //ImageViews
        StorageReference ref = FirebaseStorage.getInstance().getReference(dbImageLocation);
        long MAXBYTES = 1024 * 1024;
        ref.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.setImageBitmap(bitmap);
            }
        });

    }
}