package ch.epfl.sdp.platforms.firebase.storage;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageGetterTest {

    @Mock
    Context mContext;

    @Mock
    Uri mUri;

    @Mock
    ImageView mImageView;

    String STRING = "1234";


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ImageGetter_LoadImageFailsIfNullContext(){
        ImageGetter.getInstance().getImage(null, mUri, mImageView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ImageGetter_LoadImageFailsIfNullLoadedObject(){
        ImageGetter.getInstance().getImage(mContext, null, mImageView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ImageGetter_LoadImageFailsIfNullImageView(){
        ImageGetter.getInstance().getImage(mContext, mUri, null);
    }

    @Test
    public void ImageGetter_GetInstanceReturnsImageGetterInstance(){
        Object ret = ImageGetter.getInstance();
        assertTrue(ret.getClass().equals(ImageGetter.class));
    }

}
