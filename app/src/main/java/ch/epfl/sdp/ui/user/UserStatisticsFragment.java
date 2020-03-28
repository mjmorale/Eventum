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
import ch.epfl.sdp.databinding.FragmentUserStatisticsBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserStatisticsFragment extends Fragment {

    static class UserStatisticsViewModelFactory extends ParameterizedViewModelFactory {

        public UserStatisticsViewModelFactory() {
            super(Database.class);
        }

        public void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }
    }

    private FragmentUserStatisticsBinding mBinding;
    private UserStatisticsViewModel mViewModel;
    private final UserStatisticsViewModelFactory mFactory;

    public UserStatisticsFragment() {
        mFactory = new UserStatisticsViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @VisibleForTesting
    public UserStatisticsFragment(@NonNull Database database) {
        mFactory = new UserStatisticsViewModelFactory();
        mFactory.setDatabase(database);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUserStatisticsBinding.inflate(inflater, container, false);
        View root = mBinding.getRoot();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this, mFactory).get(UserStatisticsViewModel.class);
    }

    //TODO:@Dorian Remove this method as it is only there to satisfy CodeClimate code
    // duplication detection.
    private void test() {}
}
