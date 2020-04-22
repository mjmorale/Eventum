package ch.epfl.sdp.ui.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentDefaultEventBinding;

public class EventDetailFragment extends Fragment {
    private FragmentDefaultEventBinding mBinding;
    private Event mEvent;
    private Fragment mFragment;

    public EventDetailFragment(Event event, Fragment fragment){
        super();
        this.mEvent = event;
        this.mFragment = fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, null, savedInstanceState);
        mBinding = FragmentDefaultEventBinding.inflate(inflater, null,false);
        mBinding.date.setText(mEvent.getDate().toString());
        mBinding.description.setText(mEvent.getDescription());
        mBinding.title.setText(mEvent.getTitle());
        mBinding.address.setText(mEvent.getAddress());

        String URL = mEvent.getImageId();
        if (URL == null) URL = getResources().getString(R.string.defaultImageURL);
        Glide.with(getContext())
                .load(URL)
                .into(mBinding.imageView);

        mBinding.backButton.setClickable(true);
        Fragment thisFragment = this;
        mBinding.backButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(thisFragment.getId(), mFragment).commit());
        return mBinding.getRoot();
    }
}
