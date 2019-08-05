package repository;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.meetmyage.com.meetmyageapp.R;

import Util.CommonUtil;
import data.ApiClient;
import data.ApiInterface;
import data.SessionManagementUtil;
import data.model.ProfilePhotoRequest;
import data.model.ProfilePhotoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import views.pages.BottomNavigation;

/*
    Singleton class that fetches data from service
    It also caches the information
 */
public class ProfileRepository {

    private static ProfileRepository sInstance;
    public static ProfileRepository getInstance()
    {
        if(sInstance == null)
        {
            sInstance = new ProfileRepository();
        }
        return sInstance;
    }

    public MutableLiveData<String> getProfileImagePath() {
        final MutableLiveData<String> profileImagePath = new MutableLiveData<>();

        //Update Data to server
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ProfilePhotoResponse> call = null;
        call = apiService.fetchProfileData(SessionManagementUtil.getUserData().getProfileId());
        call.enqueue(new Callback<ProfilePhotoResponse>() {
            @Override
            public void onResponse(Call<ProfilePhotoResponse> call, Response<ProfilePhotoResponse> response) {
                ProfilePhotoResponse responseProfile = response.body();
                String data = responseProfile.getData();
                if (data.isEmpty())
                {
                    profileImagePath.setValue("");
                }
                else
                {
                   profileImagePath.setValue(data);
                }
            }
            @Override
            public void onFailure(Call<ProfilePhotoResponse> call, Throwable t) {
                profileImagePath.setValue("");
            }
        });
        return profileImagePath;
    }

    public void saveImagePathToServer(String imagePath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap profileBitmap = BitmapFactory.decodeFile(imagePath, bmOptions);

        if (profileBitmap != null)
        {
            String base64String = CommonUtil.encodeImage(profileBitmap);
            //Update Data to server
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<Void> call = null;
            call = apiService.uploadProfilePhoto(SessionManagementUtil.getUserData().getProfileId(),
                    new ProfilePhotoRequest(SessionManagementUtil.getUserData().getProfileName(), base64String));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful())
                    {
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t)
                {

                }
            });
        }
    }
}
