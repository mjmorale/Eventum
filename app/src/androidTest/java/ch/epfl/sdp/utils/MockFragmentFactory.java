package ch.epfl.sdp.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

public class MockFragmentFactory<F extends Fragment> extends FragmentFactory {

    Class<F> mFragmentType;
    Constructor<F> mConstructor;
    ParameterizedViewModelFactory mFactory;

    public MockFragmentFactory(@NonNull Class<F> fragmentType, @NonNull ParameterizedViewModelFactory factory) {
        if(fragmentType == null || factory == null) {
            throw new IllegalArgumentException();
        }
        mFragmentType = fragmentType;
        try {
            mConstructor = mFragmentType.getConstructor(ParameterizedViewModelFactory.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(fragmentType.getSimpleName() +
                    " does not have a constructor that takes only a " +
                    ParameterizedViewModelFactory.class.getSimpleName());
        }
        mFactory = factory;
    }

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        if(!mFragmentType.getName().equals(className)) {
            throw new IllegalArgumentException("Cannot construct a fragment of type " +
                    className +
                    " with factory of type " +
                    mFragmentType.getName());
        }
        try {
            return mConstructor.newInstance(mFactory);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot instantiate " + mFragmentType.getSimpleName() + " class");
        }
    }
}