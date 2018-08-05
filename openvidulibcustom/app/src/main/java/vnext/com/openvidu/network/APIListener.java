package vnext.com.openvidu.network;



/**
 * Created by TuanDM on 26/07/2018.
 */

public interface APIListener {
    public void onSessionSuccess(String message);
    public void onSessionError(String message);

    public void onTokenSuccess(String message);
    public void onTokenError(String message);

    public void onRequestError(String message);
}
