package views.pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meetmyage.com.meetmyageapp.R;

import java.util.List;

import butterknife.ButterKnife;
import data.ApiClient;
import data.ApiInterface;
import data.SessionManagementUtil;
import data.model.Group;
import data.model.Profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendedGroupsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    LinearLayout mGroupDetailsGrid = null;
    TextView mGroupName = null;
    TextView mGroupDesc = null;
    List<Group> mRecommendedGroups = null;
    private ProgressBar mProgressDialog;
    private LayoutInflater mInflater;

    public RecommendedGroupsFragment() {
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
        View myGroupDetailsView  = inflater.inflate(R.layout.fragment_recommended_groups, container, false);
        ButterKnife.bind(this,myGroupDetailsView);
        mGroupDetailsGrid= myGroupDetailsView.findViewById(R.id.group_details_grid);
        mProgressDialog = myGroupDetailsView.findViewById(R.id.group_details_progress);
        mProgressDialog.setVisibility(View.VISIBLE);
        mInflater = inflater;
        getRecommendGroups();

        return myGroupDetailsView;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void getRecommendGroups() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Group>> call = null;
        Profile myProfile = SessionManagementUtil.getUserData();
        Log.i("LOGGED_IN_PROFILE_ID",String.valueOf(myProfile.getProfileId()));
        call = apiService.getRecommendedGroupsForProfile(myProfile.getProfileId());

        call.enqueue(new Callback<List<Group>>() {

            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                mRecommendedGroups = response.body();
                mProgressDialog.setVisibility(View.GONE);
                prepareGroupsData();
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.e("ERROR GETTING Groups", t.toString());
            }
        });
    }

    private void prepareGroupsData(){
        for (Group myGroup:mRecommendedGroups) {
            View myView = mInflater.inflate(R.layout.fragment_recommended_groups_view, null);
            mGroupName = myView.findViewById(R.id.group_details_holder_image_group_name);
            mGroupDesc = myView.findViewById(R.id.group_details_holder_image_group_desc);
            mGroupName.setText(myGroup.getGroupName());
            mGroupDesc.setText(myGroup.getGroupStory());
            final Group mySelectedGroup = myGroup;
            FloatingActionButton myFloatingButton = myView.findViewById(R.id.group_details_holder_viewMore);
            myFloatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionManagementUtil.setSelectedGroup(mySelectedGroup);
                    Fragment fragment = new GroupDetailsFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_conatiner, fragment)
                            .commit();
                }
            });
            mGroupDetailsGrid.addView(myView);
        }

    }
}
