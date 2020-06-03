package ch.epfl.sdp.ui.offline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ch.epfl.sdp.databinding.ActivityOfflineBinding;
import ch.epfl.sdp.platforms.android.AndroidConnectivityLiveData;
import ch.epfl.sdp.ui.auth.AuthActivity;
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

        AndroidConnectivityLiveData mConnectivityLiveData = new AndroidConnectivityLiveData(getApplicationContext());
        mConnectivityLiveData.observeForever(isConnected -> {
            if (isConnected) {
                Toast.makeText(this, "Back online!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AuthActivity.class));
            }
        });

        setContentView(mBinding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .replace(mBinding.mainContainer.getId(), new AttendingListFragment()).commit();
    }
}
