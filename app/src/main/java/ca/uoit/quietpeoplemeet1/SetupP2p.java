package ca.uoit.quietpeoplemeet1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.*;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;



public class SetupP2p {


    public WifiP2pManager wpm;
    WifiP2pManager.Channel channel;

    final String TAG = "setupP2P";


    NsdManager mNsdManager;

    public SetupP2p(Context c) {

        wpm = (WifiP2pManager) c.getSystemService(Context.WIFI_P2P_SERVICE);

        this.channel = wpm.initialize(c,Looper.getMainLooper(),null);

        //WifiDirectBroadcastReceiver receiver = new WifiDirectBroadcastReceiver(wpm,channel,c);


    }





}
