package ch.epfl.sdp.ui.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.databinding.DefaultEventFragmentBinding;

public class EventDetailFragment extends Fragment {
    private DefaultEventFragmentBinding mBinding;
    private Event mEvent;
    private SwipeFragment mSwipeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public EventDetailFragment(Event event, SwipeFragment swipeFragment){
        super();
        this.mEvent = event;
        this.mSwipeFragment = swipeFragment;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, null, savedInstanceState);
        mBinding = DefaultEventFragmentBinding.inflate(inflater, null,false);
        mBinding.defaultEventErrorLayout.setVisibility(View.INVISIBLE);
        mBinding.date.setText(mEvent.getDate().toString());
        mBinding.description.setText(mEvent.getDescription());
        mBinding.title.setText(mEvent.getTitle());
        mBinding.imageView.setImageResource(mEvent.getImageID());
        mBinding.backButton.setClickable(true);
        Fragment thisFragment = this;
        mBinding.backButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(thisFragment.getId(), mSwipeFragment).commit());
        return mBinding.getRoot();
    }

}
