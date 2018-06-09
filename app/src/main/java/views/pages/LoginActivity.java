package views.pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.login.widget.LoginButton;
import com.meetmyage.com.meetmyageapp.R;

import Presenter.LoginContract;
import Presenter.LoginPresenterImpl;

/**
 * A login screen that offers login via Facebook
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.view{
    private static String TAG = LoginActivity.class.getSimpleName();
    private LoginContract.Presenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginPresenter = new LoginPresenterImpl(this,getApplicationContext());
//        boolean loggedIn = mLoginPresenter.isLoggedIn();
//        if (loggedIn)
//        {
//
//        }

        setContentView(R.layout.activity_login);
        final LoginButton loginButton = (LoginButton) findViewById(R.id.facbookSignin);
        mLoginPresenter.fbLoginButtonClicked(loginButton);

    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(LoginActivity.this, TabbedLayout.class);
        startActivity(intent);
    }

    @Override
    public void onFailure(String msg) {

    }

    @Override
    public void onCancelLogin() {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showToast(String msg) {
      //  Toast.makeText()
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         mLoginPresenter.onActivityResult(requestCode,resultCode,data);
    }
}