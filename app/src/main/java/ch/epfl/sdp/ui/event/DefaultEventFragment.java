package ch.epfl.sdp.ui.event;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.DefaultEventFragmentBinding;
import ch.epfl.sdp.ui.EventViewModelFactory;
import ch.epfl.sdp.ui.FirestoreEventViewModelFactory;

public class DefaultEventFragment extends Fragment implements View.OnClickListener {

    protected DefaultEventViewModel mViewModel;
    private DefaultEventFragmentBinding mBinding;

    private ShareContent mShareContent;
    private ShareDialog mSharedDialog;

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

                prepareShare();

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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.sharingButton:
                mSharedDialog.show(mShareContent, ShareDialog.Mode.AUTOMATIC);
                break;
        }
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
