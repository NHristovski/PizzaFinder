package nikola.lab3.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import nikola.lab3.repository.PlacesRepositorySingleton;

import static nikola.lab3.ui.MapsActivity.TAG;

public class PlacesService extends Service {

    private BroadcastReceiver timeReceiver;
    private Location myLocation;
    private LocationManager locationManager;

    @Override
    public void onCreate() {

        super.onCreate();
        Log.i(TAG, "Service created");

        locationManager = getSystemService(LocationManager.class);

        getNewData();

        timeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "1 minute passed. Getting new data.");

                getNewData();

            }
        };
        registerReceiver(timeReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }


    @SuppressLint("MissingPermission")
    private void getNewData() {
        myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (myLocation != null) {
            PlacesRepositorySingleton
                    .getPlacesRepository(PlacesService.this)
                    .getDataForLocation(myLocation);
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"Service destroyed");
        super.onDestroy();
        if (timeReceiver != null) {
            unregisterReceiver(timeReceiver);
            timeReceiver = null;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public PlacesService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PlacesService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service
        return mBinder;
    }
}
