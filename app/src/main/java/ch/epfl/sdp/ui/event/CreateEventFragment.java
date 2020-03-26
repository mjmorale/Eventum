package ch.epfl.sdp.ui.event;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.CreateEventFragmentBinding;
import ch.epfl.sdp.db.Database;


public class CreateEventFragment extends Fragment implements View.OnClickListener {
    private EventViewModel mViewModel;
    private CreateEventFragmentBinding mBinding;
    private Database mDb;
    private static final int THRESHOLD = 2;
    private DelayAutoCompleteTextView mGeoAutocomplete;
    private ImageView mGeoAutocompleteClear;

    public CreateEventFragment(Database db) {
        mDb = db;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        mViewModel.setDb(mDb);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = CreateEventFragmentBinding.inflate(inflater, container, false);
        mBinding.createButton.setOnClickListener(this);

        mGeoAutocompleteClear = mBinding.geoAutocompleteClear;
        mGeoAutocomplete = mBinding.geoAutocomplete;

        mGeoAutocomplete.setThreshold(THRESHOLD);
        mGeoAutocomplete.setAdapter(new GeoAutoCompleteAdapter(getContext()));

        mGeoAutocomplete.setOnItemClickListener((adapterView, view, position, id) -> {
            GeoSearchResult result = (GeoSearchResult) adapterView.getItemAtPosition(position);
            mGeoAutocomplete.setText(result.getAddress());
        });

        mGeoAutocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    mGeoAutocompleteClear.setVisibility(View.VISIBLE);
                } else {
                    mGeoAutocompleteClear.setVisibility(View.GONE);
                }
            }
        });

        mGeoAutocompleteClear.setOnClickListener(v -> {
            mGeoAutocomplete.setText("");
        });

        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void checkInput(String title, String description, String date) throws IllegalArgumentException {
        if (title == null || description == null || date == null) {
            throw new IllegalArgumentException();
        }
        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public EventViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createButton:
                String title = mBinding.title.getText().toString();
                String description = mBinding.description.getText().toString();
                String date = mBinding.date.getDayOfMonth()+"/"+mBinding.date.getMonth()+"/"+mBinding.date.getYear();

                try {
                    checkInput(title, description, date);
                    LiveData<String> ref = mViewModel.createEvent(title, description, date);
                    ref.observe(getViewLifecycleOwner(), result -> {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, EventFragment.newInstance(result, mDb))
                                .commitNow();
                    });
                } catch (ParseException e) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Invalid date", Toast.LENGTH_SHORT).show();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Invalid input", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
