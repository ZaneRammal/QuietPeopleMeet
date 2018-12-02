package ca.uoit.quietpeoplemeet1;

import android.media.MediaRecorder;

/**
 * this class handles all microphone interactions to get sound data
 */
public class SoundReader {

    // boolean to indicate if microphone is running
    public boolean isRunning = false;
    //media player class used to interface with phone's microphone
    private MediaRecorder mic = null;

    /**
     * start mic recording, sets destination to /dev/null so it doesnt take up storage space
     * @throws Exception throws various exceptions if microphone doesnt work properly
     */
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


    /**
     * stop microphone recording
     */
    public void stop() {
        if (mic != null) {
            mic.stop();
            mic.release();
            mic = null;
            isRunning = false;
        }
    }

    /**
     * get amplitude of mic sound data
     * @return returns 16 bit double to represent volume data
     */
    public double getAmplitude() {
        if (mic != null)
            return  mic.getMaxAmplitude();
        else
            return -1;

    }

}





