package siltarae.mytestapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.*;

import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class LoginActivity extends Activity {

    String id;
    String password;
//    String correctId = "mulsy";
//    String correctPw = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AsyncHttpClient client = MyHttpClient.getInstance();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
    }

    public void loginBtnClicked(View view) {
        EditText idEditText = (EditText)findViewById(R.id.idEditText);
        EditText pwEditText = (EditText)findViewById(R.id.pwEditText);
        id = idEditText.getText().toString();
        password = pwEditText.getText().toString();

        JSONObject obj = new JSONObject();
        StringEntity entity = null;
        try {
            obj.put("userid", id);
            obj.put("password", password);
            entity = new StringEntity(obj.toString());
        } catch(JSONException e) {
        } catch(UnsupportedEncodingException e) {
        }

        MyHttpClient.post("login", entity, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                loginCheck(new String(bytes));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }, 0);
    }

    public void loginCheck(String check) {
        if(check.compareTo("Success") == 0) {
            Toast.makeText(getApplicationContext(), "로그인했습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putBoolean("success", true);
            bundle.putString("userid", id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호를 정확하게 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
