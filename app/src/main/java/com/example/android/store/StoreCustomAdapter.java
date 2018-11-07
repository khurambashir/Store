package com.example.android.store;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.store.data.StoreContract.StoreEntry;
import com.example.android.store.data.StoreDbHelper;


public class StoreCustomAdapter extends CursorAdapter {
    int quantityChange;

    StoreDbHelper mStoreDbHelper;


    public StoreCustomAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = view.findViewById(R.id.name);

        TextView quantityTextView = view.findViewById(R.id.quantity);

        TextView priceTextView = view.findViewById(R.id.price);



        // Find the columns of Perfume attributes that we're interested in
        int idColumnIndex = cursor.getColumnIndex(StoreEntry._ID);

        int productNameColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PERFUME_NAME);

        int quantityColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PERFUME_QUANTITY);

        int priceColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PERFUME_PRICE);

        // Read the book attributes from the Cursor for the current Perfume
        String productName = cursor.getString(productNameColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);
        double price = cursor.getDouble(priceColumnIndex);

        // Update the TextViews with the attributes for the current book
        nameTextView.setText(productName);
        quantityTextView.setText("Quantity: " + quantity);
        priceTextView.setText("Price: " + price);


    }


}
