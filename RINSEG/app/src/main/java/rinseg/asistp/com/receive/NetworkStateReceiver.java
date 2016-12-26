package rinseg.asistp.com.receive;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import rinseg.asistp.com.intent_services.RopIntentServices;
import rinseg.asistp.com.rinseg.R;

/**
 * Created by Carlos Ramos on 19/12/2016.
 */
public class NetworkStateReceiver extends WakefulBroadcastReceiver {

    private static boolean firstConnect = true;
    private static int typeConect = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            return;
        }


        if (intent.getAction() != null && intent.getAction().equals("recieve")) {
            Log.d("", "broadcast received !!");
        }


        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();

        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
          //  if (firstConnect) {
                try{
                    Intent newIntent = new Intent(context, RopIntentServices.class);
                    context.startService(newIntent);
                }catch (Exception e){
                    e.printStackTrace();
                    return;
                }


                if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
                    Log.e("conectado", "si wifi");
                    if (typeConect != ni.getType()) {
                        typeConect = ni.getType();
                        firstConnect = true;
                    }else {
                        firstConnect = false;
                    }



                } else if (ni.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Log.e("conectado", "si datos");
                    if (typeConect != ni.getType()) {
                        typeConect = ni.getType();
                        firstConnect = true;
                    }else {
                        firstConnect = false;
                    }
                }


            //}


        } else {
            firstConnect = true;
            Log.e("conectado", "no");
        }
    }
}
