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
import java.util.List;

public class TextNodeSend {

    private static final String TAG = "TEXT_NODE_SEND";
    public TextNode textNode;
    //public InetAddress inetAddress;

    public TextNodeSend(TextNode textNode) {


        this.textNode = textNode;
        //this.inetAddress = inetAddress;


        Log.d(TAG, "Entered TextNodeSend");
        new AsyncSend().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private class AsyncSend extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            for (InetAddress address : NetworkInfo.peerAddresses) {
                Log.d(TAG, "Attempting to connect to" + address);

                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(address, NetworkInfo.SERVER_PORT_NUMBER));

                    Log.d(TAG, "Connected to " + address);
                    OutputStream outputStream = socket.getOutputStream();

                    outputStream.write(serialize(textNode));

                    outputStream.close();
                    Log.d(TAG, "Closed Stream to send to" + address);

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
        Log.d(TAG,"Byte array size is :" + out.toByteArray().length );
        return out.toByteArray();
    }


}

