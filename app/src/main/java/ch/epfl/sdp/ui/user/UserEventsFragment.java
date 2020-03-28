package ch.epfl.sdp.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.lifecycle.ViewModelProvider;
import ch.epfl.sdp.databinding.FragmentUserEventsBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserEventsFragment extends Fragment {

    static class UserEventsViewModelFactory extends ParameterizedViewModelFactory {

        public UserEventsViewModelFactory() {
            super(Database.class);
        }

        public void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }
    }

    private FragmentUserEventsBinding mBinding;
    private UserEventsViewModel mViewModel;
    private final UserEventsViewModelFactory mFactory;

    public UserEventsFragment() {
        mFactory = new UserEventsViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @VisibleForTesting
    public UserEventsFragment(@NonNull Database database) {
        mFactory = new UserEventsViewModelFactory();
        mFactory.setDatabase(database);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUserEventsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this, mFactory).get(UserEventsViewModel.class);
    }
}
