package com.gramiware.mobile.tslocator.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.gramiware.mobile.tslocator.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by r.ghali on 1/25/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private String _dbPath = "";
    private boolean _isValidDB = true;

    public DBHelper(Context pContext){
        super(pContext,"TS_LOCATOR.db",null,1);
        //_dbPath = new StringBuffer(pDBPath).append("/").append(DB_NAME).toString();
    }


    public DBHelper(Context pContext, String dbName, int dbVersion){
        super(pContext,dbName,null,dbVersion);
        //_dbPath = new StringBuffer(pDBPath).append("/").append(DB_NAME).toString();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create DB tables
        db.execSQL(AppDataContract.TowerStation.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     add one or more records to a table
     */
    public long insertRecord(String pTableName, JSONArray pData){
        long result = 0;
        if(!TextUtils.isEmpty(pTableName) && pData != null && pData.length() > 0){
            SQLiteDatabase db = getWritableDatabase();
            for(int i=0; i<pData.length(); i++){
                try{
                    ContentValues newRecord = new ContentValues();
                    JSONObject jsonObj = pData.getJSONObject(i);
                    Iterator jsonKeys = jsonObj.keys();
                    while(jsonKeys.hasNext()){
                        String key = (String) jsonKeys.next();
                        if(jsonObj.get(key) instanceof Integer)
                            newRecord.put(key,(int) jsonObj.get(key));
                        else
                            newRecord.put(key,(String) jsonObj.get(key));
                    }
                    result += db.insert(pTableName,null,newRecord);
                }
                catch (JSONException ex){}
            }
            db.close();
        }
        return result;
    }

    private long insertRecord(SQLiteDatabase db, String pTableName, JSONArray pData){
        long result = 0;
        if(!TextUtils.isEmpty(pTableName) && pData != null && pData.length() > 0){

            for(int i=0; i<pData.length(); i++){
                try{
                    ContentValues newRecord = new ContentValues();
                    JSONObject jsonObj = pData.getJSONObject(i);
                    Iterator jsonKeys = jsonObj.keys();
                    while(jsonKeys.hasNext()){
                        String key = (String) jsonKeys.next();
                        if(jsonObj.get(key) instanceof Integer)
                            newRecord.put(key,(int) jsonObj.get(key));
                        else
                            newRecord.put(key,(String) jsonObj.get(key));
                    }
                    result += db.insert(pTableName,null,newRecord);
                }
                catch (JSONException ex){}
            }

        }
        //db.close();
        return result;
    }

    /**
     select all rows from a table
     */
    public JSONArray readTable(String pTableName, String[] pFields){
        JSONArray result = new JSONArray();
        if(!TextUtils.isEmpty(pTableName) && pFields != null && pFields.length > 0){
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(pTableName, pFields, null, null, null, null, null);
            result = cursorToJSONArray(cursor);
            db.close();
        }
        return result;
    }

    /**
     select rows from a table
     */
    public JSONArray readTable(String pTableName, String[] pFields, String[] pFilterField, String[] pFilterValue){
        JSONArray result = new JSONArray();
        if(!(TextUtils.isEmpty(pTableName) || Utils.IsArrayNullOrEmpty(pFields) ||
                Utils.IsArrayNullOrEmpty(pFilterField) || Utils.IsArrayNullOrEmpty(pFilterValue))){
            SQLiteDatabase db = getReadableDatabase();
            //Build where clause
            String whereClause = "";
            for(int i=0; i<pFilterField.length; i++){
                if(TextUtils.isEmpty(whereClause))
                    whereClause = pFilterField[i] + " =? ";
                else
                    whereClause += " AND " + pFilterField[i] + " =? ";
            }
            Cursor cursor = db.query(pTableName, pFields, whereClause, pFilterValue, null, null, null);
            result = cursorToJSONArray(cursor);
            db.close();
        }
        return result;
    }

    /**
     select rows from two tables or more
     */
    public JSONArray readTable(String[] pTableName, String[] pFields, String[] pFilterField, String[] pFilterValue,
                               String[] pTableLink){
        JSONArray result = new JSONArray();
        if(!(Utils.IsArrayNullOrEmpty(pTableName) || Utils.IsArrayNullOrEmpty(pFields) ||
                Utils.IsArrayNullOrEmpty(pFilterField) || Utils.IsArrayNullOrEmpty(pFilterValue) ||
                Utils.IsArrayNullOrEmpty(pTableLink))){
            SQLiteDatabase db = getReadableDatabase();

            //Build FROM clause
            String selectClause = "";
            for(int i=0; i < pFields.length; i++){
                if(TextUtils.isEmpty(selectClause))
                    selectClause = pFields[i];
                else
                    selectClause += "," + pFields[i];
            }

            //Build FROM clause
            String fromClause = "";
            for(int i=0; i < pTableName.length; i++){
                if(TextUtils.isEmpty(fromClause))
                    fromClause = pTableName[i];
                else
                    fromClause += "," + pTableName[i];
            }

            //Build where clause
            String whereClause = "";
            for(int i=0; i<pFilterField.length; i++){
                if(TextUtils.isEmpty(whereClause))
                    whereClause = pFilterField[i] + " =? ";
                else
                    whereClause += " AND " + pFilterField[i] + " =? ";
            }

            //Build links between tables
            for(int i=0; i < pTableLink.length; i++){
                if(TextUtils.isEmpty(whereClause))
                    whereClause = pTableLink[i];
                else
                    whereClause += " AND " + pTableLink[i];
            }

            String sql = "SELECT " + selectClause + " FROM " + fromClause + " WHERE " + whereClause;

            Cursor cursor = db.rawQuery(sql, pFilterValue);
            result = cursorToJSONArray(cursor);
            db.close();
        }
        return result;
    }

    public int deleteAllRecords(String pTableName){
        int result =0;
        if(!(TextUtils.isEmpty(pTableName))){
            SQLiteDatabase db = getWritableDatabase();
            result = db.delete(pTableName, null, null);
            db.close();
        }
        return result;
    }

    public int deleteRecord(String pTableName, String[] pSelection, String[] pSelectionArgs){
        int result =0;
        if(!(TextUtils.isEmpty(pTableName) || Utils.IsArrayNullOrEmpty(pSelection) ||
                Utils.IsArrayNullOrEmpty(pSelectionArgs))){
            //Build where clause
            String whereClause = "";
            for(int i=0; i<pSelection.length; i++){
                if(TextUtils.isEmpty(whereClause))
                    whereClause = pSelection[i] + " =? ";
                else
                    whereClause += " AND " + pSelection[i] + " =? ";
            }
            SQLiteDatabase db = getWritableDatabase();
            result = db.delete(pTableName,whereClause,pSelectionArgs);
            db.close();
        }
        return result;
    }

    public int updateRecord(String pTableName, String[] pSelection, String[] pSelectionArgs,
                            String[] pUpdateCols, String[] pUpdateArgs){
        int result =0;
        if(!(TextUtils.isEmpty(pTableName) || Utils.IsArrayNullOrEmpty(pSelection) ||
                Utils.IsArrayNullOrEmpty(pSelectionArgs) || Utils.IsArrayNullOrEmpty(pUpdateCols) ||
                Utils.IsArrayNullOrEmpty(pUpdateArgs) || pUpdateCols.length != pUpdateArgs.length ||
                pSelection.length != pSelectionArgs.length)){
            //Build where clause
            String whereClause = "", updateClause = "";
            ContentValues updateValues = new ContentValues();
            for(int i=0; i < pSelection.length; i++){
                if(TextUtils.isEmpty(whereClause))
                    whereClause = pSelection[i] + " =? ";
                else
                    whereClause += " AND " + pSelection[i] + " =? ";
            }

            for(int i=0; i < pUpdateCols.length; i++){
                updateValues.put(pUpdateCols[i] , pUpdateArgs[i]);
            }

            SQLiteDatabase db = getWritableDatabase();
            result = db.update(pTableName, updateValues, whereClause, pSelectionArgs);
            db.close();
        }
        return result;
    }

    public int updateAll(String pTableName, String[] pUpdateCols, String[] pUpdateArgs){
        int result =0;
        if(!(TextUtils.isEmpty(pTableName) || Utils.IsArrayNullOrEmpty(pUpdateCols) ||
                Utils.IsArrayNullOrEmpty(pUpdateArgs) || pUpdateCols.length != pUpdateArgs.length)){

            ContentValues updateValues = new ContentValues();
            for(int i=0; i < pUpdateCols.length; i++){
                updateValues.put(pUpdateCols[i] , pUpdateArgs[i]);
            }

            SQLiteDatabase db = getWritableDatabase();
            result = db.update(pTableName, updateValues,null,null);
            db.close();
        }
        return result;
    }

    /**
     converts cursor to JSON Array
     */
    private JSONArray cursorToJSONArray(Cursor pCursor){
        JSONArray result = new JSONArray();
        if(pCursor != null){
            if(pCursor.moveToFirst()){
                while (!pCursor.isAfterLast()){
                    JSONObject newJSON = new JSONObject();
                    try{
                        for(int i=0; i < pCursor.getColumnCount(); i++){
                            String columnName = pCursor.getColumnName(i);
                            //int columnType = cursor.getType(i);
                            Object value = "NA";
                            switch (pCursor.getType(i)){
                                case Cursor.FIELD_TYPE_INTEGER:
                                    value = pCursor.getInt(i);
                                    break;
                                case Cursor.FIELD_TYPE_STRING:
                                    value = pCursor.getString(i);
                                    break;
                                case Cursor.FIELD_TYPE_FLOAT:
                                    value = pCursor.getFloat(i);
                                    break;
                                default:
                                    value = pCursor.getString(i);
                                    break;
                            }
                            newJSON.put(columnName,value);
                        }
                    }
                    catch (JSONException ex){ }
                    result.put(newJSON);
                    pCursor.moveToNext();
                }
            }
        }
        return result;
    }
}

