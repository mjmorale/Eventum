package ch.epfl.sdp.ui.event.sharing;

import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.List;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;


/**
 * Sharing functionality to be able to share an event on all the social platforms
 */
public class Sharing {

    public static final String DOMAIN_URL = "https://eventum.com/";

    private Intent mSendIntent;

    /**
     * Constructor
     *
     * @param arguments list of strings
     */
    Sharing(@NonNull List<String> arguments) {
        verifyNotNull(arguments);

        StringBuilder url = new StringBuilder(DOMAIN_URL);
        for(String s: arguments) {
            url.append(s).append("/");
        }

        mSendIntent = new Intent();
        mSendIntent.setAction(Intent.ACTION_SEND);
        mSendIntent.putExtra(Intent.EXTRA_TEXT, url.toString());
        mSendIntent.setType("text/plain");
    }

    /**
     * Method to get the sharing intent
     *
     * @return the sharing intent
     */
    public Intent getShareIntent() {
        return Intent.createChooser(mSendIntent, "Share via");
    }

}
