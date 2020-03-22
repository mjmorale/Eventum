package ch.epfl.sdp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import ch.epfl.sdp.databinding.AuthActivityBinding;
import ch.epfl.sdp.firebase.auth.FirebaseAuthenticator;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String USER_EXTRA = "user_data";

    private final static String TAG = "AuthActivity";
    private final static int RC_GOOGLE_SIGN_IN = 9001;

    private AuthViewModel<AuthCredential> mViewModel;
    private AuthActivityBinding mBinding;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = AuthActivityBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.btnGoogleSignIn.setOnClickListener(this);
        mBinding.btnGoogleSignIn.setEnabled(false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        AuthViewModelFactory<AuthCredential> viewModelFactory = new AuthViewModelFactory<>(new FirebaseAuthenticator(FirebaseAuth.getInstance()));
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(AuthViewModel.class);

        mViewModel.getUser().observe(this, user -> {
            if(user == null) {
                mBinding.btnGoogleSignIn.setEnabled(true);
            }
            else {
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                mainActivityIntent.putExtra(USER_EXTRA, user);
                startActivity(mainActivityIntent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mViewModel.login(credential);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_google_sign_in:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
                break;
        }
    }
}
