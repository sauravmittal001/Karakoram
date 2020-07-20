package com.example.karakoram.childFragment.bill.fullscreenimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.karakoram.childFragment.bill.fullscreenimage.FullscreenImageAdapter;
import com.example.karakoram.R;

import java.util.List;

public class fullimageActivity extends AppCompatActivity {
  ViewPager mpager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        //initialize view pager
        mpager=findViewById(R.id.image_view_pager);
        List<Integer> bill=getIntent().getExtras().getIntegerArrayList("bill");
        int currentbill=getIntent().getExtras().getInt("currentbill");
        FullscreenImageAdapter adapter=new FullscreenImageAdapter(getSupportFragmentManager(),bill);
        mpager.setAdapter(adapter);
        mpager.setCurrentItem(currentbill);



    }
}
