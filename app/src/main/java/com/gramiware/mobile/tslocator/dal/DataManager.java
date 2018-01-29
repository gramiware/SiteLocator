package com.gramiware.mobile.tslocator.dal;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.gramiware.mobile.tslocator.TSApp;
import com.gramiware.mobile.tslocator.utils.Utils;

import org.json.JSONArray;

/**
 * Created by r.ghali on 1/27/2018.
 */

public class DataManager  implements IDataSource {

    private DBHelper _DBHelper;
    private final String DB_NAME = "TS_LOCATOR.db";
    private final int DB_VERSION = 1;
    private IDataManagerListener _Listener;

    public interface IDataManagerListener{
        void OnInsertResult(String tableName, boolean isSuccessful);
    }

    public void setOnResultListener(IDataManagerListener dataManagerListener){
        _Listener = dataManagerListener;
    }

    public DBHelper getDB(){
        if(_DBHelper == null)
            _DBHelper = new DBHelper(TSApp.getDefault().getContext(), DB_NAME, DB_VERSION);

        return _DBHelper;
    }

    public DataManager() {
        this._DBHelper = new DBHelper(TSApp.getDefault().getContext(), DB_NAME, DB_VERSION);
    }

    @Override
    public boolean insert(String pTableName, JSONArray pValue) {
        new InsertAsyncTask(pTableName, pValue).execute();

        return true;
    }

    @Override
    public boolean delete(String pTableName, String[] pSelection, String[] pSelectionArgs) {
        boolean result = false;
        if(!TextUtils.isEmpty(pTableName)){
            int deletedCount = 0;
            if(Utils.IsArrayNullOrEmpty(pSelection) || Utils.IsArrayNullOrEmpty(pSelectionArgs)){
                deletedCount = getDB().deleteAllRecords(pTableName);
            }
            else{
                deletedCount = getDB().deleteRecord(pTableName, pSelection, pSelectionArgs);
            }
            result = deletedCount > 0;
        }
        return result;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean update(String pTableName, String[] pSelection, String[] pSelectionArgs, String[] pUpdateCols, String[] pUpdateArgs) {
        return getDB().updateRecord(pTableName,pSelection,pSelectionArgs,pUpdateCols,pUpdateArgs) > 0;
    }

    @Override
    public JSONArray readAll(String pTableName, String[] pFields) {
        JSONArray result = getDB().readTable(pTableName, pFields);
        return result;
    }

    @Override
    public JSONArray read(String pTableName, String[] pFields, String[] pSelection, String[] pSelectionArgs, String pGroupBy, String pOrderBy) {
        JSONArray result = getDB().readTable(pTableName, pFields,pSelection,pSelectionArgs);
        return result;
    }

    @Override
    public JSONArray read(String[] pTableName, String[] pFields, String[] pFilterField, String[] pFilterValue,
                          String[] pTableLink){
        JSONArray result = getDB().readTable(pTableName,pFields,pFilterField,pFilterValue,pTableLink);
        return result;
    }

    private class InsertAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private String _TableName = "";
        private JSONArray _DataValues;

        public InsertAsyncTask(String tableName, JSONArray pValue){
            _DataValues = pValue;
            _TableName = tableName;
        }

        @Override
        protected Boolean doInBackground(Void... strings) {
            boolean result = false;
            if(strings != null && strings.length > 0){
                if(!TextUtils.isEmpty(_TableName) && _DataValues != null && _DataValues.length() > 0){
                    long recordCount = getDB().insertRecord(_TableName,_DataValues);
                    result = recordCount > 0;
                }
            }

            return result;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(_Listener != null)
                _Listener.OnInsertResult(_TableName, aBoolean);
        }
    }
}

