package ch.epfl.sdp.mocks;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

public class MockFragmentFactory<F extends Fragment> extends FragmentFactory {

    Class<F> mFragmentType;
    Constructor<F> mConstructor;
    List<Object> mArguments;

    public MockFragmentFactory(@NonNull Class<F> fragmentType, Object... arguments) {
        if(fragmentType == null) {
            throw new IllegalArgumentException();
        }
        mArguments = Arrays.asList(arguments);
        mFragmentType = fragmentType;

        Constructor<F> fragmentConstructor = getMatchingConstructor(getTypes(mArguments));
        if(fragmentConstructor != null) {
            mConstructor = fragmentConstructor;
        }
        else {
            throw new IllegalArgumentException("Fragment type " +
                    fragmentType.getSimpleName() +
                    " does not have a constructor that matches the provided arguments");
        }
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
            return mConstructor.newInstance(mArguments.toArray());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot instantiate " + mFragmentType.getSimpleName() + " class");
        }
    }

    private Class<?>[] getTypes(List<Object> objects) {
        Class<?>[] types = new Class<?>[objects.size()];
        for(int i = 0; i < types.length; i++) {
            types[i] = objects.get(i).getClass();
        }
        return types;
    }

    @SuppressWarnings("unchecked")
    private Constructor<F> getMatchingConstructor(Class<?>[] types) {
        for(Constructor<?> constructor: mFragmentType.getConstructors()) {
            Constructor<F> fragmentConstructor = (Constructor<F>) constructor;
            if(constructorMatchesParameterTypes(types, fragmentConstructor)) {
                return fragmentConstructor;
            }
        }
        return null;
    }

    private boolean constructorMatchesParameterTypes(Class<?>[] types, Constructor<F> constructor) {
        Class<?>[] constructorTypes = constructor.getParameterTypes();
        if(constructorTypes.length != types.length) {
            return false;
        }
        for(int i = 0; i < constructorTypes.length; i++) {
            if(!constructorTypes[i].isAssignableFrom(types[i])) {
                return false;
            }
        }
        return true;
    }
}