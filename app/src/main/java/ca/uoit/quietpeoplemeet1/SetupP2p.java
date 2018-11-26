package ca.uoit.quietpeoplemeet1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class SetupP2p {


    final String TAG = "setupP2P";


    NsdManager mNsdManager;

    public SetupP2p() {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        // tag of which is used for others on the network to recognize the program by
        serviceInfo.setServiceName("QuietPeopleMeet");

        // uses http protocol using tcp sockets
        serviceInfo.setServiceType("_http._tcp.");

        // arbitrary port number
        serviceInfo.setPort(50210);

    }

}
