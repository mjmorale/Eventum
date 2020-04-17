package ch.epfl.sdp.ui.sharing;

import java.util.Arrays;
import java.util.List;
import ch.epfl.sdp.ObjectUtils;

public class SharingBuilder {
    private List<String> mRef;

    public Sharing build() {
        ObjectUtils.verifyNotNull(mRef);
        return new Sharing(mRef);
    }

    public SharingBuilder setRef(String ... ref){
        for(String r :ref)
            ObjectUtils.verifyNotNull(r);
        mRef = Arrays.asList(ref);
        return this;
    }
}
