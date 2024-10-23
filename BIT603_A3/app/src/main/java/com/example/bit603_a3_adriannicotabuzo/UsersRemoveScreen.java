package com.example.bit603_a3_adriannicotabuzo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UsersRemoveScreen extends AppCompatActivity {

    private Button buttonRemoveUser;

    private EditText editTextUsername;

    public static MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_remove_screen);

        // Initialise database.
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "users db")
                .allowMainThreadQueries().build();

        // Initialise widgets.
        editTextUsername = findViewById(R.id.editTextiRemoveUsername);
        buttonRemoveUser = findViewById(R.id.buttonIRemoveUser);

        buttonRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First, check if username exist in the database. And if not found, display appropriate message.
                String removeUser = editTextUsername.getText().toString(); // Obtain username from widget.

                // See if username is existing from the database.
                int userExist = myDatabase.dao().removeUserCheck(removeUser.toLowerCase());
                if (userExist == 0) { // If value is 0 then no result found in the database.
                    Toast.makeText(getBaseContext(), "User not existing", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If user is not found the show a dialog box to confirm with admin to remove the user.
                removeDialog(removeUser);
            }
        });
    }


    // Creating the menu on the Remove User Screen.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manageusers_menu, menu);
        return true;
    }
    // Setting the on-click event listener when menu is clicked.
    //  Taking the admin back to Manage User menu screen.
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

    // Generate dialog box for confirmation.
    private void removeDialog(String username) {
        // Instantiate.
        AlertDialog.Builder builder = new AlertDialog.Builder(UsersRemoveScreen.this);
        // Set characteristics.
        builder.setMessage(R.string.DialogMessage).setTitle(R.string.DialogTitle);

        // Deletion is confirmed. Continuing to delete user.
        builder.setPositiveButton(R.string.DialogYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDatabase.dao().deleteUser(username.toLowerCase()); // Remove user from database.
                Toast.makeText(getBaseContext(), "User removed.", Toast.LENGTH_SHORT).show(); // Show removed message.
                editTextUsername.setText(""); // Clear the editText box
            }
        });

        builder.setNegativeButton(R.string.DialogNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        // Get Confirm-Remove Dialog.
        AlertDialog removeConfirmDialog = builder.create();

        // Show dialog.
        removeConfirmDialog.show();
    }
}