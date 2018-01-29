package com.gramiware.mobile.tslocator.dal.models;

/**
 * Created by r.ghali on 1/25/2018.
 */

public class DBTowerStation {

    public long Id;
    public String Address;
    public String TId;
    public double Latitude, Longitude;

    public DBTowerStation() { }

    public DBTowerStation(long id, String tId, double lat, double lon, String address){
        this.Id = id;
        this.Address = address;
        this.TId = tId;
        this.Latitude = lat;
        this.Longitude = lon;

    }
}
