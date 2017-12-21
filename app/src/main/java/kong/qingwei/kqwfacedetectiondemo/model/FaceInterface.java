package kong.qingwei.kqwfacedetectiondemo.model;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by qihang on 14/12/2017.
 */

public interface FaceInterface {

    @Multipart
    @POST("detect")
    Call<ResponseBody> detect(@Part("api_key") RequestBody api_key,
                              @Part("api_secret") RequestBody api_secret,
                              @Part("return_attributes") RequestBody return_attributes,
                              @Part("return_landmark") RequestBody return_landmark,
                              @Part MultipartBody.Part image_file);


    @Multipart
    @POST("search")
    Call<ResponseBody> search(@Part("api_key") RequestBody api_key,
                              @Part("api_secret") RequestBody api_secret,
                              @Part("outer_id") RequestBody outer_id,
                              @Part MultipartBody.Part image_file);
}


