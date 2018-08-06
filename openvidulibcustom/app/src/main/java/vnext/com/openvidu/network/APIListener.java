package vnext.com.openvidu.network;



/**
 * Created by TuanDM on 26/07/2018.
 */

public interface APIListener {
  void onSessionSuccess(String message);
  void onSessionError(String message);
   void onTokenSuccess(String message);
  void onTokenError(String message);
 void onRequestError(String message);
}
