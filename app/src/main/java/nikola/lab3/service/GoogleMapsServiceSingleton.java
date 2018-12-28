package nikola.lab3.service;

public class GoogleMapsServiceSingleton {

    private static GoogleMapsService googleMapsService;

    private GoogleMapsServiceSingleton(){}

    public static synchronized GoogleMapsService getService(){
        if (googleMapsService == null){

            googleMapsService = RetrofitSingleton.getRetrofit()
                    .create(GoogleMapsService.class);
        }
        return googleMapsService;
    }
}