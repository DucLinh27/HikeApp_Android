package com.example.hikeapplication.Fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.hikeapplication.ConnectDb;
import com.example.hikeapplication.R;
import com.example.hikeapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;


public class AddHikeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AddHikeFragment() {
        // Required empty public constructor
    }

    public static AddHikeFragment newInstance(String param1, String param2) {
        AddHikeFragment fragment = new AddHikeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private EditText name, location, date, length, level, description;
    private RadioGroup radioGroup;
    private RadioButton btn_yes, btn_no;
    private DatePickerDialog datePickerDialog;
    private Button save_btn, dateButton;
    private Spinner levelSpinner;
    private Boolean setValid = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setTitle("Add new hike");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_hike, container, false);

        name = view.findViewById(R.id.name);
        location = view.findViewById(R.id.location);
        date = view.findViewById(R.id.date);
        length = view.findViewById(R.id.length);
        level = view.findViewById(R.id.level);
        description = view.findViewById(R.id.description);
        radioGroup = view.findViewById(R.id.radioGroup);
        btn_yes = view.findViewById(R.id.radioButton_yes);
        btn_no = view.findViewById(R.id.radioButton_no);

        date.setText(getTodayDate());

        initDatePicker();
        levelSpinner = view.findViewById(R.id.levelSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.level, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        levelSpinner.setAdapter(adapter);
        dateButton = view.findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        save_btn = view.findViewById(R.id.save_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        value.put("name", name_hike);
                        value.put("location", location_hike);
                        value.put("date", date_hike);
                        value.put("parking", parking);
                        value.put("length", length_hike);
                        value.put("level", level_hike);
                        value.put("description", description_hike);
                        String message = "New hike will be added: \n\n" +
                                "Name: " + name_hike + ",\n" +
                                "Location: " + location_hike + ",\n" +
                                "Date of the hike: " + date_hike + ",\n" +
                                "Parking available: " + parking + ",\n" +
                                "Length of the hike: " + length_hike + ",\n" +
                                "Difficulty level: " + level_hike + ",\n" +
                                "Description: " + description_hike +".\n\n" +
                                "Are you sure?";

                        showConfirmDialog(message, value);

                    } else if (btn_no.isChecked()) {
                        String parking = btn_no.getText().toString().trim();
                        ContentValues value = new ContentValues();
                        value.put("name", name_hike);
                        value.put("location", location_hike);
                        value.put("date", date_hike);
                        value.put("parking", parking);
                        value.put("length", length_hike);
                        value.put("level", level_hike);
                        value.put("description", description_hike);
                        String message = "New hike will be added: \n\n" +
                                "Name: " + name_hike + "\n" +
                                "Location: " + location_hike + "\n" +
                                "Date of the hike: " + date_hike + "\n" +
                                "Parking available: " + parking + "\n" +
                                "Length of the hike: " + length_hike + "\n" +
                                "Difficulty level: " + level_hike + "\n" +
                                "Description: " + description_hike+ "\n\n" +
                                "Are you sure?";


                        showConfirmDialog(message, value);
                    }
                }
            }
        });

        return view;
    }

//    SHOW ALERT ERROR DIALOG
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConnectDb db = new ConnectDb(getActivity());
                db.addHike(values.getAsString("name"),
                        values.getAsString("location"),
                        values.getAsString("date"),
                        values.getAsString("parking"),
                        values.getAsString("length"),
                        values.getAsString("level"),
                        values.getAsString("description"));
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                Menu menu = bottomNavigationView.getMenu();
                MenuItem home = menu.findItem(R.id.home);
                home.setChecked(true);
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
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
        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);

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
        if (day<10) {
            return "0"+ day + "/" + getMonthFormat(month) + "/" +year;
        }
        return  day + "/" + getMonthFormat(month) + "/" +year;
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

