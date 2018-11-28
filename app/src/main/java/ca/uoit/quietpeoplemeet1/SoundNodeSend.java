package ca.uoit.quietpeoplemeet1;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SoundNodeSend {

private static final String TAG = "SOUND_NODE_SEND";

    public SoundNodeSend(SoundNode soundNode,WifiP2pDevice device){

        try{
            Socket socket = new Socket();
            socket.bind(null);
            socket.connect(new InetSocketAddress(device.deviceAddress.toString(), NetworkInfo.SERVER_PORT_NUMBER));

            Log.d(TAG,"Connected to " + device.deviceName);
            OutputStream outputStream = socket.getOutputStream();

            outputStream.write(serialize(soundNode));

            outputStream.close();

        } catch (Exception e){
            Log.d(TAG,"Could not send to " + device.deviceName);
            e.printStackTrace();
        }

    }



    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }


}

