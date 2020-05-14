package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.db.DatabaseObjectBuilder;

public class ChatMessageDatabaseBuilder extends DatabaseObjectBuilder<ChatMessage> {

    @Override
    public ChatMessage buildFromMap(@NonNull Map data) {

        String text = (String) data.get("text");
        Timestamp timestamp = (Timestamp)data.get("date");
        String uid = (String) data.get("uid");
        String name = (String) data.get("name");


        ChatMessage message = new ChatMessage(text, uid, name);
        if (timestamp != null)
            message.setDate(timestamp.toDate());

        return message;
    }

    @Override
    public boolean hasLocation() {
        return false;
    }

    @Override
    public Map<String, Object> serializeToMap(@NonNull ChatMessage message) {
        return new HashMap<String, Object>() {{
            put("text", message.getText());
            put("date", FieldValue.serverTimestamp());
            put("uid", message.getUid());
            put("name", message.getName());
        }};
    }

    @Nullable
    @Override
    public LatLng getLocation(@NonNull ChatMessage message) {
        return null;
    }
}
