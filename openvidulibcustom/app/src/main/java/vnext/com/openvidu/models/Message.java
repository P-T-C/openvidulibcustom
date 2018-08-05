package vnext.com.openvidu.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TuanDM on 26/07/2018.
 */

public class Message {
    @SerializedName("message")
    private String message;
    @SerializedName("value")
    private String value;

    public Message(String message, String value) {
        this.message = message;
        this.value = value;
    }
}
