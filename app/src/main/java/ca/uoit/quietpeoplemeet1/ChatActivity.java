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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity {

    IntentFilter mIntentFilter;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    String allPeers;
    public String recText;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    //WifiP2pManager.PeerListListener myPeerListListener;

    TextView peerView;
    TextView btView;
    Button blueToothButton;
    String passedMessage = "0";

    public static final String TAG = "DEVICE_FINDER_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        allPeers = "Peers: ";

        Bundle result = getIntent().getExtras();
        if (result != null) {

            recText = result.getString("Chat");
            passedMessage = recText;
        }
        //check permissions



        new TextNodeReceive().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        peerView = (TextView) findViewById(R.id.peerView);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new ChatDirectBroadcastReceiver(mManager, mChannel, this);


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

    public TextNode getCurrentTextNode() {


        String message;
        message = passedMessage;

        Log.d(TAG, "Text Node");
        Log.d(TAG, "Message:");


        return new TextNode(message);
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

                    new NetworkDiscovery(getApplicationContext(), (NsdManager) getApplicationContext().getSystemService(Context.NSD_SERVICE));

                }
                peerView.setText(allPeers);
            }
        });
    }

    public void attemptSend(View v) {
        new TextNodeSend(getCurrentTextNode());
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

        if (NetworkInfo.textNodes == null) {
            tv.setText("TextNodes is Empty");
        } else {

            String s = "< \n";
           TextNode textNode;
            for (int i = 0; i < NetworkInfo.textNodes.size(); i++) {
                textNode = NetworkInfo.textNodes.get(i);
                s += "latitude : ";
                s += textNode.getMessageLevel();
                s += "\n";
            }
            s += " >";


            tv.setText(s);
        }


    }

}
