package ch.epfl.sdp.ui.auth;

import android.content.Context;
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
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.lifecycle.ViewModelProvider;

import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentAuthBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.offline.ConnectivityService;
import ch.epfl.sdp.platforms.android.AndroidConnectivityService;
import ch.epfl.sdp.platforms.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;

public class AuthFragment extends Fragment implements View.OnClickListener {

    public interface OnAuthFragmentResultListener {
        void onLoggedIn(String userRef);

        void onOffline();
    }

    private final static String TAG = "AuthFragment";

    private OnAuthFragmentResultListener mAuthListener;

    private final AuthViewModel.AuthViewModelFactory mFactory;
    private FragmentAuthBinding mBinding;
    private AuthViewModel<AuthCredential> mViewModel;

    private GoogleSignInClient mGoogleSignInClient;
    private ConnectivityService mConnectivityService;

    public AuthFragment() {
        mFactory = new AuthViewModel.AuthViewModelFactory();
        mFactory.setAuthenticator(ServiceProvider.getInstance().getAuthenticator());
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());

        mConnectivityService = new AndroidConnectivityService();
    }

    @VisibleForTesting
    public AuthFragment(@NonNull Authenticator authenticator, @NonNull Database database, @NonNull ConnectivityService connectivityService) {
        mFactory = new AuthViewModel.AuthViewModelFactory();
        mFactory.setAuthenticator(authenticator);
        mFactory.setDatabase(database);

        mConnectivityService = connectivityService;
    }

    @VisibleForTesting
    protected void setAuthListener(OnAuthFragmentResultListener listener) {
        mAuthListener = listener;
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
        mViewModel.getUserRef().observe(getViewLifecycleOwner(), userRef -> {
            if(userRef == null) {
                mBinding.btnGoogleSignIn.setEnabled(true);
            }
            else if (mAuthListener != null) {
                if (mConnectivityService.isNetworkAvailable(getContext())) {
                    mAuthListener.onLoggedIn(userRef);
                } else {
                    mAuthListener.onOffline();
                }
            } else {
                Log.d(TAG, "Logged in successful but no AuthListener");
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof OnAuthFragmentResultListener) {
            mAuthListener = (OnAuthFragmentResultListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAuthListener = null;
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
                Toast.makeText(getContext(), "You need to be online for your first connection", Toast.LENGTH_LONG).show();
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
