package ca.uoit.quietpeoplemeet1;

import android.media.MediaRecorder;

public class SoundReader {

    public boolean isRunning = false;
    private MediaRecorder mic = null;

    public void start() throws Exception{
        if (mic == null) {


            mic = new MediaRecorder();
            mic.setAudioSource(MediaRecorder.AudioSource.MIC);
            mic.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mic.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mic.setOutputFile("/dev/null");
            mic.prepare();
            mic.start();
            isRunning = true;
        }
    }

    //stop the recording and release the resource

    public void stop() {
        if (mic != null) {
            mic.stop();
            mic.release();
            mic = null;
            isRunning = false;
        }
    }

    //get sound wave amplitude at any given time

    public double getAmplitude() {
        if (mic != null)
            return  mic.getMaxAmplitude();
        else
            return -1;

    }

}





