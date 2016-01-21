package Tools;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by tanch on 2016/1/18.
 */
public class FileHelper {

    @Nullable
    public static String ReadToString(String path)
    {
        File file=new File(path);
        byte[] b=ReadFileToByte(file);
        try
        {
            return new String(b,"UTF-8");
        }catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 读取文件到byte[]
     * @param file
     * @return
     */
    @Nullable
    public static byte[] ReadFileToByte(File file)
    {
        if (!file.exists())
            return null;
        try
        {
            FileInputStream fis=new FileInputStream(file);
            byte[] b=new byte[fis.available()];
            fis.read(b);
            fis.close();
            return b;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 写入byte[]到文件
     * @param b
     * @param file
     * @return
     */
    public static boolean WriteByteToFile(byte[] b, File file) {
        try {
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(b);
            fops.flush();
            fops.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
