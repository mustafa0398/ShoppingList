package com.oeztuerk.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class IconAdapter extends ArrayAdapter<Integer> {
    private final Context context;
    private final Integer[] iconResourceIds;

    public IconAdapter(Context context, Integer[] iconResourceIds) {
        super(context, R.layout.list_item_icon, iconResourceIds);
        this.context = context;
        this.iconResourceIds = iconResourceIds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_icon, parent, false);
        }

        ImageView iconImageView = convertView.findViewById(R.id.iconImageView);

        // Set the vector icon for this item in the ImageView
        iconImageView.setImageResource(iconResourceIds[position]);

        return convertView;
    }
}