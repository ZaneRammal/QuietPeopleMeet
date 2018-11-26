package ca.uoit.quietpeoplemeet1;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

// reference https://developer.android.com/training/volley/request

/**
 * this class uses Volley library to get all the node data from the server
 * 
 */

public class getAllNodes {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();


    // url to get all nodes list
    private static String url_all_nodes = "http://127.0.0.1/quietPeopleMeetServer/get_all_sound_nodes.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_nodes = "nodes";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";

    //context of activity that created object
    private Context context;

    // nodes JSONArray
    JSONArray nodes = null;


    public getAllNodes(Context c) {

        //set context
        this.context = c;

        // Loading nodes in Background Thread
        new LoadAllNodes().execute();


    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllNodes extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting All nodes from url
         * */

        private URL url = null;
        protected String doInBackground(String... args) {
            // Building Parameters
            //List<NameValuePair> params = new ArrayList<NameValuePair>();

            try {
                url = new URL(args[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_nodes, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All nodes: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // nodes found
                    // Getting Array of nodes
                    nodes = json.getJSONArray(TAG_nodes);

                    // looping through All nodes
                    for (int i = 0; i < nodes.length(); i++) {
                        JSONObject c = nodes.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        //nodesList.add(map);
                    }
                } else {
                    // failed to get request
                    Toast.makeText(context,"request failed",Toast.LENGTH_SHORT);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {


        }

    }
}
