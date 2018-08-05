package vnext.com.openvidu.network;
/**
 * Created by TuanDM on 26/07/2018.
 */


import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vnext.com.openvidu.models.Content;

import vnext.com.openvidu.models.TokenModel;

interface APIInterface {

    @GET("/citiesJSON?")
    Call<Content> getContents(@Query("north") String north, @Query("south") String south, @Query("east") String east, @Query("west") String west);
    @Headers({
            "Authorization: Basic T1BFTlZJRFVBUFA6TVlfU0VDUkVU",
            "Content-Type: application/json"
    })
    @POST("/api/sessions")
    Call<JsonElement> getSession();
    @Headers({
            "Authorization: Basic T1BFTlZJRFVBUFA6TVlfU0VDUkVU",
            "Content-Type: application/json"
    })
    @POST("/api/tokens")
    Call<JsonElement> getTokens(@Body TokenModel token);

}