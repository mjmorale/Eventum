package ch.epfl.sdp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.databinding.ActivityAuthBinding;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.main.MainActivity;

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
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.putExtra(UIConstants.BUNDLE_USER_REF, userRef);
        startActivity(mainActivityIntent);
        finish();
    }
}
