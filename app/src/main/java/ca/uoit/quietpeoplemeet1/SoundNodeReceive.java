package ca.uoit.quietpeoplemeet1;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * this class handles receiving socket connections to specifically receive SoundNode objects
 */
public class SoundNodeReceive extends AsyncTask<Void, Void, SoundNode> {

    //debug tag
    private final String TAG = "SoundNodeReceive";

    /**
     * asynchronously handle receiving a socket connection and data transfer
     *
     * @param params nothing
     * @return return sound node object
     */
    protected SoundNode doInBackground(Void... params) {

        NetworkInfo.ServerSocketInUse = true;

        SoundNode soundNodeReturn = null;

        Log.d(TAG, "Starting SoundNodeReceive Listener");


        try {

            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            ServerSocket serverSocket = new ServerSocket(NetworkInfo.SERVER_PORT_NUMBER);
            Socket client = serverSocket.accept();
            Log.d(TAG, "Accepted Client " + client.getInetAddress());

            //if this has been reached the client has accepted

            InputStream inputstream = client.getInputStream();

            byte[] buffer = new byte[115];

            //assume receiving a sound node object and read and cast
            inputstream.read(buffer, 0, buffer.length);

            soundNodeReturn = (SoundNode) deserialize(buffer);


            serverSocket.close();

            Log.d(TAG, "Adding sound level " + soundNodeReturn.getSoundLevel()
                    + " Lat " + soundNodeReturn.getLatitude() + " long " + soundNodeReturn.getLongitude());

            NetworkInfo.soundNodes.add(soundNodeReturn);
        } catch (IOException e) {
            Log.e(TAG, "IOEXCEPTION : " + e.getStackTrace());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Class Exception : " + e.getMessage());
        }


        return soundNodeReturn;

    }

    /**
     * sets boolean to say that server socket is no longer in use
     * @param soundNode unused in this case
     */
    protected void onPostExecute(SoundNode soundNode) {
        
        Log.d(TAG, "Ending Receiver!");

        NetworkInfo.ServerSocketInUse = false;
    }

    /**
     * converts a byte array into a usable object
     * @param data byte array of data to be converted into an object
     * @return returns deserialized object
     * @throws IOException for reading streams
     * @throws ClassNotFoundException if object not found
     */
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

}
