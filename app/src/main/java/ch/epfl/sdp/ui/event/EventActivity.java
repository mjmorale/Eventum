package ch.epfl.sdp.ui.event;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.databinding.EventActivityBinding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EventActivity extends AppCompatActivity {

    public static final String EVENT_MODE_EXTRA = "event_mode_extra";
    public static final String EVENT_REF_EXTRA = "event_ref_extra";

    public enum EventActivityMode {
        ORGANIZER,
        ATTENDEE
    }

    private EventActivityBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = EventActivityBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.eventToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        EventActivityMode mode = (EventActivityMode) intent.getSerializableExtra(EVENT_MODE_EXTRA);
        if(mode == null) {
            mode = EventActivityMode.ATTENDEE;
        }
        String eventRef = intent.getStringExtra(EVENT_REF_EXTRA);
        if(eventRef == null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        switch(mode) {
            case ORGANIZER:
            case ATTENDEE:
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(mBinding.eventContent.getId(), DefaultEventFragment.newInstance(eventRef)).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
}
