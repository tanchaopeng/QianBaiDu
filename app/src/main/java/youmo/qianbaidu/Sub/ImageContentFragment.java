package youmo.qianbaidu.Sub;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import Core.MatrixImageView;
import youmo.qianbaidu.R;

/**
 * Created by tanch on 2016/1/21.
 */
public class ImageContentFragment extends Fragment {
    private MatrixImageView image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_image,container,false);

        image=(MatrixImageView)v.findViewById(R.id.fragment_content_image);
        String url= getArguments().getString("url");
        new GetImg().execute(url);
        return v;
    }


    class GetImg extends AsyncTask<String,Integer,Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... params) {
            String url= params[0];
            try{
                URL address=new URL(url);
                HttpURLConnection http=(HttpURLConnection)address.openConnection();
                http.setConnectTimeout(6000);
                http.setUseCaches(false);
                Log.i("HTTP缓存开关",String.valueOf(http.getUseCaches()));
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
            //Toast.makeText(getActivity(),"W:"+String.valueOf(b.getWidth())+" H:"+String.valueOf(b.getHeight()),Toast.LENGTH_SHORT).show();
            image.setImageBitmap(b);
        }
    }
}
