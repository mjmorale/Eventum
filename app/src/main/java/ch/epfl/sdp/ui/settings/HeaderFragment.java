package ch.epfl.sdp.ui.settings;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import ch.epfl.sdp.R;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class HeaderFragment extends PreferenceFragmentCompat {

    private AccountFragment.OnLogoutListener mLogoutListener = null;

    private SettingsViewModel mViewModel;

    private Preference mLogoutPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.header_preferences, rootKey);

        mLogoutPreference = verifyNotNull(findPreference("log_out"));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(getActivity()).get(SettingsViewModel.class);

        mViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if(user != null) {
                mLogoutPreference.setSummary(user.getEmail());
            }
        });

        setupLogoutPref(mLogoutPreference);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof AccountFragment.OnLogoutListener) {
            mLogoutListener = (AccountFragment.OnLogoutListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mLogoutListener = null;
    }

    private void setupLogoutPref(@NonNull Preference logoutPref) {
        verifyNotNull(logoutPref);

        logoutPref.setOnPreferenceClickListener(preference -> {
            mViewModel.logout();

            if(mLogoutListener != null) {
                mLogoutListener.onLogout();
            }
            return true;
        });
    }
}
