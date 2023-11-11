package com.oeztuerk.shoppinglist;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.UUID;

import petrov.kristiyan.colorpicker.ColorPicker;


public class AddEditListActivity extends AppCompatActivity {

    private EditText editTextListName;
    private ImageView selectedIconImageView;
    private Button buttonSelectIcon;
    private Button buttonSelectColor;
    private Button buttonSave;
    private SharedPreferencesListService listService; // To store the listService instance

    private int selectedIconResourceId; // To store the selected icon resource ID
    private int selectedColor; // To store the selected color

    private boolean isEditMode = false; // Flag to indicate edit mode
    private UUID listId; // ID of the shopping list being edited (if in edit mode)

    Integer[] iconResourceIds = {
            R.drawable.bag,     // Use the resource IDs for your vector icons
            R.drawable.library,
            R.drawable.medication
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_list_activity);

        // Initialize listService
        listService = new SharedPreferencesListService(this);

        // Find and initialize UI elements
        editTextListName = findViewById(R.id.editTextListName);
        selectedIconImageView = findViewById(R.id.selectedIconImageView);
        buttonSelectIcon = findViewById(R.id.buttonSelectIcon);
        buttonSelectColor = findViewById(R.id.buttonSelectColor);
        buttonSave = findViewById(R.id.buttonSave);

        // Find the Toolbar by its ID
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);

        // Set the title for the ActionBar
        getSupportActionBar().setTitle("Neue Einkaufsliste erstellen");



            // Handle adding a new shopping list (not editing)
            // Create a default ShoppingList object with placeholder values
        ShoppingList newShoppingList = new ShoppingList(UUID.randomUUID(), "New Shopping List", R.drawable.default_icon, Color.GRAY);

        // Set the default values for a new shopping list
        editTextListName.setText(newShoppingList.getName());
        selectedIconResourceId = newShoppingList.getIcon();
        selectedColor = newShoppingList.getColor();
        // Initialize UI elements with the default icon and color
        selectedIconImageView.setImageResource(selectedIconResourceId);
        selectedIconImageView.setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);


        buttonSelectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom dialog to display icon selection
                Dialog iconSelectionDialog = new Dialog(AddEditListActivity.this);
                iconSelectionDialog.setContentView(R.layout.dialog_icon_selection);

                // Initialize the ListView in the custom dialog
                ListView iconListView = iconSelectionDialog.findViewById(R.id.iconListView);

                // Set up the adapter for the ListView with your list of icons
                IconAdapter iconAdapter = new IconAdapter(AddEditListActivity.this, iconResourceIds);
                iconListView.setAdapter(iconAdapter);

                // Set an item click listener to handle icon selection
                iconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Handle icon selection and update the selectedIconResourceId
                        selectedIconResourceId = iconResourceIds[position];

                        // Update the selected icon in the ImageView
                        selectedIconImageView.setImageResource(selectedIconResourceId);

                        // Dismiss the dialog when an icon is selected
                        iconSelectionDialog.dismiss();
                    }
                });

                // Show the custom icon selection dialog
                iconSelectionDialog.show();
            }
        });

        buttonSelectColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorPicker colorPicker = new ColorPicker(AddEditListActivity.this);
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        // Update the selectedColor with the chosen color
                        selectedColor = color;

                        // Change the color of the selected icon in the ImageView
                        selectedIconImageView.setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onCancel() {
                        // Handle cancel (if needed)
                    }
                });
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = editTextListName.getText().toString();

                // Check if the list name is empty
                if (listName.isEmpty()) {
                    Toast.makeText(AddEditListActivity.this, "Bitte geben Sie einen Namen für die Liste ein", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Initialize listId
                UUID listId = UUID.randomUUID();

                listService.add(listName, selectedIconResourceId, selectedColor);

                // Send the result back to MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("shoppingListId", listId.toString());
                setResult(RESULT_OK, resultIntent);

                // Finish the activity
                finish();
            }
        });
    }
}