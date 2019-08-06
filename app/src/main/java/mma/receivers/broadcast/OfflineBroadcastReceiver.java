package mma.receivers.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class OfflineBroadcastReceiver<T> extends BroadcastReceiver {
    private Type type;
    public OfflineBroadcastReceiver(Type type) {
        this.type = type;
    }
    public Type getType() {
        return type;
    }
    public enum Type {
        GroupMessage;
    }

    public abstract IntentFilter getCriteria();

    @Override
    public void onReceive(Context context, Intent intent) {
        T convertedObject = convert(intent);
        onReceive(convertedObject);
    }

    public abstract void onReceive(T pMessage);

    public abstract T convert(Intent intent);
}
