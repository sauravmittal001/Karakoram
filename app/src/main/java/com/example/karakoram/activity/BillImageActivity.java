package com.example.karakoram.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.karakoram.DynamicImageView;
import com.example.karakoram.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jsibbold.zoomage.ZoomageView;

import java.util.Objects;

public class BillImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_bill_image);
        final ImageView myImageView = (ZoomageView) findViewById(R.id.myZoomageView);
        String key = getIntent().getExtras().getString("key");
        String dbImageLocation = "eventImages/" + key + ".png";
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child(dbImageLocation).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(BillImageActivity.this)
                        .load(uri.toString())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(new RequestOptions().fitCenter())
                        .into(myImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public int getImage(String imageName) {
        return this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());
    }
}