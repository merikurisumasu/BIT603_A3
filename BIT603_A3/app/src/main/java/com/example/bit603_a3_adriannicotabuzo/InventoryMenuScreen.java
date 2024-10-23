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
import android.widget.Toast;

import java.util.Random;

public class InventoryMenuScreen extends AppCompatActivity {

    private Button buttonInventoryStatus, buttonInventoryAddItem, buttonInventoryClearItem,
            buttonInventoryAddTestItem, buttonInventoryLogOut;

    public static MyDatabase myDatabase;

    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_menu_screen);

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
        buttonInventoryStatus = findViewById(R.id.buttonIInventoryStatus);
        buttonInventoryAddItem = findViewById(R.id.buttonIInventoryAddItem);
        buttonInventoryClearItem = findViewById(R.id.buttonIInventoryClearItems);
        buttonInventoryAddTestItem = findViewById(R.id.buttonIInventoryAddTest);
        buttonInventoryLogOut = findViewById(R.id.buttonIInventoryLogOut);

        // Takes the user to the inventory menu screen.
        buttonInventoryStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(new Intent(getApplicationContext(), InventoryStatusScreen.class));
            }
        });

        // Takes the user to the add new item screen.
        buttonInventoryAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(new Intent(getApplicationContext(), InventoryAddItemsScreen.class));
            }
        });

        // Clears all the items from the Inventory database table.
        buttonInventoryClearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmDialog("RemoveAllItems");
            }
        });

        // Create new 20 test items in the Inventory database table.
        buttonInventoryAddTestItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmDialog("AddTestItems");
            }
        });

        // Takes user back to the login screen.
        buttonInventoryLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
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

    // Generate dialog box for confirmation.
    private void ConfirmDialog(String dialogAction) {
        // Instantiate.
        AlertDialog.Builder builder = new AlertDialog.Builder(InventoryMenuScreen.this);

        // Set characteristics.
        builder.setMessage(R.string.DialogMessage).setTitle(R.string.DialogTitle);

        builder.setPositiveButton(R.string.DialogYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // OK button is clicked.
                if (dialogAction.equals("AddTestItems"))
                    addTestItems();
                else if (dialogAction.equals("RemoveAllItems"))
                    clearInventory();
            }
        });

        builder.setNegativeButton(R.string.DialogNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Not OK button is clicked.
            }
        });

        // Get AlertDialog.
        AlertDialog dialog = builder.create();

        // Show dialog.
        dialog.show();
    }

    // Generate 20 inventory items.
    private void addTestItems() {
        int itemAdded = 0; // Number of added items.

        // Set 20 items to be selected as new test item.
        String[] InventoryItems = {"Cacao", "Capers", "Olives", "Peanut", "Baking powder",
                "Baking soda", "Brown sugar", "Cornstarch", "Milk", "Plain yogurt",
                "Corn", "Tortillas", "Blackberries", "Blueberries", "Peaches",
                "Ice Cream", "Chocolate", "Cake", "Rice", "Cupcake"};

        // Types to be randomly selected.
        String[] InventoryTypes = {"Biscuit", "Cookie", "Cake", "Ingredient", "Other"};

        // Initialise random object.
        Random randomIndex = new Random();

        // Randomly selecting item name, type and quantity for 20 test objects.
        for (int index = 0; index < 20; index++) {
            Inventory inventoryTestItem = new Inventory();

            // Check if username is already in the database.
            int itemCheckCount = myDatabase.dao().itemInDatabase(InventoryItems[index].toLowerCase());
            if (itemCheckCount == 0) {
                inventoryTestItem.setName(InventoryItems[index]);
                inventoryTestItem.setQuantity(randomIndex.nextInt(1000));
                inventoryTestItem.setType(InventoryTypes[randomIndex.nextInt(5)]);

                // Add the new Items with random details.
                myDatabase.dao().addItem((inventoryTestItem));

                itemAdded++;
            }
        }
        // Shows message that adding new 20 items was successful.
        Toast.makeText(getBaseContext(), "Added new " + String.valueOf(itemAdded) + " test items.", Toast.LENGTH_SHORT).show();
    }

    // Clears the inventory database.
    private void clearInventory () {
        myDatabase.dao().clearInventory();
        // Shows message that clearing was successful.
        Toast.makeText(getBaseContext(), "Cleared all inventory items!", Toast.LENGTH_SHORT).show();
    }


    // Creating the menu button for the admin, navigating to Users Menu screen.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAdmin) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.manageusers_menu, menu);
            return true;
        }
        return false;
    }
    // Set the on-click event listener when menu is clicked.
    //  Taking the admin back to Users Menu screen.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        changeActivity(new Intent(getApplicationContext(), UsersManageScreen.class));
        return super.onOptionsItemSelected(item);
    }
}