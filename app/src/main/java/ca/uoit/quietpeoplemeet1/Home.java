package ca.uoit.quietpeoplemeet1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.Manifest;

public class Home extends AppCompatActivity {

    TextView tv;
    SoundReader sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        tv = findViewById(R.id.soundLevelTextView);
        sr = new SoundReader();
        try {
            sr.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

       // soundUpdater.run();

    }


    public void readSound(View v){

        String TextValue = Double.toString(sr.getAmplitude());

        tv.setText(TextValue);



    }




}
