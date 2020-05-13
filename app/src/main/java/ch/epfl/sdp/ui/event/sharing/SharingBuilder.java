package ch.epfl.sdp.ui.event.sharing;


import java.util.Arrays;
import java.util.List;
import ch.epfl.sdp.ObjectUtils;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Builder for the sharing feature
 */
public class SharingBuilder {

    private List<String> mRef;

    /**
     * Build the sharing
     *
     * @return the sharing {@link ch.epfl.sdp.ui.event.sharing.Sharing}
     */
    public Sharing build() {
        verifyNotNull(mRef);
        return new Sharing(mRef);
    }

    /**
     * Set the reference to the sharing builder
     *
     * @param ref the reference
     * @return the builder
     */
    public SharingBuilder setRef(String ... ref){

        for(String r :ref)
            ObjectUtils.verifyNotNull(r);
        mRef = Arrays.asList(ref);

        return this;
    }
}
