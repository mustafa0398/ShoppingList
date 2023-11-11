package com.oeztuerk.shoppinglist;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public final class Utilities {

    public static String toString(ShoppingList shoppingList) {
        return new Gson().toJson(shoppingList);
    }

    public static ShoppingList shoppingListFromString(String shoppingListAsString) {
        return new Gson().fromJson(shoppingListAsString, ShoppingList.class);
    }

    public static String listOfShoppingListsToString(List<ShoppingList> listOfShoppingLists) {
        return new Gson().toJson(listOfShoppingLists);
    }

    public static List<ShoppingList> listOfShoppingListsFromString(String listOfShoppingListsAsString) {
        if (listOfShoppingListsAsString == null || listOfShoppingListsAsString.isEmpty()) {
            return new ArrayList<>();
        } else {
            return new Gson().fromJson(listOfShoppingListsAsString, new TypeToken<List<ShoppingList>>() {}.getType());
        }
    }

}
