package com.oeztuerk.shoppinglist;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class SharedPreferencesListService implements ListService {

    private static final String PREFS_NAME = "ShoppingListPrefs";
    private static final String SHOPPING_LISTS_KEY = "shoppingLists";

    private final Context context;
    private static SharedPreferences sharedPreferences = null;
    private final Gson gson;
    private final Type shoppingListType;

    public SharedPreferencesListService(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        shoppingListType = new TypeToken<List<ShoppingList>>() {}.getType();
    }


    public List<ShoppingList> shoppingLists(SortOrder sortOrder) {
        // Retrieve the list of shopping lists from SharedPreferences
        List<ShoppingList> shoppingLists = getShoppingListsFromPrefs();

        // Implement sorting logic based on the `sortOrder` parameter
        // You can sort the shoppingLists list here

        return shoppingLists;
    }

    @Nullable
    @Override
    public ShoppingList shoppingList(UUID listId) {
        // Retrieve a specific shopping list by its UUID from SharedPreferences
        List<ShoppingList> shoppingLists = getShoppingListsFromPrefs();
        for (ShoppingList shoppingList : shoppingLists) {
            if (shoppingList.getId().equals(listId)) {
                return shoppingList;
            }
        }
        return null; // Shopping list not found
    }

    @Override
    public void add(String name, @DrawableRes int icon, @ColorInt int color) {
        // Create a new shopping list and add it to SharedPreferences
        UUID listId = UUID.randomUUID();
        ShoppingList shoppingList = new ShoppingList(listId, name, icon, color);
        List<ShoppingList> shoppingLists = getShoppingListsFromPrefs();
        shoppingLists.add(shoppingList);
        saveShoppingListsToPrefs(shoppingLists);
    }

    @Override
    public void remove(UUID listId) {
        // Remove a shopping list from SharedPreferences
        List<ShoppingList> shoppingLists = getShoppingListsFromPrefs();
        ShoppingList shoppingListToRemove = null;

        for (ShoppingList shoppingList : shoppingLists) {
            if (shoppingList.getId().equals(listId)) {
                shoppingListToRemove = shoppingList;
                break;
            }
        }

        if (shoppingListToRemove != null) {
            shoppingLists.remove(shoppingListToRemove);
            saveShoppingListsToPrefs(shoppingLists);
        }
    }

    public void update(ShoppingList updatedShoppingList) {
        // Update an existing shopping list in SharedPreferences
        List<ShoppingList> shoppingLists = getShoppingListsFromPrefs();

        for (int i = 0; i < shoppingLists.size(); i++) {
            ShoppingList shoppingList = shoppingLists.get(i);
            if (shoppingList.getId().equals(updatedShoppingList.getId())) {
                shoppingLists.set(i, updatedShoppingList);
                saveShoppingListsToPrefs(shoppingLists);
                return; // Exit the loop once updated
            }
        }
    }

    // Implement other methods similarly

    // Helper methods to get and set shopping lists in SharedPreferences

    private static List<ShoppingList> getShoppingListsFromPrefs() {
        String json = sharedPreferences.getString(SHOPPING_LISTS_KEY, "");
        return Utilities.listOfShoppingListsFromString(json);
    }

    private static void saveShoppingListsToPrefs(List<ShoppingList> shoppingLists) {
        String json = Utilities.listOfShoppingListsToString(shoppingLists);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHOPPING_LISTS_KEY, json);
        editor.apply();
    }

    @Override
    public void addEntry(UUID listId, String name) {
        // Find the shopping list by listId and add an entry
        List<ShoppingList> shoppingLists = getShoppingListsFromPrefs();

        for (ShoppingList shoppingList : shoppingLists) {
            if (shoppingList.getId().equals(listId)) {
                ShoppingListEntry entry = new ShoppingListEntry(UUID.randomUUID(), name, false);
                shoppingList.getUncheckedEntries().add(entry);
                saveShoppingListsToPrefs(shoppingLists);
                return;
            }
        }
    }

    @Override
    public void checkEntry(UUID listId, int row) {

    }

    @Override
    public void uncheckEntry(UUID listId, int row) {

    }

    @Override
    public void uncheckAllEntries(UUID listId) {

    }

    @Override
    public void changeName(UUID listId, String name) {
        // Find the shopping list by listId and change its name
        List<ShoppingList> shoppingLists = getShoppingListsFromPrefs();

        for (ShoppingList shoppingList : shoppingLists) {
            if (shoppingList.getId().equals(listId)) {
                shoppingList.setName(name);
                saveShoppingListsToPrefs(shoppingLists);
                return;
            }
        }
    }

    @Override
    public void changeIcon(UUID listId, @DrawableRes int icon) {
        // Find the shopping list by listId and change its icon
        List<ShoppingList> shoppingLists = getShoppingListsFromPrefs();

        for (ShoppingList shoppingList : shoppingLists) {
            if (shoppingList.getId().equals(listId)) {
                shoppingList.setIcon(icon);
                saveShoppingListsToPrefs(shoppingLists);
                return;
            }
        }
    }

    @Override
    public void changeColor(UUID listId, @ColorInt int color) {
        // Find the shopping list by listId and change its color
        List<ShoppingList> shoppingLists = getShoppingListsFromPrefs();

        for (ShoppingList shoppingList : shoppingLists) {
            if (shoppingList.getId().equals(listId)) {
                shoppingList.setColor(color);
                saveShoppingListsToPrefs(shoppingLists);
                return;
            }
        }
    }
}