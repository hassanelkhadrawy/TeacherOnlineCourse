package com.example.teacheronlinecourse.Commans;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.teacheronlinecourse.Models.RegisterModel;
import com.example.teacheronlinecourse.R;

public class Commans {
    public static ProgressDialog progressDialog;
   public static RegisterModel registerModel;

    public static void Prograss(Context context,String Message){
        progressDialog=new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(Message);
        progressDialog.setCancelable(false);

    }

    //for checking connection internet
    public static boolean isConnectToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

}
