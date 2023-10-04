package com.example.hikeapplication.Hike;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hikeapplication.ConnectDb;
import com.example.hikeapplication.Fragment.HomeFragment;
import com.example.hikeapplication.MainActivity;
import com.example.hikeapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.util.Calendar;

public class EditHikeActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private EditText name, location, date, length, level, description;
    private RadioGroup radioGroup;
    private RadioButton btn_yes, btn_no;
    private Button save_btn, dateButton;
    private String id_hike, name_hike, location_hike, date_hike, parking_hike, length_hike, level_hike, description_hike;
    private Spinner levelSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Detail Hike");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hike);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        length = findViewById(R.id.length);
        level = findViewById(R.id.level);
        description = findViewById(R.id.description);
        radioGroup = findViewById(R.id.radioGroup);
        btn_yes = findViewById(R.id.radioButton_yes);
        btn_no = findViewById(R.id.radioButton_no);
        try {
            getAndSetData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initDatePicker();
        dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        levelSpinner = findViewById(R.id.levelSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.level, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        levelSpinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(level_hike);
        levelSpinner.setSelection(spinnerPosition);


        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(v -> {

            String name_hike = name.getText().toString().trim();
            String location_hike = location.getText().toString().trim();
            String date_hike = date.getText().toString().trim();
            String length_hike = length.getText().toString().trim();
            String level_hike = levelSpinner.getSelectedItem().toString().trim();
            String description_hike = description.getText().toString().trim();
            int idGroup = radioGroup.getCheckedRadioButtonId();
            if (idGroup < 0) {
                showAlertDialog();
            } else if (name_hike.length() == 0
                    | location_hike.length() == 0
                    | date_hike.length() == 0
                    | length_hike.length() == 0
                    | level_hike.length() == 0
                    | description_hike.length() == 0
            ) {
                showAlertDialog();
            } else {
                if (btn_yes.isChecked()) {
                    String parking = btn_yes.getText().toString().trim();
                    ContentValues value = new ContentValues();
                    value.put("id", id_hike);
                    value.put("name", name_hike);
                    value.put("location", location_hike);
                    value.put("date", date_hike);
                    value.put("parking", parking);
                    value.put("length", length_hike);
                    value.put("level", level_hike);
                    value.put("description", description_hike);
                    String message = "New hike will be added: \n" +
                            "Name: " + name_hike + "\n" +
                            "Location: " + location_hike + "\n" +
                            "Date of the hike: " + date_hike + "\n" +
                            "Parking available: " + parking + "\n" +
                            "Length of the hike: " + length_hike + "\n" +
                            "Difficulty level: " + level_hike + "\n" +
                            "Description: " + description_hike;

                    showConfirmDialog(message, value);

                } else if (btn_no.isChecked()) {
                    String parking = btn_no.getText().toString().trim();
                    ContentValues value = new ContentValues();
                    value.put("id", id_hike);
                    value.put("name", name_hike);
                    value.put("location", location_hike);
                    value.put("date", date_hike);
                    value.put("parking", parking);
                    value.put("length", length_hike);
                    value.put("level", level_hike);
                    value.put("description", description_hike);
                    String message = "Hike will be edited: \n" +
                            "Name: " + name_hike + "\n" +
                            "Location: " + location_hike + "\n" +
                            "Date of the hike: " + date_hike + "\n" +
                            "Parking available: " + parking + "\n" +
                            "Length of the hike: " + length_hike + "\n" +
                            "Difficulty level: " + level_hike + "\n" +
                            "Description: " + description_hike + "\n\n" +
                            "Are you sure?";


                    showConfirmDialog(message, value);
                }
            }
        });


    }

    public void getAndSetData() throws ParseException {
        if (
                getIntent().hasExtra("id")
                        && getIntent().hasExtra("name")
                        && getIntent().hasExtra("location")
                        && getIntent().hasExtra("date")
                        && getIntent().hasExtra("parking")
                        && getIntent().hasExtra("length")
                        && getIntent().hasExtra("level")
                        && getIntent().hasExtra("description")
        ) {
            int parkingId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(parkingId);
            id_hike = getIntent().getStringExtra("id");
            name_hike = getIntent().getStringExtra("name");
            location_hike = getIntent().getStringExtra("location");
            date_hike = getIntent().getStringExtra("date");
            parking_hike = getIntent().getStringExtra("parking");
            length_hike = getIntent().getStringExtra("length");
            level_hike = getIntent().getStringExtra("level");
            description_hike = getIntent().getStringExtra("description");

            name.setText(name_hike);
            location.setText(location_hike);
            date.setText(date_hike);
            length.setText(length_hike);
            description.setText(description_hike);


            if (parking_hike.equals("Yes")) {
                btn_yes.setChecked(true);
                btn_no.setChecked(false);
            } else {

                btn_yes.setChecked(false);
                btn_no.setChecked(true);
            }
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    //    SHOW ALERT ERROR DIALOG
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditHikeActivity.this);
        builder.setTitle("Error");
        builder.setMessage("All required fields must be filled!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //    SHOW ALERT CONFIRM DIALOG
    private void showConfirmDialog(String message, ContentValues values) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditHikeActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConnectDb db = new ConnectDb(EditHikeActivity.this);
                db.editHike(values.getAsString("id"),
                        values.getAsString("name"),
                        values.getAsString("location"),
                        values.getAsString("date"),
                        values.getAsString("parking"),
                        values.getAsString("length"),
                        values.getAsString("level"),
                        values.getAsString("description"));
                BottomNavigationView bottomNavigationView = EditHikeActivity.this.findViewById(R.id.bottomNavigationView);
                Menu menu = bottomNavigationView.getMenu();
                MenuItem home = menu.findItem(R.id.home);
                home.setChecked(true);
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = EditHikeActivity.this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, homeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //    DATE PICKER DIALOG
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dateP = makeDateString(day, month, year);
                date.setText(dateP);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        if (day < 10) {
            return "0" + day + "/" + getMonthFormat(month) + "/" + year;
        }
        return day + "/" + getMonthFormat(month) + "/" + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1) return "01";
        if (month == 2) return "02";
        if (month == 3) return "03";
        if (month == 4) return "04";
        if (month == 5) return "05";
        if (month == 6) return "06";
        if (month == 7) return "07";
        if (month == 8) return "08";
        if (month == 9) return "09";
        if (month == 10) return "10";
        if (month == 11) return "11";
        if (month == 12) return "12";

        return "01";
    }
}