package com.meetmyage.com.meetmyageapp.pages;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.meetmyage.com.meetmyageapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import model.Profile;
import model.ProfilePost;
import restinterface.ApiClient;
import restinterface.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.SessionManagementUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static String TAG = LoginActivity.class.getSimpleName();
    private CallbackManager callbackManager;
    private SessionManagementUtil session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManagementUtil(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
//        if (loggedIn)
//        {
//            Intent intent = new Intent(LoginActivity.this, TabbedLayout.class);
//            startActivity(intent);
//        }
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton) findViewById(R.id.facbookSignin);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                getAndSaveUserDetails(loginResult,loginButton);
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"cancel login");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "Facebook exception"+exception.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void getAndSaveUserDetails(LoginResult loginResult, final LoginButton loginButton) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response)
                    {
                     Log.d(TAG, "Facebook value"+json_object.toString());
                     // Post the Profile data to Server
                        ApiInterface apiService =
                                ApiClient.getClient().create(ApiInterface.class);

                        Call<Profile> call = null;
                        try {
                            call = apiService.postProfile(new ProfilePost(json_object.getString("name"),"",""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        call.enqueue(new Callback<Profile>() {
                            @Override
                            public void onResponse(Call<Profile> call, Response<Profile> response) {
                                Profile profile = response.body();
                                SessionManagementUtil.createLoginSession(profile.getProfileId(),profile.getProfileName(),"",profile.getProfileStory(),profile.getProfileWork());
                                Intent intent = new Intent(LoginActivity.this, TabbedLayout.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Profile> call, Throwable t) {
                                // Log error here since request failed
                                Log.e(TAG, t.toString());
                            }
                        });
                    }

                });

        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }}

