package Presenter.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationListenerImpl implements LocationListener {

    Location mLocation = null;
    LocationManager locationManager = null;
    Context mContext = null;

    public LocationListenerImpl(Context pContext) {
        mContext = pContext;
    }

    public Location getLocation() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
        Log.i("BEST PROVIDER", bestProvider);
        mLocation = null;
        try {
            if (mLocation == null) {
                locationManager.requestLocationUpdates(bestProvider, 1000, 10, this);

                if (locationManager != null) {
                    mLocation = locationManager
                            .getLastKnownLocation(bestProvider);
                    if (mLocation != null) {
                        Log.i("LATITUDE", "" + mLocation.getLatitude());
                        Log.i("LONGITUDE", "" + mLocation.getLongitude());
                    }
                }
            }
        } catch (SecurityException e) {
            Log.i("ACCESS PERMISSION", "cannot access location");
        }
        return mLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }
    }
}
