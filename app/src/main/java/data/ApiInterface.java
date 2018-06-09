package data;


import java.util.List;

import data.model.FBRequest;
import data.model.Group;
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

    @GET("profiles/{id}/recommendedGroups")
    Call<List<Group>> getRecommendedGroupsForProfile(@Path("id") int id);

    @POST("/profiles")
    Call<Profile> postProfile(@Body ProfilePost profile);

    @POST("/profiles/{id}")
    Call<Profile> updateProfile(@Path("id") int id ,@Body ProfilePost profile);

    @POST("/profiles/validateFacebookToken")
    Call<Profile> validateAndFetchFBProfile(@Body FBRequest fbToken);

}