package data;

import data.model.gmaps.GmapResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GMapApiInterface {

    @GET("/maps/api/geocode/json")
    Call<GmapResponse> getLocationForLatLong(@Query("latlng") String pLocation, @Query("key") String key);
}
