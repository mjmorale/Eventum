package ch.epfl.sdp.ui.user.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import javax.annotation.Nullable;

import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentUserProfileBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.ServiceProvider;

import static android.app.Activity.RESULT_OK;
import static ch.epfl.sdp.ui.UIConstants.RC_CHOOSE_PHOTO;

/**
 * The fragment responsible of the user profile editing
 */
public class UserProfileFragment extends Fragment {

    private static final int PERMISSION_STORAGE = 100;
    private UserProfileViewModel mViewModel;
    private FragmentUserProfileBinding mBinding;
    private FirestoreStorage.UrlReadyCallback mUploadCallBack;
    private final UserProfileViewModel.UserProfileViewModelFactory mFactory;
    private Uri mImageURi;
    /**
     * the constructor
     */
    public UserProfileFragment(){
        mFactory = new UserProfileViewModel.UserProfileViewModelFactory();
        mFactory.setStorage(ServiceProvider.getInstance().getStorage());
        mFactory.setAuthenticator(ServiceProvider.getInstance().getAuthenticator());
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
    }

    /**
     * Should only be used for testing
     * @param storage to upload pictures
     * @param authenticator to get the current user id
     * @param database to get the current user info
     */
    @VisibleForTesting
    public UserProfileFragment(@NonNull Storage storage,@NonNull Authenticator authenticator,@NonNull Database database){
        mFactory = new UserProfileViewModel.UserProfileViewModelFactory();
        mFactory.setStorage(storage);
        mFactory.setAuthenticator(authenticator);
        mFactory.setDatabase(database);
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

        mViewModel = new ViewModelProvider(this, mFactory).get(UserProfileViewModel.class);

        mBinding.userProfilePhoto.setOnClickListener(v -> {
            loadImage(new FirestoreStorage.UrlReadyCallback() {
                @Override
                public void onSuccess(String url) {
                    mViewModel.updateImageId(url);
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getContext(), "Cannot change the profile picture, try again later", Toast.LENGTH_SHORT).show();
                }
            });
        });
        mViewModel.getUserLive().observe(getViewLifecycleOwner(), user -> {
            mBinding.userProfileName.setText(user.getName());
            mBinding.userProfileBio.getText().clear();
            mBinding.userProfileBio.append(user.getDescription());
            if(!user.getImageId().isEmpty())
                ImageGetter.getInstance().getImage(getContext(), user.getImageId(), mBinding.userProfilePhoto);
        });

        mBinding.userProfileCheckButton.setOnClickListener(v -> {
            mViewModel.updateDescription(mBinding.userProfileBio.getText().toString());
            if(mImageURi!=null)
                mViewModel.uploadImage(mImageURi, mUploadCallBack);
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
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
    }

    /**
     * @brief load an image from the phone gallery
     * @param uploadCallBack the callback after the upload result
     */
    public void loadImage(FirestoreStorage.UrlReadyCallback uploadCallBack) {
        mUploadCallBack = uploadCallBack;
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

    /**
     * @brief the result of the activity of choosing from the gallery intent
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                mImageURi= data.getData();
                mViewModel.displayImage(mImageURi, getContext(), mBinding.userProfilePhoto);
            } else {
                Toast.makeText(getContext(), R.string.no_image_chosen, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
