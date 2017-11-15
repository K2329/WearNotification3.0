package fi.jamk.wearnotification;

import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.view.View;

/**
 * Created by osmel on 15.11.2017.
 */

public class ScalingScrollLayoutCallback extends WearableLinearLayoutManager.LayoutCallback {

    private static final float MAX_CHILD_SCALE = 0.65f;
    private float mProgressToCenter;

    @Override
    public void onLayoutFinished(View child, RecyclerView parent) {

        float centerOffset = ((float) child.getHeight() / 2.0f) /  (float) parent.getHeight();
        float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

        mProgressToCenter = Math.abs(0.5f - yRelativeToCenterOffset);
        mProgressToCenter = Math.min(mProgressToCenter, MAX_CHILD_SCALE);

        child.setScaleX(1 - mProgressToCenter);
        child.setScaleY(1 - mProgressToCenter);
    }
}