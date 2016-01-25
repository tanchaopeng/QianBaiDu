package Tools;


import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by tanch on 2016/1/25.
 */
public class OkHttpHelper {
    OkHttpClient httpClient;
    public OkHttpHelper()
    {
        httpClient = new OkHttpClient();
    }

    public String SyncGet(String url)
    {
        String ret=null;
        final Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            ret = httpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
      return ret;
    }
    public void AsynGet(String url,Callback callback)
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //new call
        httpClient.newCall(request).enqueue(callback);
    }
    public void AsynGet(String url,int tag,Callback callback)
    {
        final Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .build();
        //new call
        httpClient.newCall(request).enqueue(callback);
    }

    public void AsynPost(String url,HashMap<String,String> values,Callback callback)
    {
        FormBody.Builder fb=new FormBody.Builder();
        for (Map.Entry<String,String> map:values.entrySet())
        {
            fb.add(map.getKey(),map.getValue());
        }

        Request request = new Request.Builder()
                .url(url)
                .post(fb.build())
                .build();
        httpClient.newCall(request).enqueue(callback);
    }
}
