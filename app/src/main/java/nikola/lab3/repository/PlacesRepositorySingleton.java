package nikola.lab3.repository;

import android.content.Context;

public class PlacesRepositorySingleton {
    private static PlacesRepository placesRepository;

    public static synchronized PlacesRepository getPlacesRepository(Context context){
        if(placesRepository == null){
            placesRepository = new PlacesRepository(context);
        }
        return placesRepository;
    }
}
