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

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meetmyage.com.meetmyageapp.R;

import java.util.Iterator;
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
    private ViewGroup mContainer;
    private Bundle mBundle;
    Iterator<Group> mIterator;
    View mGroupDetailsView;
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
        mGroupDetailsView  = inflater.inflate(R.layout.fragment_recommended_groups, container, false);
        ButterKnife.bind(this,mGroupDetailsView);
        mGroupDetailsGrid= mGroupDetailsView.findViewById(R.id.group_details_grid);
        mProgressDialog = mGroupDetailsView.findViewById(R.id.group_details_progress);
        mProgressDialog.setVisibility(View.VISIBLE);
        mInflater = inflater;

        getRecommendGroups();
        return mGroupDetailsView;
    }

    private void addGroupDetails(Group myGroup) {
        long myStartTime = System.currentTimeMillis();
        View myGroupDetails =  mInflater.inflate(R.layout.fragment_group_details,null);
        GroupDetailsFragmentHelper myHelper = new GroupDetailsFragmentHelper(myGroup.getGroupId(),myGroup.getGroupName(),myGroup.getGroupStory(),myGroup.getLocation(),getContext(),mInflater,getFragmentManager());

        mGroupDetailsGrid.removeAllViews();
        myHelper.getRecommendGroups(myGroupDetails);
        Log.d("Render time card ",String.valueOf(System.currentTimeMillis()-myStartTime));
        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.swing_up_left);
        Animation slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.swing_up_right);
        myGroupDetails.startAnimation(slideUp);
        myGroupDetails.setVisibility(View.VISIBLE);
        mGroupDetailsGrid.addView(myGroupDetails);
    }

    private void addGroupDetailsReject(Group myGroup) {
        View myGroupDetails =  mInflater.inflate(R.layout.fragment_group_details,null);
        GroupDetailsFragmentHelper myHelper = new GroupDetailsFragmentHelper(myGroup.getGroupId(),myGroup.getGroupName(),myGroup.getGroupStory(),myGroup.getLocation(),getContext(),mInflater,getFragmentManager());

        mGroupDetailsGrid.removeAllViews();
        myHelper.getRecommendGroups(myGroupDetails);
        Animation slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.swing_up_right);
        myGroupDetails.startAnimation(slideDown);
        myGroupDetails.setVisibility(View.VISIBLE);
        mGroupDetailsGrid.addView(myGroupDetails);
    }

    private void addGroupNav(View pView) {
        FloatingActionButton myFloatingButton = pView.findViewById(R.id.group_details_holder_viewMore);
        myFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIterator.hasNext()) {
                    Group myCurrentGroup = mIterator.next();
                    if (myCurrentGroup != null) {
                        addGroupDetails(myCurrentGroup);
                    }
                }
            }
        });

        FloatingActionButton myFloatingButtonReject = pView.findViewById(R.id.group_details_holder_join);
        myFloatingButtonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIterator.hasNext()) {
                    Group myCurrentGroup = mIterator.next();
                    if (myCurrentGroup != null) {
                        addGroupDetailsReject(myCurrentGroup);
                    }
                }
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
                mIterator = mRecommendedGroups.iterator();
                if (mIterator.hasNext()) {
                    Group myCurrentGroup = mIterator.next();
                    if (myCurrentGroup != null) {
                        addGroupDetails(myCurrentGroup);
                    }
                    addGroupNav(mGroupDetailsView);
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.e("ERROR GETTING Groups", t.toString());
            }
        });
    }



}
