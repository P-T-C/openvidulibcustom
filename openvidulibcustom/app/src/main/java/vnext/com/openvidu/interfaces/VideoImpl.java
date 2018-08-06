package vnext.com.openvidu.interfaces;

import android.content.Context;
import android.provider.MediaStore;

import org.json.JSONException;
import org.json.JSONObject;
import vnext.com.openvidu.R;
import vnext.com.openvidu.configs.JSONConstants;
import vnext.com.openvidu.models.TokenModel;
import vnext.com.openvidu.network.APIListener;
import vnext.com.openvidu.network.MainApi;
public class VideoImpl implements VideoInterface, APIListener {

    MainApi mainApi;
    VideoListener videoListener;
    String role, data;
    Context context;

    public VideoImpl(Context context,VideoListener videoListener){
        mainApi = new MainApi(context, this);
        this.context = context;
        this.videoListener = videoListener;
    }
    @Override
    public void createRoom(String sessionId,String role, String data) {
        this.role = role;
        this.data = data;
        mainApi.getSession();
    }

    @Override
    public void joinRoom(String link) {

    }

    @Override
    public void muteAudio() {

    }
    @Override
    public void hideVideo() {

    }
    @Override
    public void onSessionSuccess(String message) {
        try {
            JSONObject jsonData = new JSONObject(message);
            String session = jsonData.getString(JSONConstants.ID);
            TokenModel tokenModel = new TokenModel(session, role, data);
            mainApi.getTokens(tokenModel);
        } catch (JSONException e) {
            e.printStackTrace();
            videoListener.onCreateRoomError(context.getResources().getString(R.string.server_error));
        } catch (Exception e) {
            e.printStackTrace();
            videoListener.onCreateRoomError(context.getResources().getString(R.string.server_error));
        }
    }
    @Override
    public void onTokenSuccess(String message) {
        try {
            JSONObject jsonData = new JSONObject(message);
            String token = jsonData.getString(JSONConstants.TOKEN);
            if (token != null && token.length() > 1) {
                videoListener.onCreateRoomSuccess(token);
            } else {
                videoListener.onCreateRoomError(context.getResources().getString(R.string.server_error));
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.getResources().getString(R.string.server_error);
        }
    }

    @Override
    public void onSessionError(String message) {
        videoListener.onCreateRoomError(message);
    }

    @Override
    public void onTokenError(String message) {
        videoListener.onCreateRoomError(message);
    }

    @Override
    public void onRequestError(String message) {
        videoListener.onCreateRoomError(message);
    }
}
