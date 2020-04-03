package ch.epfl.sdp.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class ParameterizedViewModelFactory implements ViewModelProvider.Factory {

    private List<Object> mParameters = new ArrayList<>();
    private List<Class<?>> mTypes = new ArrayList<>();

    public ParameterizedViewModelFactory(Class<?>... types) {
        for(Class<?> type: types) {
            mTypes.add(verifyNotNull(type));
            mParameters.add(null);
        }
    }

    public void setValue(int id, Object object) {
        if(id < 0 || id >= mParameters.size()) {
            throw new IllegalArgumentException("Value index out of range");
        }
        if(!mTypes.get(id).isAssignableFrom(object.getClass())) {
            throw new IllegalArgumentException("Object is required to be of the type " +
                    mTypes.get(id).getSimpleName() +
                    ", but is of the type " +
                    object.getClass().getSimpleName());
        }
        mParameters.set(id, object);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        verifyNotNull(modelClass);

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
        verifyNotNull(modelClass);

        for(Constructor<?> constructor: modelClass.getConstructors()) {
            if(constructorMatchesParameterTypes(constructor)) {
                return (Constructor<T>) constructor;
            }
        }
        return null;
    }

    private boolean constructorMatchesParameterTypes(Constructor<?> constructor) {
        Class<?> types[] = constructor.getParameterTypes();
        if(types.length != mTypes.size()) {
            return false;
        }
        for(int i = 0; i < types.length; i++) {
            if(!types[i].isAssignableFrom(mTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

}