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

import ch.epfl.sdp.offline.ImageCache;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ImageCacheTest {

    @Mock
    Context mContext;

    @Mock
    Uri mUri;

    @Mock
    ImageView mImageView;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ImageGetter_LoadImageFailsIfNullContext(){
        ImageCache.getInstance().getImage(null, mUri, mImageView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ImageGetter_LoadImageFailsIfNullLoadedObject(){
        ImageCache.getInstance().getImage(mContext, null, mImageView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ImageGetter_LoadImageFailsIfNullImageView(){
        ImageCache.getInstance().getImage(mContext, mUri, null);
    }

    @Test
    public void ImageGetter_GetInstanceReturnsImageGetterInstance(){
        Object ret = ImageCache.getInstance();
        assertTrue(ret.getClass().equals(ImageCache.class));
    }

}
