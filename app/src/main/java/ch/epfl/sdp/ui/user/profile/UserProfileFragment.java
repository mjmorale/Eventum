package ch.epfl.sdp.ui.user.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import javax.annotation.Nullable;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentUserProfileBinding;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.ServiceProvider;

import static android.app.Activity.RESULT_OK;
import static ch.epfl.sdp.ui.UIConstants.RC_CHOOSE_PHOTO;

public class UserProfileFragment extends Fragment {

    private static final int PERMISSION_STORAGE = 100;
    private final UserProfileViewModel.UserProfileViewModelFactory mUserProfileViewModelFactory;
    private UserProfileViewModel mViewModel;
    private FragmentUserProfileBinding mBinding;
    private Uri mImageUri;
    private FirestoreStorage.UrlReadyCallback mUploadCallBack;
    private Storage mStorage;


    public UserProfileFragment() {
        mUserProfileViewModelFactory = new UserProfileViewModel.UserProfileViewModelFactory();
        mUserProfileViewModelFactory.setStorage(ServiceProvider.getInstance().getStorage());
        mUserProfileViewModelFactory.setAuthenticator(ServiceProvider.getInstance().getAuthenticator());
        mUserProfileViewModelFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this, mUserProfileViewModelFactory).get(UserProfileViewModel.class);
        mBinding.userProfilePhoto.setOnClickListener(v -> {
            loadImage(ServiceProvider.getInstance().getStorage(), new FirestoreStorage.UrlReadyCallback() {
                @Override
                public void onSuccess(String url) {
                    mViewModel.updateImageId(url);
                }

                @Override
                public void onFailure() {
                }
            });
        });
        mViewModel.getUserLive().observe(getViewLifecycleOwner(), user -> {
            mBinding.userProfileName.setText(user.getName());
            mBinding.userProfileBio.setText(user.getDescription());
            ImageGetter.getInstance().getImage(getContext(), user.getImageId(), mBinding.userProfilePhoto);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.updateDescription(mBinding.userProfileBio.getText().toString());
    }

    public void loadImage(Storage storage, FirestoreStorage.UrlReadyCallback uploadCallBack) {
        mUploadCallBack = uploadCallBack;
        mStorage = storage;
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean hasPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;

        if (hasPermission) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RC_CHOOSE_PHOTO, null);

        } else {
            Toast.makeText(getContext(), R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                mImageUri = data.getData();
                ImageGetter.getInstance().getImage(getContext(), mImageUri, mBinding.userProfilePhoto);
                mStorage.uploadImage(mImageUri, mUploadCallBack);
            } else {
                Toast.makeText(getContext(), R.string.no_image_chosen, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
