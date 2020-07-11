package com.example.karakoram;

import com.google.firebase.database.DataSnapshot;

public interface OnGetDataListener {
    void onSuccess(Object object);
    void onStart();
    void onFailure();
}
