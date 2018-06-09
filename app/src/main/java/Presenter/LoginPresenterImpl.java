package Presenter;

import android.content.Context;
import android.content.Intent;
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
import data.SessionManagementUtil;
import data.model.FBRequest;
import data.model.Profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenterImpl  implements LoginContract.Presenter {

    private final static String TAG = LoginPresenterImpl.class.getSimpleName();
    private LoginContract.view mLoginView;
    private CallbackManager callbackManager;
    private SessionManagementUtil session;

    public LoginPresenterImpl(LoginContract.view view, Context context)
    {
        this.mLoginView = view;
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
