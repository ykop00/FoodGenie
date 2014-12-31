package morozovs.foodgenie.utils;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import morozovs.foodgenie.interfaces.ILocationReceiver;

public class LocationHelper implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    static Location currentLocation;
    static GoogleApiClient mGoogleApiClient;
    static LocationRequest mLocationRequest;
    static ILocationReceiver mLocationReceiver;
    static boolean locationRequested;

    public LocationHelper(ILocationReceiver callback){
        mLocationReceiver = callback;
        init();
    }

    private void init(){
        buildGoogleApiClient();
        createLocationRequest();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            currentLocation = location;
            if(locationRequested){
                mLocationReceiver.gotLocation(currentLocation);
                locationRequested = false;
            }
        }
    }

    public void startAcquiringLocation(){
        locationRequested = true;
        if(currentLocation != null)
            returnLocation();
        else {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(location == null) {
                init();
                mGoogleApiClient.connect();
            }
            else {
                currentLocation = location;
                returnLocation();
            }
        }
    }

    private static void returnLocation(){
        if(locationRequested) {
            locationRequested = false;
            mLocationReceiver.gotLocation(currentLocation);
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        if(mGoogleApiClient != null)
            return;

        mGoogleApiClient = new GoogleApiClient.Builder(AppController.getInstance())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(location != null) {
            currentLocation = location;
            returnLocation();
        }
        else
            startLocationUpdates();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
