package youmo.qianbaidu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Core.CoreActivity;
import Core.MenuModel;
import Tools.OkHttpHelper;
import Tools.StringHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import youmo.qianbaidu.Sub.SubContentActivity;
import youmo.qianbaidu.Sub.SubItemFragment;

public class MainActivity extends CoreActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<MenuModel> Lmm=new ArrayList<MenuModel>();
    OkHttpHelper http=new OkHttpHelper();

    MenuAdapter ma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Lmm.add(new MenuModel("1","2","3"));
        //new FromWeb("http://1144.la");
        GetWeb("http://1144.la");

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.menu_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ma=new MenuAdapter(Lmm);

        ma.SetSub_Cilck(new MenuAdapter.Sub_Cilck() {
            @Override
            public void OnClick(View v, int i) {
                String _url=Lmm.get(i).SubUrl;
                if (_url.indexOf("tupian")!=-1)
                {
                    startActivity(new Intent(v.getContext(),SubContentActivity.class).putExtra("url",_url));
                   // startActivity(new Intent(v.getContext(),SubContentActivity.class));
                }
                else if (_url.indexOf("vodlist")!=-1)
                {

                }else if (_url.indexOf("xiaoshuo")!=-1)
                {

                }else if (_url.indexOf("xiazai")!=-1)
                {

                }else if (_url.indexOf("zaixianshipin")!=-1)
                {

                }
                else
                {
                    startActivity(new Intent(v.getContext(),SubContentActivity.class));
                    // ShowToast("无相关数据");
                }

            }
        });
        recyclerView.setAdapter(ma);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void GetWeb(String url)
    {
        http.AsynGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("信息",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<String> Lmenu=StringHelper.MidListString(response.body().string(),"<ul>","</ul>");
                for (String s:Lmenu)
                {
                    String _title=StringHelper.MidString(s,"<font color=","</font>");
                    _title= _title.substring(10);
                    List<String> _sub=StringHelper.MidListString(s,"li class=\"item has-panel\">(.*?)</li>");
                    for (int i=1;i<_sub.size()-1;i++)
                    {
                        String _url=StringHelper.MidString(_sub.get(i),"href=\"","\"");
                        if (_url.indexOf("http")<0)
                            _url=call.request().url()+"/"+_url;
                        String _name=StringHelper.MidString(_sub.get(i),"target=\"_blank\">","</a>");
                        Lmm.add(new MenuModel(_title,_name,_url));
                    }

//                   for (String _s:_sub)
//                    {
//                        String _url=StringHelper.MidString(_s,"href=\"","\"");
//                        if (_url.indexOf("http")<0)
//                            _url=call.request().url()+"/"+_url;
//                        String _name=StringHelper.MidString(_s,"target=\"_blank\">","</a>");
//                        Lmm.add(new MenuModel(_title,_name,_url));
//                    }

                }
                Log.i("信息","访问完成");
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ma.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
