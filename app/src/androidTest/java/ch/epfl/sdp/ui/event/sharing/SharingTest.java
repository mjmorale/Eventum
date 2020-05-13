package ch.epfl.sdp.ui.event.sharing;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ch.epfl.sdp.auth.Authenticator;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SharingTest {

    @Mock
    private Authenticator mAuthenticatorMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Sharing_Test() {

    }

    @Test
    public void sharing(){
        Sharing sharing = new Sharing(Arrays.asList("anyRef"));
        assertTrue(sharing.getShareIntent()!=null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void sharing_null_argument(){
        Sharing sharing = new Sharing(null);
        sharing.getShareIntent();
    }

    @Test
    public void sharingBuilder(){
        SharingBuilder sharingBuilder = new SharingBuilder();
        sharingBuilder.setRef("anyRef");
        assertTrue(sharingBuilder.build()!=null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void sharingBuilder_null_argument(){
        SharingBuilder sharingBuilder = new SharingBuilder();
        sharingBuilder.build();
    }
}
