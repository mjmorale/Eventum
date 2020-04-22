package ch.epfl.sdp.ui.event.chat;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sdp.databinding.FragmentChatBinding;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.UIConstants;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;
import static java.util.Objects.requireNonNull;

public class ChatFragment extends Fragment {

    private ChatViewModel mViewModel;
    private final ChatViewModel.ChatViewModelFactory mFactory;
    private FragmentChatBinding mBinding;
    private MessageListAdapter mAdapter;
    private String  mEventRef;

    public static ChatFragment getInstance(@NonNull String eventRef) {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF,  verifyNotNull(eventRef));

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public ChatFragment() {
        mFactory = new ChatViewModel.ChatViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
        mFactory.setAuthenticator(new FirebaseAuthenticator(FirebaseAuth.getInstance()));

    }

    @VisibleForTesting
    public ChatFragment(@NonNull Database database, @NonNull String eventRef, @NonNull  FirebaseAuthenticator firebaseAuthenticator) {
        verifyNotNull(database, eventRef);
        mFactory = new ChatViewModel.ChatViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setEventRef(eventRef);
        mFactory.setAuthenticator(firebaseAuthenticator);
        mEventRef = eventRef;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);

        mBinding.buttonChatboxSend.setOnClickListener(v->{
            trySendMessage(new ChatViewModel.OnMessageAddedCallback() {
                @Override
                public void onSuccess(String messageRef) {

                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getContext(), "Couldn't send message", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Bundle args = getArguments();
        if(args != null) {
            mFactory.setEventRef(requireNonNull(args.getString(UIConstants.BUNDLE_EVENT_REF)));
        }

        mViewModel = new ViewModelProvider(this, mFactory).get(ChatViewModel.class);

        mAdapter = new MessageListAdapter(mViewModel.getUserRef());
        mBinding.reyclerviewMessageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.reyclerviewMessageList.setAdapter(mAdapter);

        mViewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            mAdapter.setChatList(messages);
            mAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void trySendMessage(@NonNull ChatViewModel.OnMessageAddedCallback callback) {

        String message = mBinding.edittextChatbox.getText().toString();
        if(!message.isEmpty()){
            mBinding.edittextChatbox.getText().clear();

            //to database
            mViewModel.addMessage(message, callback);
        }

    }

}
