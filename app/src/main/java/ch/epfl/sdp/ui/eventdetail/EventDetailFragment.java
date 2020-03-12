package ch.epfl.sdp.ui.eventdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.databinding.EventFragmentBinding;
import ch.epfl.sdp.ui.swiper.SwiperFragment;

public class EventDetailFragment extends Fragment {
    private EventFragmentBinding mBinding;
    private Event mEvent;
    private SwiperFragment mSwiperFragment;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }
    public EventDetailFragment(Event event, SwiperFragment swiperFragment){
        super();
        this.mEvent = event;
        this.mSwiperFragment = swiperFragment;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, null, savedInstanceState);
        mBinding = EventFragmentBinding.inflate(inflater, null,false);
        mBinding.date.setText(mEvent.getDate().toString());
        mBinding.description.setText(mEvent.getDescription());
        mBinding.title.setText(mEvent.getTitle());
        mBinding.imageView.setImageResource(mEvent.getImageID());
        return mBinding.getRoot();
    }


}
