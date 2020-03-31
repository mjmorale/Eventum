package ch.epfl.sdp.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ch.epfl.sdp.databinding.EventFragmentBinding;
import ch.epfl.sdp.db.Database;


public class EventFragment extends Fragment {

    private EventViewModel mViewModel;
    private EventFragmentBinding mBinding;
    private String mRef;
    private Database mDb;

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
        configureShare();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }



    public EventViewModel getViewModel() {
        return mViewModel;
    }


    private void configureShare(){
//        ShareDialog mSharedDialog = new ShareDialog(this);
//        BitmapDrawable bitmapDrawable = ((BitmapDrawable) mBinding.imageView.getDrawable());
//        Bitmap bitmap = bitmapDrawable.getBitmap();
//        ShareContent mShareContent = new ShareMediaContent.Builder()
//                .setShareHashtag(
//                        new ShareHashtag.Builder().setHashtag(
//                                mBinding.title.getText().toString() + " :\n\n " +
//                                        mBinding.description.getText().toString()).build())
//                .addMedium(new SharePhoto.Builder().setBitmap(bitmap).build())
//                .build();
        mBinding.sharingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(v.getId()==mBinding.sharingButton.getId() && mSharedDialog.canShow(mShareContent))
//                    mSharedDialog.show(mShareContent, ShareDialog.Mode.AUTOMATIC);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://eventum.com/");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });
    }
}
