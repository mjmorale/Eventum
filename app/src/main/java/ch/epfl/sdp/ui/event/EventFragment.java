package ch.epfl.sdp.ui.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.FacebookSdk;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.EventFragmentBinding;
import ch.epfl.sdp.db.Database;

import com.facebook.share.internal.MessageDialogFeature;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;

import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.List;


public class EventFragment extends Fragment  implements View.OnClickListener {

    private EventViewModel mViewModel;
    private EventFragmentBinding mBinding;
    private String mRef;
    private Database mDb;

    private ShareContent mShareContent;
    private ShareDialog mSharedDialog;


    public static EventFragment newInstance(String ref, Database db) {
        Bundle bundle = new Bundle();
        bundle.putString("dbRef", ref);

        EventFragment fragment = new EventFragment(db);
        fragment.setArguments(bundle);

        return fragment;
    }

    public EventFragment(Database db) {
        mDb = db;
    }


    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            mRef = bundle.getString("dbRef");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        mViewModel.setDb(mDb);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = EventFragmentBinding.inflate(inflater, container, false);
        readBundle(getArguments());
        View view = mBinding.getRoot();

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getEvent(mRef).observe(getViewLifecycleOwner(), event -> {
            mBinding.date.setText(mViewModel.formatDate(event.getDate()));
            mBinding.description.setText(event.getDescription());
            mBinding.title.setText(event.getTitle());
        });
        prepareShare();
        return view;
    }

    @Override
    public void onClick(View view){
        if(mSharedDialog.canShow(mShareContent)) {
            mSharedDialog.show(mShareContent, ShareDialog.Mode.AUTOMATIC);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }



    public EventViewModel getViewModel() {
        return mViewModel;
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
