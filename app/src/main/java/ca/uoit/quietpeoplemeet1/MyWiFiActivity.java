package ca.uoit.quietpeoplemeet1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.net.nsd.NsdManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MyWiFiActivity extends AppCompatActivity {

    IntentFilter mIntentFilter;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    String allPeers;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    //WifiP2pManager.PeerListListener myPeerListListener;

    TextView peerView;
    TextView btView;
    Button blueToothButton;

    public static final String TAG = "DEVICE_FINDER_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wifi);
        allPeers = "Peers: ";
        //check permissions

        new SoundNodeReceive().execute();


        peerView = (TextView) findViewById(R.id.peerView);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);


        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        //allow listener for ONE incoming node

    }


    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }


    public void connectToPeer(WifiP2pDevice d) {

        final WifiP2pDevice device = d;
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                //success logic
                Log.d(TAG, "Connected to device : " + device.deviceName);
            }

            @Override
            public void onFailure(int reason) {
                //failure logic
                Log.d(TAG, "Failed to connect to device : " + device.deviceName);
            }
        });

    }

    public SoundNode getCurrentSoundNode() {

        LocationFinder locationFinder = new LocationFinder(this);
        double longitude, latitude;
        latitude = locationFinder.getLatitude();
        longitude = locationFinder.getLongitude();
        SoundReader s = new SoundReader();

        double currentSoundLevel = 0;
        try {
            s.start();

            //busyWait until a sound value is given
            while (s.getAmplitude() == -1) ;
            currentSoundLevel = s.getAmplitude();


        } catch (Exception e) {
            e.printStackTrace();
        }


        return new SoundNode(latitude, longitude, currentSoundLevel);
    }


    public void requestPeers(View v) {
        mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {

                for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {

                    //Log what devices were found
                    Log.d(TAG, "found device : " + device.deviceName);
                    Toast.makeText(getApplicationContext(), "found device : " + device.deviceName, Toast.LENGTH_SHORT).show();
                    allPeers = allPeers.concat(device.deviceName);
                    allPeers = allPeers.concat(" | ");



                    /**
                     * OK here's my idea:
                     * for each peer start a thread attempting to connect and send the info needed
                     *
                     * create a listener that starts on a thread (AsyncTask?) that awaits incoming connections from a set port
                     * that listener will be ready to receive the location and sound data and then package in a SoundNode object
                     * the sound node will then be sent to a global List where the map activity will read it and post the found sound nodes
                     *
                     * hoping to give a timestamp to the sound nodes so the global list can get rid of old soundNodes
                     *
                     */




                        new NetworkDiscovery(getApplicationContext(),(NsdManager)getApplicationContext().getSystemService(Context.NSD_SERVICE));

                }
                peerView.setText(allPeers);
            }
        });
    }

    public void attemptSend(View v){
        new SoundNodeSend();
    }

    //button reaction to discover peers
    public void discoverPeers(View v) {

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "DISCOVER PEERS SUCCESS", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(getApplicationContext(), "DISCOVER PEERS FAIL WITH CODE " + reasonCode, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void updateNodeList(View v) {

        TextView tv = findViewById(R.id.NodeListTextView);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        if (NetworkInfo.soundNodes == null) {
            tv.setText("SoundNodes is Empty");
        } else {

            String s = "< \n";
            SoundNode soundNode;
            for (int i = 0; i < NetworkInfo.soundNodes.size(); i++) {
                soundNode = NetworkInfo.soundNodes.get(i);
                s += "latitude : ";
                s += soundNode.getLatitude();
                s += ", longitude : ";
                s += soundNode.getLongitude();
                s += ", soundLevel : ";
                s += soundNode.getSoundLevel();
                s += "\n";
            }
            s += " >";


            tv.setText(s);
        }


    }




}
