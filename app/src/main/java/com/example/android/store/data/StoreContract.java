package com.example.android.store.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class StoreContract {
    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.store";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/books/ is a valid path for
     * looking at pet data. content://com.example.android.store/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_STORE = "store";


    private StoreContract(){}

    public static final class StoreEntry implements BaseColumns{

        /**
         * The content URI to access the store data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STORE);
        // name of data base table for store
        public static final String TABLE_NAME_PERFUME = "perfume";



        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_STORE;



        public static final String CONTENT_ITEM_TYPE = ContentResolver.ANY_CURSOR_ITEM_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_STORE;

        /**
         * Unique ID number for the pet (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;
        /**
         * Name of the Perfume.
         *
         * Type: TEXT
         */
        public static final  String COLUMN_PERFUME_NAME = "name";
        /**
         * price of the perfume.
         *
         * Type: TEXT
         */
        public static final String COLUMN_PERFUME_PRICE = "price";
        /**
         *quantity of perfume
         *
         * Type: INTEGER
         **/
        public static final String COLUMN_PERFUME_QUANTITY = "quantity";
        /**
         * The supplier of the perfumes
         *
         * Type: INTEGER
         */
        public static final String COLUMN_PERFUME_SUPPLIER_NAME = "supplier_name";
        /**
         * Phone number of the supplier
         *
         * Type: TEXT
         */
        public static final String COLUMN_PERFUME_SUPPLIER_CONTACT = "supplier_contact";

    }

}
