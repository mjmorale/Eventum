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
    private Intent mSendIntent, mShareIntent;


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
        mSendIntent = new Intent();
        mSendIntent.setAction(Intent.ACTION_SEND);
        mSendIntent.putExtra(Intent.EXTRA_TEXT, "https://eventum.com/");
        mSendIntent.setType("text/plain");
        mShareIntent = Intent.createChooser(mSendIntent, null);
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

        mBinding.sharingButton.setOnClickListener(v -> {
            startActivity(mShareIntent);
        });
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

}
