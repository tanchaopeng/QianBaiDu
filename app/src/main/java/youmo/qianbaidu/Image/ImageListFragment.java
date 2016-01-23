package youmo.qianbaidu.Image;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import Core.ImageModel;
import Tools.BitmapHelper;
import Tools.CacheHelper;
import Tools.HttpHelper;
import Tools.StringHelper;
import youmo.qianbaidu.R;

/**
 * Created by tanch on 2016/1/20.
 */
public class ImageListFragment extends Fragment {

    private RecyclerView ImagesRecycler;
    private List<ImageModel> ImagesData=new ArrayList<ImageModel>();
    private ImageAdapter ImagesAda;
    private List<String> ImageUrlList;
    private CacheHelper ch ;

    private GetImgs getImg;

    private int ImageWidth;


    public List<String> GiveInfo()
    {
        List<String> Lurl =new ArrayList<String>();
        Lurl.add("http://img4.imgtn.bdimg.com/it/u=926486782,2007696240&fm=21&gp=0.jpg");
        Lurl.add("http://pic8.nipic.com/20100728/4800623_122400048931_2.jpg");
        Lurl.add("http://img5.imgtn.bdimg.com/it/u=2738507974,1197415021&fm=21&gp=0.jpg");
        Lurl.add("http://img3.mypsd.com.cn/20110212/Mypsd_13866_201102121751510008B.jpg");
        Lurl.add("http://img03.tooopen.com/uploadfile/downs/images/20120613/sy_201206131606553550.jpg");
        Lurl.add("http://img.taopic.com/uploads/allimg/120426/1717-12042614222511.jpg");
        Lurl.add("http://img.sucai.redocn.com/attachments/images/201112/20111213/Redocn_2011121101173010.jpg");
        Lurl.add("http://img4.imgtn.bdimg.com/it/u=926486782,2007696240&fm=21&gp=0.jpg");
        Lurl.add("http://pic8.nipic.com/20100728/4800623_122400048931_2.jpg");
        Lurl.add("http://img5.imgtn.bdimg.com/it/u=2738507974,1197415021&fm=21&gp=0.jpg");
        Lurl.add("http://img3.mypsd.com.cn/20110212/Mypsd_13866_201102121751510008B.jpg");
        Lurl.add("http://img03.tooopen.com/uploadfile/downs/images/20120613/sy_201206131606553550.jpg");
        Lurl.add("http://img.taopic.com/uploads/allimg/120426/1717-12042614222511.jpg");
        Lurl.add("http://img.sucai.redocn.com/attachments/images/201112/20111213/Redocn_2011121101173010.jpg");
        Lurl.add("http://img4.imgtn.bdimg.com/it/u=926486782,2007696240&fm=21&gp=0.jpg");
        Lurl.add("http://pic8.nipic.com/20100728/4800623_122400048931_2.jpg");
        Lurl.add("http://img5.imgtn.bdimg.com/it/u=2738507974,1197415021&fm=21&gp=0.jpg");
        Lurl.add("http://img3.mypsd.com.cn/20110212/Mypsd_13866_201102121751510008B.jpg");
        Lurl.add("http://img03.tooopen.com/uploadfile/downs/images/20120613/sy_201206131606553550.jpg");
        Lurl.add("http://img.taopic.com/uploads/allimg/120426/1717-12042614222511.jpg");
        Lurl.add("http://img.sucai.redocn.com/attachments/images/201112/20111213/Redocn_2011121101173010.jpg");
        Lurl.add("http://img4.imgtn.bdimg.com/it/u=926486782,2007696240&fm=21&gp=0.jpg");
        Lurl.add("http://pic8.nipic.com/20100728/4800623_122400048931_2.jpg");
        Lurl.add("http://img5.imgtn.bdimg.com/it/u=2738507974,1197415021&fm=21&gp=0.jpg");
        Lurl.add("http://img3.mypsd.com.cn/20110212/Mypsd_13866_201102121751510008B.jpg");
        Lurl.add("http://img03.tooopen.com/uploadfile/downs/images/20120613/sy_201206131606553550.jpg");
        Lurl.add("http://img.taopic.com/uploads/allimg/120426/1717-12042614222511.jpg");
        Lurl.add("http://img.sucai.redocn.com/attachments/images/201112/20111213/Redocn_2011121101173010.jpg");
        return Lurl;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_img,container,false);

        getImg=new GetImgs(getActivity(),ImageWidth,ImageWidth);

        ch = new CacheHelper(getActivity());
        ch.ClearAll();
        ImagesRecycler=(RecyclerView)v.findViewById(R.id.fragment_recycler);
        ImagesAda=new ImageAdapter(ImagesData);
        ImagesRecycler.setHasFixedSize(true);
        ImagesRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        ImagesRecycler.setAdapter(ImagesAda);

        final Point p=new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
        ImageWidth=p.x/2;
        Log.i("图片宽度",String.valueOf(ImageWidth));
        final String url=getArguments().getString("url");

        new GetImgs(getActivity(),ImageWidth,ImageWidth).execute(url);

        return v;
    }


    //取得图片
    class GetImgs extends AsyncTask<String,Integer,String>
    {
        private Context context;
        private int ImageWidth,ImageHeight;
        public GetImgs(Context context, int width, int height)
        {
            this.context=context;
            this.ImageWidth=width;
            this.ImageHeight=height;
        }
        @Override
        protected String doInBackground(String... params) {
            //List<String> list=GiveInfo();
            List<String> list;
            String html=HttpHelper.Get(params[0]);
            if (html!=null&&html.length()>0)
                list= StringHelper.MidListString(html,"([a-zA-z]+://[^\\s]*)(.jpg|.png)",0);
            else
                list=GiveInfo();
            ImageUrlList=list;
            for (String s:list)
            {
                String url=s;
                if (url!=null)
                {
                    String key= CacheHelper.hashKeyForDisk(url);
                    byte[] b;
                    if (ch.IsExists(key))
                        b=ch.ReadCacheFile(key);
                    else
                    {
                        b= HttpHelper.GetToByte(url);
                        ch.WriteCacheFile(key,b);
                    }
                    Bitmap bitmap= BitmapHelper.DecodeBitmapFromByte(b,ImageWidth,ImageHeight);
                    Log.i("信息","W:"+String.valueOf(bitmap.getWidth())+" H:"+String.valueOf(bitmap.getHeight()));

                    ImagesData.add(new ImageModel(null,url,bitmap));

                    publishProgress(0);
                }
            }
            Log.i("步骤","doInBackground 完成");
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            ImagesAda.notifyDataSetChanged();
            super.onProgressUpdate(values);
        }
    }

    class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        private List<ImageModel> Data;

        public ImageAdapter(List<ImageModel> data)
        {
            this.Data=data;
        }

        private class ImageHolder extends RecyclerView.ViewHolder
        {
            public ImageView imageView;
            public ImageHolder(View v) {
                super(v);
                imageView=(ImageView)v.findViewById(R.id.adapter_image);
                imageView.setLayoutParams(new GridLayoutManager.LayoutParams(ImageWidth,ImageWidth));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentTransaction transaction =getFragmentManager().beginTransaction();
                        ImageContentFragment imageContent = new ImageContentFragment();
                        Bundle b = new Bundle();
                     //   b.putString("url",ImagesData.get(getAdapterPosition()).url);
                        b.putStringArrayList("url",new ArrayList<String>(ImageUrlList));
                        b.putInt("index",getAdapterPosition());
                        imageContent.setArguments(b);
                        transaction.add(R.id.sub_content_fragment, imageContent);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        Log.i("消息","打开Fragment");
                    }
                });
            }
        }
        @Override
        public int getItemCount() {
            return Data.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image, parent, false);
            return new ImageHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ImageHolder)holder).imageView.setImageBitmap(Data.get(position).image);
        }
    }

}
