package ch.epfl.sdp.ui.sharing;


import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ch.epfl.sdp.ObjectUtils;

import static androidx.core.content.ContextCompat.startActivity;


public class Sharing {


    private Intent mSendIntent;

    public Sharing(List<String> ref){
        ObjectUtils.verifyNotNull(ref);

        String url = "https://eventum.com/";
        for(String r: ref)
            url+= r+"/";

        mSendIntent = new Intent();
        mSendIntent.setAction(Intent.ACTION_SEND);
        mSendIntent.putExtra(Intent.EXTRA_TEXT, url);
        mSendIntent.setType("text/plain");

    }
    public Intent getShareIntent(){
        return  Intent.createChooser(mSendIntent, "Share via");
    }

}
