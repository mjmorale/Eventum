package ch.epfl.sdp.ui.main.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapView;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentDefaultEventBinding;

public class EventDetailView extends RelativeLayout {
    private FragmentDefaultEventBinding mBinding;

    public EventDetailView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.fragment_default_event, this);

        mBinding = FragmentDefaultEventBinding.bind(this);

        mBinding.backButton.setVisibility(GONE);
        mBinding.calendarButton.setVisibility(GONE);
        mBinding.chatButton.setVisibility(GONE);
        mBinding.sharingButton.setVisibility(GONE);
    }

    public void setEvent(Event event) {
        mBinding.defaultEventLayout.scrollTo(0, 0);
        
        mBinding.title.setText(event.getTitle());
        mBinding.description.setText(event.getDescription());
        mBinding.date.setText(event.getDateStr());
        mBinding.address.setText(event.getAddress());

        Glide.with(getContext())
                .load(event.getImageId())
                .into(mBinding.imageView);
    }

    public MapView getMapView() {
        return mBinding.minimap;
    }
}
