package ch.epfl.sdp.ui.main.swipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.MapView;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.EventSwipeInfoBinding;
import ch.epfl.sdp.storage.Storage;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View that shows information about a clicked event, it is a static view and thus not interactive
 * compared to {@link ch.epfl.sdp.ui.event.EventFragment}.
 */
public class EventDetailView extends RelativeLayout {

    private final static String TAG = "EventDetailView";

    private EventSwipeInfoBinding mBinding;

    /**
     * When the view is created the button are removed from visibility since no interaction is
     * provided with this view.
     *
     * @param context current context when creating the view
     * @param attrs not used here since no attributes are set in the layout
     */
    public EventDetailView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.event_swipe_info, this);

        mBinding = EventSwipeInfoBinding.bind(this);
    }

    /**
     * Sets the event that should be displayed.
     *
     * @param event to display
     */
    public void setEvent(@NonNull Event event, @NonNull Storage storage) {
        verifyNotNull(event, storage);

        mBinding.defaultEventLayout.scrollTo(0, 0);
        
        mBinding.title.setText(event.getTitle());
        mBinding.description.setText(event.getDescription());
        mBinding.date.setText(event.getDateStr());
        mBinding.address.setText(event.getAddress());
        storage.downloadImage(getContext().getCacheDir(), "events", event.getImageId(), 1, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                mBinding.imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Cannot download image from database", e);
            }
        });
    }

    /**
     * @return MapView associated to this view
     */
    public MapView getMapView() {
        return mBinding.minimap;
    }
}
