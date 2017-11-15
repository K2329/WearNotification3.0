package fi.jamk.wearnotification.handlers;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import fi.jamk.wearnotification.GlobalNotificationBuilder;
import fi.jamk.wearnotification.R;
import fi.jamk.wearnotification.StandaloneMainActivity;
import fi.jamk.wearnotification.mock.MockDatabase;

/**
 * Created by osmel on 15.11.2017.
 */

public class BigTextIntentService extends IntentService {

    private static final String TAG = "BigTextService";

    public static final String ACTION_DISMISS = "com.example.android.wearable.wear.wearnotifications.handlers.action.DISMISS";
    public static final String ACTION_SNOOZE = "com.example.android.wearable.wear.wearnotifications.handlers.action.SNOOZE";
    private static final long SNOOZE_TIME = TimeUnit.SECONDS.toMillis(5);

    public BigTextIntentService() {
        super("BigTextIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent(): " + intent);

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DISMISS.equals(action)) {
                handleActionDismiss();
            } else if (ACTION_SNOOZE.equals(action)) {
                handleActionSnooze();
            }
        }
    }

    private void handleActionDismiss() {
        Log.d(TAG, "handleActionDismiss()");

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(StandaloneMainActivity.NOTIFICATION_ID);
    }

    private void handleActionSnooze() {
        Log.d(TAG, "handleActionSnooze()");

        NotificationCompat.Builder notificationCompatBuilder = GlobalNotificationBuilder.getNotificationCompatBuilderInstance();

        if (notificationCompatBuilder == null) {
            notificationCompatBuilder = recreateBuilderWithBigTextStyle();
        }

        Notification notification;
        notification = notificationCompatBuilder.build();


        if (notification != null) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.cancel(StandaloneMainActivity.NOTIFICATION_ID);

            try {
                Thread.sleep(SNOOZE_TIME);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            notificationManagerCompat.notify(StandaloneMainActivity.NOTIFICATION_ID, notification);
        }

    }

    private NotificationCompat.Builder recreateBuilderWithBigTextStyle() {

        MockDatabase.BigTextStyleReminderAppData bigTextStyleReminderAppData = MockDatabase.getBigTextStyleData();

        String notificationChannelId = bigTextStyleReminderAppData.getChannelId();

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(bigTextStyleReminderAppData.getBigText())
                .setBigContentTitle(bigTextStyleReminderAppData.getBigContentTitle())
                .setSummaryText(bigTextStyleReminderAppData.getSummaryText());

        Intent mainIntent = new Intent(this, BigTextMainActivity.class);

        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snoozeIntent = new Intent(this, BigTextIntentService.class);
        snoozeIntent.setAction(BigTextIntentService.ACTION_SNOOZE);

        PendingIntent snoozePendingIntent = PendingIntent.getService(this, 0, snoozeIntent, 0);
        NotificationCompat.Action snoozeAction = new NotificationCompat.Action.Builder(R.drawable.ic_snooze2, "Snooze", snoozePendingIntent)
                .build();

        Intent dismissIntent = new Intent(this, BigTextIntentService.class);
        dismissIntent.setAction(BigTextIntentService.ACTION_DISMISS);

        PendingIntent dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, 0);
        NotificationCompat.Action dismissAction = new NotificationCompat.Action.Builder(R.drawable.ic_dismiss2, "Dismiss", dismissPendingIntent)
                .build();

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext(), notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        notificationCompatBuilder
                .setStyle(bigTextStyle)
                .setContentTitle(bigTextStyleReminderAppData.getContentTitle())
                .setContentText(bigTextStyleReminderAppData.getContentText())
                .setSmallIcon(R.mipmap.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_backround2))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(bigTextStyleReminderAppData.getPriority())
                .setVisibility(bigTextStyleReminderAppData.getChannelLockscreenVisibility())
                .addAction(snoozeAction)
                .addAction(dismissAction);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            NotificationCompat.Action mainAction = new NotificationCompat.Action.Builder(R.drawable.ic_open2, "Open", mainPendingIntent)
                    .build();

            notificationCompatBuilder.addAction(mainAction);

        } else {
            notificationCompatBuilder.setContentIntent(mainPendingIntent);
        }

        return notificationCompatBuilder;
    }
}