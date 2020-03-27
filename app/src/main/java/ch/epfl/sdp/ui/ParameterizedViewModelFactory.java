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

        try {
            Constructor<?> constructors[] = modelClass.getConstructors();
            for(Constructor<?> constructor: constructors) {
                Class<?> types[] = constructor.getParameterTypes();
                if(types.length == mParameters.size()) {
                    boolean matches = true;
                    for(int i = 0; i < types.length; i++) {
                        if(!types[i].isAssignableFrom(mParameters.get(i).getClass())) {
                            matches = false;
                        }
                    }
                    if(matches) return (T) constructor.newInstance(mParameters.toArray());
                }
            }
            throw new IllegalArgumentException(modelClass.getSimpleName() + " does not have a constructor that matches the factory arguments");
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot instantiate " + modelClass.getSimpleName() + " class");
        }
    }
}
