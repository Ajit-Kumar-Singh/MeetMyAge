package data;


import data.model.Profile;
import data.model.ProfilePost;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiInterface {
    @GET("profiles/{id}")
    Call<Profile> getProfileById(@Path("id") int id);

    @POST("/profiles")
    Call<Profile> postProfile(@Body ProfilePost profile);

    @POST("/profiles/{id}")
    Call<Profile> updateProfile(@Path("id") int id ,@Body ProfilePost profile);
}