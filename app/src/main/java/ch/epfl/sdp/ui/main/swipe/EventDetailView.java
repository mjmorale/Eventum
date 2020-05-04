package ch.epfl.sdp.ui.main.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.CardviewEventDetailBinding;

public class EventDetailView extends LinearLayout {
    private CardviewEventDetailBinding mBinding;

    public EventDetailView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.cardview_event_detail, this);

        mBinding = CardviewEventDetailBinding.bind(this);
    }

    public void setEvent(Event event) {
        mBinding.title.setText(event.getTitle());
        mBinding.description.setText(event.getDescription());
        mBinding.date.setText(event.getDateStr());
        mBinding.address.setText(event.getAddress());

        Glide.with(getContext())
                .load(event.getImageId())
                .into(mBinding.imageView);

        //mMapView.onCreate(savedInstanceState);
/*        mMapView.getMapAsync(googleMap -> {

            mViewModel.addMapManager(new GoogleMapManager(googleMap));
            mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
                mViewModel.setEventOnMap(event.getLocation(), event.getTitle(), mZoomLevel);
            });

        });*/
    }
}
