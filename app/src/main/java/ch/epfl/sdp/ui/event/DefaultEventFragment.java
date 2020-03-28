package ch.epfl.sdp.ui.event;

import androidx.annotation.VisibleForTesting;
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
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentDefaultEventBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;
import ch.epfl.sdp.ui.UIConstants;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class DefaultEventFragment extends Fragment implements View.OnClickListener {

    static class DefaultEventViewModelFactory extends ParameterizedViewModelFactory {

        public DefaultEventViewModelFactory() {
            super(Database.class, String.class);
        }

        public void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }

        public void setEventRef(@NonNull String eventRef) {
            setValue(1, verifyNotNull(eventRef));
        }
    }

    private DefaultEventViewModel mViewModel;
    private FragmentDefaultEventBinding mBinding;
    private final DefaultEventViewModelFactory mFactory;

    private ShareContent mShareContent;
    private ShareDialog mSharedDialog;

    public static DefaultEventFragment getInstance(@NonNull String eventRef) {
        verifyNotNull(eventRef);
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, eventRef);

        DefaultEventFragment fragment = new DefaultEventFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public DefaultEventFragment() {
        mFactory = new DefaultEventViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @VisibleForTesting
    public DefaultEventFragment(@NonNull Database database, @NonNull String eventRef) {
        mFactory = new DefaultEventViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setEventRef(eventRef);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDefaultEventBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if(args != null) {
            mFactory.setEventRef(args.getString(UIConstants.BUNDLE_EVENT_REF));
        }

        mViewModel = new ViewModelProvider(this, mFactory).get(DefaultEventViewModel.class);

        mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            mBinding.date.setText(event.getDate().toString());
            mBinding.description.setText(event.getDescription());
            mBinding.title.setText(event.getTitle());
        });

        prepareShare();
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
