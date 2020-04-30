package ch.epfl.sdp.ui.user;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.databinding.ActivityUserBinding;
import ch.epfl.sdp.ui.user.events.UserEventsFragment;
import ch.epfl.sdp.ui.user.stats.UserStatisticsFragment;

import com.google.android.material.tabs.TabLayout;

/**
 * Activity for informations about a user (statistics and user's events)
 */
public class UserActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private ActivityUserBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityUserBinding.inflate(getLayoutInflater());
        View root = mBinding.getRoot();
        setContentView(root);

        setSupportActionBar(mBinding.userToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mBinding.userTabLayout.addOnTabSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(mBinding.userContent.getId(), new UserStatisticsFragment()).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch(tab.getPosition()) {
            case 0:
                // User statistics tab was selected
                getSupportFragmentManager().beginTransaction()
                        .replace(mBinding.userContent.getId(), new UserStatisticsFragment()).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(mBinding.userContent.getId(), new UserEventsFragment()).commit();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) { }

    @Override
    public void onTabReselected(TabLayout.Tab tab) { }
}
