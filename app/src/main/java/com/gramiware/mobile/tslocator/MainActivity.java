package com.gramiware.mobile.tslocator;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.gramiware.mobile.tslocator.dal.models.DBTowerStation;
import com.gramiware.mobile.tslocator.fragments.AddTowerStationFragment;
import com.gramiware.mobile.tslocator.fragments.TowerStationListFragment;
import com.gramiware.mobile.tslocator.utils.Utils;

import java.net.URI;

public class MainActivity extends AppCompatActivity  implements TowerStationListFragment.OnListFragmentInteractionListener,
        View.OnClickListener, AddTowerStationFragment.OnTowerAddedListener {

    private FrameLayout _FrmMain;
    private FloatingActionButton _FabAdd;


    private enum ViewEnum {
        TowersListView,
        AddTowerView
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _FrmMain = (FrameLayout) findViewById(R.id.frmMain);
        _FabAdd = (FloatingActionButton) findViewById(R.id.fabAddTowerStation);
        _FabAdd.setOnClickListener(this);

        showFragment(ViewEnum.TowersListView, null);


    }

    @Override
    public void onListFragmentInteraction(DBTowerStation item) {

    }

    @Override
    public void onNavigateToLocation(DBTowerStation item){

        if(item == null){
            Utils.showToast(this, Utils.ToastType.Error, "This tower has invalid location");
            return;
        }

        openMapForNavigation(item.Latitude, item.Longitude);
    }


    @Override
    public void onEdit(DBTowerStation item){
        editTower(item.TId);
    }

    @Override
    public void onTowerAddedResult(boolean saved) {
        showFragment(ViewEnum.TowersListView, null);
        View focusView = getCurrentFocus();
        if(focusView != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        }
    }

    private void openMapForNavigation(double lat, double lon){

        Uri gMapUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
        Intent gMapIntent = new Intent(Intent.ACTION_VIEW, gMapUri);
        gMapIntent.setPackage("com.google.android.apps.maps");

        if(gMapIntent.resolveActivity(getPackageManager()) != null){
            startActivity(gMapIntent);
        }
        else{
            Utils.showToast(this, Utils.ToastType.Info, "Google Maps app not found");
        }
    }


    private void editTower(String towerStationId){
        if(towerStationId.length() == 0)
            return;

        showFragment(ViewEnum.AddTowerView, towerStationId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabAddTowerStation:
                showFragment(ViewEnum.AddTowerView, null);
                break;

        }

    }


    private void showFragment(ViewEnum viewEnum, String towerStationId){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (viewEnum){
            case AddTowerView:
                _FabAdd.setVisibility(View.GONE);
                setActionBarTitle(getString(R.string.ab_title_tower_add));
                AddTowerStationFragment addTowerStationFragment = AddTowerStationFragment.newInstance(towerStationId, "");

                ft.replace(_FrmMain.getId(), addTowerStationFragment, TSApp.FragmentTags.AddTowerStation);
                ft.commit();
                break;
            case TowersListView:
                _FabAdd.setVisibility(View.VISIBLE);
                setActionBarTitle(getString(R.string.ab_title_tower_list));
                TowerStationListFragment listFragment = (TowerStationListFragment) fm.findFragmentByTag(TSApp.FragmentTags.TowersList);
                if(listFragment == null){
                    listFragment = TowerStationListFragment.newInstance(1);
                }

                ft.replace(_FrmMain.getId(), listFragment, TSApp.FragmentTags.TowersList);
                ft.commit();
                break;
        }


    }


    private void setActionBarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(title);
        }
    }


}
