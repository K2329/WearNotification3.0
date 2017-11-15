package fi.jamk.wearnotification;

import android.support.v4.app.NotificationCompat;

/**
 * Created by osmel on 15.11.2017.
 */

public final class GlobalNotificationBuilder {

    private static NotificationCompat.Builder sGlobalNotificationCompatBuilder = null;

    private GlobalNotificationBuilder() { }

    public static void setNotificationCompatBuilderInstance (NotificationCompat.Builder builder) {
        sGlobalNotificationCompatBuilder = builder;
    }

    public static NotificationCompat.Builder getNotificationCompatBuilderInstance(){
        return sGlobalNotificationCompatBuilder;
    }
}