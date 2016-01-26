package Tools;


import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.CacheControl;
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

    public OkHttpHelper(Context context)
    {
        long cacheSize=10*1024*1024;//10mb
        File cacheDir=context.getCacheDir();//缓存地址
        Cache cache=new Cache(cacheDir,cacheSize);
        httpClient = new OkHttpClient.Builder().cache(cache).build();
    }

    public void ClearCache()
    {
        if (httpClient.cache()!=null)
        try {
            httpClient.cache().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String SyncGet(String url)
    {
        String ret=null;
        final Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response=httpClient.newCall(request).execute();
            ret = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
      return ret;
    }
    public void AsynGet(String url,Callback callback)
    {

        final Request.Builder request = new Request.Builder();
        httpClient.newCall(request.url(url).build()).enqueue(callback);
    }
    public Call AsynGet(String url,int tag,Callback callback)
    {
        final Request.Builder request = new Request.Builder();
        request.tag(tag).url(url);
        Call call=httpClient.newCall(request.build());
        call.enqueue(callback);
        return call;
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
