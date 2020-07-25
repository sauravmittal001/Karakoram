package com.example.karakoram.childFragment.EventResources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.karakoram.R;
import com.google.android.gms.tasks.OnFailureListener;
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
    String dbImageLocation;

    /*Views*/
    TextView mTitle;
    TextView mTime;
    TextView mDescription;
    ImageView image;

//    String url = "https://firebasestorage.googleapis.com/v0/b/karakoram-94656.appspot.com/o/eventImages%2F7221.jpg?alt=media&token=63850a20-ce2c-40a7-ae9d-3c6f886f6842";
String url;
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
        dbImageLocation = "eventImages/" + key + ".png";
    }

    private void initViews() {
        mTitle = findViewById(R.id.tv_event_title);
        mTime = findViewById(R.id.tv_event_time);
        mDescription = findViewById(R.id.tv_event_description);
        image = findViewById(R.id.iv_event_image);
    }

    private void setViews() {
        // TextViews
        mTitle.setText(title);
        mTime.setText(time);
        mDescription.setText(description);

        //ImageViews
//        StorageReference ref = FirebaseStorage.getInstance().getReference(dbImageLocation);
//        long MAXBYTES = 1024 * 1024;
//        ref.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                image.setImageBitmap(bitmap);
//            }
//        });


//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReferenceFromUrl(dbImageLocation);
//        Log.i("AAA", "Imgur");
//
//        RequestOptions requestOption = new RequestOptions()
//                .placeholder(R.drawable.download_6)
//                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                .centerCrop();
//        Glide.with(getApplicationContext()).load(url)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .apply(requestOption)
//                .into(image);

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child(dbImageLocation).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = uri.toString();
                Log.i("GETSTRIGN", url);
                loadGlideImage(url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }

    private void loadGlideImage(String url) {
        RequestOptions requestOption = new RequestOptions()
                .placeholder(R.drawable.e03)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
        Glide.with(getApplicationContext())
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .into(image);
    }
}