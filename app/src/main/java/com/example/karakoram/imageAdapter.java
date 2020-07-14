package com.example.karakoram;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class imageAdapter extends BaseAdapter {
    private Context mContext;
    public imageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return bill.length;
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
        ImageView imageView=new ImageView(mContext);

        imageView.setImageResource(bill[i]);
        return imageView;
    }

    public Integer[] bill={
    R.drawable.download_1,R.drawable.download,R.drawable.images,R.drawable.images_1,R.drawable.images_2,R.drawable.images_3,R.drawable.download_2,R.drawable.download_3,
    R.drawable.download_4,R.drawable.download_5,R.drawable.download_6};
}
