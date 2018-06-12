package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GMapApiClient {
    public static final String GMAP_BASE_URL = "https://maps.googleapis.com";
    public static final String GMAP_KEY = "AIzaSyCu52Gi6hkmSAs3WfBFCqqpLkoy6DxFYmQ";
    private static Retrofit retrofit = null;

    public static Retrofit getClientForGMAP() {


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(GMAP_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
