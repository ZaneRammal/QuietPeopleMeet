package ca.uoit.quietpeoplemeet1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

public class P2pReceive extends BroadcastReceiver {


    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private P2PActivity activity;

    public P2pReceive(WifiP2pManager manager, WifiP2pManager.Channel channel, P2PActivity activity) {

        this.manager = manager;
        this.channel = channel;
        this.activity = activity;


    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras() != null) {

            //  Default values are used to indicate when something has gone wrong, because the values are unlikely if not impossible
            double latitude = intent.getDoubleExtra("latitude", 0);
            double longitude = intent.getDoubleExtra("longitude", 0);
            int soundLevel = intent.getIntExtra("soundLevel", -1);



        } else {
            System.out.println("Empty intent given to broadcast receiver");
        }


    }


}
