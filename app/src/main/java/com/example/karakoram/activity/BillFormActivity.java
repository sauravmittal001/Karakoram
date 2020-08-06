package com.example.karakoram.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.ComplaintArea;
import com.example.karakoram.resource.HostelBill;
import com.example.karakoram.resource.User;
import com.example.karakoram.resource.UserType;
import com.example.karakoram.views.CustomSpinner;
import com.example.karakoram.views.CustomSpinnerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.Objects;

public class BillFormActivity extends AppCompatActivity {

    private String[] dropDownArray;
    private String itemSelected;
    private CustomSpinner categorySpinner;
    private Uri imageUri;
    private TextView mError;
    private EditText mAmount;
    private EditText mDescription;
    private Intent intent;
    private  boolean editMode;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        intent = getIntent();
        setContentView(R.layout.activity_bill_form);
        setVariables();
        setViews();
    }

    private void setVariables() {
        //category spinner array
        Category[] category = Category.values();
        dropDownArray = new String[category.length+1];
        dropDownArray[0] = "";
        for (int i = 0; i < category.length; i++)
            dropDownArray[i+1] = String.valueOf(category[i]);
        editMode = intent.getBooleanExtra("editMode",false);
        mImage = findViewById(R.id.div_bill_image);
        mError = (TextView) findViewById(R.id.tv_error);
        mAmount = findViewById(R.id.et_amount);
        mDescription = findViewById(R.id.et_description);
    }

    private void setViews() {
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, dropDownArray);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        categorySpinner = findViewById(R.id.spinner_category);
        categorySpinner.setAdapter(adapter);
        itemSelected = getIntent().getExtras().getString("category");
        if (itemSelected != null) {
            int spinnerPosition = adapter.getPosition(itemSelected);
            categorySpinner.setSelection(spinnerPosition);
        }

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                itemSelected = dropDownArray[categorySpinner.getSelectedItemPosition()];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        if(editMode) {
            String amount = intent.getStringExtra("amount");
            mAmount.setText(amount);
            String key = intent.getStringExtra("key");
            String description = intent.getStringExtra("description");
            mDescription.setText(description);
            FirebaseQuery.getBillImageRef(key).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUri = uri;
                            loadGlideImage(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // image not loading
                }
            });
            ((TextView)findViewById(R.id.tv_image)).setVisibility(View.VISIBLE);
        }
    }

    private void loadGlideImage(String url) {
        Log.i("CRASHHH", url); // not getting here
        RequestOptions requestOption = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter();
        Glide.with(BillFormActivity.this)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .into(mImage);
    }

    public void onClickUploadBill(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","loggedOut");
        UserType userType = UserType.valueOf(sharedPreferences.getString("type","Student"));
        if(userId.equals("loggedOut"))
            Toast.makeText(getApplicationContext(),"please login to continue", Toast.LENGTH_SHORT).show();
        else {
            if (userType.equals(UserType.Student))
                Toast.makeText(getApplicationContext(), "you are not authorized to perform this action", Toast.LENGTH_SHORT).show();
            HostelBill bill = new HostelBill();
            if (!editMode && (imageUri == null || String.valueOf(imageUri).equals(""))) {
                mError.setVisibility(View.VISIBLE);
                return;
            }
            String amount = String.valueOf(mAmount.getText());
            String description = mDescription.getText().toString();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (!amount.equals("")) {
                    bill.setAmount(Double.parseDouble(amount));
                    findViewById(R.id.et_amount).setBackground(getDrawable(R.drawable.background_rounded_section_task));
                } else {
                    findViewById(R.id.et_amount).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                    return;
                }
                if (!description.equals("")) {
                    bill.setDescription(description);
                    findViewById(R.id.et_description).setBackground(getDrawable(R.drawable.background_rounded_section_task));
                } else {
                    findViewById(R.id.et_description).setBackground(getDrawable(R.drawable.background_rounded_section_task_red));
                    return;
                }
            }

            bill.setUserId(userId);
            bill.setTimeStamp(new Date());
            bill.setCategory(Category.valueOf((itemSelected)));

            if (editMode) {
                String key = intent.getStringExtra("key");
                FirebaseQuery.updateBill(key, bill, imageUri);
                Toast.makeText(getApplicationContext(), "bill has been updated", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseQuery.addBill(bill, imageUri);
                Toast.makeText(getApplicationContext(), "new bill added", Toast.LENGTH_SHORT).show();
            }
            super.onBackPressed();
        }
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
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            findViewById(R.id.tv_image).setVisibility(View.VISIBLE);
            mError.setVisibility(View.INVISIBLE);
            imageUri = data.getData();
            mImage.setImageURI(imageUri);
        } else {
            Log.d("Image", "upload failure");
        }
    }


    public void backPressed(View view) {
        onBackPressed();
    }
}