package restinterface;


import model.Profile;
import model.ProfilePost;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("profiles/{id}")
    Call<Profile> getProfileById(@Path("id") int id);

    @POST("/profiles")
    Call<Profile> postProfile(@Body ProfilePost profile);

    @POST("/profiles/{id}")
    Call<Profile> updateProfile(@Path("id") int id ,@Body ProfilePost profile);
}