package nikola.lab3.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static Retrofit retrofit;

    private RetrofitSingleton(){}

    public static synchronized Retrofit getRetrofit() {
        if (retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(GoogleMapsService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
