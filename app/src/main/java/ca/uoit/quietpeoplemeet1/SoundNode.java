package ca.uoit.quietpeoplemeet1;

public class SoundNode {

    private double latitude, longitude;
    private int soundLevel;

    public SoundNode(double latitude, double longitude, int soundLevel){
        this.latitude = latitude;
        this.longitude = longitude;
        this.soundLevel = soundLevel;
    }

    public int getSoundLevel() {
        return soundLevel;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
