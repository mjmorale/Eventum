package ch.epfl.sdp.ui.event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sdp.databinding.CreateEventFragmentBinding;


public class CreateEventFragment extends Fragment {
    private EventViewModel viewModel;
    private CreateEventFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CreateEventFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.createButton.setOnClickListener(v -> {
            viewModel.createEvent(binding.title.toString(),
                                   binding.description.toString(),
                                   binding.date.toString());
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
