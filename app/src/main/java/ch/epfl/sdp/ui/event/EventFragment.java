package ch.epfl.sdp.ui.event;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.epfl.sdp.databinding.EventFragmentBinding;

public class EventFragment extends Fragment {

    private EventViewModel mViewModel;
    private EventFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = EventFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            binding.date.setText(event.getDate().toString());
            binding.description.setText(event.getDescription());
            binding.title.setText(event.getTitle());
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public EventViewModel getViewModel() {
        return mViewModel;
    }
}
