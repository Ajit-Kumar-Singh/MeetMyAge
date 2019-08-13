package data.model.message;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Type;

public class MessageParcel implements Serializable {
    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("messageType")
    @Expose
    private String messageType;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public static JsonDeserializer<MessageParcel> getJsonDeserializer() {
        return DeserializeProvider.INSTANCE;
    }

    private static final class DeserializeProvider {
        private static final MessageParcelAdapter INSTANCE = new MessageParcelAdapter();
    }

    public static class MessageParcelAdapter implements  JsonDeserializer<MessageParcel>{
        public MessageParcel deserialize(JsonElement jsonElement, Type type,
                                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonPrimitive messageTypeJson = (JsonPrimitive) jsonObject.get("messageType");
            MessageParcel messageParcel = new MessageParcel();
            messageParcel.setMessageType(messageTypeJson.getAsString());
            Message.Type messageType = Message.Type.valueOf(messageTypeJson.getAsString());
            if (Message.Type.GroupMessage.equals(messageType)) {
                GroupMessage groupMessage = jsonDeserializationContext.deserialize(jsonObject.get("message"), GroupMessage.class);
                messageParcel.setMessage(groupMessage);
            }
            return messageParcel;
        }
    }
}
