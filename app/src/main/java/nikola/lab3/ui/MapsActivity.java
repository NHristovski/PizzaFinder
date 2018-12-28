package nikola.lab3.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import nikola.lab3.R;
import nikola.lab3.data.database.DatabaseSingleton;
import nikola.lab3.data.database.Place;
import nikola.lab3.service.GoogleMapsService;
import nikola.lab3.service.GoogleMapsServiceSingleton;
import nikola.lab3.service.PlacesService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String TAG = "nikola";
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;

    private GoogleMap googleMap;
    private GoogleMapsService googleMapsService;
    private PlacesService placesService;

    private Future<LiveData<List<Place>>> databaseResult;

    private LocationManager locationManager;
    private Location myLocation;

    private boolean bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            final String errorMessage = "mapFragment = null.";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

        locationManager = getSystemService(LocationManager.class);

        googleMapsService = GoogleMapsServiceSingleton.getService();

        bound = false;

        Callable<LiveData<List<Place>>> getDataFromDatabase =
                () -> DatabaseSingleton.getDatabase(MapsActivity.this)
                        .getPlacesDao()
                        .getAll();

        databaseResult = Executors.newSingleThreadExecutor().submit(getDataFromDatabase);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);

        }else {
            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        // Bind to LocalService

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(this, PlacesService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);

            bound = true;
            Log.i(TAG,"Bound to service!");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (bound) {
            if (connection != null) {
                unbindService(connection);
                bound = false;
            }
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlacesService.LocalBinder binder = (PlacesService.LocalBinder) service;
            placesService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    ,REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            prepareMap();
            setMarkersOnMap();
        }
    }

    private void setMarkersOnMap() {
        try {
            final LiveData<List<Place>> placesLiveData  = databaseResult.get(700, TimeUnit.MILLISECONDS);

            placesLiveData.observe(this, this::setMarkers);

        } catch (ExecutionException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setMarkers(List<Place> places) {
        if (places == null || places.isEmpty()){
            Log.i(TAG,"Places is null in live data");
            googleMap.clear();

        }else {
            Log.i(TAG,places.toString());

            for (Place place : places) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getLatitude(), place.getLongitude()))
                        .title(place.getName())
                        .snippet(place.isOpen() ? "Open now" : "Closed"));
            }
        }
        moveCameraToMyLocation();
    }


    private void moveCameraToMyLocation() {
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(myLocation.getLatitude()
                                         ,myLocation.getLongitude())
                                         , 14.5f));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION){

            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    prepareMap();
                    setMarkersOnMap();
                }
            }

        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    private void prepareMap() {
        googleMap.setMyLocationEnabled(true);
        moveCameraToMyLocation();
    }
}
