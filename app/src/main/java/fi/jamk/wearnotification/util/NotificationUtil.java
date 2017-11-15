package fi.jamk.wearnotification.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import fi.jamk.wearnotification.mock.MockDatabase;

/**
 * Created by osmel on 15.11.2017.
 */

public class NotificationUtil {

    public static String createNotificationChannel(
            Context context,
            MockDatabase.MockNotificationData mockNotificationData) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = mockNotificationData.getChannelId();
            CharSequence channelName = mockNotificationData.getChannelName();
            String channelDescription = mockNotificationData.getChannelDescription();

            int channelImportance = mockNotificationData.getChannelImportance();
            boolean channelEnableVibrate = mockNotificationData.isChannelEnableVibrate();
            int channelLockscreenVisibility = mockNotificationData.getChannelLockscreenVisibility();

            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            return null;
        }
    }
}