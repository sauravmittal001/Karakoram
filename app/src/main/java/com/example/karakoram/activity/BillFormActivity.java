package com.example.karakoram.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.HostelBill;
import com.example.karakoram.resource.User;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;

import java.util.Date;
import java.util.Objects;

public class BillFormActivity extends AppCompatActivity {

    private String[] dropDownArray;
    private String itemSelected;
    private CustomSpinner categorySpinner;
    private Uri imageUri;
    private TextView mError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_bill_form);
        setVariables();
        setViews();

    }

    private void setVariables() {
        //category spinner array
        Category[] category = Category.values();
        dropDownArray = new String[category.length];
        for (int i = 0; i < category.length; i++)
            dropDownArray[i] = String.valueOf(category[i]);

//        itemSelected = Objects.requireNonNull(getIntent().getExtras()).getString("category");
    }

    private void setViews() {
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, dropDownArray);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        categorySpinner = findViewById(R.id.spinner_category);
        categorySpinner.setAdapter(adapter);

        mError = (TextView) findViewById(R.id.tv_error);

    }

    public void onClickUploadBill(View view) {
        itemSelected = dropDownArray[categorySpinner.getSelectedItemPosition()];
        Log.i("SPINNER", itemSelected);
        HostelBill bill = new HostelBill();
        if (imageUri == null || String.valueOf(imageUri).equals("")) {
            mError.setVisibility(View.VISIBLE);
            return;
        }
        String amount = String.valueOf(((EditText) findViewById(R.id.et_amount)).getText());
        if (!amount.equals("")) {
            bill.setAmount(Integer.parseInt(amount));
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.et_amount).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","loggedOut");
        bill.setUserId(userId);
        bill.setTimeStamp(new Date());
        bill.setCategory(Category.valueOf((itemSelected)));
        bill.setDescription(((EditText) findViewById(R.id.et_description)).getText().toString());
        FirebaseQuery.addBill(bill, imageUri);
    }

    public void onClickChooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        findViewById(R.id.tv_image).setVisibility(View.VISIBLE);
        mError.setVisibility(View.INVISIBLE);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView eventImage = findViewById(R.id.div_bill_image);
            eventImage.setImageURI(imageUri);
        } else {
            Log.d("123hello", "upload failure");
        }
    }


    public void backPressed(View view) {
        onBackPressed();
    }
}