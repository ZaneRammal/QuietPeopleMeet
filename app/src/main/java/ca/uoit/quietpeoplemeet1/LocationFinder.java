package ca.uoit.quietpeoplemeet1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static android.support.constraint.Constraints.TAG;

public class LocationFinder implements LocationListener {

    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private Criteria criteria;
    private String provider;


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "OnProviderEnabled: Provider is enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: Provider is disabled");
    }

    /**
     * When location is detected to have changed, changed long and lat values
     *
     * @param location new location value
     */
    @Override
    public void onLocationChanged(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    /**
     * Constructor for LocationFinder
     * sets up locationManager and if permission for location is approved then it sets the newest location
     *
     * @param context the context of the calling activity
     */
    public LocationFinder(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, true);

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1,
                    0, this);
            setMostRecentLocation(locationManager.getLastKnownLocation(provider));

        }


    }

    /**
     * sets newest location value
     * @param location newest location to be set to
     */
    public void setMostRecentLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }


    /**
     * gets current location
     * @return Location object of current location
     */
    public Location getLocation() {

        Location l = null;

        l.setLongitude(this.longitude);
        l.setLatitude(this.latitude);

        return l;

    }


}
