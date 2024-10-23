package com.example.bit603_a3_adriannicotabuzo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UsersAddScreen extends AppCompatActivity {

    private EditText editTextAddUsername, editTextAddPassword, editTextAddDOB, editTextAddEID,
    editTextAddPhone, editTextAddAddress;

    private Button buttonAddUser, buttonClear;

    public static MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_screen);

        // Initialise database.
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "users db")
                .allowMainThreadQueries().build();

        // Initialise widgets.
        editTextAddUsername = findViewById(R.id.editTextIAddUsername);
        editTextAddPassword = findViewById(R.id.editTextIAddPassword);
        editTextAddDOB = findViewById(R.id.editTextIAddDOB);
        editTextAddEID = findViewById(R.id.editTextIAddEID);
        editTextAddPhone = findViewById(R.id.editTextIAddPhone);
        editTextAddAddress = findViewById(R.id.editTextIAddAddress);

        // Stores all editText widgets into an array for ease of clearing each widgets.
        EditText[] editTextArray = new EditText[] {editTextAddUsername, editTextAddPassword,
                editTextAddDOB, editTextAddEID, editTextAddPhone, editTextAddAddress};

        editTextAddUsername.requestFocus(); // Sets focus to the username.

        buttonAddUser = findViewById(R.id.buttonIAddUser);
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, password, DOB, address;
                int EID, phone;

                // Formatter for the date.
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                // Get the date today for checking.
                Date dateToday = Calendar.getInstance().getTime();

                // Obtain data from the interface and check if input is valid.
                try {
                    // Check if any editText widget is empty.
                    for (EditText editTextInput : editTextArray) {
                        if (editTextInput.getText().toString().equals("")) {
                            editTextInput.requestFocus();
                            Toast.makeText(getBaseContext(), "Please provide all information", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    // Obtain data from the interface
                    username = editTextAddUsername.getText().toString();
                    password = editTextAddPassword.getText().toString();
                    DOB = editTextAddDOB.getText().toString();
                    EID = Integer.parseInt(editTextAddEID.getText().toString());
                    phone = Integer.parseInt(editTextAddPhone.getText().toString());
                    address = editTextAddAddress.getText().toString();

                    // Check if date is in correct format.
                    Date checkDOB = new Date(01/01/2000);
                    formatter.setLenient(false);
                    try {
                        checkDOB = formatter.parse(DOB);
                    }
                    catch (Exception ex) {
                        Toast.makeText(getBaseContext(), "Invalid date.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Check that day is not later than today's date.
                    if (checkDOB.after(dateToday)) {
                        Toast.makeText(getBaseContext(), "DOB cannot be later than date today", Toast.LENGTH_SHORT).show();
                        editTextAddDOB.requestFocus();
                        return;
                    }

                    // Check if username is already in the database.
                    int usernameCheckCount = myDatabase.dao().userInDatabase(username.toLowerCase());
                    if (usernameCheckCount == 1) {
                        Toast.makeText(getBaseContext(), "Username is already used.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if Employee ID is already in the database.
                    int EIDCheckCount = myDatabase.dao().EIDINDatabase(EID);
                    if (EIDCheckCount == 1) {
                        Toast.makeText(getBaseContext(), "Employee ID is already used.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // Throws a message if input was not valid.
                catch (Exception ex) {
                    Toast.makeText(getBaseContext(), "Please provide correct details.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create new User object.
                Users user = new Users();
                user.setUsername(username);
                user.setPassword(password);
                user.setDob(DOB);
                user.setEID(EID);
                user.setPhone(phone);
                user.setAddress(address);

                // Insert customer into database
                myDatabase.dao().addUser(user);

                // Display a message
                Toast.makeText(getBaseContext(), "User added successfully!", Toast.LENGTH_SHORT).show();

                // Clear the text
                Clear(editTextArray);
            }
        });

        // Clears all the fields in the Add User Screen.
        buttonClear = findViewById(R.id.buttonIAddClear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clear(editTextArray);
            }
        });
    }

    // Creating the menu on the Add User Screen.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manageusers_menu, menu);
        return true;
    }
    // Set the on-click event listener when menu is clicked.
    //  Take the admin back to Manage User menu screen.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        changeActivity(new Intent(getBaseContext(), UsersManageScreen.class));
        return super.onOptionsItemSelected(item);
    }


    // Open an activity.
    public void changeActivity(Intent intent) {
        startActivity(intent);
        finish();
    }

    // Clear fields on the screen.
    public void Clear(EditText[] editTexts) {
        for (int i = 0; i < editTexts.length; i++)
            editTexts[i].setText("");
        editTextAddUsername.requestFocus();
    }
}