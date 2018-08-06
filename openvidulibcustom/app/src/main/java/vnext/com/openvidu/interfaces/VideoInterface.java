package vnext.com.openvidu.interfaces;

import android.content.Context;

public interface VideoInterface {

 void createRoom(String sessionId,String role,String data);
 void joinRoom(String link);
  void muteAudio();
  void hideVideo();

}
