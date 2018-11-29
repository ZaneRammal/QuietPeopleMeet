package ca.uoit.quietpeoplemeet1;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SoundNodeSend {

    private static final String TAG = "SOUND_NODE_SEND";
    public SoundNode soundNode;
    //public InetAddress inetAddress;

    public SoundNodeSend() {
        Log.d(TAG, "Attempting to connect to list of addresses");

        //this.soundNode = soundNode;
        //this.inetAddress = inetAddress;


        AsyncSend asyncSend = new AsyncSend();
        asyncSend.execute();

    }

    private class AsyncSend extends AsyncTask<InetAddress, Void, Void> {

        @Override
        protected Void doInBackground(InetAddress... inetAddresses) {

            for (InetAddress address : NetworkInfo.peerAddresses) {


                try {
                    Socket socket = new Socket();
                    socket.bind(null);
                    socket.connect(new InetSocketAddress(address, NetworkInfo.SERVER_PORT_NUMBER));

                    Log.d(TAG, "Connected to " + address);
                    OutputStream outputStream = socket.getOutputStream();

                    outputStream.write(serialize(soundNode));

                    outputStream.close();

                } catch (Exception e) {
                    Log.d(TAG, "Could not send to " + address);
                    e.printStackTrace();
                }

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

