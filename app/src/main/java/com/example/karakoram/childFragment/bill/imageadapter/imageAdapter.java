package com.example.karakoram.childFragment.bill.imageadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class imageAdapter extends BaseAdapter {
    private Context mContext;
    List<Integer> bill;
    public imageAdapter(List<Integer> bill ,Context c) {
        this.bill=bill;
        mContext = c;
    }

    @Override
    public int getCount() {
        return bill.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView=(ImageView) view;
        if(imageView==null){
         imageView=new ImageView(mContext);
         imageView.setLayoutParams(new GridView.LayoutParams(600,450));
         imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        imageView.setImageResource(bill.get(i));
        return imageView;
    }


}
