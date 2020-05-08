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
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;

import static android.app.Activity.RESULT_OK;
import static ch.epfl.sdp.ui.UIConstants.RC_CHOOSE_PHOTO;

public class UserProfileFragment extends Fragment {
    private UserProfileViewModel mViewModel;
    private final UserProfileViewModel.MyViewModelFactory mFactory;
    private static final int PERMISSION_STORAGE = 100;

    private FragmentUserProfileBinding mBinding;
    private Uri mImageUri;


    public UserProfileFragment() {
        mFactory = new UserProfileViewModel.MyViewModelFactory();
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
        mFactory.setStorage(ServiceProvider.getInstance().getStorage());
        mFactory.setAuthenticator(ServiceProvider.getInstance().getAuthenticator());
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
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_STORAGE);
        });
        mBinding.userProfileName.setText(mViewModel.getUser().getDisplayName());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean hasPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;

        if (hasPermission) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RC_CHOOSE_PHOTO);

        } else {
            Toast.makeText(getContext(), R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                mImageUri = data.getData();
                displayImage();
                mViewModel.uploadImage(mImageUri);
            } else {
                Toast.makeText(getContext(), R.string.no_image_chosen, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void displayImage() {
        ImageGetter.getInstance().getImage(getContext(), this.mImageUri, this.mBinding.userProfilePhoto);
        mBinding.userProfilePhoto.setTag("new_image");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

}
