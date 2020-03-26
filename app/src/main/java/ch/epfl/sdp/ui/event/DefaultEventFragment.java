package ch.epfl.sdp.ui.event;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sdp.databinding.DefaultEventFragmentBinding;
import ch.epfl.sdp.ui.EventViewModelFactory;
import ch.epfl.sdp.ui.FirestoreEventViewModelFactory;

public class DefaultEventFragment extends Fragment {

    protected DefaultEventViewModel mViewModel;
    private DefaultEventFragmentBinding mBinding;

    public static DefaultEventFragment newInstance(String eventRef) {
        Bundle bundle = new Bundle();
        bundle.putString(EventActivity.EVENT_REF_EXTRA, eventRef);

        DefaultEventFragment fragment = new DefaultEventFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DefaultEventFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if(args != null) {
            String eventRef = args.getString(EventActivity.EVENT_REF_EXTRA);
            if(eventRef != null) {
                EventViewModelFactory factory = FirestoreEventViewModelFactory.getInstance(eventRef);
                mViewModel = new ViewModelProvider(this, factory).get(DefaultEventViewModel.class);

                mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
                    mBinding.date.setText(event.getDate().toString());
                    mBinding.description.setText(event.getDescription());
                    mBinding.title.setText(event.getTitle());
                });

                mBinding.defaultEventErrorLayout.setVisibility(View.INVISIBLE);
                return;
            }
        }
        mBinding.defaultEventLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
