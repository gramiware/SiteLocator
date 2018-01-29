package com.gramiware.mobile.tslocator.dal;

import org.json.JSONArray;

/**
 * Created by r.ghali on 1/25/2018.
 */

public interface IDataSource {
    boolean insert(String pTableName, JSONArray pValue);
    boolean delete(String pTableName, String[] pSelection, String[] pSelectionArgs);
    boolean update();
    boolean update(String pTableName, String[] pSelection, String[] pSelectionArgs, String[] pUpdateCols, String[] pUpdateArgs);
    JSONArray readAll(String pTableName, String[] pFields);
    JSONArray read(String pTableName, String[] pFields, String[] pSelection, String[] pSelectionArgs, String pGroupBy, String pOrderBy);
    JSONArray read(String[] pTableName, String[] pFields, String[] pFilterField, String[] pFilterValue,
                   String[] pTableLink);
}
