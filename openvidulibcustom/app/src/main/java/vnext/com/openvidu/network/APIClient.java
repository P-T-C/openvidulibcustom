package vnext.com.openvidu.network;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import vnext.com.openvidu.R;
import vnext.com.openvidu.configs.LinkApi;
import vnext.com.openvidu.utils.DialogUtils;

/**
 * Created by TuanDM on 26/07/2018.
 */

public class APIClient {

    private APIListener apiListener;
    private Context context;
    private Retrofit retrofit;
    APIInterface apiInterface;
    DialogUtils dialogUtils;

    public APIClient(Context context, APIListener apiListener) {
        this.context = context;
        this.apiListener = apiListener;
        this.retrofit = null;
        dialogUtils=new DialogUtils(context);
        apiInterface = getClient().create(APIInterface.class);

    }

    public APIInterface getApiInterface() {
        return apiInterface;
    }
    public void setApiInterface(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }
    protected Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(LinkApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
    private void getApiTypeSuccess(String type,String message){
        if (type.equals(context.getResources().getString(R.string.session))){
            apiListener.onSessionSuccess(message);
        }
        else if(type.equals(context.getResources().getString(R.string.token))){
            apiListener.onTokenSuccess(message);
        }
        else{
            apiListener.onRequestError(context.getResources().getString(R.string.bad_request));
        }
    }


    private void getApiTypeError(String type,String message){
        if (type.equals(context.getResources().getString(R.string.session))){
            apiListener.onSessionError(message);
        }
        else if(type.equals(context.getResources().getString(R.string.token))){
            apiListener.onTokenError(message);
        }
        else{
            apiListener.onRequestError(context.getResources().getString(R.string.bad_request));
        }
    }
    protected void baseApi(Call<JsonElement> call, final String type) {
        dialogUtils.showLoadingDialog();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                dialogUtils.dismissDialog();
                try {
                    getApiTypeSuccess(type,response.body().toString());
                } catch (Exception e) {
                    getApiTypeError(type,e.getMessage());
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                dialogUtils.dismissDialog();
                getApiTypeError(type,t.getMessage());

            }
        });
    }

    public APIListener getApiListener() {
        return apiListener;
    }

    public void setApiListener(APIListener apiListener) {
        this.apiListener = apiListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }
}