package com.zzh.lib.utils.extend;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

/**
 * 通知管理
 */
public class HNotification {
    private final Context mContext;

    private NotificationManager mManager;

    public HNotification(Context context) {
        mContext = context.getApplicationContext();
    }

    public NotificationManager getManager() {
        if (mManager == null)
            mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        return mManager;
    }

    public Notification.Builder newBuilder() {
        return newBuilder(null);
    }

    public Notification.Builder newBuilder(String channelId) {
        if (Build.VERSION.SDK_INT >= 26) {
            if (TextUtils.isEmpty(channelId))
                channelId = mContext.getPackageName();

            return new Notification.Builder(mContext, channelId);
        } else {
            return new Notification.Builder(mContext);
        }
    }
}
