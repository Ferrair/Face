package kong.qingwei.kqwfacedetectiondemo.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongqw.util.FaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kong.qingwei.kqwfacedetectiondemo.R;
import kong.qingwei.kqwfacedetectiondemo.config.Constants;
import kong.qingwei.kqwfacedetectiondemo.model.FaceInterface;
import kong.qingwei.kqwfacedetectiondemo.util.CoalitionUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowFaceActivity extends AppCompatActivity {

    private static final String TAG = "ShowFaceActivity";
    private static final String FACE = "face";
    ImageView show_face_image;
    TextView emotion_text_view;
    Bitmap mBitmapFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_face);

        emotion_text_view = (TextView) findViewById(R.id.emotion);
        show_face_image = (ImageView) this.findViewById(R.id.face);
        mBitmapFace = FaceUtil.getImage(this, FACE);
        show_face_image.setImageBitmap(mBitmapFace);


        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl("https://api-cn.faceplusplus.com/facepp/v3/")
                .build();
        FaceInterface faceInterface = retrofit.create(FaceInterface.class);
        File file = FaceUtil.getFaceFile(this, FACE);
        if (file != null) {
            RequestBody requestAPIKey = RequestBody.create(MediaType.parse("text/plain"), Constants.api_key);
            RequestBody requestAPISecret = RequestBody.create(MediaType.parse("text/plain"), Constants.api_secret);
            RequestBody requestAttribute = RequestBody.create(MediaType.parse("text/plain"), "emotion");
            RequestBody requestLandmark = RequestBody.create(MediaType.parse("text/plain"), "1");
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image_file", file.getName(), requestFile);

            Call<ResponseBody> call = faceInterface.detect(requestAPIKey, requestAPISecret, requestAttribute, requestLandmark, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            parseJSON(response.body().string());
                        } else {
                            Log.e(TAG, response.errorBody().string());
                            emotion_text_view.setText("出了错");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        emotion_text_view.setText("出了错");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "onFailure=" + t.getMessage());
                    emotion_text_view.setText("出了错");
                }
            });
        }
    }

    private void parseJSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject face = jsonObject.getJSONArray("faces").getJSONObject(0);

        JSONObject landmark = face.getJSONObject("landmark");
        JSONObject emotion = face.getJSONObject("attributes").getJSONObject("emotion");

        /*
         * Show Emotion
         */
        Map<String, Double> emotionMap = new HashMap<>();
        emotionMap.put("sadness", emotion.getDouble("sadness"));
        emotionMap.put("neutral", emotion.getDouble("neutral"));
        emotionMap.put("disgust", emotion.getDouble("disgust"));
        emotionMap.put("anger", emotion.getDouble("anger"));
        emotionMap.put("surprise", emotion.getDouble("surprise"));
        emotionMap.put("happiness", emotion.getDouble("happiness"));

        String emotionString = CoalitionUtil.findMax(emotionMap);
        emotion_text_view.setText("Emotion: " + emotionString);

        Log.i(TAG, emotionString);

        /*
         * Show Landmark
         */
        // 从文件中读取的图片是不可被改变的，所以不能直接使用setPixel()来设置他的像素点颜色，要想把图片变成可改变的，要Copy一份
        Bitmap mBitmapFaceCopy = mBitmapFace.copy(mBitmapFace.getConfig(), true);
        Iterator<String> sIterator = landmark.keys();
        while (sIterator.hasNext()) {
            // 获得key
            String key = sIterator.next();
            // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
            JSONObject value = landmark.getJSONObject(key);

            int originHeight = mBitmapFace.getHeight();
            int originWidth = mBitmapFace.getWidth();
            int x = value.getInt("x");
            int y = value.getInt("y");

            if (x < originWidth && y < originHeight) {
                mBitmapFaceCopy.setPixel(x, y, Color.RED);
                Log.i(TAG, x + "," + y);
            }


        }
        show_face_image.setImageBitmap(mBitmapFaceCopy);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
}
