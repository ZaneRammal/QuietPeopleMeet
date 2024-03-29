package ca.uoit.quietpeoplemeet1;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class TextNodeReceive extends AsyncTask<Void, Void, TextNode> {

    private final String TAG = "TextNodeReceive";


    protected TextNode doInBackground(Void... params) {

        TextNode textNodeReturn = null;

        Log.d(TAG, "Starting TextNodeReceive Listener");


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


            inputstream.read(buffer, 0, buffer.length);

            textNodeReturn = (TextNode) deserialize(buffer);


            serverSocket.close();

            Log.d(TAG, "Okay: "
                    + " Received: " + textNodeReturn.getMessageLevel());

            NetworkInfo.textNodes.add(textNodeReturn);
        } catch (IOException e) {
            Log.e(TAG, "IOEXCEPTION : " + e.getStackTrace());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Class Exception : " + e.getMessage());
        }


        return textNodeReturn;

    }

    protected void onPostExecute(TextNode textNode) {

        if (textNode != null) {
            NetworkInfo.textNodes.add(textNode);
        }

        Log.d(TAG, "Ending Receiver!");

    }


    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

}
