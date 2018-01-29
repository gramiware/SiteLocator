package com.gramiware.mobile.tslocator;

import android.content.Context;
import android.graphics.Typeface;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by r.ghali on 1/25/2018.
 */

public class TSApp {

    private Context _Context;
    private static TSApp _Instance;
    public Typeface AppFont;


    public Bus Messenger = new Bus(ThreadEnforcer.ANY); //Enforces that all events are ONLY sent from the main thread.

    public final class FragmentTags {
        public static final String AddTowerStation = "FTAG_ADD_TOWER";
        public static final String TowersList = "FTAG_TOWER_LIST";
    }

    public static TSApp getDefault(){
        if(_Instance == null)
            _Instance = new TSApp();

        return _Instance;
    }

    public Context getContext(){
        return _Context;
    }
}
