package ch.epfl.sdp.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import ch.epfl.sdp.R;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Fragment for the account information of the user
 */
public class AccountFragment extends PreferenceFragmentCompat {

    public interface OnLogoutListener {
        void onLogout();
    }

    private static final String TAG = "AccountFragment";

    private OnLogoutListener mLogoutListener = null;

    private SettingsViewModel mViewModel;

    private Preference mAccountNamePreference;
    private Preference mAccountEmailPreference;
    private Preference mAccountDeletePreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.account_preferences, rootKey);

        mAccountNamePreference = verifyNotNull(findPreference("account_name"));
        mAccountEmailPreference = verifyNotNull(findPreference("account_email"));
        mAccountDeletePreference = verifyNotNull(findPreference("account_delete"));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(getActivity()).get(SettingsViewModel.class);

        mViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if(user != null) {
                mAccountNamePreference.setSummary(user.getName());
                mAccountEmailPreference.setSummary(user.getEmail());
            }
        });

        setupAccountDeletePref(mAccountDeletePreference);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof OnLogoutListener) {
            mLogoutListener = (OnLogoutListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mLogoutListener = null;
    }

    private void setupAccountDeletePref(@NonNull Preference accountDeletePreference) {
        verifyNotNull(accountDeletePreference);

        accountDeletePreference.setOnPreferenceClickListener(preference -> {
            new AlertDialog.Builder(getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete Account")
                    .setMessage("You are about to delete your user account. Are you sure ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        mViewModel.deleteAccount(result -> {
                            if(result.isSuccessful()) {
                                mViewModel.logout();

                                if(mLogoutListener != null) {
                                    mLogoutListener.onLogout();
                                }
                            }
                            else {
                                Log.e(TAG, "Failed to delete account", result.getException());
                                Toast.makeText(getContext(), "Cannot delete account", Toast.LENGTH_LONG).show();
                            }
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }
}
