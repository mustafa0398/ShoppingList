package com.oeztuerk.shoppinglist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView gridRecyclerView;
    private static ShoppingListAdapter adapter;
    private static List<ShoppingList> shoppingLists;
    private static SharedPreferencesListService listService;
    private static final int ADD_LIST_REQUEST_CODE = 1;
    private static final int EDIT_LIST_REQUEST_CODE = 2; // Define a request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the Toolbar by its ID
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);

        // Set the title for the ActionBar
        getSupportActionBar().setTitle("Einkaufslisten");

        // Find and initialize the RecyclerView
        gridRecyclerView = findViewById(R.id.gridRecyclerView);
        gridRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Create an instance of SharedPreferencesListService
        listService = new SharedPreferencesListService(this); // Use the class-level variable

        // Retrieve shopping lists from SharedPreferencesListService
        shoppingLists = listService.shoppingLists(ListService.SortOrder.Alphabetical);

        // Initialize the adapter with the shopping lists
        adapter = new ShoppingListAdapter(shoppingLists, listService);

        // Set the adapter to the RecyclerView
        gridRecyclerView.setAdapter(adapter);

        // Find the FloatingActionButton by its ID
        FloatingActionButton fabAddList = findViewById(R.id.fabAddList);

        refreshShoppingLists();

        // Set a click listener for the FloatingActionButton
        fabAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the AddListActivity with a request code
                Intent intent = new Intent(MainActivity.this, AddEditListActivity.class);
                startActivityForResult(intent, ADD_LIST_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == ADD_LIST_REQUEST_CODE || requestCode == EDIT_LIST_REQUEST_CODE)) {
            // Refresh shopping lists data
            refreshShoppingLists();
        }
    }



    private void refreshShoppingLists() {
        shoppingLists = listService.shoppingLists(ListService.SortOrder.Alphabetical);
        adapter.setShoppingLists(shoppingLists); // Ensure your adapter has this method
        adapter.notifyDataSetChanged();
    }
}