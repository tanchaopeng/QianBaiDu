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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import Core.MatrixImageView;
import Tools.CacheHelper;
import youmo.qianbaidu.R;

/**
 * Created by tanch on 2016/1/21.
 */
public class ImageContentFragment extends Fragment {
    private MatrixImageView image;
    private ViewPager viewPager;
    private int index;
    List<ImageView> list;
    private ImageAdapter ia;

    private CacheHelper ch ;
    private List<String> ImageUrlList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_image,container,false);


        ImageUrlList= getArguments().getStringArrayList("url");
        index=getArguments().getInt("index");

        ch=new CacheHelper(getActivity());
        viewPager=(ViewPager)v.findViewById(R.id.viewPager_image);
        //image=(MatrixImageView)v.findViewById(R.id.fragment_content_image);
        LayoutInflater lf=getActivity().getLayoutInflater().from(getActivity());
        list=new ArrayList<ImageView>();
        for (int i=0;i<ImageUrlList.size();i++)
            list.add((ImageView)lf.inflate(R.layout.viewpager_image,null));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                new GetImg().execute(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ia= new ImageAdapter(list);
        viewPager.setAdapter(ia);

        new GetImg().execute(index);
        viewPager.setCurrentItem(index);
        //new GetImg().execute(url);
        return v;
    }

    class GetImg extends AsyncTask<Integer,Integer,Bitmap>
    {
        private int Index;
        @Override
        protected Bitmap doInBackground(Integer... params) {
            Index=params[0];
            String url=ImageUrlList.get(Index);
            Log.i("图片URL",url);
            try{
                URL address=new URL(url);
                HttpURLConnection http=(HttpURLConnection)address.openConnection();
                http.setConnectTimeout(6000);
                http.setUseCaches(false);
                Bitmap b1= BitmapFactory.decodeStream(http.getInputStream());
                http.disconnect();
                return b1;
            }
            catch(Exception e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            list.get(Index).setImageBitmap(b);
            ia.notifyDataSetChanged();
            Log.i("图片状态","加载完成");
        }
    }
    class ImageAdapter extends PagerAdapter
    {
        private List<ImageView> Liv;

        ImageAdapter(List<ImageView> data)
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
