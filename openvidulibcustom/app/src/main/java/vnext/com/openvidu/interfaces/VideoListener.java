package vnext.com.openvidu.interfaces;

public interface VideoListener {
    void onCreateRoomSuccess(String link);
    void onJoinRoomSuccess(String message);
    void onJoinRoomError(String message);
    void onCreateRoomError(String message);
}
