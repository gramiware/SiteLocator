package com.gramiware.mobile.tslocator.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.gramiware.mobile.tslocator.R;
import com.gramiware.mobile.tslocator.TSApp;

/**
 * Created by r.ghali on 1/25/2018.
 */

public class Utils {

    private static LayoutInflater AppLayoutInflater = null;

    public enum ToastType{
        Error,
        Instruction,
        Info
    }

    /*
    check whether array is empty or null
     */
    public static boolean IsArrayNullOrEmpty(Object[] pValue){
        return pValue == null || pValue.length == 0;
    }

    public static void showToast(Context pContext, ToastType pToastType, String pMessage){
        if(!TextUtils.isEmpty(pMessage)){
            if(AppLayoutInflater == null)
                AppLayoutInflater = (LayoutInflater)pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int tvResourceID = R.layout.tv_toast_template;
            int tvBackgroundResourceID = R.drawable.toast_background;

            /*switch (pToastType){
                case Error:
                    tvBackgroundResourceID = R.drawable.toast_error_background;
                    break;
                case Instruction:
                    tvBackgroundResourceID = R.drawable.toast_instruction_background;
                    break;
                case Info:
                    tvBackgroundResourceID = R.drawable.toast_background;
                    break;
            }*/

            TextView tv = (TextView) AppLayoutInflater.inflate(tvResourceID, null);
            tv.setBackground(ContextCompat.getDrawable(pContext, tvBackgroundResourceID));
            tv.setText(pMessage);
            tv.setTypeface(TSApp.getDefault().AppFont);

            Toast toast = new Toast(pContext);
            toast.setView(tv);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
}
