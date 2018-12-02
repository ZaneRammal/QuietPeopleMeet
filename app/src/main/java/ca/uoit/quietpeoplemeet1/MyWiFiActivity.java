package ca.uoit.quietpeoplemeet1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class handles all activity requests related to sending and receiving sound node data
 */
public class MyWiFiActivity extends AppCompatActivity {

    //intent filter required by WifiP2p
    IntentFilter mIntentFilter;

    //Wifi p2p components of manager and channel to handle info
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;

    //listens for broadcasts
    BroadcastReceiver mReceiver;
    String allPeers;
    LocationFinder locationFinder;
    SoundReader soundReader;

    //text views that hold peers detected
    TextView peerView;
    TextView btView;

    //button intended to initiate bluetooth communication
    Button blueToothButton;

    // debugging tag
    public static final String TAG = "DEVICE_FINDER_ACTIVITY";

    /**
     * @param savedInstanceState android default saved state
     *                           <p>
     *                           setup UI elements and start essential operations for the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wifi);
        allPeers = "Peers: ";

        //start location finder
        this.locationFinder = new LocationFinder(this);
        // start sound reader
        this.soundReader = new SoundReader();
        try {
            soundReader.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to start sound recording");
        }


        //start thread that indefinitely resets the server socket to receive messages
        NetworkInfo.ServerSocketInUse = false;
        new Thread(new Runnable() {
            public void run() {

                while (true) {

                    if (NetworkInfo.ServerSocketInUse == false) {
                        NetworkInfo.ServerSocketInUse = true;
                        Log.d("ServerSocket thread", "Starting server Socket");
                        new SoundNodeReceive().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        peerView = (TextView) findViewById(R.id.peerView);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);


        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }


    /**
     * register the broadcast receiver with the intent values to be matched
     */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    /**
     * unregister the broadcast receiver
     */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    /**
     * @param d device to connect to
     *          used to connect to a device via wifiDirect
     */
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

    /**
     * @return return sound node object
     * <p>
     * used to get the current location and sound info
     */
    public SoundNode getCurrentSoundNode() {


        //get location data
        double longitude, latitude;
        latitude = locationFinder.getLatitude();
        longitude = locationFinder.getLongitude();

        Log.d(TAG, "Sound Node : Latitude " + latitude + ", Longitude : " + longitude);


        //get sound data
        double currentSoundLevel = 0;
        try {

            double max = currentSoundLevel;
            for (int i = 0; i < 1000; i++) {
                currentSoundLevel = soundReader.getAmplitude();
                if (currentSoundLevel > max) {
                    max = currentSoundLevel;
                }
            }
            currentSoundLevel = max;
            Log.d(TAG, "SoundLevelCaptured to node: " + soundReader.getAmplitude() +
                    " current Sound level is : " + currentSoundLevel);


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        //package data into SoundNode object and return
        return new SoundNode(latitude, longitude, currentSoundLevel);
    }


    /**
     * button response used to detect peers on network
     * @param v button view pressed to initiate method
     */
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


                    new NetworkDiscovery(getApplicationContext(), (NsdManager) getApplicationContext().getSystemService(Context.NSD_SERVICE));

                }
                peerView.setText(allPeers);
            }
        });
    }

    /**
     * Button response to send sound node data to all discovered IP addresses
     * @param v button view pressed to initiate method
     */
    public void attemptSend(View v) {
        new SoundNodeSend(getCurrentSoundNode());
    }

    /**
     * Button response to discover peers on network
     * @param v button view pressed to initiate method
     */
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

    /**
     * update text view to show contents of current soundNode objects found
     * @param v button view pressed to initiate method
     */
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
