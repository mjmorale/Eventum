package ch.epfl.sdp.ui.event;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.epfl.sdp.databinding.EventFragmentBinding;

public class EventFragment extends Fragment {

    private EventViewModel viewModel;
    private EventFragmentBinding binding;
    private String ref;

    public static EventFragment newInstance(String ref) {
        Bundle bundle = new Bundle();
        bundle.putString("dbRef", ref);

        EventFragment fragment = new EventFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            ref = bundle.getString("dbRef");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = EventFragmentBinding.inflate(inflater, container, false);
        readBundle(getArguments());
        View view = binding.getRoot();

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.getEvent(ref).observe(getViewLifecycleOwner(), event -> {
            binding.date.setText(viewModel.formatDate(event.getDate()));
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
        return viewModel;
    }
}
