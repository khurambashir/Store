package com.example.android.store.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.store.data.StoreContract.StoreEntry;

public class StoreProvider extends ContentProvider {

    private static final String LOG_TAG = StoreProvider.class.getSimpleName();

    //Uri match code for Content URI
    public final static int PERFUME = 100;

    //Uri Match code for Content Uri for Single perfume
    public final static int PERFUME_ID = 101;

    private static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_STORE, PERFUME);

        mUriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_STORE + "/#", PERFUME_ID);
    }

    StoreDbHelper mStoreDB;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        //get version form the Books Database
        mStoreDB = new StoreDbHelper(getContext());
        return true;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //get readable database instance
        SQLiteDatabase database = mStoreDB.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        //set the matcher of the store URI
        final int match = mUriMatcher.match(uri);

        // Figure out if the URI matcher can match the URI to a specific code
        switch (match) {
            case PERFUME:
                cursor = database.query(StoreEntry.TABLE_NAME_PERFUME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PERFUME_ID:
                selection = StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StoreEntry.TABLE_NAME_PERFUME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PERFUME:
                return StoreEntry.CONTENT_LIST_TYPE;
            case PERFUME_ID:
                return StoreEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        //initialize the matcher
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PERFUME:
                return insertPerfume(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertPerfume(Uri uri, ContentValues values) {

        // Check that the name is not null
        String name = values.getAsString(StoreEntry.COLUMN_PERFUME_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Perfume Name is Required");
        }

        // Check that the price is not null and is not lesser than 0
        Integer price = values.getAsInteger(StoreEntry.COLUMN_PERFUME_PRICE);
        if (price == null && price < 0) {
            throw new IllegalArgumentException("Please Add Valid Price");
        }

        // Check that the quantity is not null and is not lesser than 0
        Integer quantity = values.getAsInteger(StoreEntry.COLUMN_PERFUME_QUANTITY);
        if (quantity == null && quantity < 0) {
            throw new IllegalArgumentException("Perfume Quantity is Required");
        }

        // Check that the supplier name is not null
        String supplierName = values.getAsString(StoreEntry.COLUMN_PERFUME_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Please Enter Supplier Name ");
        }
        // Check that the store phone is not null
        String storePhone = values.getAsString(StoreEntry.COLUMN_PERFUME_SUPPLIER_CONTACT);
        if (storePhone == null) {
            throw new IllegalArgumentException("Please enter 10 digit Phone Number");
        }

        // Get writable database
        SQLiteDatabase database = mStoreDB.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(StoreEntry.TABLE_NAME_PERFUME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = mStoreDB.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted;
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PERFUME:
                rowsDeleted = database.delete(StoreEntry.TABLE_NAME_PERFUME, selection, selectionArgs);
                break;
            case PERFUME_ID:
                selection = StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(StoreEntry.TABLE_NAME_PERFUME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PERFUME:
                return updatePerfume(uri, values, selection, selectionArgs);
            case PERFUME_ID:
                selection = StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePerfume(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePerfume(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Check that the name is not null
        if (values.containsKey(StoreEntry.COLUMN_PERFUME_NAME)) {
            String name = values.getAsString(StoreEntry.COLUMN_PERFUME_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Perfume Name is Required");
            }
        }

        // Check that the price is not null and is not lesser than 0
        if (values.containsKey(StoreEntry.COLUMN_PERFUME_PRICE)) {
            Integer price = values.getAsInteger(StoreEntry.COLUMN_PERFUME_PRICE);
            if (price == null && price < 0) {
                throw new IllegalArgumentException("Price is Required");
            }
        }

        // Check that the quantity is not null and is not lesser than 0
        if (values.containsKey(StoreEntry.COLUMN_PERFUME_QUANTITY)) {
            Integer quantity = values.getAsInteger(StoreEntry.COLUMN_PERFUME_QUANTITY);
            if (quantity == null && quantity < 0) {
                throw new IllegalArgumentException("quantity is required");
            }
        }

        // Check that the supplier name is not null
        if (values.containsKey(StoreEntry.COLUMN_PERFUME_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(StoreEntry.COLUMN_PERFUME_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Supplier Name is Required");
            }
        }
        // Check that the store phone is not null
        if (values.containsKey(StoreEntry.COLUMN_PERFUME_SUPPLIER_CONTACT)) {
            String storePhone = values.getAsString(StoreEntry.COLUMN_PERFUME_SUPPLIER_CONTACT);
            if (storePhone == null) {
                throw new IllegalArgumentException("Please enter Supplier Contact");
            }
        }
        //get the writable database to update into
        SQLiteDatabase database = mStoreDB.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(StoreEntry.TABLE_NAME_PERFUME, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}

