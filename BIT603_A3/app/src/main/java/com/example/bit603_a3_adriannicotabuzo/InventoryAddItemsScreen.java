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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class InventoryAddItemsScreen extends AppCompatActivity {

    public static MyDatabase myDatabase;

    private EditText editTextInventoryAddName, editTextInventoryAddQuantity;

    private Spinner spinnerInventoryItemTypes;

    private Button buttonInventoryAddItem, buttonInventoryClear;

    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_add_items);

        // Initialise database.
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "inventory db")
                .allowMainThreadQueries().build();

        // Retrieving the passed values from the previous activity.
        try {
            Bundle bundle = getIntent().getExtras();
            isAdmin = bundle.getBoolean("inUser");
        }
        catch (Exception ex) {
            isAdmin = false;
        }

        // Initialise widgets.
        editTextInventoryAddName = findViewById(R.id.editTextIInventoryAddName);
        editTextInventoryAddQuantity = findViewById(R.id.editTextIInventoryAddQuantity);
        buttonInventoryAddItem = findViewById(R.id.buttonIInventoryAdd);
        buttonInventoryClear = findViewById(R.id.buttonIInvetoryClear);
        spinnerInventoryItemTypes = findViewById(R.id.spinnerIInventoryItemTypes);


        // Set the items of the array from the String.xml to avoid raw text.
        ArrayAdapter<String> inventoryAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.InventoryTypes));

        // Set the array adapter to be the list displayed in the drop down menu for type selection.
        inventoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInventoryItemTypes.setAdapter(inventoryAdapter);

        buttonInventoryAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ItemName, ItemType;
                int ItemQuantity;

                try {
                    // Check if input is not empty.
                    if (editTextInventoryAddName.getText().toString().equals("") ||
                            editTextInventoryAddQuantity.getText().toString().equals("")) {
                        Toast.makeText(getBaseContext(), "Please provide all details", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Obtain data from the interface.
                    ItemName = editTextInventoryAddName.getText().toString();
                    ItemQuantity = Integer.parseInt(editTextInventoryAddQuantity.getText().toString());
                    ItemType = spinnerInventoryItemTypes.getSelectedItem().toString();
                }
                catch (Exception ex) {
                    // In case input is invalid.
                    Toast.makeText(getBaseContext(), "Please provide correct details.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if item is already in the database.
                int itemCheckCount = myDatabase.dao().itemInDatabase(ItemName.toLowerCase());
                if (itemCheckCount == 1) {
                    Toast.makeText(getBaseContext(), "Item is added to the inventory.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create new User object.
                Inventory newInventoryItem = new Inventory();
                newInventoryItem.setName(ItemName);
                newInventoryItem.setQuantity(ItemQuantity);
                newInventoryItem.setType(ItemType);

                // Insert customer into database.
                myDatabase.dao().addItem(newInventoryItem);

                // Display a message.
                Toast.makeText(InventoryAddItemsScreen.this, "Item added successfully.", Toast.LENGTH_SHORT).show();

                // Clear the text.
                Clear();
            }
        });

        // Clear EditText's text.
        buttonInventoryClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clear();
            }
        });
    }


    // Creating the menu button for navigating back to Inventory Menu screen.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manageinventory_menu, menu);
        return true;
    }
    // Set the on-click event listener when menu is clicked.
    //  Taking the user back to Inventory Menu screen.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        changeActivity(new Intent(getApplicationContext(), InventoryMenuScreen.class));
        return super.onOptionsItemSelected(item);
    }

    // Opening the next activity.
    public void changeActivity(Intent intent) {
        if (isAdmin) {
            // Keeps the admin status of the user..
            Bundle bundle = new Bundle();
            bundle.putBoolean("inUser", true);
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    // Clear fields on the screen.
    public void Clear() {
        // Clear EditText's text.
        editTextInventoryAddName.setText("");
        editTextInventoryAddQuantity.setText("");
        // Set focus on Name EditText box.
        editTextInventoryAddName.requestFocus();
    }
}