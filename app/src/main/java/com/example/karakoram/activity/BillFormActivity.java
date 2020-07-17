package com.example.karakoram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.karakoram.R;
import com.example.karakoram.firebase.FirebaseQuery;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.HostelBill;

public class BillFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_form);
    }

    public void onClickUploadBill(View view){
        HostelBill bill = new HostelBill();
        bill.setAmount(Integer.parseInt(((EditText)findViewById(R.id.amount_input)).getText().toString()));
        bill.setCategory(Category.valueOf(((EditText)findViewById(R.id.category_input)).getText().toString()));
        bill.setDescription(((EditText)findViewById(R.id.description_input)).getText().toString());
        FirebaseQuery.addBill(bill);
    }
}