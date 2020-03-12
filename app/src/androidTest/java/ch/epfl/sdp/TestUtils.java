package ch.epfl.sdp;

import android.app.Activity;
import android.content.Intent;

public class TestUtils {

    public static void dismissSystemPopups(Activity activity) {
        // Dismiss any system dialog that could hijack the focus
        // See: https://stackoverflow.com/questions/39457305/android-testing-waited-for-the-root-of-the-view-hierarchy-to-have-window-focus
        activity.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
}
