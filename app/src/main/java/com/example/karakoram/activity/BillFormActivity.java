package com.example.karakoram.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.HostelBill;

public class BillFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] category = {"Mess", "Maintenance", "Sports", "Cultural", "Others"};
    String bill_category;
    Spinner spin;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_form);

        spin = findViewById(R.id.category_input);
        spin.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(dropDownAdapter);
    }

    public void onClickUploadBill(View view) {
        HostelBill bill = new HostelBill();
        bill.setAmount(Integer.parseInt(((EditText) findViewById(R.id.amount_input)).getText().toString()));
        bill.setCategory(Category.valueOf((bill_category)));
        bill.setDescription(((EditText) findViewById(R.id.description_input)).getText().toString());
        FirebaseQuery.addBill(bill,imageUri);
    }

    public void onClickChooseImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            ImageView billImage = findViewById(R.id.image_chosen);
            billImage.setImageURI(imageUri);
        }
        else{
            Log.d("123hello","upload failure");
        }
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