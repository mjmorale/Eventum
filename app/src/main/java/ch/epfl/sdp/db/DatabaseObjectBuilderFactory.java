package ch.epfl.sdp.db;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseObjectBuilderFactory {

    private static final Map<Class, DatabaseObjectBuilder> mBuilders = new HashMap<>();

    public static <T, B extends DatabaseObjectBuilder<T>> void registerBuilder(@NonNull Class<T> type, @NonNull Class<B> builder)
            throws InstantiationException, IllegalAccessException {
        if(type == null || builder == null) {
            throw new IllegalArgumentException();
        }
        if(mBuilders.containsKey(type)) {
            throw new IllegalArgumentException();
        }
        mBuilders.put(type, builder.newInstance());
    }

    @Nullable
    public static <T, R extends DatabaseObjectBuilder<T>> R getBuilder(@NonNull Class<T> type) {
        if(type == null) {
            throw new IllegalArgumentException();
        }
        return (R) mBuilders.get(type);
    }
}
