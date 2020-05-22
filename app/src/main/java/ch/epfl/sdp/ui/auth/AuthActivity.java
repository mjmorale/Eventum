package ch.epfl.sdp.ui.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.ObjectUtils;
import ch.epfl.sdp.databinding.ActivityAuthBinding;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.event.EventActivity;
import ch.epfl.sdp.ui.main.MainActivity;
import ch.epfl.sdp.ui.offline.OfflineActivity;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AuthActivity extends AppCompatActivity implements AuthFragment.OnAuthFragmentResultListener {

    private ActivityAuthBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
    }

    @Override
    public void onLoggedIn(String userRef) {
        Intent outputIntent;
        Intent inputIntent = getIntent();
        String action = inputIntent.getAction();
        Uri data = inputIntent.getData();
        if(Intent.ACTION_VIEW.equals(action) && data != null) {
            // The activity was started by clicking on a shared link
            outputIntent = getEventIntentFromUri(data);
        }
        else {
            // The activity was started normally
            outputIntent = new Intent(this, MainActivity.class);
        }
        outputIntent.putExtra(UIConstants.BUNDLE_USER_REF, userRef);
        startActivity(outputIntent);
        finish();
    }

    @Override
    public void onOffline() {
        startActivity(new Intent(this, OfflineActivity.class));
        finish();
    }

    /**
     * Utility method that creates an Intent given a URI created by the share button
     *
     * @param data URI to transform
     * @return Intent containing the event reference used afterwards to query the event
     */
    private Intent getEventIntentFromUri(@NonNull Uri data) {
        verifyNotNull(data);

        List<String> args = data.getPathSegments();
        String eventRef = args.get(args.size() - 1);

        Intent eventIntent = new Intent(this, EventActivity.class);
        eventIntent.putExtra(UIConstants.BUNDLE_EVENT_MODE_REF, EventActivity.EventActivityMode.ATTENDEE);
        eventIntent.putExtra(UIConstants.BUNDLE_EVENT_REF, eventRef);

        return eventIntent;
    }
}
