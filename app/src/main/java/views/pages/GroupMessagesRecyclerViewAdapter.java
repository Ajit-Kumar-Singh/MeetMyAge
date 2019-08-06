package views.pages;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.meetmyage.com.meetmyageapp.R;
import com.meetmyage.com.meetmyageapp.databinding.LayoutChatMessageBinding;

import java.util.ArrayList;
import java.util.List;

import data.SessionManagementUtil;
import data.model.message.GroupMessage;
import mma.listeners.GroupMessageDatasetListener;
import mma.services.factory.ServiceFactory;

public class GroupMessagesRecyclerViewAdapter extends RecyclerView.Adapter<GroupMessagesRecyclerViewAdapter.GroupMessageViewHolder> implements GroupMessageDatasetListener {
    private List<GroupMessage> groupMessageList;
    private RecyclerView recyclerView;
    private Long groupId;
    public GroupMessagesRecyclerViewAdapter(Long pGroupId, RecyclerView pRecyclerView) {
        groupMessageList = new ArrayList<>();
        List<GroupMessage> previousMessages = ServiceFactory.getInstance().getGroupMessageService().loadAllGroupMessages(pGroupId, 0, 0, this);
        if (previousMessages != null) {
            groupMessageList.addAll(previousMessages);
        }
        groupId = pGroupId;
        recyclerView = pRecyclerView;
    }
    @Override
    public GroupMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutChatMessageBinding chatMessageBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_chat_message, parent, false);
        GroupMessageViewHolder groupMessageViewHolder = new GroupMessageViewHolder(chatMessageBinding);
        return groupMessageViewHolder;
    }

    @Override
    public void onBindViewHolder(GroupMessageViewHolder holder, int position) {
        holder.bindMessage(groupMessageList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return groupMessageList.size();
    }

    @Override
    public void onReceive(GroupMessage pGroupMessage) {
        groupMessageList.add(pGroupMessage);
        notifyItemInserted(getItemCount() - 1);
        recyclerView.smoothScrollToPosition(getItemCount() - 1);
    }

    @Override
    public boolean isApplicable(GroupMessage pGroupMessage) {
        return pGroupMessage.getGroupId().equals(groupId);
    }


    public class GroupMessageViewHolder extends RecyclerView.ViewHolder {
        private LayoutChatMessageBinding chatMessageBinding;
        public GroupMessageViewHolder(LayoutChatMessageBinding chatMessageBinding) {
            super(chatMessageBinding.getRoot());
            this.chatMessageBinding = chatMessageBinding;
        }

        public void bindMessage(GroupMessage pGroupMessage, int pIndex) {
            String messageSender = pGroupMessage.getSender();
            boolean hideSenderName = messageSender != null && SessionManagementUtil.getUserData().getProfileEmail().equals(pGroupMessage.getSender());
            pGroupMessage.setSenderSelf(hideSenderName);
            if (!hideSenderName && pIndex > 0) {
                GroupMessage previousGroupMessage = groupMessageList.get(pIndex - 1);
                hideSenderName = messageSender != null && messageSender.equals(previousGroupMessage.getSender());
            }
            pGroupMessage.setHideSender(hideSenderName);
            this.chatMessageBinding.setGroupMessage(pGroupMessage);
        }
    }
}
