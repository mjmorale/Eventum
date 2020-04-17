package ch.epfl.sdp.ui.event.chat;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sdp.databinding.FragmentChatBinding;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.UIConstants;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class ChatFragment extends Fragment {

    private ChatViewModel mViewModel;
    private final ChatViewModel.ChatViewModelFactory mFactory;

    private FragmentChatBinding mBinding;

    public static ChatFragment getInstance(@NonNull String eventRef) {
        verifyNotNull(eventRef);
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, eventRef);

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public ChatFragment() {
        mFactory = new ChatViewModel.ChatViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @VisibleForTesting
    public ChatFragment(@NonNull Database database, @NonNull String eventRef) {
        mFactory = new ChatViewModel.ChatViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setEventRef(eventRef);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if(args != null) {
            mFactory.setEventRef(args.getString(UIConstants.BUNDLE_EVENT_REF));
        }

        mViewModel = new ViewModelProvider(this, mFactory).get(ChatViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

}
