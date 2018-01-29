package com.gramiware.mobile.tslocator.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gramiware.mobile.tslocator.R;
import com.gramiware.mobile.tslocator.TSApp;
import com.gramiware.mobile.tslocator.dal.AppDataContract;
import com.gramiware.mobile.tslocator.dal.models.DBTowerStation;
import com.gramiware.mobile.tslocator.dal.repositories.TSRepository;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TowerStationListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int TS_LOADER_ID = 1;
    private TowerStationRecyclerViewAdapter _RecyclerViewAdapter;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TowerStationListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TowerStationListFragment newInstance(int columnCount) {
        TowerStationListFragment fragment = new TowerStationListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            _RecyclerViewAdapter = new TowerStationRecyclerViewAdapter(null, mListener);
            recyclerView.setAdapter(_RecyclerViewAdapter);

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                    //Delete the attached item

                    Uri uri = AppDataContract.TowerStation.CONTENT_URI;

                    uri = uri.buildUpon().appendPath(String.valueOf(viewHolder.itemView.getTag())).build();

                    getContext().getContentResolver().delete(uri, null, null);


                }
            }).attachToRecyclerView(recyclerView);
            //recyclerView.setAdapter(new TowerStationRecyclerViewAdapter(TSRepository.getDefault().getAll(), mListener));
        }
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        getLoaderManager().initLoader(TS_LOADER_ID, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id){
            case TS_LOADER_ID:
                Uri baseUri = AppDataContract.TowerStation.CONTENT_URI;

                String[] projection = new String[] {
                        AppDataContract.TowerStation._ID,
                        AppDataContract.TowerStation.TID,
                        AppDataContract.TowerStation.LATITUDE,
                        AppDataContract.TowerStation.LONGITUDE,
                        AppDataContract.TowerStation.ADDRESS
                };
                return new CursorLoader(getContext(), baseUri, projection, null, null, null);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        _RecyclerViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        _RecyclerViewAdapter.swapCursor(null);

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(DBTowerStation item);
        void onNavigateToLocation(DBTowerStation item);
        void onEdit(DBTowerStation item);
    }
}
