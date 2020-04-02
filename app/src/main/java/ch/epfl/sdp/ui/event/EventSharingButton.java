package ch.epfl.sdp.ui.event;


import android.content.Context;
import android.content.Intent;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ch.epfl.sdp.Event;

import static androidx.core.content.ContextCompat.startActivity;

public class EventSharingButton {

    public  EventSharingButton(Context context, FloatingActionButton sharingButton, Event event){
        Intent mSendIntent = new Intent();
        mSendIntent.setAction(Intent.ACTION_SEND);
        mSendIntent.putExtra(Intent.EXTRA_TEXT, "https://eventum.com/"+event.getImageID());
        mSendIntent.setType("text/plain");

        Intent mShareIntent = Intent.createChooser(mSendIntent, null);
        sharingButton.setOnClickListener(v -> {startActivity(context,mShareIntent, null);});
    }

}
