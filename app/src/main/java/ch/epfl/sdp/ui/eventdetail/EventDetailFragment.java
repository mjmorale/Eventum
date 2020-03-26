package ch.epfl.sdp.ui.eventdetail;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.databinding.EventFragmentBinding;

import ch.epfl.sdp.ui.swipe.SwipeFragment;
public class EventDetailFragment extends Fragment implements View.OnClickListener {
    private EventFragmentBinding mBinding;
    private Event mEvent;
    private SwipeFragment mSwipeFragment;

    private ShareContent mShareContent;
    private ShareDialog mSharedDialog;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public EventDetailFragment(Event event, SwipeFragment swipeFragment){
        super();
        this.mEvent = event;
        this.mSwipeFragment = swipeFragment;

    }
    @Override
    public void onClick(View view){
        if(view.getId()==mBinding.sharingButton.getId())
            if(mSharedDialog.canShow(mShareContent)) {
                mSharedDialog.show(mShareContent, ShareDialog.Mode.AUTOMATIC);
            }
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
        mBinding.backButton.setClickable(true);
        Fragment thisFragment = this;
        mBinding.backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                getActivity().getSupportFragmentManager().beginTransaction().replace(thisFragment.getId(), mSwipeFragment).commit();
            }
        });
        prepareShare();
        return mBinding.getRoot();
    }

    private void prepareShare(){
        mSharedDialog = new ShareDialog(this);
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) mBinding.imageView.getDrawable());
        Bitmap bitmap = bitmapDrawable.getBitmap();
        mShareContent = new ShareMediaContent.Builder()
                .setShareHashtag(
                        new ShareHashtag.Builder().setHashtag(
                                mBinding.title.getText().toString() + " :\n\n " +
                                        mBinding.description.getText().toString()).build())
                .addMedium(new SharePhoto.Builder().setBitmap(bitmap).build())
                .build();
        mBinding.sharingButton.setOnClickListener(this);
    }

}
