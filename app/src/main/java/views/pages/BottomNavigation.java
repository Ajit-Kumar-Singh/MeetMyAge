package views.pages;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.meetmyage.com.meetmyageapp.R;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Session;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import data.SessionManagementUtil;
import data.model.Group;
import data.model.Location;
import data.model.message.MessageParcel;
import mma.SmackChatManager;
import mma.receivers.broadcast.IncomingGroupMessageReceiver;
import mma.receivers.broadcast.OfflineBroadcastReceiver;

public class BottomNavigation extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
EventsDetailsFragment.OnFragmentInteractionListener,
CreateGroupFragment.OnFragmentInteractionListener,
RecommendedGroupsFragment.OnFragmentInteractionListener,
ProfileDetails.OnFragmentInteractionListener,
ProfileSettings.OnFragmentInteractionListener,
JoinedGroupMembers.OnListFragmentInteractionListener,
GroupEventsFragment.OnListFragmentInteractionListener,
        MyGroupsFragment.OnFragmentInteractionListener
{
    private Bitmap mBitmap = null;
    private BottomNavigationView bottomNavigationView;
    private boolean mKeyboardVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbedlayout);
        //TempFix for location
        Location location = SessionManagementUtil.getLocation();
        if (location == null) {
            Location tempLocation = new Location();
            tempLocation.setAddressLine1("Prime Legend Apartment");
            tempLocation.setCity("Hyderabad");
            tempLocation.setState("Telengana");
            tempLocation.setPinCode(500084L);
            tempLocation.setLatitude(17.385044);
            tempLocation.setLongitude(78.486671);
            SessionManagementUtil.updateLocation(tempLocation);
        }
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.default_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.groups);
        disableShiftMode(bottomNavigationView);
        loadDefaultFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.groups:
                                item.setCheckable(true);
                                Fragment groupsFragment = new RecommendedGroupsFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, groupsFragment).addToBackStack("rootStack").commit();
                                break;
                            case R.id.addGroup:
                                item.setCheckable(true);
                                Fragment addGroupFragment = new CreateGroupFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, addGroupFragment).commit();
                                break;
                            case R.id.events:
                                item.setCheckable(true);
                                Fragment eventFragment= new MyGroupsFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, eventFragment).commit();
                                break;
                            case R.id.profile:
                                item.setCheckable(true);
                                Fragment profileFragment = new ProfileFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, profileFragment).commit();
                                break;
                        }
                        return true;
                    }
                });
        MyChatAsyncTask myChatAsyncTask = new MyChatAsyncTask(this);
        myChatAsyncTask.execute();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getBottomNavigationView().getViewTreeObserver()
                .addOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPause() {
        super.onPause();
        getBottomNavigationView().getViewTreeObserver()
                .removeOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener mLayoutKeyboardVisibilityListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    final Rect rectangle = new Rect();
                    View contentView = getBottomNavigationView();
                    contentView.getWindowVisibleDisplayFrame(rectangle);
                    int screenHeight = contentView.getRootView().getHeight();

                    // r.bottom is the position above soft keypad or device button.
                    // If keypad is shown, the rectangle.bottom is smaller than that before.
                    int keypadHeight = screenHeight - rectangle.bottom;
                    // 0.15 ratio is perhaps enough to determine keypad height.
                    boolean isKeyboardNowVisible = keypadHeight > screenHeight * 0.15;

                    if (mKeyboardVisible != isKeyboardNowVisible) {
                        if (isKeyboardNowVisible) {
                            onKeyboardShown();
                        } else {
                            onKeyboardHidden();
                        }
                    }
                    mKeyboardVisible = isKeyboardNowVisible;
                    }
                };


    private void onKeyboardShown() {
        getBottomNavigationView().setVisibility(View.GONE);
    }

    private void onKeyboardHidden() {
        getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    private void loadDefaultFragment()
    {
        Fragment groupsFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conatiner, groupsFragment).commit();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
        // Empty for now USed for interaction
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        //Empty for now used for interaction
    }


    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }


    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }


    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }

    @Override
    public void onListFragmentInteraction(JoinedGroupMemberDetail.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(GroupEventCardContent.GroupEventCardItem item) {

    }

    public ServiceConnection getChatServiceConnection(final Context pContext) {
        return new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }

    public class ChatService extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    public class MyChatAsyncTask extends AsyncTask {
        private Context context;
        public MyChatAsyncTask(Context pContext) {
            this.context = pContext;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            Looper.prepare();
            OfflineBroadcastReceiver groupMessageReceiver = IncomingGroupMessageReceiver.getInstance();
            LocalBroadcastManager.getInstance(this.context).registerReceiver(groupMessageReceiver, groupMessageReceiver.getCriteria());
            ChatManager chatManager = SmackChatManager.getInstance().getChatManager();
            chatManager.addChatListener(new MmaChatManagerListener(this.context));
            Looper.loop();
            return null;
        }
    }

    public class MmaChatManagerListener implements ChatManagerListener {
        private Context context;
        public MmaChatManagerListener(Context pContext) {
            context = pContext;
        }
        @Override
        public void chatCreated(Chat chat, boolean b) {
            chat.addMessageListener(new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    if (message.getBody() != null) {
                        String loggedInUserEmail = SessionManagementUtil.getUserData().getProfileEmail();
                        String messageBody = message.getBody();
                        messageBody = StringEscapeUtils.unescapeHtml4(messageBody);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Type messageType = new TypeToken<List<Group>>() {}.getType();
                        gsonBuilder.registerTypeAdapter(messageType, MessageParcel.getJsonDeserializer());
                        MessageParcel messageParcel = gsonBuilder.create().fromJson(messageBody, messageType);
                        if (messageParcel.getMessage() != null && !loggedInUserEmail.equals(messageParcel.getMessage().getSender())) {
                            messageParcel.getMessage().setIncomingMessage(true);
                        }
                        if (messageParcel.getMessage().isIncomingMessage()) {
                            Intent messageIntent = new Intent(messageParcel.getMessageType());
                            messageIntent.putExtra("messageParcel", messageParcel);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(messageIntent);
                        }
                    }
                }
            });

        }
    }
}
