package youmo.qianbaidu.Sub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Core.CoreActivity;
import Core.SubItemModel;
import Tools.FileHelper;
import Tools.HttpHelper;
import Tools.OkHttpHelper;
import Tools.SPHelper;
import Tools.StringHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
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
    private OkHttpHelper http=new OkHttpHelper();

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


        ExtraUrl=getArguments().getString("url");

        if (ExtraUrl==null||ExtraUrl.length()<1)
            return super.onCreateView(inflater,container,savedInstanceState);

        GetImagesList(ExtraUrl);

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
                        GetImagesList(NextUrl);
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

    private void GetImagesList(final String url)
    {
        http.AsynGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
                        _url= url.substring(0,url.lastIndexOf("/")+1)+_url;
                    Lsi.add(new SubItemModel(_title,_url));
                }

                new Handler(getActivity().getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Sia.notifyDataSetChanged();
                        load.setVisibility(View.GONE);
                        IsLoad=false;
                    }
                });

            }
        });

    }

}
