package views.pages;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetmyage.com.meetmyageapp.R;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.List;

import Util.RecyclerViewItemClickListener;
import data.MockData;
import data.model.Group;
import data.model.message.GroupMessage;
import data.model.message.MessageParcel;
import mma.services.factory.ServiceFactory;

public class MyGroupsRecyclerViewAdapter  extends RecyclerView.Adapter<MyGroupsRecyclerViewAdapter.GroupViewHolder>{

    private List<Group> groups;
    private Long profileId;
    private RecyclerViewItemClickListener itemClickListener;
    private Context context;
    private FragmentManager fragmentManager;

    public MyGroupsRecyclerViewAdapter(Context pContext, Long profileId, FragmentManager pFragmentManager) {
        groups = ServiceFactory.getInstance().getGroupService().findJoinedGroups(profileId);
        this.profileId = profileId;
        context = pContext;
        fragmentManager = pFragmentManager;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groupView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_group, parent, false);
        GroupViewHolder myGroupViewHolder = new GroupViewHolder(groupView);
        groupView.setOnClickListener(myGroupViewHolder);
        LocalBroadcastManager.getInstance(context).registerReceiver(myGroupViewHolder.getBroadCastReceiver(), new IntentFilter("GroupMessage"));
        return myGroupViewHolder;
    }

    @Override
    public void onViewRecycled(GroupViewHolder pViewHolder) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(pViewHolder.getBroadCastReceiver());;
    }


    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class MyGroupsBroadcastReceiver extends BroadcastReceiver {
        private GroupViewHolder groupViewHolder;
        public MyGroupsBroadcastReceiver(GroupViewHolder pGroupViewHolder) {
            groupViewHolder = pGroupViewHolder;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w("app", intent.toString());
            MessageParcel messageParcel = (MessageParcel)intent.getSerializableExtra("messageParcel");
            GroupMessage groupMessage = (GroupMessage)messageParcel.getMessage();
            if (groupMessage.getGroupId().intValue() == groupViewHolder.getAdapterPosition()) {
                if (groupViewHolder.unreadMessageView != null) {
                    groupViewHolder.unreadMessageView.setText(groupMessage.getChatMessage());
                    groupViewHolder.unreadMessageView.invalidate();
                    groupViewHolder.unreadMessageView.postInvalidate();
                }
            }
        }
    }
    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView groupImageView;
        public TextView groupNameView;
        public TextView groupStoryView;
        public TextView unreadMessageView;
        public Group group;
        public GroupViewHolder(View itemView) {
            super(itemView);
            groupImageView = itemView.findViewById(R.id.groupImage);
            groupNameView = itemView.findViewById(R.id.groupName);
            groupStoryView = itemView.findViewById(R.id.groupStory);
            itemView.setOnClickListener(this);
        }

        public void bind(Group pGroup) {
            this.group = pGroup;
            groupNameView.setText(this.group.getGroupName());
            groupStoryView.setText(this.group.getGroupStory());
        }

        @Override
        public void onClick(View view) {
            GroupChat groupChatFragment = GroupChat.newInstance(1L, Long.valueOf(this.group.getGroupId()));
            fragmentManager.beginTransaction().replace(R.id.fragment_conatiner, groupChatFragment, "goToGroupChat").addToBackStack(null).commit();
        }

        public BroadcastReceiver getBroadCastReceiver() {
            return new MyGroupsBroadcastReceiver(this);
        }
    }


}
