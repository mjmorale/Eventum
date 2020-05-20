package ch.epfl.sdp.ui.createevent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import androidx.lifecycle.ViewModelProvider;
import ch.epfl.sdp.BitmapUtils;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.databinding.FragmentCreateEventBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;
import static android.app.Activity.RESULT_OK;
import static ch.epfl.sdp.ObjectUtils.verifyNotNull;
import static ch.epfl.sdp.ui.UIConstants.RC_CHOOSE_PHOTO;

/**
 * Fragment for the creation of a new event
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CreateEventFragment";

    private FragmentCreateEventBinding mBinding;
    private CreateEventViewModel mViewModel;
    private final CreateEventViewModel.CreateEventViewModelFactory mFactory;

    private static final int THRESHOLD = 2;
    private LatLng mSelectedLocation;

    private static final int PERMISSION_STORAGE = 100;

    private Bitmap mCurrentImage;

    /**
     * Constructor of the create event fragment
     */
    public CreateEventFragment() {
        mFactory = new CreateEventViewModel.CreateEventViewModelFactory();
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
        mFactory.setStorage(ServiceProvider.getInstance().getStorage());
        mFactory.setAuthenticator(ServiceProvider.getInstance().getAuthenticator());
    }

    /**
     * Constructor of the create event fragment, only for testing purpose!
     *
     * @param storage {@link ch.epfl.sdp.storage.Storage}
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    @VisibleForTesting
    public CreateEventFragment(@NonNull Storage storage, @NonNull Database database, @NonNull Authenticator authenticator) {
        mFactory = new CreateEventViewModel.CreateEventViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setStorage(storage);
        mFactory.setAuthenticator(authenticator);
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
        mBinding.time.setIs24HourView(true);
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
                } catch (IllegalArgumentException e) {
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
                Uri imageUri = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                    mCurrentImage = BitmapUtils.resizeWithHeight(imageBitmap, 600);
                    imageBitmap.recycle();
                    mBinding.imageView.setImageBitmap(mCurrentImage);
                    mBinding.imageView.setTag("new_image");
                } catch (IOException e) {
                    Log.e(TAG, "Cannot retrieve bitmap from file uri", e);
                    Toast.makeText(getContext(), "Cannot load image!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), R.string.no_image_chosen, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void checkInput(String title, String description, Date date, String address) throws IllegalArgumentException {
        verifyNotNull(title, description, date);
        if (title.isEmpty() || description.isEmpty() || address.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void tryCreateEvent(@NonNull CreateEventViewModel.OnEventCreatedCallback callback) {
        String title = mBinding.title.getText().toString();
        String description = mBinding.description.getText().toString();
        Calendar calendar = Calendar.getInstance();
        calendar.set(mBinding.date.getYear(), mBinding.date.getMonth(), mBinding.date.getDayOfMonth(),
                mBinding.time.getCurrentHour(), mBinding.time.getCurrentMinute(), 0);
        Date date = calendar.getTime();

        String address = mBinding.geoAutocomplete.getText().toString();
        checkInput(title, description, date, address);

        EventBuilder eventBuilder = new EventBuilder()
                .setTitle(title)
                .setDescription(description)
                .setDate(date)
                .setLocation(mSelectedLocation)
                .setAddress(address);

        mViewModel.insertEvent(eventBuilder, mCurrentImage, callback);
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
