package com.gramiware.mobile.tslocator.controllers;

import android.text.TextUtils;

import com.gramiware.mobile.tslocator.R;
import com.gramiware.mobile.tslocator.TSApp;
import com.gramiware.mobile.tslocator.dal.models.DBTowerStation;
import com.gramiware.mobile.tslocator.dal.repositories.TSRepository;
import com.gramiware.mobile.tslocator.messages.OnConnectivityChangedMessage;
import com.gramiware.mobile.tslocator.messages.OnCreateNewTowerStationMessage;
import com.gramiware.mobile.tslocator.messages.OnSaveNewTowerStationMessage;
import com.gramiware.mobile.tslocator.models.TowerStation;
import com.gramiware.mobile.tslocator.utils.NetworkCheckTask;
import com.squareup.otto.Subscribe;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by r.ghali on 1/27/2018.
 */

public class TowerStationController implements TSRepository.ITowerStationRepositoryListener {

    private static TowerStationController instance;
    private TowerStation currentObservation;

    public static TowerStationController getDefault(){
        if(instance == null)
            instance = new TowerStationController();

        return instance;
    }

    private TowerStationController(){
        initCurrentObservation();
        TSRepository.getDefault().setOnResultListener(this);
        TSApp.getDefault().Messenger.register(this);
    }


    public void loadLocalObservations(){

    }

    public boolean add(DBTowerStation towerStation){
        if(towerStation == null || TextUtils.isEmpty(towerStation.TId)){
            return false;
        }


        return true;
    }

    @Subscribe
    public void onCreateNewObservation(OnCreateNewTowerStationMessage msg){
        if(msg == null)
            throw new NullPointerException(TSApp.getDefault().getContext()
                    .getString(R.string.msg_invalid_towerstation));

        initCurrentObservation();
    }

    @Subscribe
    public void onSaveNewObservation(OnSaveNewTowerStationMessage msg){
        //First check internet connectivity to save to the cloud
        new NetworkCheckTask(TSApp.getDefault().getContext(), TSApp.getDefault()
                .getContext().getString(R.string.msg_check_network), "",
                new NetworkCheckTask.NetCheckResultListener() {
            @Override
            public void OnResult(boolean pIsConnected) {
                if(pIsConnected){
                    String id = UUID.randomUUID().toString();
                    /*DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
                    CloudObservation observation = new CloudObservation(id, currentObservation.getLatitude(),
                            currentObservation.getLongitude(), currentObservation.getNotes(),
                            currentObservation.getType().ordinal());
                    observation.Images = currentObservation.getImages();
                    FirebaseManager.getDefault().addObservation(observation);*/
                }
                else{
                    TSApp.getDefault().Messenger.post(new OnConnectivityChangedMessage(false));
                    //Save the current observation locally
                    /*DBObservation dbObservation = new DBObservation();
                    dbObservation.Notes = currentObservation.getNotes();
                    dbObservation.LocationAsJSON = String.format("%f;%f", currentObservation.getLatitude(), currentObservation.getLongitude());
                    dbObservation.Type = String.valueOf(currentObservation.getType().ordinal());


                    if(ObservationRepository.getDefault().add(dbObservation)){

                        ThisApp.getDefault().Messenger.post(new OnNewObservationSaveResultMessage(""));
                    }
                    else {
                        ThisApp.getDefault().Messenger.post(new OnNewObservationSaveResultMessage("Error saving the observation to the local storage"));
                    }*/
                }
            }
        }).execute();

    }

    private void initCurrentObservation(){
        currentObservation = new TowerStation();


    }

    @Override
    public void OnTowerStationAddedResult(boolean isSuccessful) {
        if(isSuccessful){
            //TODO: Notify that a new observation is saved locally
        }
    }
}

