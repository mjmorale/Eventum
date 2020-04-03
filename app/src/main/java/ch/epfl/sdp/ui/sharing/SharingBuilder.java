package ch.epfl.sdp.ui.sharing;



import java.util.ArrayList;
import java.util.List;
import ch.epfl.sdp.ObjectUtils;

public class SharingBuilder {
    private List<String> mRef;

    public Sharing build() {
        ObjectUtils.verifyNotNull(mRef);
        return new Sharing(mRef);
    }

    public SharingBuilder setRef(String ... ref){
        ObjectUtils.verifyNotNull(ref);
        mRef = new ArrayList<>();
        for(String s: ref)
            mRef.add(s);
        return this;
    }
}
