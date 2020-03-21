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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.ui.main.AuthViewModel;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "AuthFragment";
    private final static int RC_GOOGLE_SIGN_IN = 9001;

    private AuthViewModel mViewModel;
    private Intent activityIntent;
    private GoogleSignInClient mGoogleSignInClient;
    private Authenticator mAuthenticator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.auth_activity);
        SignInButton btnGoogleSignIn = (SignInButton) findViewById(R.id.btn_google_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuthenticator = new FirebaseAuthenticator(FirebaseAuth.getInstance());

        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mViewModel.getUser().observe(this, user -> {
            if(user != null) {
                btnGoogleSignIn.setEnabled(false);
                activityIntent = new Intent(this, MainActivity.class);
                startActivity(activityIntent);
            }
            else {
                btnGoogleSignIn.setEnabled(true);
            }
        });

        btnGoogleSignIn.setOnClickListener(this);
        mViewModel.setUser(mAuthenticator.getCurrentUser());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuthenticator.login(credential, result -> {
                    if(result.isSuccessful()) {
                        mViewModel.setUser(result.getUser());
                    } else {
                        Log.w(TAG, "Firebase authentication failed", result.getException());
                    }
                });
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

            case R.id.btn_logout:
                mAuthenticator.logout();
                mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> mViewModel.setUser(null));
                break;
        }
    }
}
