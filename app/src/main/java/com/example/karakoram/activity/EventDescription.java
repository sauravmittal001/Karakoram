package com.example.karakoram.activity;

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

import java.util.Objects;

public class EventDescription extends AppCompatActivity {

    /* Variables */
    private String title;
    private String time;
    private String description;
    private String dbImageLocation;

    /*Views*/
    private TextView mTitle;
    private TextView mTime;
    private TextView mDescription;
    private ImageView mImage;
    private Button mButtonDone;

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
        description = getIntent().getExtras().getString("description");
        mButtonDone = findViewById(R.id.button_event_description_done);

        String key = getIntent().getExtras().getString("key");
        dbImageLocation = "eventImages/" + key + ".png";
    }

    private void initViews() {
        mTitle = (TextView) findViewById(R.id.tv_event_title);
        mTime = (TextView) findViewById(R.id.tv_event_time);
        mDescription = (TextView) findViewById(R.id.tv_event_description);
        mImage = (DynamicImageView) findViewById(R.id.div_event_image);
    }

    private void setViews() {
        // TextViews
//        mTitle.setText("Some Title which I want to display since this is it");
//        mTime.setText("4th july | 11:00 AM");
//        mDescription.setText("Luckily, you never have to be. The U.S. Navy Pre-Flight School developed a scientific method to fall asleep day or night, in any conditions, in under two minutes. After six weeks of practice, 96 percent of pilots could fall asleep in two minutes or less. Even after drinking coffee, with machine gunfire being played in the background. That’s what happened with U.S. fighter pilots in World War II. The U.S. military realized many of its pilots were making terrible, avoidable decisions due to stress and the resulting sleeplessness. Shooting down friendlies. Being shot down themselves. Even when pilots clocked off, they couldn’t relax and they couldn’t sleep. So their stress and fatigue built up, till they made a fatal error.");

        mTitle.setText(title);
        mTime.setText(time);
        mDescription.setText(description);

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

        //Button
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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