package ch.epfl.sdp.ui;

import org.junit.Test;

import androidx.lifecycle.ViewModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParameterizedViewModelFactoryTest {

    static class MockViewModel extends ViewModel {

        String mString;
        Integer mInteger;

        public MockViewModel() {
            mString = null;
            mInteger = null;
        }

        public MockViewModel(String s, Integer i) {
            mString = s;
            mInteger = i;
        }
    }

    static class MockAbstractViewModel extends ViewModel {

        Object mObject;

        public MockAbstractViewModel(Object o) {
            mObject = o;
        }
    }

    private final static String DUMMY_STRING = "test";
    private final static Integer DUMMY_INTEGER = 4;

    @Test (expected = IllegalArgumentException.class)
    public void ParameterizedViewModelFactory_SetValue_FailsWithIndexSmallerThanZero() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory();
        factory.setValue(-1, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ParameterizedViewModelFactory_SetValue_FailsWithIndexGreaterThanParameterCount() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory(String.class);
        factory.setValue(1, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ParameterizedViewModelFactory_SetValue_FailsWithIndexEqualsToZeroAndNoArgument() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory();
        factory.setValue(0, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ParameterizedViewModelFactory_SetValue_FailsWithIncorrectParameterType() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory(String.class);
        factory.setValue(0, new Object());
    }

    @Test (expected = IllegalArgumentException.class)
    public void ParameterizedViewModelFactory_Create_FailsWithNullArgument() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory(String.class);
        factory.create(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ParameterizedViewModelFactory_Create_FailsIfNoMatchingConstructor1() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory(String.class);
        factory.create(MockViewModel.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ParameterizedViewModelFactory_Create_FailsIfNoMatchingConstructor2() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory(String.class, String.class);
        factory.create(MockViewModel.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ParameterizedViewModelFactory_Create_FailsIfNoMatchingConstructor3() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory(String.class, Object.class);
        factory.create(MockViewModel.class);
    }

    @Test
    public void ParameterizedViewModelFactory_Create_SetsTheCorrectParameters() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory(String.class, Integer.class);
        factory.setValue(0, DUMMY_STRING);
        factory.setValue(1, DUMMY_INTEGER);
        MockViewModel vm = factory.create(MockViewModel.class);

        assertEquals(DUMMY_STRING, vm.mString);
        assertEquals(DUMMY_INTEGER, vm.mInteger);
    }

    @Test
    public void ParameterizedViewModelFactory_Create_WorksWithEmptyConstructor() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory();
        MockViewModel vm = factory.create(MockViewModel.class);

        assertNull(vm.mString);
    }

    @Test
    public void ParameterizedViewModelFactory_Create_WorksWithSubclass() {
        ParameterizedViewModelFactory factory = new ParameterizedViewModelFactory(String.class);
        factory.setValue(0, DUMMY_STRING);
        MockAbstractViewModel vm = factory.create(MockAbstractViewModel.class);

        assertEquals(DUMMY_STRING, vm.mObject);
    }
}
