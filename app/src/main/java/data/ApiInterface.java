package data;


import java.util.List;

import data.model.FBRequest;
import data.model.Group;
import data.model.GroupRequest;
import data.model.GroupResponse;
import data.model.Profile;
import data.model.ProfilePhotoRequest;
import data.model.ProfilePhotoResponse;
import data.model.ProfilePost;
import data.model.gmaps.GroupWithSubscription;
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

    @GET("profiles/{id}/myGroups")
    Call<List<GroupWithSubscription>> getMyGroups(@Path("id") int id);

    @POST("/profiles")
    Call<Profile> postProfile(@Body ProfilePost profile);

    @POST("/profiles/{id}")
    Call<Profile> updateProfile(@Path("id") int id ,@Body Profile profile);

    @POST("/profiles/validateFacebookToken")
    Call<Profile> validateAndFetchFBProfile(@Body FBRequest fbToken);

    // For creating a group
    @POST("profiles/{id}/myGroups")
    Call<GroupResponse> createGroup(@Path("id") int id , @Body GroupRequest groupRequest);

    @POST("profiles/{id}/uploadProfilePhoto")
    Call<Void> uploadProfilePhoto(@Path("id") int id, @Body ProfilePhotoRequest photoRequest);

    @GET("profiles/{id}/profilePhoto")
    Call<ProfilePhotoResponse> fetchProfileData(@Path("id") int id);

}