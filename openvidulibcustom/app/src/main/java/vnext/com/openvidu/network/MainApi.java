package vnext.com.openvidu.network;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vnext.com.openvidu.R;
import vnext.com.openvidu.models.Content;
import vnext.com.openvidu.models.TokenModel;

/**
 * Created by TuanDM on 26/07/2018.
 */

public class MainApi extends APIClient{

    public MainApi(Context context, APIListener apiListener) {
        super(context,apiListener);
    }
    public void getSession() {
        Call<JsonElement> call = getApiInterface().getSession();
        this.baseApi(call,getContext().getResources().getString(R.string.session));
    }
    public void getTokens(TokenModel tokenModel) {
        Call<JsonElement> call =getApiInterface().getTokens(tokenModel);
                this.baseApi(call,getContext().getResources().getString(R.string.token));
    }
}
