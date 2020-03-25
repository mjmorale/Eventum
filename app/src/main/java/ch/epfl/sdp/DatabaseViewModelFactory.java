package ch.epfl.sdp;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.epfl.sdp.db.Database;

public class DatabaseViewModelFactory implements ViewModelProvider.Factory {

    private final Database mDatabase;

    public DatabaseViewModelFactory(@NonNull Database database) {
        if(database == null) {
            throw new IllegalArgumentException();
        }
        mDatabase = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (DatabaseViewModel.class.isAssignableFrom(modelClass)) {
            try {
                Constructor<T> constructor = modelClass.getConstructor(Database.class);
                return constructor.newInstance(mDatabase);
            }
            catch(NoSuchMethodException e) {
                throw new IllegalArgumentException(modelClass.getSimpleName() + " does not have a constructor that only takes a Database");
            }
            catch(IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new IllegalArgumentException("Cannot instantiate an the class " + modelClass.getSimpleName());
            }
        }
        throw new IllegalArgumentException(modelClass.getSimpleName() + " is not a subclass of DatabaseViewModel");
    }
}
