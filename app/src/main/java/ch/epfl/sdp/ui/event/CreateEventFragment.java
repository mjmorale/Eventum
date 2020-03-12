package ch.epfl.sdp.ui.event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.CreateEventFragmentBinding;


public class CreateEventFragment extends Fragment {
    private EventViewModel mViewModel;
    private CreateEventFragmentBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = CreateEventFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        mBinding.createButton.setOnClickListener(v -> {
            String title = mBinding.title.getText().toString();
            String description = mBinding.description.getText().toString();
            String date = mBinding.date.getText().toString();
            try {
                checkInput(title, description, date);
                LiveData<String> ref = mViewModel.createEvent(title, description, date);
                ref.observe(getViewLifecycleOwner(), result -> {
                                getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, EventFragment.newInstance(result))
                    .commitNow();
                });
            } catch (ParseException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Invalid date", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });

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
}
