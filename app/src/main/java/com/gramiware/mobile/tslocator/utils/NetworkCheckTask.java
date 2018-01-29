package com.gramiware.mobile.tslocator.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

/**
 * Created by r.ghali on 1/27/2018.
 */

public class NetworkCheckTask  extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog _pDlg;
    private Context _context;
    private String _message, _title;
    private NetCheckResultListener _listenr;

    public interface NetCheckResultListener{
        void OnResult(boolean pIsConnected);
    }

    public NetworkCheckTask(Context pContext, String pMessage, String pTitle, NetCheckResultListener pResultListener) {
        this._context = pContext;
        _message = pMessage;
        _title = pTitle;
        _listenr = pResultListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _pDlg = new ProgressDialog(_context);
        _pDlg.setMessage(_message);
        _pDlg.setTitle(_title);
        _pDlg.setIndeterminate(false);
        _pDlg.setCancelable(true);
        _pDlg.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    @Override
    protected void onPostExecute(Boolean t3){
        _pDlg.dismiss();
        if(_listenr != null)
            _listenr.OnResult(t3);
    }
}

