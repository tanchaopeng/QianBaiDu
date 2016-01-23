package youmo.qianbaidu.Sub;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.app.Fragment;

import youmo.qianbaidu.Image.ImageListFragment;
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
            SubItemFragment subItemFragment = new SubItemFragment();
            Bundle b = new Bundle();
            b.putString("url",url);
            subItemFragment.setArguments(b);
            getFragmentManager().beginTransaction().addToBackStack(null);
            getFragmentManager().beginTransaction().add(R.id.sub_content_fragment,subItemFragment).commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            getFragmentManager().popBackStack();
            int i =getFragmentManager().getBackStackEntryCount();
            if (i<1)
                return super.onKeyDown(keyCode, event);
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
