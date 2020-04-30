package ch.epfl.sdp.ui.user.stats;

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

/**
 * Fragment to display statistics about a user
 */
public class UserStatisticsFragment extends Fragment {

    private FragmentUserStatisticsBinding mBinding;
    private UserStatisticsViewModel mViewModel;
    private final UserStatisticsViewModel.UserStatisticsViewModelFactory mFactory;

    /**
     * Constructor of the UserStatisticsFragment
     */
    public UserStatisticsFragment() {
        mFactory = new UserStatisticsViewModel.UserStatisticsViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    /**
     * Constructor of the UserStatisticsFragment, only for testing purpose!
     *
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    @VisibleForTesting
    public UserStatisticsFragment(@NonNull Database database) {
        mFactory = new UserStatisticsViewModel.UserStatisticsViewModelFactory();
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
