package ch.epfl.sdp.ui;

import androidx.annotation.NonNull;

public class Event<T> {

    public interface EventHandler<T> {
        boolean handle(@NonNull T value);
    }

    private T mValue;
    private boolean mHandled;

    public Event(T value) {
        mValue = value;
        mHandled = false;
    }

    public void handle(EventHandler<T> handler) {
        if(!mHandled) {
            mHandled = handler.handle(mValue);
        }
    }
}
