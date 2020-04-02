package ch.epfl.sdp.ui.sharing;


import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;


public class Sharing {

    private Intent mShareIntent;
    private Context mContext;

    public Sharing(Context context, List<String> ref){

        mContext = context;

        String url = new String("https://eventum.com/");
        for(String r: ref)
            url+= r+"/";

        Intent mSendIntent = new Intent();
        mSendIntent.setAction(Intent.ACTION_SEND);
        mSendIntent.putExtra(Intent.EXTRA_TEXT, url);
        mSendIntent.setType("text/plain");
        mShareIntent = Intent.createChooser(mSendIntent, "Share via");

    }
    public void share(){
        startActivity(mContext, mShareIntent, null);
    }

}
