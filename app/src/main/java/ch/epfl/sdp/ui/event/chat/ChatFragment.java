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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.databinding.FragmentChatBinding;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.UIConstants;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class ChatFragment extends Fragment {

    private ChatViewModel mViewModel;
    private final ChatViewModel.ChatViewModelFactory mFactory;

    private FragmentChatBinding mBinding;

    private MessageListAdapter mAdapter;

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

        mBinding.buttonChatboxSend.setOnClickListener(v->{
            trySendMessage(new ChatViewModel.OnMessageAddedCallback() {
                @Override
                public void onSuccess(String messageRef) {

                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_SHORT).show();
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
            mFactory.setEventRef(args.getString(UIConstants.BUNDLE_EVENT_REF));
        }

        mViewModel = new ViewModelProvider(this, mFactory).get(ChatViewModel.class);

        mAdapter = new MessageListAdapter(new ArrayList<>(), "");
        mBinding.reyclerviewMessageList.setAdapter(mAdapter);
        mBinding.reyclerviewMessageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setUid(mViewModel.getUserRef());


        if(mViewModel.getMessages().hasObservers()) {
            mViewModel.getMessages().removeObservers(getViewLifecycleOwner());
        }

        mViewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
//            String toast = "no message";
//            if (!messages.isEmpty()) toast = messages.get(messages.size() - 1).getText();
//            Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
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
