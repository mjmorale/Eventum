package ch.epfl.sdp.db;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventDatabaseBuilder;

public class DatabaseObjectBuilderRegistry {

    private static Map<Class<?>, DatabaseObjectBuilder<?>> mBuilders = new HashMap<>();

    static {
        mBuilders.put(Event.class, new EventDatabaseBuilder());
    }

    public static <T> void registerBuilder(@NonNull Class<T> type, @NonNull Class<? extends DatabaseObjectBuilder<T>> builder) {
        if(type == null || builder == null) {
            throw new IllegalArgumentException();
        }
        if(!mBuilders.containsKey(type)) {
            try {
                mBuilders.put(type, builder.newInstance());
            }
            catch (IllegalAccessException | InstantiationException e) {
                throw new IllegalArgumentException("Builder type " + builder.getSimpleName() + " does not have an accessible default constructor");
            }
        }
    }

    @NonNull
    public static <T, R extends DatabaseObjectBuilder<T>> R getBuilder(@NonNull Class<T> type) {
        if(type == null) {
            throw new IllegalArgumentException();
        }
        if(!mBuilders.containsKey(type)) {
            throw new IllegalArgumentException("Missing builder for type " + type.getSimpleName());
        }
        return (R) mBuilders.get(type);
    }
}
