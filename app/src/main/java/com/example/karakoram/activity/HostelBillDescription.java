package com.example.karakoram.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.karakoram.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import lombok.SneakyThrows;

public class HostelBillDescription extends AppCompatActivity {

    /* Variables */
    String key;
    String dbImageLocation;


    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_hostel_bill_description);

        initVariables();
        initViews();
        setViewsss();

    }

    private void initVariables() {
        key = Objects.requireNonNull(getIntent().getExtras()).getString("key");
        dbImageLocation = "hostelBillImages/" + "-MCm_9FDgiWWwqk_UwWp" + ".png";
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    private void initViews() {
        mImageView = (ImageView) findViewById(R.id.div_bill_image);
    }

    @SneakyThrows
    private void setViews() {
        //ImageViews
        StorageReference ref = FirebaseStorage.getInstance().getReference(dbImageLocation);
        long MAXBYTES = 1024 * 1024;
        ref.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                image.setImageBitmap(bitmap);
                Log.i("AAA", "Imgur");

                RequestOptions requestOption = new RequestOptions()
                        .fitCenter();
                Glide.with(getApplicationContext()).load(bytes)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOption)
                        .into(mImageView);
            }
        });


//        FutureTarget<File> future = Glide.with(applicationContext)
//                .load(yourUrl)
//                .downloadOnly(500, 500);
//        File cacheFile = future.get();

//        File file = Glide.with(this).asFile().load("https://images.unsplash.com/photo-1593677229754-9a4628d9260b?ixlib=rb-1.2.1&auto=format&fit=crop&w=900&q=60").submit().get();
//        String path = file.getPath();
    }

    private void setViewsss() {

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child(dbImageLocation).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                loadGlideImage(uri.toString());
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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter();
        Glide.with(getApplicationContext())
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .into(mImageView);
    }



    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            mImageView.setScaleX(mScaleFactor);
            mImageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}
