package fi.jamk.wearnotification.handlers;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import fi.jamk.wearnotification.GlobalNotificationBuilder;
import fi.jamk.wearnotification.R;
import fi.jamk.wearnotification.StandaloneMainActivity;
import fi.jamk.wearnotification.mock.MockDatabase;

/**
 * Created by osmel on 15.11.2017.
 */

public class BigPictureSocialIntentService extends IntentService {

    private static final String TAG = "BigPictureService";
    public static final String ACTION_COMMENT = "com.example.android.wearable.wear.wearnotifications.handlers.action.COMMENT";
    public static final String EXTRA_COMMENT = "com.example.android.wearable.wear.wearnotifications.handlers.extra.COMMENT";

    public BigPictureSocialIntentService() {
        super("BigPictureSocialIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent(): " + intent);

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_COMMENT.equals(action)) {
                handleActionComment(getMessage(intent));
            }
        }
    }

    private void handleActionComment(CharSequence comment) {
        Log.d(TAG, "handleActionComment(): " + comment);

        if (comment != null) {

            NotificationCompat.Builder notificationCompatBuilder = GlobalNotificationBuilder.getNotificationCompatBuilderInstance();

            if (notificationCompatBuilder == null) {
                notificationCompatBuilder = recreateBuilderWithBigPictureStyle();
            }

            Notification updatedNotification = notificationCompatBuilder
                    .setRemoteInputHistory(new CharSequence[]{comment})
                    .build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(StandaloneMainActivity.NOTIFICATION_ID, updatedNotification);
        }
    }

    private CharSequence getMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(EXTRA_COMMENT);
        }
        return null;
    }

    private NotificationCompat.Builder recreateBuilderWithBigPictureStyle() {

        MockDatabase.BigPictureStyleSocialAppData bigPictureStyleSocialAppData = MockDatabase.getBigPictureStyleData();

        String notificationChannelId = bigPictureStyleSocialAppData.getChannelId();
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(getResources(), bigPictureStyleSocialAppData.getBigImage()))
                .setBigContentTitle(bigPictureStyleSocialAppData.getBigContentTitle())
                .setSummaryText(bigPictureStyleSocialAppData.getSummaryText());

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

        final NotificationCompat.Action.WearableExtender inlineActionForWear2 =
                new NotificationCompat.Action.WearableExtender()
                        .setHintDisplayActionInline(true)
                        .setHintLaunchesActivity(false);

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(R.mipmap.ic_logo, replyLabel, replyActionPendingIntent)
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

        return notificationCompatBuilder;
    }
}