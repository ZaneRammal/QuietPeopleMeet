package ca.uoit.quietpeoplemeet1;

import android.media.MediaRecorder;

import java.io.File;

public class SoundReader {

    MediaRecorder mr;

    public SoundReader(){

        mr = new  MediaRecorder();

    }

    int getSoundLevel(){
        int soundLevel = 0;

        /**
         * record some audio (5 seconds),
         * read that audio file,
         * read sound levels and take average of soundlevel/second
         *
         * return a value (in dB?) for what the sound level is
         */



        return soundLevel;
    }

    void recordSample(){

    }

    int readSoundLevel(File soundFile){
       int soundLevel = 0;

       return soundLevel;
    }




}
