package com.example.android.store;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.store.data.StoreContract.StoreEntry;
import com.example.android.store.data.StoreDbHelper;

public class StoreActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    //loader unique ID
    public static final int STORE_LOADER = 0;

    private StoreDbHelper mStoreDbHelper;

    // initialize the cursor adapter
    StoreCustomAdapter cursorAdapter;

    //initialize the books listView
    ListView perfumeListView;

    private String mCurFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        perfumeListView = findViewById(R.id.list);


        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);

        perfumeListView.setEmptyView(emptyView);

        cursorAdapter = new StoreCustomAdapter(this, null);

        perfumeListView.setAdapter(cursorAdapter);

        getLoaderManager().initLoader(STORE_LOADER, null, this);

            // Setup FAB to open EditorActivity
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StoreActivity.this, StoreEditor.class);
                    startActivity(intent);
                }
            });

            perfumeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //send the Book Url using intent to the EditorActivity to use it as edit book
                    Intent intent = new Intent(StoreActivity.this, StoreEditor.class);
                    Uri contentUri = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, id);
                    intent.setData(contentUri);
                    startActivity(intent);

                }
            });
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu options from the res/menu/menu_catalog.xml file.
            // This adds menu items to the app bar.
            getMenuInflater().inflate(R.menu.menu_catalog, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // User clicked on a menu option in the app bar overflow menu
            switch (item.getItemId()) {
                // Respond to a click on the "Insert dummy data" menu option
                case R.id.action_insert_dummy_data:
                    insertDummy();
                    return true;
                // Respond to a click on the "Delete all entries" menu option
                case R.id.action_delete_all_entries:
                    deleteAllPerfumes();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void deleteAllPerfumes () {
            int rowsDeleted = getContentResolver().delete(StoreEntry.CONTENT_URI, null, null);
            Log.v("StoreActivity", rowsDeleted + " rows deleted from pet database");
        }


        private void insertDummy () {
            //initialize the object that will cary tha dummy data
            ContentValues values = new ContentValues();
            //add the dummy values
            values.put(StoreEntry.COLUMN_PERFUME_NAME, "Versace Eros");
            values.put(StoreEntry.COLUMN_PERFUME_PRICE, 100);
            values.put(StoreEntry.COLUMN_PERFUME_QUANTITY, 250);
            values.put(StoreEntry.COLUMN_PERFUME_SUPPLIER_NAME, "Versace");
            values.put(StoreEntry.COLUMN_PERFUME_SUPPLIER_CONTACT, "01244368863565");

            Uri Uri = getContentResolver().insert(StoreEntry.CONTENT_URI, values);

            if (Uri != null) {
                Toast.makeText(this, getString(R.string.dummy_data_inserted),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public Loader<Cursor> onCreateLoader ( int id, Bundle args){
            // This is called when a new Loader needs to be created.  This
            // sample only has one Loader, so we don't care about the ID.
            // First, pick the base URI to use depending on whether we are
            // currently filtering.
            Uri baseUri;
            if (mCurFilter != null) {
                baseUri = Uri.withAppendedPath(StoreEntry.CONTENT_URI,
                        Uri.encode(mCurFilter));
            } else {
                baseUri = StoreEntry.CONTENT_URI;
            }

            String[] projection = {StoreEntry._ID, StoreEntry.COLUMN_PERFUME_NAME,
                    StoreEntry.COLUMN_PERFUME_PRICE,
                    StoreEntry.COLUMN_PERFUME_QUANTITY};

            return new CursorLoader(getApplication(), baseUri,
                    projection, null, null,
                    null);
        }

        @Override
        public void onLoadFinished (Loader < Cursor > loader, Cursor data){
            cursorAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset (Loader < Cursor > loader) {
            // This is called when the last Cursor provided to onLoadFinished()
            // above is about to be closed.  We need to make sure we are no
            // longer using it.
            cursorAdapter.swapCursor(null);
        }
    }
