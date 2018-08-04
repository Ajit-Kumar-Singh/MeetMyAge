package views.pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetmyage.com.meetmyageapp.R;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;

import java.util.ArrayList;
import java.util.List;

import Util.CommonUtil;
import data.ApiClient;
import data.ApiInterface;
import data.SessionManagementUtil;
import data.model.Group;
import data.model.Profile;
import data.model.ProfilePhotoResponse;
import data.model.gmaps.GroupMembers;
import data.model.gmaps.RecommendedGroupDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailsFragmentHelper {

    long mGroupId;
    String mGroupName;
    String mGroupDesc;
    data.model.Location mGroupLocation;
    Context mContext;
    LayoutInflater mInflater;
    FragmentManager fragmentManager;
    GroupMembers[] groupMembers;
    public GroupDetailsFragmentHelper(long pGroupId, String pGroupName, String pGroupDesc,data.model.Location pGroupLocation , Context pContext, LayoutInflater pInflater, FragmentManager pFragMentManager) {
        mGroupId = pGroupId;
        mGroupName = pGroupName;
        mGroupDesc = pGroupDesc;
        mGroupLocation = pGroupLocation;
        mContext = pContext;
        mInflater = pInflater;
        fragmentManager = pFragMentManager;
    }
    public void populateDescription(View pView) {
        TextView myNameTextView = pView.findViewById(R.id.recommended_group_details_group_name);
        TextView myDescTextView = pView.findViewById(R.id.recommended_group_details_group_desc);
        myNameTextView.setText(mGroupName);
        myDescTextView.setText(mGroupDesc);
        TextView myGroupName = pView.findViewById(R.id.recommended_group_details_distance);
        float distance = 0.0f;
        if (mGroupLocation != null) {
            Location locationA = new Location("point A");

            locationA.setLatitude(mGroupLocation.getLatitude());
            locationA.setLongitude(mGroupLocation.getLongitude());

            Location locationB = new Location("point B");

            locationB.setLatitude(SessionManagementUtil.getLocation().getLatitude());
            locationB.setLongitude(SessionManagementUtil.getLocation().getLongitude());

            distance = locationA.distanceTo(locationB);
            Log.d("SETTING_LOCATION", mGroupLocation.getCity()+", "+ distance/1000 +"km" );
            myGroupName.setText(mGroupLocation.getCity()+", "+ distance/1000 +"km" );
        }

    }
    public void addImages(View pView) {
        List<MediaInfo> infos = new ArrayList<>();
        Bitmap myManImage = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.man);
        Bitmap resized = Bitmap.createScaledBitmap(myManImage, 500, 350, true);
        Bitmap myNavImage = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.nav_menu_header_bg);
        Bitmap resized2 = Bitmap.createScaledBitmap(myNavImage, 500, 350, true);
        infos.add(MediaInfo.mediaLoader(new DefaultImageLoader(resized)));
        infos.add(MediaInfo.mediaLoader(new DefaultImageLoader(resized2)));
        infos.add(MediaInfo.mediaLoader(new DefaultImageLoader(resized)));

        ScrollGalleryView scrollGalleryView = (ScrollGalleryView)pView.findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(false)
                .setFragmentManager(fragmentManager)
                .addMedia(infos)
                .hideThumbnails();

    }

    public void addMembers(View pView) {
        LinearLayout myLayout = pView.findViewById(R.id.recommendedGroupsMembers);
        for (GroupMembers myMember:groupMembers) {
            fetchProfilePicFromServerAndSaveToBitmap(myMember.getProfileId(),myLayout);
        }
    }

    public void getRecommendGroups(final View pView) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        pView.setVisibility(View.INVISIBLE);
        Call<RecommendedGroupDetails> call = null;
        Profile myProfile = SessionManagementUtil.getUserData();
        Log.i("LOGGED_IN_PROFILE_ID",String.valueOf(myProfile.getProfileId()));
        call = apiService.getRecommendedGroupDetails(myProfile.getProfileId(),(int)mGroupId);
        call.enqueue(new Callback<RecommendedGroupDetails>() {

            @Override
            public void onResponse(Call<RecommendedGroupDetails> call, Response<RecommendedGroupDetails> response) {
                mGroupLocation = response.body().getGroupDetails().getLocation();
                groupMembers = response.body().getGroupMembers();

                populateDescription(pView);
                addMembers(pView);
                addImages(pView);
                pView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<RecommendedGroupDetails> call, Throwable t) {
                Log.e("ERROR GETTING Groups", t.toString());
            }
        });
    }

    private void fetchProfilePicFromServerAndSaveToBitmap(int profileID,final LinearLayout myLayout)
    {

        final long myStartTime = System.currentTimeMillis();
        //Update Data to server
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ProfilePhotoResponse> call = null;
        call = apiService.fetchProfileData(profileID);
        call.enqueue(new Callback<ProfilePhotoResponse>() {
            @Override
            public void onResponse(Call<ProfilePhotoResponse> call, Response<ProfilePhotoResponse> response) {
                ProfilePhotoResponse responseProfile = response.body();
                String data = responseProfile.getData();
                View myView = mInflater.inflate(R.layout.fragment_group_members, null);
                Log.d("Web service call time ",String.valueOf(System.currentTimeMillis()-myStartTime));
                if (data.isEmpty())
                {
                    myLayout.addView(myView);
                }
                else
                {
                    long myStartTime2 = System.currentTimeMillis();
                    Bitmap mBitmap = CommonUtil.convertStringToBitmap(responseProfile.getData());

                    ImageView myImgView = myView.findViewById(R.id.group_members_profile_pic);
                    myImgView.setImageBitmap(mBitmap);
                    myLayout.addView(myView);
                    Log.d("Render time ",String.valueOf(System.currentTimeMillis()-myStartTime2));
                }
            }

            @Override
            public void onFailure(Call<ProfilePhotoResponse> call, Throwable t) {
                // Log error here since request failed
                View myView = mInflater.inflate(R.layout.fragment_group_members, null);
                myLayout.addView(myView);
            }
        });
    }
}
