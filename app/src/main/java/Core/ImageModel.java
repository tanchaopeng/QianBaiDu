package Core;

import android.graphics.Bitmap;

/**
 * Created by tanch on 2016/1/21.
 */
public class ImageModel {
    public String name;
    public String url;
    public Bitmap image;
    public ImageModel(String n,String u,Bitmap b)
    {
        this.name=n;
        this.url=u;
        this.image=b;
    }
}
