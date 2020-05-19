package ch.epfl.sdp.ui.user.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ch.epfl.sdp.databinding.UserProfileBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class ProfileFragment extends Fragment {
    private UserProfileBinding mBinding;
    private ProfileViewModel.ProfileViewModelFactory mFactory;
    private ProfileViewModel mViewModel;

    public static ProfileFragment getInstance(String userRef){
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_USER_REF, verifyNotNull(userRef));
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    public ProfileFragment(){
        super();
        mFactory = new ProfileViewModel.ProfileViewModelFactory();
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
    }

    @VisibleForTesting
    public ProfileFragment(Database database, String userRef){
        mFactory = new ProfileViewModel.ProfileViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setUserRef(userRef);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        mBinding = UserProfileBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            mFactory.setUserRef(verifyNotNull(args.getString(UIConstants.BUNDLE_USER_REF)));
        }

        mViewModel = new ViewModelProvider(this, mFactory).get(ProfileViewModel.class);

        mViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            ImageGetter.getInstance().getImage(getContext(), user.getImageId(), mBinding.userPicture);
            mBinding.userProfileName.setText(user.getName());
            mBinding.userInterests.setText(user.getDescription());
            mBinding.userEmail.setText(user.getEmail());
        });

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mBinding = null;
    }

}
