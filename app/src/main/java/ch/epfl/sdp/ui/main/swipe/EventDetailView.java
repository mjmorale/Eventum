package ch.epfl.sdp.ui.main.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapView;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.EventDetailBinding;

/**
 * View that shows information about a clicked event, it is a static view and thus not interactive
 * compared to {@link ch.epfl.sdp.ui.event.EventFragment}.
 */
public class EventDetailView extends RelativeLayout {
    private EventDetailBinding mBinding;

    /**
     * When the view is created the button are removed from visibility since no interaction is
     * provided with this view.
     *
     * @param context current context when creating the view
     * @param attrs not used here since no attributes are set in the layout
     */
    public EventDetailView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.event_detail, this);

        mBinding = EventDetailBinding.bind(this);

        mBinding.eventDetailCalendarButton.setVisibility(GONE);
        mBinding.eventDetailChatButton.setVisibility(GONE);
        mBinding.eventDetailSharingButton.setVisibility(GONE);
    }

    /**
     * Sets the event that should be displayed.
     *
     * @param event to display
     */
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

    /**
     * @return MapView associated to this view
     */
    public MapView getMapView() {
        return mBinding.minimap;
    }
}
