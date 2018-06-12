package Presenter;

import android.content.Intent;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import data.model.Location;
import data.model.Profile;

public interface LoginContract {
    interface view {
        void onSuccess();
        void onFailure(String msg);
        void onCancelLogin();
        void showProgressBar();
        void hideProgressBar();
        void showToast(String msg);
    }

    interface  Presenter {
        void fbLoginButtonClicked(LoginButton loginButton);
        void saveProfileToServer(LoginResult loginResult);
        void saveProfileToSessionPreference(Profile profile);
        void getCurrentLocation(double pLatitude, double pLongitude);
        boolean isLoggedIn();
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
