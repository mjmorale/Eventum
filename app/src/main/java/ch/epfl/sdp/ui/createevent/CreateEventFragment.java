package ch.epfl.sdp.ui.createevent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;

import androidx.lifecycle.ViewModelProvider;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentCreateEventBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.UIConstants;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class CreateEventFragment extends Fragment implements View.OnClickListener {
    private FragmentCreateEventBinding mBinding;
    private CreateEventViewModel mViewModel;
    private final CreateEventViewModel.CreateEventViewModelFactory mFactory;

    private static final int THRESHOLD = 2;
    private LatLng mSelectedLocation;

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
                            getActivity().setResult(Activity.RESULT_OK, resultIntent);
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
        }
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