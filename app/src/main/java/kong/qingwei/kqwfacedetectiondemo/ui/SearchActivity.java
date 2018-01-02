package kong.qingwei.kqwfacedetectiondemo.ui;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongqw.util.FaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import kong.qingwei.kqwfacedetectiondemo.R;
import kong.qingwei.kqwfacedetectiondemo.config.Constants;
import kong.qingwei.kqwfacedetectiondemo.model.FaceInterface;
import kong.qingwei.kqwfacedetectiondemo.util.NameMap;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private static final String FACE = "face";

    TextView recognition;
    ImageView show_face_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recognition = (TextView) findViewById(R.id.recognition);
        show_face_image = (ImageView) findViewById(R.id.face);

        Bitmap mBitmapFace = FaceUtil.getImage(this, FACE);
        show_face_image.setImageBitmap(mBitmapFace);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-cn.faceplusplus.com/facepp/v3/")
                .build();
        FaceInterface faceInterface = retrofit.create(FaceInterface.class);
        File file = FaceUtil.getFaceFile(this, FACE);
        if (file != null) {
            RequestBody requestAPIKey = RequestBody.create(MediaType.parse("text/plain"), Constants.api_key);
            RequestBody requestAPISecret = RequestBody.create(MediaType.parse("text/plain"), Constants.api_secret);
            RequestBody requestFaceOuterId = RequestBody.create(MediaType.parse("text/plain"), "face_project");
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image_file", file.getName(), requestFile);

            Call<ResponseBody> call = faceInterface.search(requestAPIKey, requestAPISecret, requestFaceOuterId, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            parseJSON(response.body().string());
                        } else {
                            Log.i(TAG, response.errorBody().string());
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "onFailure=" + t.getMessage());
                }
            });
        }

    }

    private void parseJSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject results = jsonObject.getJSONArray("results").getJSONObject(0);
        Log.i(TAG, results.toString());
        // TODO user_id是我自定的信息，token_id是返回的token

        String confidence = results.getString("confidence");
        String user_id = results.getString("user_id");
        String face_token = results.getString("face_token");


        String realName = NameMap.getName(face_token);

        recognition.setText(realName + "最相似, 置信度为" + confidence);

    }
}
