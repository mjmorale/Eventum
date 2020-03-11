package ch.epfl.sdp.ui.event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;

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
            String title = binding.title.getText().toString();
            String description = binding.description.getText().toString();
            String date = binding.date.getText().toString();
            try {
                checkInput(title, description, date);
                viewModel.createEvent(title, description, date);
            } catch (ParseException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Invalid date", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            }

/*            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, EventFragment.newInstance(viewModel.))
                    .commitNow();*/
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void checkInput(String title, String description, String date) throws IllegalArgumentException {
        if (title == null || description == null || date == null) {
            throw new IllegalArgumentException();
        }
        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
