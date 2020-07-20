package com.example.karakoram.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class HostelBillDescription extends AppCompatActivity {

    /* Variables */
    String key;
    String dbImageLocation;

    /*Views*/
    ImageView image;


    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_bill_description);
        initVariables();
        initViews();
        setViews();
    }

    private void initVariables() {
        key = getIntent().getExtras().getString("key");
        dbImageLocation = "hostelBillImages/" + "payroll_seq" + ".png";
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    private void initViews() {
        image = findViewById(R.id.iv_event_image);
        mImageView=(ImageView)findViewById(R.id.imageView);
    }

    private void setViews() {
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
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            mImageView.setScaleX(mScaleFactor);
            mImageView.setScaleY(mScaleFactor);
            return true;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
}
