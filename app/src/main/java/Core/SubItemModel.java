package Core;

/**
 * Created by tanch on 2016/1/18.
 */
public class SubItemModel {
    public String title;
    public String url;
    public String imageUrl;
    public SubItemModel(String t,String u)
    {
        this.title=t;
        this.url=u;
    }
    public SubItemModel(String t,String u,String imageUrl)
    {
        this.title=t;
        this.url=u;
        this.imageUrl=imageUrl;
    }
}
