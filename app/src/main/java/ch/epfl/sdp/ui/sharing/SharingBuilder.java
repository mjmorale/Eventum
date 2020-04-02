package ch.epfl.sdp.ui.sharing;



import java.util.ArrayList;

import java.util.List;

import ch.epfl.sdp.Event;

import ch.epfl.sdp.ObjectUtils;
import android.content.Context;

public class SharingBuilder {
    private Context mContext;
    private List<String> mRef;

    public Sharing build() {
        ObjectUtils.verifyNotNull(mContext, mRef);

        return new Sharing(mContext, mRef);
    }

    public SharingBuilder setContext(Context context) {
        mContext = context;
        return this;
    }

    public SharingBuilder setRef(String ... ref){
        mRef = new ArrayList<>();
        for(String s: ref)
            mRef.add(s);
        return this;
    }
}
