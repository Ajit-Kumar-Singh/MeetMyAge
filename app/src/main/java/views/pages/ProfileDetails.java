package views.pages;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meetmyage.com.meetmyageapp.R;

import Util.CommonUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import data.ApiClient;
import data.ApiInterface;
import data.SessionManagementUtil;
import data.model.GroupRequest;
import data.model.GroupResponse;
import data.model.Profile;
import data.model.ProfilePhotoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDetails extends Fragment {

    private static final String ARG_PARAM1 = "profileID";
    private static final String TAG = ProfileDetails.class.getSimpleName();
    private int mProfileId = 0;
    private OnFragmentInteractionListener mListener;

    public Profile getProfile() {
        return mProfile;
    }

    public void setProfile(Profile mProfile) {
        this.mProfile = mProfile;
    }

    private Profile mProfile;

    public String getProfileImage() {
        return mProfileImageInString;
    }

    public void setProfileImage(String mProfileImage) {
        this.mProfileImageInString = mProfileImage;
    }

    private String mProfileImageInString;
    //Add Binding on Page
    @BindView(R.id.profileDetailPersonProfileImage)
    ImageView mProfileImage;
    @BindView(R.id.profileDetailPersonName)
    TextView mProfileName;
    @BindView(R.id.profileDetailPersonStory)
    TextView mProfileStory;
    @BindView(R.id.profileDetailPersonWork)
    TextView mProfileWork;

    public ProfileDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param profileId Parameter 1.
     * @return A new instance of fragment ProfileDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileDetails newInstance(Profile pProfile, String pImage) {
        ProfileDetails fragment = new ProfileDetails();
        fragment.setProfile(pProfile);
        fragment.setProfileImage(pImage);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProfileId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_profile_details, container, false);
        ButterKnife.bind(this, view);
        getProfileFromServerAndSetProfileDetail();

        return view;
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

    // Writing code for calling Retrofit WebApi.
    // ToDo: Should move to Presenter Layer
    private void getProfileFromServerAndSetProfileDetail() {

        Profile profile = mProfile;
        mProfileName.setText(profile.getProfileName());
        mProfileStory.setText(profile.getProfileStory());
        mProfileWork.setText(profile.getProfileWork());


        Bitmap bitmap = CommonUtil.convertStringToBitmap(mProfileImageInString);
        mProfileImage.setImageBitmap(bitmap);

    }
}
