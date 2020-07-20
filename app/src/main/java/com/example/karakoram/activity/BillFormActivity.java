package com.example.karakoram.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.HostelBill;

public class BillFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] category = {"Mess", "Maintenance", "Sports", "Cultural", "Others"};
    String bill_category;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_form);
        spin = findViewById(R.id.category_input);
        spin.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }

    public void onClickUploadBill(View view) {
        HostelBill bill = new HostelBill();
        bill.setAmount(Integer.parseInt(((EditText) findViewById(R.id.amount_input)).getText().toString()));
        bill.setCategory(Category.valueOf((bill_category)));
        bill.setDescription(((EditText) findViewById(R.id.description_input)).getText().toString());
        FirebaseQuery.addBill(bill);
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        bill_category = category[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        bill_category = "Others";
    }
}