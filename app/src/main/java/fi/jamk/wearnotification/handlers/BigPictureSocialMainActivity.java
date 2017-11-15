package fi.jamk.wearnotification.handlers;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import fi.jamk.wearnotification.R;
import fi.jamk.wearnotification.StandaloneMainActivity;

/**
 * Created by osmel on 15.11.2017.
 */

public class BigPictureSocialMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture_main);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(StandaloneMainActivity.NOTIFICATION_ID);
    }
}
