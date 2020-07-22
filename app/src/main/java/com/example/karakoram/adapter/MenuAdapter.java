package com.example.karakoram.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.karakoram.R;
import com.example.karakoram.resource.Menu;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<Menu> {
    TextView mDay;
    TextView mMenuString;
    ArrayList<String> days;

    public MenuAdapter(Activity context, ArrayList<Menu> word, ArrayList<String> days) {

        /* Here, we initialize the ArrayAdapter's internal storage for the context and the list.
         * the second argument is used when the ArrayAdapter is populating a single TextView.
         * Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
         * going to use this second argument, so it can be any value. Here, we used 0.
         */
        super(context, 0, word);
        this.days = days;
        Log.i("ASDF", "print");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View list = convertView;
        if (list == null) {
            list = LayoutInflater.from(getContext()).inflate(R.layout.listview_menu, parent, false);
        }

        Menu menu = getItem(position);

        mDay = list.findViewById(R.id.tv_day);
        mMenuString = list.findViewById(R.id.tv_menu);

        if (menu != null) {
            mDay.setText(days.get(position));
            mMenuString.setText(menu.getMenuString());
        }
        return list;
    }
}
