package ca.uoit.quietpeoplemeet1;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SoundNodeSend {

    private static final String TAG = "SOUND_NODE_SEND";
    public SoundNode soundNode;
    public WifiP2pDevice device;

    public SoundNodeSend(SoundNode soundNode, WifiP2pDevice device) {

        this.soundNode = soundNode;
        this.device = device;

        AsyncSend asyncSend = new AsyncSend();
        asyncSend.execute();

    }

    private class AsyncSend extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Socket socket = new Socket();
                socket.bind(null);
                socket.connect(new InetSocketAddress(device.deviceAddress, NetworkInfo.SERVER_PORT_NUMBER));

                Log.d(TAG, "Connected to " + device.deviceName);
                OutputStream outputStream = socket.getOutputStream();

                outputStream.write(serialize(soundNode));

                outputStream.close();

            } catch (Exception e) {
                Log.d(TAG, "Could not send to " + device.deviceName);
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }


}

