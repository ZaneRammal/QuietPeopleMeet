package ca.uoit.quietpeoplemeet1;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class SoundNodeReceive extends AsyncTask<Void, Void, SoundNode> {

    private final String TAG = "SoundNodeReceive";


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

    protected void onPostExecute(SoundNode soundNode) {
        
        Log.d(TAG, "Ending Receiver!");

        NetworkInfo.ServerSocketInUse = false;
    }


    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

}
