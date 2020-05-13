package ch.epfl.sdp.ui.createevent;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.databinding.ActivityCreateEventBinding;

/**
 * Activity for the creation of a new event
 */
public class CreateEventActivity extends AppCompatActivity {

    private ActivityCreateEventBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityCreateEventBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.createEventToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(mBinding.createEventFragment.getId(), new CreateEventFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        mBinding = null;
        super.onDestroy();
    }
}
