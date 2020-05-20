package ch.epfl.sdp.offline;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ImageCacheTest {

    @Mock
    private File mCacheDir;

    @Mock
    private Bitmap mBitmap;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ImageCache_GetImage_FailsWithNullFirstArgument() {
        ImageCache cache = ImageCache.getInstance();
        cache.getImage(null, "ref");
    }

    @Test (expected = IllegalArgumentException.class)
    public void ImageCache_GetImage_FailsWithNullSecondArgument() {
        ImageCache cache = ImageCache.getInstance();
        cache.getImage(mCacheDir, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ImageCache_SaveImage_FailsWithNullFirstArgument() {
        ImageCache cache = ImageCache.getInstance();
        cache.saveImage(null, "ref", mBitmap);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ImageCache_SaveImage_FailsWithNullSecondArgument() {
        ImageCache cache = ImageCache.getInstance();
        cache.saveImage(mCacheDir, null, mBitmap);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ImageCache_SaveImage_FailsWithNullThirdArgument() {
        ImageCache cache = ImageCache.getInstance();
        cache.saveImage(mCacheDir, "ref", null);
    }

}
