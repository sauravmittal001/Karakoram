package com.example.karakoram;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class mydialog extends DialogFragment {
    private String event_name;
    private String event_details;
    private String event_time;
    View view;

    mydialog(String event_name,String event_details,String event_time)
    {
        this.event_details=event_details;
        this.event_name=event_name;
        this.event_time=event_time;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.mydialog,null,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView event_text_name=view.findViewById(R.id.dialog_event_name);
        TextView event_text_details=view.findViewById(R.id.dialog_event_details);
        TextView event_text_time=view.findViewById(R.id.dialog_event_time);

        event_text_name.setText(event_name);
        event_text_details.setText(event_details);
        event_text_time.setText(event_time);
    }
}
