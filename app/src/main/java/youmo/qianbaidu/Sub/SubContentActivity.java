package youmo.qianbaidu.Sub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Tools.BitmapHelper;
import Tools.CacheHelper;
import Tools.HttpHelper;
import Tools.StringHelper;
import youmo.qianbaidu.R;

public class SubContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String url= getIntent().getStringExtra("url");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.sub_content_fragment)!=null)
        {
            ImagesFragment imagesFragment = new ImagesFragment();
            Bundle b = new Bundle();
            b.putString("url",url);
            imagesFragment.setArguments(b);
            getFragmentManager().beginTransaction().add(R.id.sub_content_fragment, imagesFragment).commit();
            Log.i("消息","打开Fragment");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            getFragmentManager().popBackStack();
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
