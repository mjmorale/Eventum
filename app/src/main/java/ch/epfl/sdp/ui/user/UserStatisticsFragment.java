package ch.epfl.sdp.ui.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import ch.epfl.sdp.databinding.UserStatisticsFragmentBinding;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.FirestoreDatabaseViewModelFactory;

public class UserStatisticsFragment extends Fragment {

    private UserStatisticsFragmentBinding mBinding;
    private UserStatisticsViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = UserStatisticsFragmentBinding.inflate(inflater, container, false);
        View root = mBinding.getRoot();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DatabaseViewModelFactory factory = FirestoreDatabaseViewModelFactory.getInstance();
        mViewModel = new ViewModelProvider(this, factory).get(UserStatisticsViewModel.class);
    }
}
