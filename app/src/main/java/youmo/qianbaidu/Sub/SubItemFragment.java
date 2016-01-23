package youmo.qianbaidu.Sub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Core.CoreActivity;
import Core.SubItemModel;
import Tools.FileHelper;
import Tools.HttpHelper;
import Tools.SPHelper;
import Tools.StringHelper;
import youmo.qianbaidu.Image.ImageListFragment;
import youmo.qianbaidu.R;

public class SubItemFragment extends Fragment {

    List<SubItemModel> Lsi=new ArrayList<SubItemModel>();
    SubItemAdapter Sia;
    String ExtraUrl="";
    String NextUrl="";
    boolean IsLoad=false;
    TextView load;
    private SharedPreferences Sp;
    private SharedPreferences.Editor SpEditor;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.from(getActivity()).inflate(R.layout.activity_sub_img,container,false);

        load=(TextView)v.findViewById(R.id.loading);

        Sp =getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        SpEditor= Sp.edit();

        ExtraUrl=getArguments().getString("url");

        if (ExtraUrl==null||ExtraUrl.length()<1)
        {
            ExtraUrl=Sp.getString("url",null);
            if (ExtraUrl==null||ExtraUrl.length()<1)
            {
               return super.onCreateView(inflater,container,savedInstanceState);
            }
        }
        else
        {
            SpEditor.putString("url", ExtraUrl);
            SpEditor.apply();
        }

        new GetList().execute(ExtraUrl);

        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.sub_img_recycler);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager recLiner= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recLiner);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = recLiner.findLastVisibleItemPosition();
                int totalItemCount = recLiner.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (!IsLoad)
                    if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                        IsLoad=true;
                        new GetList().execute(NextUrl);
                    }
            }
        });


        Sia =new SubItemAdapter(Lsi);
        Sia.SetOnClick(new SubItemAdapter.IItemCilck() {
            @Override
            public void OnClick(View v, int i) {
                //Lsi.get(i).url
                ImageListFragment imageListFragment = new ImageListFragment();
                Bundle b = new Bundle();
                b.putString("url",Lsi.get(i).url);
                imageListFragment.setArguments(b);
                FragmentTransaction ft= getActivity().getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.add(R.id.sub_content_fragment,imageListFragment).commit();
            }
        });
        recyclerView.setAdapter(Sia);
        return v;
    }



    class GetList extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params) {
            String url=params[0];
            String html= HttpHelper.Get(url);
            NextUrl=StringHelper.MidString(html,"上一页","下一页</a");
            NextUrl=StringHelper.MidString(NextUrl,"<a href='","'>");
            NextUrl=url.substring(0,url.lastIndexOf("/")+1)+NextUrl;
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
            try {
                Sia.notifyDataSetChanged();
                load.setVisibility(View.GONE);
                IsLoad=false;
            }
            catch (Exception e){}
        }
    }
}
