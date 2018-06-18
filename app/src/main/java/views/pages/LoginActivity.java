package views.pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

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
    private  ProgressBar mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                101 );
        mLoginPresenter = new LoginPresenterImpl(this,getApplicationContext());
        boolean loggedIn = mLoginPresenter.isLoggedIn();
//        if (loggedIn)
//        {
//            //Open the tabbed layout
//            onSuccess();
//        }
//        else
//        {
            setContentView(R.layout.activity_login);
            mProgressDialog = findViewById(R.id.login_progress);
            mProgressDialog.setVisibility(View.GONE);
            final LoginButton loginButton = (LoginButton) findViewById(R.id.facbookSignin);
            mLoginPresenter.fbLoginButtonClicked(loginButton);

//        }
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
        mProgressDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressDialog.setVisibility(View.GONE);
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