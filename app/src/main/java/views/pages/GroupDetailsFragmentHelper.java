package views.pages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetmyage.com.meetmyageapp.R;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;

import java.util.ArrayList;
import java.util.List;

import data.SessionManagementUtil;

public class GroupDetailsFragmentHelper {

    long mGroupId;
    String mGroupName;
    String mGroupDesc;
    data.model.Location mGroupLocation;
    Context mContext;
    LayoutInflater mInflater;
    FragmentManager fragmentManager;
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
            myGroupName.setText(mGroupLocation.getCity()+", "+ distance );
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
                .addMedia(infos);

    }

    public void addMembers(View pView) {
        LinearLayout myLayout = pView.findViewById(R.id.recommendedGroupsMembers);
        for (int i=1; i <=15; i++) {
            View myView = mInflater.inflate(R.layout.fragment_group_members, null);
            myLayout.addView(myView);
        }
    }
}
