package ch.epfl.sdp.ui.sharing;

import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.List;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;


public class Sharing {

    public static final String DOMAIN_URL = "https://eventum.com/";

    private Intent mSendIntent;

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

    public Intent getShareIntent() {
        return Intent.createChooser(mSendIntent, "Share via");
    }

}
