package com.example.karakoram.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.karakoram.DynamicImageView;
import com.example.karakoram.R;

public class WingMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wing_map);
        ImageView myImageView = (DynamicImageView) findViewById(R.id.div_wing_map);
        Glide.with(this)
                .load(getImage("wing_map"))
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(new RequestOptions().fitCenter())
                .into(myImageView);
    }

    public int getImage(String imageName) {
        return this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());
    }
}