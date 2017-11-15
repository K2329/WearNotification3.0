package fi.jamk.wearnotification;

/**
 * Created by osmel on 15.11.2017.
 */

public class Controller {

    private StandaloneMainActivity mView;

    Controller(StandaloneMainActivity standaloneMainActivity) {
        mView = standaloneMainActivity;
    }

    public void itemSelected(String notificationStyleSelected) {
        mView.itemSelected(notificationStyleSelected);
    }
}