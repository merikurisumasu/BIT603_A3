package com.example.bit603_a3_adriannicotabuzo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class UsersManageScreen extends AppCompatActivity {

    //Declare widgets
    private Button buttonView, buttonAdd, buttonRemove, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage_screen);

        // Initialise widgets.
        buttonView = findViewById(R.id.buttonIViewUsersScreen);
        buttonAdd = findViewById(R.id.buttonIAddUserScreen);
        buttonRemove = findViewById(R.id.buttonIRemoveUserScreen);
        buttonLogout = findViewById(R.id.buttonILogout);

        // Takes the user back to login screen.
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        // Takes the admin to the View Users screen.
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(new Intent(getApplicationContext(), UsersViewScreen.class));
            }
        });

        // Takes the admin to the Add User screen.
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(new Intent(getApplicationContext(), UsersAddScreen.class));
            }
        });

        // Takes the admin to the Remove User screen.
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(new Intent(getApplicationContext(), UsersRemoveScreen.class));
            }
        });
    }

    // Creating the menu button for navigating to Inventory Menu screen.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manageinventory_menu, menu);
        return true;
    }
    // Set the on-click event listener when menu is clicked.
    //  Taking the admin to Inventory Menu screen.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        changeActivity(new Intent(getApplicationContext(), InventoryMenuScreen.class));
        return super.onOptionsItemSelected(item);
    }


    // Open an activity.
    public void changeActivity(Intent intent) {
        // Passing Name and Colour variable to the Menu activity.
        Bundle bundle = new Bundle();
        bundle.putBoolean("inUser", true);
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }
}