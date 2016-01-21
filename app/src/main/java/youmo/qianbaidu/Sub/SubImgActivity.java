package youmo.qianbaidu.Sub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

    private SharedPreferences Sp;
    private SharedPreferences.Editor SpEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_img);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        new GetList().execute(ExtraUrl);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.sub_img_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Sia =new SubItemAdapter(Lsi);
        Sia.SetOnClick(new SubItemAdapter.IItemCilck() {
            @Override
            public void OnClick(View v, int i) {
                startActivity(new Intent(v.getContext(),SubContentActivity.class).putExtra("url",Lsi.get(i).url));
               // ShowToast(Lsi.get(i).url);
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
        }
    }
}
