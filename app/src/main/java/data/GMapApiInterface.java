package data;

import java.util.List;

import data.model.gmaps.GmapResponse;
import data.model.gmaps.Result;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GMapApiInterface {

    @GET("/maps/api/geocode/json")
    Call<GmapResponse> getLocationForLatLong(@Query("latlng") String pLocation, @Query("key") String key);
}
