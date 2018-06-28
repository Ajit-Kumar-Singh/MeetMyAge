package views.pages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetmyage.com.meetmyageapp.R;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import data.SessionManagementUtil;
import data.model.Group;

public class GroupDetailsFragment extends Fragment {

    LayoutInflater mInflater = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_group_details, container, false);
        mInflater = inflater;
        ButterKnife.bind(this,view);
        addImages(view);
        populateDescription(view);
        addMembers(view);
        return view;
    }
    private void populateDescription(View pView) {
        TextView myNameTextView = pView.findViewById(R.id.recommended_group_details_group_name);
        TextView myDescTextView = pView.findViewById(R.id.recommended_group_details_group_desc);
        TextView myMembesTag = pView.findViewById(R.id.recommended_group_details_MembersTag);
        Group mySelectedGroup = SessionManagementUtil.getSelectedGroup();
        myNameTextView.setText(mySelectedGroup.getGroupName());
        myDescTextView.setText(mySelectedGroup.getGroupStory());
        myMembesTag.setText("Members");
    }
    private void addImages(View pView) {
        List<MediaInfo> infos = new ArrayList<>();
        Bitmap myManImage = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.man);
        Bitmap resized = Bitmap.createScaledBitmap(myManImage, 700, 500, true);
        infos.add(MediaInfo.mediaLoader(new DefaultImageLoader(resized)));
        infos.add(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.nav_menu_header_bg)));
        infos.add(MediaInfo.mediaLoader(new DefaultImageLoader(resized)));

        ScrollGalleryView scrollGalleryView = (ScrollGalleryView)pView.findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(false)
                .setFragmentManager(getFragmentManager())
                .addMedia(infos);

    }

    private void addMembers(View pView) {
        LinearLayout myLayout = pView.findViewById(R.id.recommendedGroupsMembers);
        for (int i=1; i <=5; i++) {
            View myView = mInflater.inflate(R.layout.fragment_group_members, null);
            TextView mMemberName = myView.findViewById(R.id.group_members_name);
            mMemberName.setText("Bharwa number "+i);
            Log.i("BHARWA NO",""+i);
            myLayout.addView(myView);
        }
    }
}
