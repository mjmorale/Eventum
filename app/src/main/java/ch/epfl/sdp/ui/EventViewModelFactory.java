package ch.epfl.sdp.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.db.Database;

public class EventViewModelFactory extends DatabaseViewModelFactory {

    protected final String mEventRef;

    public EventViewModelFactory(@NonNull Database database, @NonNull String eventRef) {
        super(database);

        if(eventRef == null) {
            throw new IllegalArgumentException();
        }
        mEventRef = eventRef;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (EventViewModel.class.isAssignableFrom(modelClass)) {
            try {
                Constructor<T> constructor = modelClass.getConstructor(Database.class, String.class);
                return constructor.newInstance(mDatabase, mEventRef);
            }
            catch(NoSuchMethodException e) {
                throw new IllegalArgumentException(modelClass.getSimpleName() + " does not have a constructor that only takes a Database and a String");
            }
            catch(IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new IllegalArgumentException("Cannot instantiate an the class " + modelClass.getSimpleName());
            }
        }
        throw new IllegalArgumentException(modelClass.getSimpleName() + " is not a subclass of EventViewModel");
    }
}
