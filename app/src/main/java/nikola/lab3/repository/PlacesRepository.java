package nikola.lab3.repository;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import nikola.lab3.data.database.DatabaseSingleton;
import nikola.lab3.data.database.Place;
import nikola.lab3.data.places.PlaceItem;
import nikola.lab3.data.places.Result;
import nikola.lab3.data.travel.DistanceResult;
import nikola.lab3.service.GoogleMapsService;
import nikola.lab3.service.GoogleMapsServiceSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static nikola.lab3.data.travel.DistanceResult.LIMIT_IN_SECONDS;

public class PlacesRepository {

    public static final String TAG = "nikola";
    public static final int RADIUS = 2500;
    // TODO API KEY
    public static final String API_KEY = "API KEY";
    public static final String TYPE = "pizzeria";
    public static final String MODE_WALKING = "walking";
    public static final String SUCCESS = "OK";

    private GoogleMapsService googleMapsService;

    private Context context;

    public PlacesRepository(Context context) {
        googleMapsService = GoogleMapsServiceSingleton.getService();
        this.context = context;
    }


    public void getDataForLocation(Location myLocation) {

        clearDatabase();

        final String latLng = myLocation.getLatitude() + "," + myLocation.getLongitude();

        googleMapsService.getPlaces(latLng, API_KEY, RADIUS, TYPE).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                if (response.isSuccessful()) {

                    handleSuccessfulRadiusResponse(response, latLng);

                } else {
                    Log.e(TAG, "Not successful " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(TAG, "Failed places api call");
                Log.e(TAG, t.toString());
            }
        });
    }

    private void clearDatabase() {
        new Thread(() -> DatabaseSingleton
                            .getDatabase(context)
                            .getPlacesDao()
                            .deleteAll())
                                        .start();
    }

    private void handleSuccessfulRadiusResponse(Response<Result> response, String latLng) {
        final Result result = response.body();

        if (result != null) {
            final List<PlaceItem> places = result.getPlaces();

            String destinationLatLng;

            for (final PlaceItem place : places) {
                destinationLatLng = place.getLat() + "," + place.getLng();

                googleMapsService.getDistance(latLng, destinationLatLng, MODE_WALKING, API_KEY).enqueue(new Callback<DistanceResult>() {
                    @Override
                    public void onResponse(Call<DistanceResult> call, Response<DistanceResult> response) {
                        if (response.isSuccessful()) {
                            handleSuccessfulDistanceResponse(response, place);
                        } else {
                            Log.i(TAG, "Unsuccessful response");
                        }
                    }

                    @Override
                    public void onFailure(Call<DistanceResult> call, Throwable t) {
                        Log.i(TAG, "Failed distance api call");
                    }
                });
            }
        }
    }

    private void handleSuccessfulDistanceResponse(Response<DistanceResult> response, PlaceItem place) {
        final DistanceResult distanceResult = response.body();

        if (distanceResult != null
                && distanceResult.getStatus() != null
                && distanceResult.getStatus().equals(SUCCESS)) {

            if (distanceResult.getWalkingTime() <= LIMIT_IN_SECONDS) {
                Place validPlace = new Place(place.getName(),
                        place.getLat(),
                        place.getLng(),
                        place.isOpen());

                new Thread(() -> DatabaseSingleton.getDatabase(context)
                        .getPlacesDao().insert(validPlace)).start();
            } else {
                Log.i(TAG, "Place " + place.getName()
                        + " is not valid. Distance: "
                        + distanceResult.getWalkingTime());
            }
        } else {
            Log.i(TAG, "Failed distance api call");
        }
    }
}
