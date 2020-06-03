package ch.epfl.sdp.ui.offline;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ch.epfl.sdp.databinding.ActivityOfflineBinding;
import ch.epfl.sdp.ui.main.attending.AttendingListFragment;

/**
 * Activity that is launched if network is not available.
 */
public class OfflineActivity extends AppCompatActivity {

    private ActivityOfflineBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityOfflineBinding.inflate(getLayoutInflater());
        mBinding.mainToolbar.setTitle("Offline mode");

        setContentView(mBinding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .replace(mBinding.mainContainer.getId(), new AttendingListFragment()).commit();
    }
}
