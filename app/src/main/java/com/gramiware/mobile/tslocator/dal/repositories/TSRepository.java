package com.gramiware.mobile.tslocator.dal.repositories;

import com.gramiware.mobile.tslocator.dal.AppDataContract;
import com.gramiware.mobile.tslocator.dal.DataManager;
import com.gramiware.mobile.tslocator.dal.models.DBTowerStation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by r.ghali on 1/27/2018.
 */

public class TSRepository implements DataManager.IDataManagerListener{
    private static TSRepository _Instance;
    private static DataManager _DataContext;
    public ITowerStationRepositoryListener _TowerStationListener;

    public interface ITowerStationRepositoryListener {
        void OnTowerStationAddedResult(boolean isSuccessful);
    }


    public void setOnResultListener(ITowerStationRepositoryListener listener){
        _TowerStationListener = listener;
    }

    private TSRepository(){
        _DataContext = new DataManager();
    }


    public static TSRepository getDefault(){
        if(_Instance == null){
            _Instance = new TSRepository();
            _DataContext.setOnResultListener(_Instance);
        }

        return _Instance;
    }


    public boolean add(DBTowerStation towerStation) {
        if(towerStation == null)
            return false;


        boolean result = false;
        JSONArray jsonArray = new JSONArray();
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(AppDataContract.TowerStation._ID, towerStation.TId);
            jsonObject.put(AppDataContract.TowerStation.LATITUDE, towerStation.Latitude);
            jsonObject.put(AppDataContract.TowerStation.LONGITUDE, towerStation.Longitude);
            jsonObject.put(AppDataContract.TowerStation.ADDRESS, towerStation.Address);

            jsonArray.put(jsonObject);
            result = _DataContext.insert(AppDataContract.TowerStation.TABLE_NAME, jsonArray);
        }
        catch (JSONException ex){
            result = false;
        }
        return result;
    }

    public List<DBTowerStation> getAll(){
        List<DBTowerStation> result = new ArrayList<>();
        //For Testing
        result.add(new DBTowerStation(1,"TS2534", 29.322233, 47.945953, "Salmiya - Block10"));
        result.add(new DBTowerStation(2,"TS2534", 29.328976, 47.960611, "Salmiya - Block10"));
        result.add(new DBTowerStation(3,"TS2534", 29.328976, 47.960611, "Salmiya - Block10"));

        return result;
        //Read local observations
        /*JSONArray jsonArrayResult = _DataContext.readAll(AppDataContract.TowerStation.TABLE_NAME,
                new String[] {
                        AppDataContract.TowerStation._ID,
                        AppDataContract.TowerStation.LATITUDE,
                        AppDataContract.TowerStation.LONGITUDE,
                        AppDataContract.TowerStation.ADDRESS
                });

        if(jsonArrayResult != null){
            try {
                for(int i=0; i < jsonArrayResult.length(); i++){
                    JSONObject jsonObservation = jsonArrayResult.getJSONObject(i);
                    DBTowerStation newDBObservation = new DBTowerStation();
                    newDBObservation.TId = jsonObservation.getString(AppDataContract.TowerStation._ID);
                    newDBObservation.Latitude = jsonObservation.getDouble(AppDataContract.TowerStation.LATITUDE);
                    newDBObservation.Longitude = jsonObservation.getDouble(AppDataContract.TowerStation.LONGITUDE);
                    newDBObservation.Address = jsonObservation.getString(AppDataContract.TowerStation.ADDRESS);

                    result.add(newDBObservation);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return result;*/
    }


    @Override
    public void OnInsertResult(String tableName, boolean isSuccessful) {
        if(_TowerStationListener != null)
            _TowerStationListener.OnTowerStationAddedResult(isSuccessful);
    }
}

