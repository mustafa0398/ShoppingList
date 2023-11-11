package com.oeztuerk.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private List<ShoppingList> shoppingLists;

    private SharedPreferencesListService listService;


    // Constructor to initialize the data source
    public ShoppingListAdapter(List<ShoppingList> shoppingLists, SharedPreferencesListService listService) {
        this.shoppingLists = shoppingLists;
        this.listService = listService; // Assign the service instance
    }

    // Define a ViewHolder to hold the views for each item
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShoppingList shoppingList = shoppingLists.get(position);

        if (shoppingList == null) {
            // Handle the case where shoppingList is null, e.g., show an error message or placeholder
            holder.nameTextView.setText("Item not available"); // Example placeholder text
            holder.iconImageView.setImageResource(R.drawable.default_icon); // Example placeholder image
            holder.iconImageView.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN); // Example placeholder color
        } else {
            holder.nameTextView.setText(shoppingList.getName());
            holder.iconImageView.setImageResource(shoppingList.getIcon());
            holder.iconImageView.setColorFilter(shoppingList.getColor(), PorterDuff.Mode.SRC_IN);

            // Set a long click listener on the item view
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // Handle long click here
                    showOptionsDialog(view.getContext(), shoppingList);
                    return true; // Consume the long click event
                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    private void showOptionsDialog(Context context, final ShoppingList shoppingList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(shoppingList.getName() + "?");

        // Edit option
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Delete option
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Zeigen Sie den Bestätigungsdialog an
                showDeleteConfirmationDialog(context, shoppingList);
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog
                dialog.dismiss();
            }
        });


        builder.show();
    }

    private void showDeleteConfirmationDialog(Context context, final ShoppingList shoppingList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Do you really want to delete '" + shoppingList.getName() + "'? This cannot be undone.");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the deletion action here
                // Remove the shopping list from the data source
                shoppingLists.remove(shoppingList);
                notifyDataSetChanged();

                // Delete the shopping list from SharedPreferences or your data source
                // Call the appropriate method from your service class to delete it
                listService.remove(shoppingList.getId());
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the deletion action
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void setShoppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }
}