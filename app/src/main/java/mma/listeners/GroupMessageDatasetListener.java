package mma.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import data.model.message.GroupMessage;

public interface GroupMessageDatasetListener {
    void onReceive(GroupMessage pGroupMessage);

    boolean isApplicable(GroupMessage pGroupMessage);
}
