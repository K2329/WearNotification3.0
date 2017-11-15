package fi.jamk.wearnotification;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import fi.jamk.wearnotification.handlers.BigPictureSocialIntentService;
import fi.jamk.wearnotification.handlers.BigPictureSocialMainActivity;
import fi.jamk.wearnotification.handlers.BigTextIntentService;
import fi.jamk.wearnotification.handlers.BigTextMainActivity;
import fi.jamk.wearnotification.mock.MockDatabase;
import fi.jamk.wearnotification.util.NotificationUtil;

public class StandaloneMainActivity extends Activity {

    private static final String TAG = "StandaloneMainActivity";
    public static final int NOTIFICATION_ID = 888;
    private static final String BIG_TEXT_STYLE = "Ilmoitus herätyksellä";
    private static final String BIG_PICTURE_STYLE = "Ilmoitus kuvalla";
    private static final String[] NOTIFICATION_STYLES =
            {BIG_TEXT_STYLE, BIG_PICTURE_STYLE};

    private NotificationManagerCompat mNotificationManagerCompat;
    private FrameLayout mMainFrameLayout;
    private WearableRecyclerView mWearableRecyclerView;
    private CustomRecyclerAdapter mCustomRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");


        setContentView(R.layout.activity_main);
        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        mMainFrameLayout = findViewById(R.id.mainFrameLayout);
        mWearableRecyclerView = findViewById(R.id.recycler_view);
        mWearableRecyclerView.setEdgeItemsCenteringEnabled(true);
        ScalingScrollLayoutCallback scalingScrollLayoutCallback = new ScalingScrollLayoutCallback();
        mWearableRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this, scalingScrollLayoutCallback));
        mWearableRecyclerView.setHasFixedSize(true);
        mCustomRecyclerAdapter = new CustomRecyclerAdapter(NOTIFICATION_STYLES, new Controller(this));
        mWearableRecyclerView.setAdapter(mCustomRecyclerAdapter);
    }

    public void itemSelected(String data) {

        Log.d(TAG, "itemSelected()");

        boolean areNotificationsEnabled = mNotificationManagerCompat.areNotificationsEnabled();

        String notificationStyle = data;

        switch (notificationStyle) {
            case BIG_TEXT_STYLE:
                generateBigTextStyleNotification();
                break;

            case BIG_PICTURE_STYLE:
                generateBigPictureStyleNotification();
                break;

            default:

        }
    }

    private void generateBigTextStyleNotification() {

        Log.d(TAG, "generateBigTextStyleNotification()");

        MockDatabase.BigTextStyleReminderAppData bigTextStyleReminderAppData = MockDatabase.getBigTextStyleData();
        String notificationChannelId = NotificationUtil.createNotificationChannel(this, bigTextStyleReminderAppData);
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
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
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


        Notification notification = notificationCompatBuilder.build();
        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);

        finish();
    }

    private void generateBigPictureStyleNotification() {

        Log.d(TAG, "generateBigPictureStyleNotification()");

        MockDatabase.BigPictureStyleSocialAppData bigPictureStyleSocialAppData = MockDatabase.getBigPictureStyleData();
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(getResources(), bigPictureStyleSocialAppData.getBigImage()))
                .setBigContentTitle(bigPictureStyleSocialAppData.getBigContentTitle())
                .setSummaryText(bigPictureStyleSocialAppData.getSummaryText());

        String notificationChannelId = NotificationUtil.createNotificationChannel(this, bigPictureStyleSocialAppData);
        Intent mainIntent = new Intent(this, BigPictureSocialMainActivity.class);

        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String replyLabel = getString(R.string.reply_label);
        RemoteInput remoteInput = new RemoteInput.Builder(BigPictureSocialIntentService.EXTRA_COMMENT)
                .setLabel(replyLabel)
                .setChoices(bigPictureStyleSocialAppData.getPossiblePostResponses())
                .build();

        Intent replyIntent = new Intent(this, BigPictureSocialIntentService.class);
        replyIntent.setAction(BigPictureSocialIntentService.ACTION_COMMENT);
        PendingIntent replyActionPendingIntent = PendingIntent.getService(this, 0, replyIntent, 0);
        final NotificationCompat.Action.WearableExtender inlineActionForWear2 = new NotificationCompat.Action.WearableExtender()
                .setHintDisplayActionInline(true)
                .setHintLaunchesActivity(false);

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.mipmap.ic_logo,
                replyLabel,
                replyActionPendingIntent)
                .addRemoteInput(remoteInput)
                .extend(inlineActionForWear2)
                .build();

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext(), notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        notificationCompatBuilder
                .setStyle(bigPictureStyle)
                .setContentTitle(bigPictureStyleSocialAppData.getContentTitle())
                .setContentText(bigPictureStyleSocialAppData.getContentText())
                .setSmallIcon(R.mipmap.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo))
                .setContentIntent(mainPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setSubText(Integer.toString(1))
                .addAction(replyAction)
                .setCategory(Notification.CATEGORY_SOCIAL)
                .setPriority(bigPictureStyleSocialAppData.getPriority())
                .setVisibility(bigPictureStyleSocialAppData.getChannelLockscreenVisibility())
                .extend(new NotificationCompat.WearableExtender()
                        .setHintContentIntentLaunchesActivity(true));
        for (String name : bigPictureStyleSocialAppData.getParticipants()) {
            notificationCompatBuilder.addPerson(name);
        }

        Notification notification = notificationCompatBuilder.build();
        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);

        finish();
    }


    private void openNotificationSettingsForApp() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);
        startActivity(intent);
    }

}