package util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import tomatoes.rotten.erkanerol.refactor.R;

/**
 * Created by erkanerol on 8/15/14.
 */
public class Connections {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            Toast.makeText(context,context.getResources().getString(R.string.connectionError),Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }






}
