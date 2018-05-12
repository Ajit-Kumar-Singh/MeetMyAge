package Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import data.ApiClient;
import data.ApiInterface;
import data.SessionManagementUtil;
import data.model.Profile;
import data.model.ProfilePost;
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
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response)
                    {
                        Log.d(TAG, "Facebook Profile data "+json_object.toString());
                        // Post the Profile data to Server
                        ApiInterface apiService =
                                ApiClient.getClient().create(ApiInterface.class);

                        Call<Profile> call = null;
                        try {
                            mLoginView.showProgressBar();
                            call = apiService.postProfile(new ProfilePost(json_object.getString("name"),"",""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        call.enqueue(new Callback<Profile>() {
                            @Override
                            public void onResponse(Call<Profile> call, Response<Profile> response) {
                                Profile profile = response.body();
                                if (profile != null)
                                {
                                    saveProfileToSessionPreference(profile);
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
                });

        //Facebook permission for data required for the app.
        Bundle permission_param = new Bundle();
        permission_param.putString("fields","id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }

    @Override
    public void saveProfileToSessionPreference(Profile profile) {
        SessionManagementUtil.createLoginSession(profile.getProfileId(),profile.getProfileName(),"",profile.getProfileStory(),profile.getProfileWork());
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
