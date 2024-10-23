package com.example.bit603_a3_adriannicotabuzo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InventoryStatusScreen extends AppCompatActivity {

    public static MyDatabase myDatabase;

    private Button buttonNext, buttonPrev;

    private ListView listViewInventoryList;

    private TextView textViewInventoryName, textViewInventoryQuantity, textViewInventoryType;

    private int index = 0;

    private List<Inventory> allInventoryItem;

    private List<String> displayItems = new ArrayList<>();;

    private static final String TAG = "InventoryStatusScreen";

    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_status_screen);

        // Initialise the database.
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

        // List of all inventory items.
        allInventoryItem = myDatabase.dao().getInventory();

        // Useful message: Prints number of all inventory items.
        Log.i(TAG, "Inventory size: " + String.valueOf(allInventoryItem.size()));

        // Initialise widgets.
        textViewInventoryName = findViewById(R.id.textViewIInventoryName);
        textViewInventoryQuantity = findViewById(R.id.textViewIInventoryQuantity);
        textViewInventoryType = findViewById(R.id.textViewIInventoryType);
        buttonNext = findViewById(R.id.buttonIInventoryNext);
        buttonPrev = findViewById(R.id.buttonIInventoryPrev);
        listViewInventoryList = findViewById(R.id.listViewIInventoryList);

        // Displays the first 5 items on the list view for initial.
        displayNextFiveItems();

        // Update the listview.
        showList();

        // For the event when an item is click from the listview.
        listViewInventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get from the database the user selected.
                Inventory selectedInventoryItemDetails = myDatabase.dao().getInventoryItemDetails(displayItems.get(position));

                // Print each detail to their respective textview.
                textViewInventoryName.setText(selectedInventoryItemDetails.getName());
                textViewInventoryQuantity.setText(String.valueOf(selectedInventoryItemDetails.getQuantity()));
                textViewInventoryType.setText(selectedInventoryItemDetails.getType());
            }
        });

        // Show the next 5 item from inventory list to the listview.
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNextFiveItems();
            }
        });

        // Show the previous 5 item from inventory list to the listview.
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPreviousFiveItems();
            }
        });
    }

    // Store the next 5 items from the inventory list to a list to then be displayed on the list view.
    private void displayNextFiveItems() {
        // Store 5 items into the subset list.
        // But will not proceed once the index is greater than whole inventory list.
        if (index < allInventoryItem.size()) {
            // Refresh the list of 5 items to be displayed.
            displayItems.clear();
            for (int i = 0; i < 5; i++) {
                if (index < allInventoryItem.size())
                    displayItems.add(allInventoryItem.get(index).getName());
                index++;
            }
        }
        else { // Show message: No more items to display.
            Toast.makeText(getBaseContext(), "No more items!", Toast.LENGTH_SHORT).show();
        }
        // Display current 5 items to the listView widget.
        showList();

        // Clear the item details textView.
        clearItemDetails();
    }

    // Store the previous 5 items from the inventory list to a list to then be displayed on the list view.
    private void displayPreviousFiveItems() {
        // If the index is at 10 or greater then it is currently showing the next 5 or
        //  the further 5 from the list.
        if (index >= 10) {
            displayItems.clear();

            //  Need to subtract 10 from current index to start from previous 5,
            //   so that we can start from the first to last of that 5 batch items.
            index -= 10;

            // Iterate 5 times through the inventory list.
            for (int i = 0; i < 5; i++) {
                // Add the previous 5 items to the list to be displayed.
                //  If the index is not within the size of whole inventory list then skip
                //   but still deduct 1 from the index.
                if (!(index > allInventoryItem.size()))
                    displayItems.add(allInventoryItem.get(index).getName());
                index++;
            }
        }

        else { // Show message: No more items to display.
            Toast.makeText(getBaseContext(), "No more items!", Toast.LENGTH_SHORT).show();
        }
        // Display current 5 items to the listView widget.
        showList();

        // Clear the item details textView.
        clearItemDetails();
    }


    // Display to listView.
    private void showList() {
        // Setting adapter for the listView items to reflect usernames list.
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayItems);
        // Setting the listView adapter.
        listViewInventoryList.setAdapter(arrayAdapter);
    }

    // Clear the item details textView.
    private void clearItemDetails() {
        textViewInventoryName.setText("");
        textViewInventoryQuantity.setText("");
        textViewInventoryType.setText("");
    }


    // Creating the menu on the Inventory Status Screen.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manageinventory_menu, menu);
        return true;
    }
    // Setting the on-click event listener when menu is clicked.
    //  Take the user back to Inventory screen.
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
}