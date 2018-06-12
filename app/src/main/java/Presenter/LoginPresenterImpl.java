package Presenter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import data.ApiClient;
import data.ApiInterface;
import Presenter.location.LocationListenerImpl;
import data.SessionManagementUtil;
import data.model.FBRequest;
import data.model.Profile;
import data.model.gmaps.GmapResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import data.model.gmaps.GmapResponse;
import data.model.gmaps.Result;
import data.GMapApiClient;
import data.GMapApiInterface;

public class LoginPresenterImpl  implements LoginContract.Presenter {

    private final static String TAG = LoginPresenterImpl.class.getSimpleName();
    private LoginContract.view mLoginView;
    private CallbackManager callbackManager;
    private SessionManagementUtil session;
    data.model.Location mLocation = null;
    Context mContext;
    public LoginPresenterImpl(LoginContract.view view, Context context)
    {
        this.mLoginView = view;
        mContext = context;
        session = new SessionManagementUtil(context);
    }

    @Override
    public void fbLoginButtonClicked(final LoginButton loginButton) {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                saveProfileToServer(loginResult);
                LocationListenerImpl myLocationListener = new LocationListenerImpl(mContext);
                Location myLocation = myLocationListener.getLocation();
                Log.i("NEW_Latitude",""+myLocation.getLatitude());
                Log.i("NEW_Longitude",""+myLocation.getLongitude());
                myLocationListener.stopUsingGPS();
                getCurrentLocation(myLocation.getLatitude(),myLocation.getLongitude());
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"cancel login");
                loginButton.setVisibility(View.VISIBLE);
                mLoginView.showToast("Please try again");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "Facebook exception "+exception.toString());
                mLoginView.onFailure(exception.toString());
            }
        });
    }

    @Override
    public void saveProfileToServer(LoginResult loginResult) {
        ApiInterface apiService =
                                ApiClient.getClient().create(ApiInterface.class);

                        Call<Profile> call = null;
                        mLoginView.showProgressBar();
                        call = apiService.validateAndFetchFBProfile(new FBRequest(loginResult.getAccessToken().getToken()));

                        call.enqueue(new Callback<Profile>() {
                            @Override
                            public void onResponse(Call<Profile> call, Response<Profile> response) {
                                Profile profile = response.body();
                                if (profile != null)
                                {
                                    saveProfileToSessionPreference(profile);
                                    mLoginView.onSuccess();
                                }
                                else
                                {
                                    mLoginView.hideProgressBar();
                                    mLoginView.showToast("Something went Wrong while fetching data");
                                }
                            }

                            @Override
                            public void onFailure(Call<Profile> call, Throwable t) {
                                // Log error here since request failed
                                mLoginView.onFailure("Server is Down. Please try again");
                                Log.e(TAG, t.toString());
                            }
                        });
    }

    @Override
    public void getCurrentLocation(final double pLatitude, final double pLongitude) {
        GMapApiInterface apiService =
                GMapApiClient.getClientForGMAP().create(GMapApiInterface.class);

        Call<GmapResponse> call = null;
        String myLatLong = String.valueOf(pLatitude)+","+String.valueOf(pLongitude);
        call = apiService.getLocationForLatLong(myLatLong,GMapApiClient.GMAP_KEY);
        mLocation = new data.model.Location();
        call.enqueue(new Callback<GmapResponse>() {
            @Override
            public void onResponse(Call<GmapResponse> call, Response<GmapResponse> response) {
                GmapResponse myGmapResponse = response.body();
                if (myGmapResponse != null)
                {
                    String myCurrentAddress = myGmapResponse.getResults().get(0).getFormattedAddress();
                    String [] myAddressComponents = myCurrentAddress.split(",");
                    int myLastIndex= myAddressComponents.length;
                    mLocation.setLatitude(pLatitude);
                    mLocation.setLongitude(pLongitude);
                    mLocation.setCity(myAddressComponents[myLastIndex - 3]);
                    mLocation.setAddressLine1(myAddressComponents[0]);
                    mLocation.setAddressLine2(myAddressComponents[1]);
                    String myStateZip = myAddressComponents[myLastIndex - 2];
                    String myZip = myStateZip.substring(myStateZip.lastIndexOf(" "));
                    String myState = myStateZip.replaceAll(myZip,"");
                    mLocation.setState(myState.trim());
                    mLocation.setPinCode(myZip.trim());
                    SessionManagementUtil.updateLocation(mLocation);

                }
                else
                {
                    Log.e("ERROR GETTING LOCATION","no values found for current address");
                }
            }

            @Override
            public void onFailure(Call<GmapResponse> call, Throwable t) {
                Log.e("ERROR GETTING LOCATION", t.toString());
            }
        });
    }

    @Override
    public void saveProfileToSessionPreference(Profile profile) {
        SessionManagementUtil.createLoginSession(profile.getProfileId(),profile.getProfileName(),"",profile.getProfileStory(),profile.getProfileWork(),profile.getLocation());
        mLoginView.hideProgressBar();

    }

    @Override
    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
