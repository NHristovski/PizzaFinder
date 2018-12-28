package nikola.lab3.service;


import nikola.lab3.data.travel.DistanceResult;
import nikola.lab3.data.places.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsService {
    String BASE_URL = "https://maps.googleapis.com";

    @GET("/maps/api/place/textsearch/json")
    Call<Result> getPlaces(@Query("location") String latLng,
                           @Query("key") String apiKey,
                           @Query("radius") int radius,
                           @Query("query") String type);


    @GET("/maps/api/distancematrix/json")
    Call<DistanceResult> getDistance(@Query("origins") String origins,
                                     @Query("destinations") String destinations,
                                     @Query("mode") String mode,
                                     @Query("key") String apiKey);

}