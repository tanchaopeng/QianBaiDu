package youmo.qianbaidu.Sub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Core.CoreActivity;
import Core.SubItemModel;
import Tools.FileHelper;
import Tools.SPHelper;
import Tools.StringHelper;
import youmo.qianbaidu.R;

public class SubImgActivity extends CoreActivity {

    List<SubItemModel> Lsi=new ArrayList<SubItemModel>();
    SubItemAdapter Sia;
    String ExtraUrl="";
    TextView load;

    private SharedPreferences Sp;
    private SharedPreferences.Editor SpEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_img);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        load=(TextView)findViewById(R.id.loading);

        Sp =this.getSharedPreferences("config", Context.MODE_PRIVATE);
        SpEditor= Sp.edit();

        Intent intent=getIntent();
        ExtraUrl=intent.getStringExtra("url");

        if (ExtraUrl==null||ExtraUrl.length()<1)
        {
            ExtraUrl=Sp.getString("url",null);
            if (ExtraUrl==null||ExtraUrl.length()<1)
            {
                finish();
                return;
            }
        }
        else
        {
            SpEditor.putString("url", ExtraUrl);
            SpEditor.apply();
        }

        final SwipeRefreshLayout swipe=(SwipeRefreshLayout)findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ShowToast("我刷新了");
            }
        });

        new GetList().execute(ExtraUrl);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.sub_img_recycler);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager recLiner= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recLiner);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = recLiner.findLastVisibleItemPosition();
                int totalItemCount = recLiner.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    ShowToast("我要开始加载了");
                    swipe.setRefreshing(false);
                    new GetList().execute(ExtraUrl);
                }
            }
        });


        Sia =new SubItemAdapter(Lsi);
        Sia.SetOnClick(new SubItemAdapter.IItemCilck() {
            @Override
            public void OnClick(View v, int i) {
                startActivity(new Intent(v.getContext(),SubContentActivity.class).putExtra("url",Lsi.get(i).url));
            }
        });
        recyclerView.setAdapter(Sia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("url",ExtraUrl);
        super.onSaveInstanceState(outState);
    }

    class GetList extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params) {
            String html=HttpGet(params[0]);
            List<String> List= StringHelper.MidListString(html,"<li> <img","</li>");
            for (String s:List)
            {
                String _title=StringHelper.MidString(s,"target=\"_blank\">","</a>");
                String _url=StringHelper.MidString(s,"<a href=\"","\"");
                if (_url.indexOf("http")<0)
                    _url= params[0].substring(0,params[0].lastIndexOf("/")+1)+_url;
                Lsi.add(new SubItemModel(_title,_url));
            }
            return html;
        }

        @Override
        protected void onPostExecute(String s) {
            Sia.notifyDataSetChanged();
            load.setVisibility(View.GONE);
        }
    }
}
