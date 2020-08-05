package com.example.karakoram.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.karakoram.DynamicImageView;
import com.example.karakoram.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.Objects;

public class EventDescription extends AppCompatActivity {

    /* Variables */
    private String title;
    private String time;
    private String date;
    private String description;
    private String dbImageLocation;
    private String key;
    private boolean isImageAttached;
    private String dateTime;

    /*Views*/
    private TextView mTitle;
    private TextView mTime;
    private TextView mDate;
    private TextView mDescription;
    private ImageView mImage;
    private ImageView mButtonBack;
    private ImageView mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_event_description);
        initVariables();
        initViews();
        setViews();
    }

    private void initVariables() {
        title = Objects.requireNonNull(getIntent().getExtras()).getString("title");
        time = getIntent().getExtras().getString("time");
        date = getIntent().getExtras().getString("date");
        description = getIntent().getExtras().getString("description");
        isImageAttached = getIntent().getBooleanExtra("isImageAttached",false);
        key = getIntent().getExtras().getString("key");

        String key = getIntent().getExtras().getString("key");
        dbImageLocation = "eventImages/" + key + ".png";
        dateTime = getIntent().getExtras().getString("dateTime");
    }

    private void initViews() {
        mTitle = findViewById(R.id.tv_event_title);
        mTime = findViewById(R.id.tv_event_time);
        mDescription = findViewById(R.id.tv_event_description);
        mImage = (DynamicImageView) findViewById(R.id.div_event_image);
        mButtonBack = findViewById(R.id.iv_back_button);
        mEdit = findViewById(R.id.iv_edit_button);
        mDate = findViewById(R.id.tv_event_date);
    }

    private void setViews() {
        // TextViews

        mTitle.setText(title);
        mTime.setText(time);
        mDescription.setText(description);
        mDate.setText(date);

        if(isImageAttached) {
            findViewById(R.id.fl_image).setVisibility(View.VISIBLE);
            //DB image
            StorageReference ref = FirebaseStorage.getInstance().getReference();
            ref.child(dbImageLocation).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    loadGlideImage(uri.toString());
//                adjustImageSize();
                    removePlaceholderImage();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        else
            findViewById(R.id.fl_image).setVisibility(View.GONE);

        //Button
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDescription.this, EventFormActivity.class);
                intent.putExtra("editMode",true);
                intent.putExtra("description",description);
                intent.putExtra("title",title);
                intent.putExtra("isImageAttached",isImageAttached);
                intent.putExtra("dateTime",dateTime);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });
    }

    private void loadGlideImage(String url) {
        RequestOptions requestOption = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter();
        Glide.with(getApplicationContext())
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .into(mImage);
    }


    private void adjustImageSize() {
        FrameLayout frameLayout = findViewById(R.id.fl_image);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
        int widthView = params.width;

        int h = mImage.getMaxHeight(), w = mImage.getMaxWidth();

        int ratio = (int) widthView / w;
        mImage.setMaxHeight(h * ratio);
        mImage.setMaxWidth(w * ratio);

        Log.i("SCREEN", "widthView = " + widthView);
        Log.i("SCREEN", "h = " + h);
        Log.i("SCREEN", "w = " + w);
        Log.i("SCREEN", "ratio = " + ratio);
    }


    private void removePlaceholderImage() {
        findViewById(R.id.div_event_placeholder_image).setVisibility(View.GONE);
    }

}