package ca.uoit.quietpeoplemeet1;

import java.io.Serializable;

/**
 * this class holds sound and location information
 * must be serializable to allow socket sending
 */
public class SoundNode implements Serializable {

    private double latitude, longitude, soundLevel;


    public SoundNode(double latitude, double longitude, double soundLevel) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.soundLevel = soundLevel;
    }

    public double getSoundLevel() {
        return soundLevel;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
