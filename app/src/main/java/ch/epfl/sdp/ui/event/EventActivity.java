package ch.epfl.sdp.ui.event;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.databinding.ActivityEventBinding;
import ch.epfl.sdp.ui.UIConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activity used for a single event
 */
public class EventActivity extends AppCompatActivity {

    public enum EventActivityMode {
        ORGANIZER,
        ATTENDEE
    }

    private ActivityEventBinding mBinding;

    /**
     * Create a start intent for the EventActivity
     *
     * @param context The context to use in the intent
     * @param mode The start mode for the activity
     * @param eventRef The database reference of the event to display
     * @return The constructed intent to start the activity
     */
    public static Intent getStartIntent(Context context, EventActivity.EventActivityMode mode, String eventRef) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra(UIConstants.BUNDLE_EVENT_MODE_REF, mode);
        intent.putExtra(UIConstants.BUNDLE_EVENT_REF, eventRef);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEventBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.eventToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        EventActivityMode mode = (EventActivityMode) intent.getSerializableExtra(UIConstants.BUNDLE_EVENT_MODE_REF);
        if(mode == null) {
            mode = EventActivityMode.ATTENDEE;
        }
        String eventRef = intent.getStringExtra(UIConstants.BUNDLE_EVENT_REF);
        if(eventRef == null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        switch(mode) {
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(mBinding.eventContent.getId(), DefaultEventFragment.getInstance(eventRef)).commit();
        }
    }

    @Override
    protected void onDestroy() {
        mBinding = null;
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }
        finish();
        return true;
    }
}
