package ch.epfl.sdp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.AuthFragmentBinding;

public class AuthFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "AuthFragment";
    private final static int RC_GOOGLE_SIGN_IN = 9001;

    private AuthViewModel mViewModel;
    private AuthFragmentBinding mBinding;

    private GoogleSignInClient mGoogleSignInClient;
    private Authenticator mAuthenticator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mAuthenticator = new FirebaseAuthenticator(FirebaseAuth.getInstance());

        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mViewModel.getUser().observe(this, user -> {
            if(user != null) {
                mBinding.txtUserName.setText(user.getName());
                mBinding.txtUserEmail.setText(user.getEmail());
                mBinding.btnGoogleSignIn.setEnabled(false);
                mBinding.btnLogout.setEnabled(true);
            }
            else {
                mBinding.txtUserName.setText("Anonymous");
                mBinding.txtUserEmail.setText("");
                mBinding.btnGoogleSignIn.setEnabled(true);
                mBinding.btnLogout.setEnabled(false);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = AuthFragmentBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        mBinding.btnGoogleSignIn.setOnClickListener(this);
        mBinding.btnLogout.setOnClickListener(this);
        mBinding.btnTest.setOnClickListener(this);

        mViewModel.setUser(mAuthenticator.getCurrentUser());

        return view;
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
                mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), task -> mViewModel.setUser(null));
                break;

            case R.id.btn_test:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
