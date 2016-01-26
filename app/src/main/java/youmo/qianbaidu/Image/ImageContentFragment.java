package youmo.qianbaidu.Image;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import Core.MatrixImageView;
import Tools.CacheHelper;
import Tools.OkHttpHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import youmo.qianbaidu.R;

/**
 * Created by tanch on 2016/1/21.
 */
public class ImageContentFragment extends Fragment {
    private MatrixImageView image;
    private ViewPager viewPager;
    private int index;
    List<MatrixImageView> list;
    private ImageAdapter ia;
    private OkHttpHelper http;

    private List<String> ImageUrlList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_image,container,false);

        http=new OkHttpHelper(getActivity());

        ImageUrlList= getArguments().getStringArrayList("url");
        index=getArguments().getInt("index");

        viewPager=(ViewPager)v.findViewById(R.id.viewPager_image);
        //image=(MatrixImageView)v.findViewById(R.id.fragment_content_image);
        LayoutInflater lf=getActivity().getLayoutInflater().from(getActivity());
        list=new ArrayList<MatrixImageView>();
        for (int i=0;i<ImageUrlList.size();i++)
            list.add((MatrixImageView)lf.inflate(R.layout.adapter_image_content,null));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                GetImage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ia= new ImageAdapter(list);
        viewPager.setAdapter(ia);

        GetImage(index);
        viewPager.setCurrentItem(index);
        return v;
    }

    private void GetImage(int index)
    {
        String url=ImageUrlList.get(index);
        http.AsynGet(url,index, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final int i=(int)call.request().tag();
                final Bitmap b1= BitmapFactory.decodeStream(response.body().byteStream());
                if (isVisible())
                new android.os.Handler(getActivity().getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        list.get(i).setImageBitmap(b1);
                        ia.notifyDataSetChanged();
                        Log.i("图片状态","加载完成"+i);
                    }
                });

            }
        });
    }
    class ImageAdapter extends PagerAdapter
    {
        private List<MatrixImageView> Liv;

        ImageAdapter(List<MatrixImageView> data)
        {
            this.Liv=data;
        }
        @Override
        public int getCount() {
            return Liv.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i("图片适配器","删除"+position);
            container.removeView(Liv.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("图片适配器","添加"+position);
            container.addView(Liv.get(position), 0);//
            return Liv.get(position);
        }
    }
}
