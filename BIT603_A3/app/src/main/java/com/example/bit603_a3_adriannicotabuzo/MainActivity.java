package com.example.bit603_a3_adriannicotabuzo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Hard-coded credentials for Admin account.
    final String AdminUsername = "Admin";
    final String AdminPassword = "CookieManagement84";

    // Declare variables.
    public static MyDatabase myDatabase;

    private Button buttonLogin, buttonClear;
    private EditText editTextLogin, editTextPassword;
    private String username, password;

    private List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialises the database.
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "users db").allowMainThreadQueries().build();

        // Initiate variables.
        buttonLogin = findViewById(R.id.buttonILogin);
        buttonClear = findViewById(R.id.buttonIClear);
        editTextLogin = findViewById(R.id.editTextIUsername);
        editTextPassword = findViewById(R.id.editTextIPassword);

        // Store all users with their details from the database.
        usersList = myDatabase.dao().getUsers();

        // Clears the text on the editText boxes.
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextLogin.setText("");
                editTextPassword.setText("");
            }
        });

        // Checks the database to see if this matches the records stored by reading
        //  data from a data structure with the table information stored.
        //  If the user enters an incorrect login, they will receive the appropriate message and
        //  fail to log in. If they enter the correct log-in details, they are taken to the menu screen.
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gets user inputs.
                username = editTextLogin.getText().toString();
                password = editTextPassword.getText().toString();

                // Check if login account is the Admin.
                if (username.toLowerCase().equals(AdminUsername.toLowerCase()) && password.equals(AdminPassword)) {
                    Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    changeActivity(new Intent(getBaseContext(), UsersManageScreen.class));
                }

                // Check if user enters correct login.
                else {
                    for (Users user : usersList) {
                        if (user.getUsername().toLowerCase().equals(username.toLowerCase())) { // Check username.
                            if (user.getPassword().equals(password)) { // Check password.
                                Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                changeActivity(new Intent(getApplicationContext(), InventoryMenuScreen.class));
                            }
                        }
                    }
                }
                // User entered incorrect login.
                Toast.makeText(getBaseContext(), "Please enter correct login details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Open an activity.
    public void changeActivity(Intent intent) {
        startActivity(intent);
        finish();
    }
}