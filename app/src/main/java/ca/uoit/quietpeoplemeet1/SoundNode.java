package ca.uoit.quietpeoplemeet1;

public class SoundNode {

    private double latitude, longitude , soundLevel;


    public SoundNode(double latitude, double longitude, double soundLevel){
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
