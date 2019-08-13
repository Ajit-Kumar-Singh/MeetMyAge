package views.pages;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetmyage.com.meetmyageapp.R;

import data.MockData;
import data.SessionManagementUtil;
import data.model.Group;
import data.model.Profile;
import data.model.message.GroupMessage;
import data.model.message.Message;
import data.model.message.MessageParcel;
import mma.SmackChatManager;
import mma.services.GroupMessageService;
import mma.services.factory.ServiceFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupChat.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupChat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupChat extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GROUPID = "GroupId";
    private static final String ARG_PROFILEID = "ProfileId";

    // TODO: Rename and change types of parameters
    private Long mGroupId;
    private Long mProfileId;
    private Group mGroup;

    private OnFragmentInteractionListener mListener;
    private GroupMessagesRecyclerViewAdapter mGroupMessagesRecyclerViewAdapter;

    public GroupChat() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pProfileId Profile Id.
     * @param pGroupId Group Id.
     * @return A new instance of fragment GroupChat.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupChat newInstance(Long pProfileId, Long pGroupId) {
        GroupChat fragment = new GroupChat();
        Bundle args = new Bundle();
        args.putLong(ARG_GROUPID, pGroupId);
        args.putLong(ARG_PROFILEID, pProfileId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGroupId = getArguments().getLong(ARG_GROUPID);
            mProfileId = getArguments().getLong(ARG_PROFILEID);
            mGroup = ServiceFactory.getInstance().getGroupService().findGroupById(mGroupId);
        }
    }

    private void getMucSubRoomInfo() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupDetailView =  inflater.inflate(R.layout.fragment_group_chat, container, false);
        ((TextView)groupDetailView.findViewById(R.id.groupName)).setText(mGroup.getGroupName());
        ((TextView)groupDetailView.findViewById(R.id.groupStory)).setText(mGroup.getGroupStory());
        View groupMessagesView = groupDetailView.findViewById(R.id.group_messages);
        if (groupMessagesView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) groupMessagesView;
            mGroupMessagesRecyclerViewAdapter = new GroupMessagesRecyclerViewAdapter(this.mGroupId, recyclerView);
            (recyclerView).setAdapter(mGroupMessagesRecyclerViewAdapter);
        }
        groupDetailView.findViewById(R.id.button_chatbox_send).setOnClickListener(this);
        groupDetailView.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        return groupDetailView;
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
    }

    @Override
    public void onPause() {
        ServiceFactory.getInstance().getGroupMessageService().unregisterDatasetListener(mGroupMessagesRecyclerViewAdapter);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        View groupChatView = getActivity().getCurrentFocus();
        TextView chatTextBox = (TextView)groupChatView.findViewById(R.id.edittext_chatbox);
        if ( chatTextBox != null && chatTextBox.getText() != null) {
            CharSequence inputMessage = chatTextBox.getText();
            GroupMessage groupMessage = new GroupMessage();
            groupMessage.setGroupId(this.mGroupId);
            groupMessage.setSender(SessionManagementUtil.getUserData().getProfileEmail());
            groupMessage.setChatMessage(inputMessage.toString());
            mGroupMessagesRecyclerViewAdapter.onReceive(groupMessage);
            chatTextBox.setText(null);
            GroupMessageService groupMessageService = ServiceFactory.getInstance().getGroupMessageService();
            groupMessageService.saveGroupMessageInternal(groupMessage);
        }
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