package ch.epfl.sdp.ui.eventdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.epfl.sdp.databinding.EventFragmentBinding;

public class EventDetailFragment extends Fragment {
    private EventDetailViewModel mViewModel;
    private EventFragmentBinding mBinding;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, null, savedInstanceState);
        mBinding = EventFragmentBinding.inflate(inflater, null,false);
        mViewModel = new EventDetailViewModel();
        mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            mBinding.date.setText(event.getDate().toString());
            mBinding.description.setText(event.getDescription());
            mBinding.title.setText(event.getTitle());
            mBinding.imageView.setImageResource(event.getImageID());
        });
        return mBinding.getRoot();
    }
}
