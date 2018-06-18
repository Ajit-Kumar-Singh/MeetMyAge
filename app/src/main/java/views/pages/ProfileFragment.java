package views.pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetmyage.com.meetmyageapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import data.ApiClient;
import data.ApiInterface;
import data.SessionManagementUtil;
import data.model.Location;
import data.model.Profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Managing the Profile in this class
public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String TAG = ProfileFragment.class.getSimpleName();
    private boolean isPreview = true;

    @BindView(R.id.userName) TextView mProfileName;
    @BindView(R.id.profileWork) TextView mProfileWork;
    @BindView(R.id.profileStory) TextView mProfileStory;
    @BindView(R.id.city) TextView mProfileCity;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.layoutPreview) LinearLayout mLayoutPreview;
    @BindView(R.id.layoutEdit) LinearLayout mLayoutEdit;
    @BindView(R.id.saveProfile) Button saveProfile;
    @BindView(R.id.editprofileName) EditText mEditProfileName;
    @BindView(R.id.editprofileStory) EditText mEditProfileStrory;
    @BindView (R.id.editprofileWork) EditText mEditProfileWork;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view =  inflater.inflate(R.layout.fragment_profile, container, false);
            ButterKnife.bind(this,view);
            return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        showPreviewPage();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (isPreview)
                    {
                        showPreviewPage();
                    }
                    return true;
                }
                return false;
            }
        });
    }


    private void showPreviewPage()
    {
        if (isPreview)
        {
            mLayoutEdit.setVisibility(View.GONE);
            mLayoutPreview.setVisibility(View.VISIBLE);
            //Set Initial data
            Profile savedProfile = SessionManagementUtil.getUserData();
            mProfileName.setText(savedProfile.getProfileName());
            mProfileStory.setText(savedProfile.getProfileStory());
            mProfileWork.setText(savedProfile.getProfileWork());
            mProfileCity.setText(savedProfile.getLocation().getCity());
            isPreview = false;
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditPage();
                }
            });

        }
    }

    private void showEditPage()
    {
        if (!isPreview)
        {
            //Set Initial data
            Profile savedProfile = SessionManagementUtil.getUserData();
            mEditProfileName.setText(savedProfile.getProfileName());
            mProfileStory.setText(savedProfile.getProfileStory());
            mEditProfileWork.setText(savedProfile.getProfileWork());
            mLayoutEdit.setVisibility(View.VISIBLE);
            mLayoutPreview.setVisibility(View.GONE);
            isPreview =  true;
            saveProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDataToServer();
                }
            });
        }
    }

    private void updateDataToServer()
    {
        //Update Data to server
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Profile> call = null;
        call = apiService.updateProfile(SessionManagementUtil.getUserData().getProfileId(),
                new Profile(SessionManagementUtil.getUserData().getProfileId(),mEditProfileName.getText().toString(),mEditProfileStrory.getText().toString(),mEditProfileWork.getText().toString(),new Location()));

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Profile responseProfile = response.body();
                SessionManagementUtil.updateProfile(
                        responseProfile.getProfileName(),"",responseProfile.getProfileStory(),responseProfile.getProfileWork());
                showPreviewPage();
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
