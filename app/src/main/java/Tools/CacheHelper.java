package Tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.text.LoginFilter;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tanch on 2016/1/20.
 */
public class CacheHelper {

    private Context context;
    private String cacheDir;

    public CacheHelper(Context c)
    {
        this.context=c;
        this.cacheDir=context.getCacheDir()+"/";
        Log.i("缓存目录",cacheDir);
    }
    @Nullable
    public byte[] ReadCacheFile(String key)
    {
        File file=new File(cacheDir+key);
        Log.i("文件大小",String.valueOf(file.length()/(float)1024/(float)1024)+" MB");
        return FileHelper.ReadFileToByte(file);
    }
    public void WriteCacheFile(String key,byte[] fileByte)
    {
        File file=new File(cacheDir+key);
        FileHelper.WriteByteToFile(fileByte,file);
    }

    public boolean IsExists(String key)
    {
        File file=new File(cacheDir+key);
        return file.exists();
    }

    public void ClearAll()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Lock lock=new ReentrantLock();
                lock.lock();
                File file=context.getCacheDir();
                for(File f:file.listFiles())
                {
                    f.delete();
                }
                lock.unlock();
            }
        }).start();
    }
    //region 取得string的MD5

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }


    @NonNull
    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    //endregion

}
