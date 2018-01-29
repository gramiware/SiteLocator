package com.gramiware.mobile.tslocator.dal;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by macbookpro on 1/29/18.
 */

public class TSContentProvider extends ContentProvider {


    private DBHelper _DBHelper;

    // Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    private static final int TOWER_STATION = 100;
    private static final int TOWER_STATION_WITH_ID = 101;
    private static final int TOWER_STATION_WITH_TID = 102;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Add Uri
        uriMatcher.addURI(AppDataContract.AUTHORITY, AppDataContract.PATH_TOWER_STATION, TOWER_STATION);
        uriMatcher.addURI(AppDataContract.AUTHORITY, AppDataContract.PATH_TOWER_STATION + "/#", TOWER_STATION_WITH_ID);
        uriMatcher.addURI(AppDataContract.AUTHORITY, AppDataContract.PATH_TOWER_STATION + "/*", TOWER_STATION_WITH_TID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        _DBHelper = new DBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteDatabase database = _DBHelper.getReadableDatabase();

        Cursor resultCursor;

        //Get Matching Code
        int matchId = sUriMatcher.match(uri);

        switch (matchId){
            case TOWER_STATION:
                resultCursor = database.query(AppDataContract.TowerStation.TABLE_NAME,
                                    projection, selection, selectionArgs, null, null, sortOrder);

                break;
            case TOWER_STATION_WITH_ID:
                String idValue = uri.getPathSegments().get(1);
                String mSelection = AppDataContract.TowerStation._ID + "=?";
                String[] mSelectionArgs = new String[] { idValue };

                resultCursor = database.query(AppDataContract.TowerStation.TABLE_NAME, projection,
                        mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            case TOWER_STATION_WITH_TID:
                String tIdValue = uri.getPathSegments().get(1);
                String mTSelection = AppDataContract.TowerStation.TID + "=?";
                String[] mTSelectionArgs = new String[] { tIdValue };

                resultCursor = database.query(AppDataContract.TowerStation.TABLE_NAME, projection,
                        mTSelection, mTSelectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        resultCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return resultCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        if(contentValues == null){
            throw new UnsupportedOperationException("Unsupported values for uri: " + uri);
        }

        SQLiteDatabase database = _DBHelper.getWritableDatabase();

        int uriMatchId = sUriMatcher.match(uri);

        Uri resultUri;

        switch (uriMatchId){
            case TOWER_STATION:
                long returnId = database.insert(AppDataContract.TowerStation.TABLE_NAME, null, contentValues);
                if(returnId > 0){
                    resultUri = ContentUris.withAppendedId(AppDataContract.TowerStation.CONTENT_URI, returnId);
                    database.close();
                }
                else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = _DBHelper.getWritableDatabase();

        int uriMatchId = sUriMatcher.match(uri);

        int numRowsDeleted = 0;
        switch (uriMatchId){
            case TOWER_STATION_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = AppDataContract.TowerStation._ID + "=?";
                String[] mSelectionArgs = new String[] { id };

                numRowsDeleted = database.delete(AppDataContract.TowerStation.TABLE_NAME, mSelection, mSelectionArgs);

                database.close();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(numRowsDeleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }


        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
