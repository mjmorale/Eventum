package ch.epfl.sdp.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Factory to create a view model with custom parameters
 */
public class ParameterizedViewModelFactory implements ViewModelProvider.Factory {

    private List<Object> mParameters = new ArrayList<>();
    private List<Class<?>> mTypes = new ArrayList<>();

    /**
     * Constructor of the ParameterizedViewModel factory
     *
     * @param types of the parameters added to the view model
     */
    public ParameterizedViewModelFactory(Class<?>... types) {
        for(Class<?> type: types) {
            addType(type);
        }
    }

    /**
     * Method to set the custom parameters to the view model
     *
     * @param id of the parameter (from 0 to the number of parameter - 1)
     * @param object the parameter to be added to the view model
     */
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

    /**
     * @param id The id of the parameter to fetch
     * @return The current value of the parameter with the specified id
     */
    public Object getValue(int id) {
        if(id < 0 || id >= mParameters.size()) {
            throw new IllegalArgumentException("Value index out of range");
        }
        return mParameters.get(id);
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

    protected void addType(@NonNull Class<?> type) {
        mTypes.add(verifyNotNull(type));
        mParameters.add(null);
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
