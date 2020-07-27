package com.example.karakoram.views;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    /*@Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }*/

    @Override
    public boolean isEnabled(int position){
        if(position == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
