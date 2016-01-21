package Core;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tanch on 2016/1/17.
 */
public class CoreActivity extends AppCompatActivity {

    public void ShowToast(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    public String HttpGet(String url)
    {
        String result=null;
        try{
            URL _url=new URL(url);
            HttpURLConnection http=(HttpURLConnection)_url.openConnection();

            BufferedReader br=new BufferedReader(new InputStreamReader(http.getInputStream()));

            String resultLine;
            while((resultLine=br.readLine())!=null)
            {
                result+=resultLine;
            }
            http.disconnect();
        }
        catch(Exception e)
        {
            ShowToast(e.getMessage());
        }
        return result;
    }
}
