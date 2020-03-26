package ch.epfl.sdp.ui;

import org.junit.Test;

public class FirestoreEventViewModelFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void FirestoreEventViewModelFactory_GetInstance_FailsWithNullArgument() {
        FirestoreEventViewModelFactory.getInstance(null);
    }
}
