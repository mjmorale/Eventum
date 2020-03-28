package ch.epfl.sdp.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

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

import androidx.lifecycle.ViewModelProvider;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentAuthBinding;
import ch.epfl.sdp.platforms.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.main.MainActivity;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AuthFragment extends Fragment implements View.OnClickListener {

    static class AuthViewModelFactory extends ParameterizedViewModelFactory {

        public AuthViewModelFactory() {
            super(Authenticator.class);
        }

        public void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(0, verifyNotNull(authenticator));
        }
    }

    private final static String TAG = "AuthFragment";

    private final AuthViewModelFactory mFactory;
    private FragmentAuthBinding mBinding;
    private AuthViewModel<AuthCredential> mViewModel;

    private GoogleSignInClient mGoogleSignInClient;

    public AuthFragment() {
        mFactory = new AuthViewModelFactory();
        mFactory.setAuthenticator(new FirebaseAuthenticator(FirebaseAuth.getInstance()));
    }

    @VisibleForTesting
    public AuthFragment(@NonNull Authenticator authenticator) {
        mFactory = new AuthViewModelFactory();
        mFactory.setAuthenticator(authenticator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAuthBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        mBinding.btnGoogleSignIn.setEnabled(false);
        mBinding.btnGoogleSignIn.setOnClickListener(this);

        mViewModel = new ViewModelProvider(this, mFactory).get(AuthViewModel.class);
        mViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if(user == null) {
                mBinding.btnGoogleSignIn.setEnabled(true);
            }
            else {
                Intent mainActivityIntent = new Intent(getActivity(), MainActivity.class);
                mainActivityIntent.putExtra(UIConstants.BUNDLE_USER_REF, user.getUid());
                startActivity(mainActivityIntent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UIConstants.RC_GOOGLE_SIGNIN) {
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
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_google_sign_in:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, UIConstants.RC_GOOGLE_SIGNIN);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
