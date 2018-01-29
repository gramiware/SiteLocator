package com.gramiware.mobile.tslocator.fragments;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gramiware.mobile.tslocator.R;
import com.gramiware.mobile.tslocator.dal.models.DBTowerStation;
import com.gramiware.mobile.tslocator.fragments.TowerStationListFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DBTowerStation} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TowerStationRecyclerViewAdapter extends RecyclerView.Adapter<TowerStationRecyclerViewAdapter.ViewHolder>
                                            implements View.OnClickListener{

    //private final List<DBTowerStation> mValues;
    private Cursor _Cursor;
    private final OnListFragmentInteractionListener mListener;

    public TowerStationRecyclerViewAdapter(Cursor dataCursor, OnListFragmentInteractionListener listener) {
        //mValues = items;
        _Cursor = dataCursor;
        mListener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(_Cursor != null && _Cursor.moveToPosition(position)){
            DBTowerStation towerStation = new DBTowerStation(
                    _Cursor.getLong(0), _Cursor.getString(1), _Cursor.getDouble(2),
                    _Cursor.getDouble(3), _Cursor.getString(4));

            holder.TowerStationData = towerStation;
            holder.mIdView.setText(towerStation.TId);
            holder.mLocationView.setText(towerStation.Latitude + ", " + towerStation.Longitude);
            holder.mAddressView.setText(towerStation.Address);
            holder.mView.setTag(towerStation.Id);
            holder.mBtnNavigateToLocation.setTag(towerStation);
            holder.mBtnEdit.setTag(towerStation);

            holder.mView.setOnClickListener(this);
            holder.mBtnEdit.setOnClickListener(this);
            holder.mBtnNavigateToLocation.setOnClickListener(this);


        }

//        holder.TowerStationData = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).TId);
//        holder.mLocationView.setText(mValues.get(position).Latitude + ", " + mValues.get(position).Longitude);
//        holder.mAddressView.setText(mValues.get(position).Address);
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.TowerStationData);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return _Cursor != null ? _Cursor.getCount() : 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnNavigateToTowerLocation:
                if(null != mListener)
                    mListener.onNavigateToLocation((DBTowerStation) view.getTag());
                break;
            case R.id.btnEditTower:
                if(null != mListener)
                    mListener.onEdit((DBTowerStation) view.getTag());
                break;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mAddressView;
        public final TextView mLocationView;
        public final ImageButton mBtnNavigateToLocation, mBtnEdit;
        public final View mAllContentView;

        public DBTowerStation TowerStationData;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mAddressView = (TextView) view.findViewById(R.id.content);
            mLocationView = (TextView) view.findViewById(R.id.latLong);

            mBtnNavigateToLocation = (ImageButton) view.findViewById(R.id.btnNavigateToTowerLocation);
            mBtnEdit = (ImageButton) view.findViewById(R.id.btnEditTower);

            mAllContentView = view.findViewById(R.id.lytContent);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAddressView.getText() + "'";
        }
    }


    public void swapCursor(Cursor cursor){
        if(cursor == _Cursor)
            return;

        _Cursor = cursor;
        notifyDataSetChanged();
    }
}
