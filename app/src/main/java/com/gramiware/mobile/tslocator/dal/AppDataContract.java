package com.gramiware.mobile.tslocator.dal;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by r.ghali on 1/25/2018.
 */

public abstract class AppDataContract {


    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.gramiware.mobile.tslocator";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_TOWER_STATION = "ts";

    public static class TowerStation implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOWER_STATION).build();

        public static final String TABLE_NAME = "TOWER_STATION";
        public static final String TID = "TID";
        public static final String LATITUDE = "LATITUDE";
        public static final String LONGITUDE = "LONGITUDE";
        public static final String ADDRESS = "ADDRESS";

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TID + " TEXT NOT NULL, " +
                LATITUDE + " TEXT NOT NULL," + LONGITUDE + " TEXT NOT NULL," +
                ADDRESS + " TEXT NOT NULL )";
    }
}
