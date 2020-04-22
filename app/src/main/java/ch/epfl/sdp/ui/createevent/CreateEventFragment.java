package ch.epfl.sdp.ui.createevent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.ParseException;
import java.util.UUID;
import androidx.lifecycle.ViewModelProvider;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentCreateEventBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.UIConstants;
import static android.app.Activity.RESULT_OK;
import static ch.epfl.sdp.ObjectUtils.verifyNotNull;
import static ch.epfl.sdp.ui.UIConstants.RC_CHOOSE_PHOTO;

public class CreateEventFragment extends Fragment implements View.OnClickListener {
    private FragmentCreateEventBinding mBinding;
    private CreateEventViewModel mViewModel;
    private final CreateEventViewModel.CreateEventViewModelFactory mFactory;

    private static final int THRESHOLD = 2;
    private LatLng mSelectedLocation;

    private static final int PERMISSION_STORAGE = 100;
    private Uri mImageUri;
    private String mImageId = "https://firebasestorage.googleapis.com/v0/b/eventum-6a6b7.appspot.com" +
            "/o/eventDefault.jpg?alt=media&token=a6d345fa-a513-478d-a019-2307ee50022b";

    public CreateEventFragment() {
        mFactory = new CreateEventViewModel.CreateEventViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @VisibleForTesting
    public CreateEventFragment(@NonNull Database database) {
        mFactory = new CreateEventViewModel.CreateEventViewModelFactory();
        mFactory.setDatabase(database);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCreateEventBinding.inflate(inflater, container, false);
        setupGeoAutoComplete();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this, mFactory).get(CreateEventViewModel.class);

        mBinding.createButton.setOnClickListener(this);
        mBinding.addImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createButton:
                try {
                    tryCreateEvent(new CreateEventViewModel.OnEventCreatedCallback() {
                        @Override
                        public void onSuccess(String eventRef) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(UIConstants.BUNDLE_EVENT_REF, eventRef);
                            getActivity().setResult(RESULT_OK, resultIntent);
                            getActivity().finish();
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            getActivity().setResult(Activity.RESULT_CANCELED);
                            getActivity().finish();
                        }
                    });
                } catch (ParseException | IllegalArgumentException e) {
                    Toast.makeText(getContext(), R.string.toast_incorrect_input, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.addImageButton:
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_STORAGE);
        }
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
                uploadImageInFirebase();
            } else {
                Toast.makeText(getContext(), R.string.no_image_chosen, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayImage() {
        Glide.with(this)
                .load(this.mImageUri)
                .into(this.mBinding.imageView);
        mBinding.imageView.setTag("new_image");
    }

    private void uploadImageInFirebase() {
        String imageUUID = UUID.randomUUID().toString();
        StorageReference reference = FirebaseStorage.getInstance().getReference(imageUUID);
        reference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                mImageId = urlTask.getResult().toString();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void checkInput(String title, String description, String date, String address) throws IllegalArgumentException {
        verifyNotNull(title, description, date);
        if (title.isEmpty() || description.isEmpty() || date.isEmpty() || address.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void tryCreateEvent(@NonNull CreateEventViewModel.OnEventCreatedCallback callback) throws ParseException {
        String title = mBinding.title.getText().toString();
        String description = mBinding.description.getText().toString();
        String date = mBinding.date.getDayOfMonth() + "/" + mBinding.date.getMonth() + "/" + mBinding.date.getYear();
        String address = mBinding.geoAutocomplete.getText().toString();
        checkInput(title, description, date, address);

        EventBuilder eventBuilder = new EventBuilder();
        Event event = eventBuilder.setTitle(title)
                .setDescription(description)
                .setDate(date)
                .setLocation(mSelectedLocation)
                .setAddress(address)
                .setImageId(mImageId)
                .build();

        mViewModel.insertEvent(event, callback);
    }


    public void setupGeoAutoComplete() {
        mBinding.geoAutocomplete.setThreshold(THRESHOLD);
        mBinding.geoAutocomplete.setAdapter(new GeoAutoCompleteAdapter(getContext()));

        mBinding.geoAutocomplete.setOnItemClickListener((adapterView, view, position, id) -> {
            GeoSearchResult result = (GeoSearchResult) adapterView.getItemAtPosition(position);
            mBinding.geoAutocomplete.setText(result.getAddress());
            mSelectedLocation = result.getLocation();
        });

        mBinding.geoAutocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    mBinding.geoAutocompleteClear.setVisibility(View.VISIBLE);
                } else {
                    mBinding.geoAutocompleteClear.setVisibility(View.GONE);
                }
            }
        });

        mBinding.geoAutocompleteClear.setOnClickListener(v -> mBinding.geoAutocomplete.setText(""));
    }

}
