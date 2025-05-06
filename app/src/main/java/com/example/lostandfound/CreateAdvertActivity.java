package com.example.lostandfound;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    private RadioGroup postTypeRadioGroup;
    private RadioButton lostRadioButton;
    private RadioButton foundRadioButton;
    private TextInputEditText nameEditText;
    private TextInputEditText phoneEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText locationEditText;
    private Button saveButton;

    private DatabaseHelper databaseHelper;
    private Calendar selectedDate = Calendar.getInstance();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create Advert");
        }

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        postTypeRadioGroup = findViewById(R.id.postTypeRadioGroup);
        lostRadioButton = findViewById(R.id.lostRadioButton);
        foundRadioButton = findViewById(R.id.foundRadioButton);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        locationEditText = findViewById(R.id.locationEditText);
        saveButton = findViewById(R.id.saveButton);

        // Set current date as default
        dateEditText.setText(dateFormatter.format(selectedDate.getTime()));

        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        saveButton.setOnClickListener(v -> saveItem());
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, monthOfYear);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    dateEditText.setText(dateFormatter.format(selectedDate.getTime()));
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveItem() {
        // Get values from form
        String type = lostRadioButton.isChecked() ? "Lost" : "Found";
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Please enter a name");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Please enter a phone number");
            return;
        }

        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError("Please enter a description");
            return;
        }

        if (TextUtils.isEmpty(location)) {
            locationEditText.setError("Please enter a location");
            return;
        }

        Item item = new Item(type, name, phone, description, date, location);

        // Save to database
        long id = databaseHelper.insertItem(item);

        if (id > 0) {
            Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show();
            finish(); // Go back to previous activity
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            Toast.makeText(this, "Error saving item", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}