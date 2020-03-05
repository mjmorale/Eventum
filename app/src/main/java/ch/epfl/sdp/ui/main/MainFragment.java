package ch.epfl.sdp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.swipe.SwipeFragment;

public class MainFragment extends Fragment implements View.OnClickListener {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        Button swipeButton = (Button) view.findViewById(R.id.swipeButton);
        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        swipeButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.swipeButton:
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new SwipeFragment());
                transaction.commit();
                break;

            case R.id.loginButton:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new AuthFragment())
                        .commit();
                break;
        }
    }
}
