package util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import tomatoes.rotten.erkanerol.refactor.R;

/**
 * Created by erkanerol on 8/15/14.
 */
public class Actions {

    public static void openAppInMarket(Context context,String appId){

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + appId));

        if (!MyStartActivity(context,intent)) {
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+appId));

            if (!MyStartActivity(context,intent)) {
                Toast.makeText(context,context.getResources().getString(R.string.marketError) , Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static void shareUrl(Context context,String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static void shareTextPlain(Context context,String share){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT,share);
        context.startActivity(sharingIntent);
    }








    private static boolean MyStartActivity(Context context,Intent aIntent) {
        try
        {
            context.startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }


}
