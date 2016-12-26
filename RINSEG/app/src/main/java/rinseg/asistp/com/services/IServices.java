package rinseg.asistp.com.services;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rinseg.asistp.com.models.ROP;

/**
 * Created by Carlos Ramos on 20/10/2016.
 */
public interface IServices {

    @FormUrlEncoded
    @POST(Services.LOGIN)
    Call<ResponseBody> setLogin(@Field("username") String usu, @Field("password") String pass);

    @GET(Services.SYNC)
    Call<ResponseBody> getSync(@Query("api_token") String api_token);

    @POST(Services.ROP_CLOSE)
    Call<ResponseBody> setRopCerrado(@Body ROP rop, @Query("api_token") String api_token);

    @Multipart
    @POST(Services.ADD_IMAGEN)
    Call<ResponseBody> setImageRop(
                              @Part("rop_id") RequestBody rop_id,
                              @Part("description") RequestBody description,
                              @Part MultipartBody.Part file_image,
                              @Query("api_token") String api_token
    );


}
