package views.pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class EditProfileFragment extends Fragment {

    private String TAG = EditProfileFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.saveProfile) Button saveProfile;
    @BindView(R.id.profileName) EditText profileName;
    @BindView(R.id.profileStory) EditText profileStory;
    @BindView (R.id.profileWork) EditText profileWork;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this,view);

        //Set Initial data
        Profile savedProfile = SessionManagementUtil.getUserData();
        profileName.setText(savedProfile.getProfileName());
        profileStory.setText(savedProfile.getProfileStory());
        profileWork.setText(savedProfile.getProfileWork());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDataToServer();
            }
        });
    }

    private void updateDataToServer()
    {
        //Update Data to server
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Profile> call = null;
        call = apiService.updateProfile(SessionManagementUtil.getUserData().getProfileId(),
                    new Profile(SessionManagementUtil.getUserData().getProfileId(),profileName.getText().toString(),profileStory.getText().toString(),profileWork.getText().toString(),new Location()));

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Profile responseProfile = response.body();
                SessionManagementUtil.updateProfile(
                        responseProfile.getProfileName(),"",responseProfile.getProfileStory(),responseProfile.getProfileWork(),new Location());
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, new ProfileFragment());
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
