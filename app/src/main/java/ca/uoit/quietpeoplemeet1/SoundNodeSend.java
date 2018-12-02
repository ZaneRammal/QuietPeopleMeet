package ca.uoit.quietpeoplemeet1;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * this class handles sending a sound node object via sockets
 */
public class SoundNodeSend {

    //debug tag
    private static final String TAG = "SOUND_NODE_SEND";

    //sound node to be sent
    public SoundNode soundNode;

    public SoundNodeSend(SoundNode soundNode) {

        this.soundNode = soundNode;

        Log.d(TAG, "Entered SoundNodeSend");

        // execute send operation via a thread
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

                    outputStream.write(serialize(soundNode));

                    outputStream.close();
                    Log.d(TAG, "Closed Stream to send to" + address);

                } catch (Exception e) {
                    Log.d(TAG, "Could not send to " + address);
                    e.printStackTrace();
                }

            }
            return null;
        }

        /**
         * does nothing but is required for AsyncTask superclass
         * @param aVoid unused
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    /**
     * convert an object into a byte array to be sent
     * @param obj object to be serialized
     * @return return byte array of object
     * @throws IOException for stream reading
     */
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        Log.d(TAG, "Byte array size is :" + out.toByteArray().length);
        return out.toByteArray();
    }


}

