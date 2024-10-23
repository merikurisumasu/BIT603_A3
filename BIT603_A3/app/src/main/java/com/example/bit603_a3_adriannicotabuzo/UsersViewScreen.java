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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class UsersViewScreen extends AppCompatActivity {

    public static MyDatabase myDatabase;

    private ListView listViewUsersListDisplay;

    private TextView textViewViewUsername, textViewViewPassword, textViewViewDOB, textViewViewEID,
            textViewViewPhone, textViewViewAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_screen);

        // Initialise the database.
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "users db")
                .allowMainThreadQueries().build();

        // Initialise the listview to display the users in the database.
        listViewUsersListDisplay = findViewById(R.id.listViewIUserList);
        textViewViewUsername = findViewById(R.id.textViewIViewUsername);
        textViewViewPassword = findViewById(R.id.textViewIViewPassword);
        textViewViewDOB = findViewById(R.id.textViewIViewDOB);
        textViewViewEID = findViewById(R.id.textViewIViewEID);
        textViewViewPhone = findViewById(R.id.textViewIViewPhone);
        textViewViewAddress = findViewById(R.id.textViewIViewAddress);

        // Load all the database entry to the list to be displayed on the listview widget.
        final List<String> usersList = myDatabase.dao().getUsernames();

        // Set adapter for the listView items to reflect usernames list.
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usersList);
        // Setting the listView adapter.
        listViewUsersListDisplay.setAdapter(arrayAdapter);

        // When an item is clicked from the listview the details of the user is presented on each respective textview.
        listViewUsersListDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get from the database the user selected.
                Users selectedUser = myDatabase.dao().getUserDetails(usersList.get(position));

                // Print each detail to their respective textview.
                textViewViewUsername.setText(selectedUser.getUsername());
                textViewViewPassword.setText(selectedUser.getPassword());
                textViewViewDOB.setText(selectedUser.getDob());
                textViewViewEID.setText(String.valueOf(selectedUser.getEID()));
                textViewViewPhone.setText(String.valueOf(selectedUser.getPhone()));
                textViewViewAddress.setText(selectedUser.getAddress());
            }
        });
    }


    // Creating the menu on the View Users Screen.
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
}