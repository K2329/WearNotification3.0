package fi.jamk.wearnotification.mock;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

import fi.jamk.wearnotification.R;

/**
 * Created by osmel on 15.11.2017.
 */

public class MockDatabase {

    public static BigTextStyleReminderAppData getBigTextStyleData() {
        return BigTextStyleReminderAppData.getInstance();
    }

    public static BigPictureStyleSocialAppData getBigPictureStyleData() {
        return BigPictureStyleSocialAppData.getInstance();
    }

    public static class BigTextStyleReminderAppData extends MockNotificationData {

        private static BigTextStyleReminderAppData sInstance = null;
        private String mBigContentTitle;
        private String mBigText;
        private String mSummaryText;

        public static BigTextStyleReminderAppData getInstance() {
            if (sInstance == null) {
                sInstance = getSync();
            }

            return sInstance;
        }

        private static synchronized BigTextStyleReminderAppData getSync() {
            if (sInstance == null) {
                sInstance = new BigTextStyleReminderAppData();
            }

            return sInstance;
        }

        private BigTextStyleReminderAppData() {

            mContentTitle = "Älä unohda...";
            mContentText = "Tehdä Androidin harjoitustyötä!";
            mPriority = NotificationCompat.PRIORITY_DEFAULT;
            mBigContentTitle = "Älä unohda...";
            mBigText = "... tehdä androidin harjoitustyötä ennen kurssin loppumista.";
            mSummaryText = "Androidkurssi";
            mChannelId = "channel_reminder_1";
            mChannelName = "Esimerkki ilmoitus";
            mChannelDescription = "Esimerkki ilmoituksesta";
            mChannelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            mChannelEnableVibrate = false;
            mChannelLockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC;
        }

        public String getBigContentTitle() {
            return mBigContentTitle;
        }

        public String getBigText() {
            return mBigText;
        }

        public String getSummaryText() {
            return mSummaryText;
        }

        @Override
        public String toString() {
            return getBigContentTitle() + getBigText();
        }
    }

    public static class BigPictureStyleSocialAppData extends MockNotificationData {

        private static BigPictureStyleSocialAppData sInstance = null;

        private int mBigImage;
        private String mBigContentTitle;
        private String mSummaryText;
        private CharSequence[] mPossiblePostResponses;
        private ArrayList<String> mParticipants;

        public static BigPictureStyleSocialAppData getInstance() {
            if (sInstance == null) {
                sInstance = getSync();
            }
            return sInstance;
        }

        private static synchronized BigPictureStyleSocialAppData getSync() {
            if (sInstance == null) {
                sInstance = new BigPictureStyleSocialAppData();
            }

            return sInstance;
        }

        private BigPictureStyleSocialAppData() {

            mContentTitle = "Petterin posti";
            mContentText = "[Picture] Mitä pidät?";
            mPriority = NotificationCompat.PRIORITY_HIGH;
            mBigImage = R.mipmap.ic_logo;
            mBigContentTitle = "Petterin posti";
            mSummaryText = "Mitä pidät?";
            mPossiblePostResponses = new CharSequence[]{"Kyllä", "En", "Ehkä"};
            mParticipants = new ArrayList<>();
            mParticipants.add("Petteri Aaltonen");
            mChannelId = "channel_social_1";
            mChannelName = "Esimerkki ilmoitus2";
            mChannelDescription = "Esimerkki ilmoituksesta2";
            mChannelImportance = NotificationManager.IMPORTANCE_HIGH;
            mChannelEnableVibrate = true;
            mChannelLockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE;
        }

        public int getBigImage() {
            return mBigImage;
        }
        public String getBigContentTitle() {
            return mBigContentTitle;
        }
        public String getSummaryText() {
            return mSummaryText;
        }
        public CharSequence[] getPossiblePostResponses() {
            return mPossiblePostResponses;
        }
        public ArrayList<String> getParticipants() {
            return mParticipants;
        }

        @Override
        public String toString() {
            return getContentTitle() + " - " + getContentText();
        }
    }

    public static abstract class MockNotificationData {


        protected String mContentTitle;
        protected String mContentText;
        protected int mPriority;
        protected String mChannelId;
        protected CharSequence mChannelName;
        protected String mChannelDescription;
        protected int mChannelImportance;
        protected boolean mChannelEnableVibrate;
        protected int mChannelLockscreenVisibility;

        public String getContentTitle() {
            return mContentTitle;
        }
        public String getContentText() {
            return mContentText;
        }
        public int getPriority() {
            return mPriority;
        }
        public String getChannelId() {
            return mChannelId;
        }
        public CharSequence getChannelName() {
            return mChannelName;
        }
        public String getChannelDescription() {
            return mChannelDescription;
        }
        public int getChannelImportance() {
            return mChannelImportance;
        }
        public boolean isChannelEnableVibrate() {
            return mChannelEnableVibrate;
        }
        public int getChannelLockscreenVisibility() {
            return mChannelLockscreenVisibility;
        }
    }
}