package siltarae.mytestapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends Activity {

    boolean successLogin = false;
    Button searchBtn;
    Button loginBtn;
    Button helpBtn;
    TextView loginText;
    String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginText = (TextView) findViewById(R.id.loginTxt);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        helpBtn = (Button) findViewById(R.id.helpBtn);
    }

    public void onLoginBtnClicked(View view){
        if(loginBtn.getText().equals("로그인")) {
            Toast.makeText(getApplicationContext(), "로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else if(loginBtn.getText().equals("히스토리")){
            Toast.makeText(getApplicationContext(), "히스토리 화면", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HistoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userid", id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void onSearchBtnClicked(View view){
        Toast.makeText(getApplicationContext(), "알약 검색 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SearchByCamera.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onMapBtnClicked(View view) {
        Toast.makeText(getApplicationContext(), "지도로 이동합니다.", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, MapActivity.class);
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

   public void onHelpBtnClicked(View view) {
       Toast.makeText(getApplicationContext(), "도움말로 이동합니다.", Toast.LENGTH_SHORT).show();
       Intent intent = new Intent(this, HelpActivity.class);
       startActivity(intent);
   }

    @Override
    protected void onNewIntent(Intent intent) {
        if(intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                id = extras.getString("userid");
                successLogin = extras.getBoolean("success");
                if (successLogin == true) {
                    loginBtn.setText("히스토리");
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
