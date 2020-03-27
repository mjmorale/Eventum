package ch.epfl.sdp.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ParameterizedViewModelFactory implements ViewModelProvider.Factory {

    private List<Object> mParameters;

    public ParameterizedViewModelFactory(Object... parameters) {
        mParameters = Arrays.asList(parameters);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == null) {
            throw new IllegalArgumentException("ViewModel class cannot be null");
        }

        Constructor<T> constructor = getMatchingConstructor(modelClass);
        if(constructor != null) {
            try {
                return constructor.newInstance(mParameters.toArray());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new IllegalArgumentException("Cannot instantiate " + modelClass.getSimpleName() + " class");
            }
        }
        throw new IllegalArgumentException(modelClass.getSimpleName() + " does not have a constructor that matches the factory arguments");
    }

    @SuppressWarnings("unchecked")
    private <T extends ViewModel> Constructor<T> getMatchingConstructor(@NonNull Class<T> modelClass) {
        if(modelClass == null) {
            throw new IllegalArgumentException("ViewModel class cannot be null");
        }

        for(Constructor<?> constructor: modelClass.getConstructors()) {
            if(constructorMatchesParameterTypes(constructor)) {
                return (Constructor<T>) constructor;
            }
        }
        return null;
    }

    private boolean constructorMatchesParameterTypes(Constructor<?> constructor) {
        Class<?> types[] = constructor.getParameterTypes();
        if(types.length == mParameters.size()) {
            for(int i = 0; i < types.length; i++) {
                if(!types[i].isAssignableFrom(mParameters.get(i).getClass())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
