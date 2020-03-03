package ch.epfl.sdp.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sdp.R;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    int RC_SIGN_IN = 123;
    Button button = null;

    private static boolean signedIn = FirebaseAuth.getInstance().getCurrentUser()!=null;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);
        button = (Button) view.findViewById(R.id.button);
        if(signedIn){
            button.setText("Sign out");
        }else{
            button.setText("Sign in");
        }
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!signedIn){
                    createSignInIntent();
                }
                else{
                    signOut();
                }

//                if(!signedIn)
//                    button.setText("Sign In");
//                else
//                    button.setText("Sign Out");
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        System.out.println(mViewModel.toString());
    }


    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers= Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                signedIn=true;
                button.setText("Sign Out");
            } else {

            }
        }
    }

    public void signOut(){
        AuthUI.getInstance()
                .signOut(this.getContext());
        signedIn=false;
        button.setText("Sign In");
    }
}
