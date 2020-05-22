package ch.epfl.sdp.platforms.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.offline.ConnectivityService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AndroidConnectivityServiceTest {

    private ConnectivityService mConnectivityService = new AndroidConnectivityService();

    @Mock
    private Context mContext;

    @Mock
    private ConnectivityManager mConnectivityManager;

    @Mock
    private NetworkInfo mNetworkInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void ConnectivityService_ShowNetworkAvailable() {
        setupConnectivity();
        when(mNetworkInfo.isConnected()).thenReturn(true);

        assertTrue(mConnectivityService.isNetworkAvailable(mContext));
    }

    @Test
    public void ConnectivityService_ShowNetworkUnavailable() {
        setupConnectivity();
        when(mNetworkInfo.isConnected()).thenReturn(false);

        assertFalse(mConnectivityService.isNetworkAvailable(mContext));
    }

    private void setupConnectivity() {
        when(mContext.getSystemService(anyString())).thenReturn(mConnectivityManager);
        when(mConnectivityManager.getActiveNetworkInfo()).thenReturn(mNetworkInfo);
    }
}
