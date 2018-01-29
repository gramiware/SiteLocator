package com.gramiware.mobile.tslocator.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.gramiware.mobile.tslocator.R;
import com.gramiware.mobile.tslocator.dal.AppDataContract;
import com.gramiware.mobile.tslocator.dal.models.DBTowerStation;
import com.gramiware.mobile.tslocator.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTowerAddedListener} interface
 * to handle interaction events.
 * Use the {@link AddTowerStationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTowerStationFragment extends Fragment implements View.OnClickListener {


    private EditText _EtId, _EtLat, _EtLong, _EtAddress;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TOWER_STATION_ID = "TS_ID";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String _TowerStationId;
    private String mParam2;

    private OnTowerAddedListener mListener;

    public AddTowerStationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param towerStationId Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTowerStationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTowerStationFragment newInstance(String towerStationId, String param2) {
        AddTowerStationFragment fragment = new AddTowerStationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TOWER_STATION_ID, towerStationId);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _TowerStationId = getArguments().getString(ARG_TOWER_STATION_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_tower_station, container, false);

        Button btnSave = (Button) root.findViewById(R.id.btnSaveNewTower);
        Button btnCancel = (Button) root.findViewById(R.id.btnCancelNewTower);

        _EtAddress = (EditText) root.findViewById(R.id.etTowerStationAddress);
        _EtId = (EditText) root.findViewById(R.id.etTowerStationId);
        _EtLat = (EditText) root.findViewById(R.id.etTowerStationLat);
        _EtLong = (EditText) root.findViewById(R.id.etTowerStationLong);

        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        //Check if it is created to edit
        if(!TextUtils.isEmpty(_TowerStationId))
            new GetTowerData().execute(_TowerStationId);

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTowerAddedResult(true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTowerAddedListener) {
            mListener = (OnTowerAddedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTowerAddedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnSaveNewTower:
                String validationError = isInputValid();
                if(!TextUtils.isEmpty(validationError)){
                    Utils.showToast(getActivity(), Utils.ToastType.Error, validationError);
                    return;
                }

                new AddTowerAsync().execute(new DBTowerStation(-1, _EtId.getText().toString(), Double.parseDouble(_EtLat.getText().toString()),
                                            Double.parseDouble(_EtLong.getText().toString()),
                                            _EtAddress.getText().toString().length() == 0? "NA" : _EtAddress.getText().toString()));

                break;
            case R.id.btnCancelNewTower:
                hideSoftKeypad();

                mListener.onTowerAddedResult(false);
                break;
        }

    }


    private void hideSoftKeypad(){
        View focusView = getActivity().getCurrentFocus();
        if(focusView != null){
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        }
    }

    private String isInputValid(){


        if(TextUtils.isEmpty(_EtId.getText())){
            return "Tower Id is required";
        }

        if(TextUtils.isEmpty(_EtLong.getText())){
            return "Tower location - longitude is required";
        }

        if(TextUtils.isEmpty(_EtId.getText())){
            return "Tower location - latitude is required";
        }

        return "";
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTowerAddedListener {
        // TODO: Update argument type and name
        void onTowerAddedResult(boolean saved);

    }

    private class GetTowerData extends AsyncTask<String, Void, DBTowerStation>{

        @Override
        protected DBTowerStation doInBackground(String... towerStationIds) {
            if(towerStationIds == null || towerStationIds[0].length() == 0)
                return null;

            Uri uri = AppDataContract.TowerStation.CONTENT_URI;
            uri = uri.buildUpon().appendEncodedPath(towerStationIds[0]).build();

            Cursor cursor = getContext().getContentResolver().query(uri,
                                            null, null, null, null);

            if(cursor != null && cursor.moveToFirst()){
                DBTowerStation towerStation = new DBTowerStation(cursor.getLong(0),
                        cursor.getString(1), cursor.getDouble(2),
                        cursor.getDouble(3), cursor.getString(4));

                return towerStation;
            }
            return null;
        }

        @Override
        protected void onPostExecute(DBTowerStation dbTowerStation) {
            if(dbTowerStation != null){
                _EtAddress.setText(dbTowerStation.Address);
                _EtId.setText(dbTowerStation.TId);
                _EtLat.setText(String.valueOf(dbTowerStation.Latitude));
                _EtLong.setText(String.valueOf(dbTowerStation.Longitude));
            }
            else {
                Utils.showToast(getActivity(), Utils.ToastType.Error, "Failed to get tower data");
            }
        }
    }

    private class AddTowerAsync extends AsyncTask<DBTowerStation, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            //TODO: Show spinning
        }

        @Override
        protected Boolean doInBackground(DBTowerStation... dbTowerStations) {
            if(dbTowerStations.length == 0 || dbTowerStations[0] == null){
                return false;
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(AppDataContract.TowerStation.ADDRESS, dbTowerStations[0].Address);
            contentValues.put(AppDataContract.TowerStation.TID, dbTowerStations[0].TId);
            contentValues.put(AppDataContract.TowerStation.LATITUDE, dbTowerStations[0].Latitude);
            contentValues.put(AppDataContract.TowerStation.LONGITUDE, dbTowerStations[0].Longitude);

            Uri resultUri = getContext().getContentResolver().insert(AppDataContract.TowerStation.CONTENT_URI, contentValues);

            if(resultUri != null)
                return true;
            else
                return false;

            //return  TSRepository.getDefault().add(dbTowerStations[0]);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            //TODO: Hide spinning

            if(isSuccess)
                mListener.onTowerAddedResult(true);
            else
                Utils.showToast(getActivity(), Utils.ToastType.Error, "Failed adding new tower");
        }
    }
}
