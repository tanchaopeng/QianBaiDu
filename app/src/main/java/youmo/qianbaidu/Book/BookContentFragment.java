package youmo.qianbaidu.Book;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import Tools.HttpHelper;
import Tools.OkHttpHelper;
import Tools.StringHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import youmo.qianbaidu.R;

/**
 * Created by tanch on 2016/1/26.
 */
public class BookContentFragment extends Fragment {

    private RecyclerView recyclerView;
    OkHttpHelper http;
    List<String> Lstr=new ArrayList<String>();
    BookAdapter bookAdapter;
    String NextUrl="";
    boolean IsLoad=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.from(getActivity()).inflate(R.layout.fragment_book,null);
        http=new OkHttpHelper(getActivity());
        recyclerView=(RecyclerView)v.findViewById(R.id.recyclerView_Book_Content);
        final LinearLayoutManager recLiner= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recLiner);
        recyclerView.setHasFixedSize(true);
        bookAdapter=new BookAdapter(Lstr);
        recyclerView.setAdapter(bookAdapter);
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
                        if (NextUrl!=null&&NextUrl.length()>0)
                        {
                            IsLoad=true;
                            GetBook(NextUrl);
                        }

                    }
            }
        });


        String url=getArguments().getString("url");
        GetBook(url);
        return v;
    }

    void GetBook(String url)
    {
        http.AsynGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html= response.body().string();
                String url= call.request().url().toString();
                NextUrl= StringHelper.MidString(html,"<div class=\"pagea\">","\">下一页</a");
                if (NextUrl!=null)
                {
                    NextUrl=NextUrl.substring(NextUrl.lastIndexOf("<a href=\"")+9,NextUrl.length());
                    NextUrl=url.substring(0,url.lastIndexOf("/")+1)+NextUrl;
                }
                String body=StringHelper.MidString(html,"<P> <font color=\"#0000FF\" style=\"font-size:15px;\">","</font></P>");
                String[] StrAarr= body.split("<br\\s{0,1}/?>");
                for (String s:StrAarr)
                {
                    Lstr.add(s);
                }
                if (isVisible())
                    new Handler(getActivity().getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            bookAdapter.notifyDataSetChanged();
                            IsLoad=false;
                        }
                    });
            }
        });
    }

    class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        List<String> Data;
        public BookAdapter(List<String> data)
        {
            this.Data=data;
        }

        class BookHolder extends RecyclerView.ViewHolder
        {
            private TextView textView;
            public BookHolder(View v) {
                super(v);
                textView=(TextView)v.findViewById(R.id.textView_Book_Content);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_book_content, parent, false);
            return new BookHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((BookHolder)holder).textView.setText(Data.get(position));
        }

        @Override
        public int getItemCount() {
            return Data.size();
        }
    }

}
