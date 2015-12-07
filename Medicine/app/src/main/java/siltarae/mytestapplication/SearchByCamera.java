package siltarae.mytestapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class SearchByCamera extends Activity {

    TextView takePicTxt;
    ImageView imageView;
    Button takePicBtn;
    Button searchBtn;
    File outputfile;
    boolean successPass = false;
    int color;
    int shape;
    String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_camera);

        imageView = (ImageView)findViewById(R.id.medicinePic);
        takePicTxt = (TextView)findViewById(R.id.takePicTxt);
        takePicBtn = (Button)findViewById(R.id.takePicBtn);
        searchBtn = (Button)findViewById(R.id.searchBtn);

        File storageDir = Environment.getExternalStorageDirectory();
        outputfile = new File(storageDir, "medicine.jpg");

        Intent intent = getIntent();
        if(intent != null) {
            Bundle extras = intent.getExtras();
            if(extras != null)
                id = (String)extras.get("userid");
        }
    }

    public void onSearchBtnClicked(View v){
        /*
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        builder.addTextBody("userid", "mansu");
//        builder.addPart("image",
//                new FileBody(new File(outputfile.getAbsolutePath())));

        HttpEntity entity = builder.build();
        HttpPost httpPost = new HttpPost("http://163.180.117.180:12334/analyze");
        httpPost.setEntity(entity);

        InputStream inputStream = null;
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            String result = stringBuilder.toString();
        }catch(IOException e) {
        }
*/

        File file = new File(outputfile.getAbsolutePath());
        byte[] fileData = new byte[(int) file.length()];
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileData);
            dis.close();
        } catch(FileNotFoundException e) {
        } catch(IOException e) {
        }
        String encodedImage = Base64.encodeToString(fileData, Base64.DEFAULT);
        JSONObject obj = new JSONObject();
        StringEntity entity = null;
        try {
            if(id != null)
                obj.put("isExist", "true");
            else
                obj.put("isExist", "false");
            obj.put("userid", id);
            obj.put("image", encodedImage);
            entity = new StringEntity(obj.toString());
        } catch(JSONException e) {
        } catch(UnsupportedEncodingException e) {
        }
        final Activity my = this;
        MyHttpClient.post("analyze", entity, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                if(new String(bytes).compareTo("fail") != 0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://terms.naver.com/" + new String(bytes)));
                    startActivity(browserIntent);
                }
                else
                    Toast.makeText(getApplicationContext(), new String(bytes), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }, 0);
    }

    public void onTakePicBtnClicked(View view){

        takePicBtn.setVisibility(View.GONE);
        searchBtn.setVisibility(View.VISIBLE);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputfile));
        startActivityForResult(intent, 1001);
    }

    protected void onStart() {
        super.onStart();
//        Intent intent = getIntent();
//        successLogin = intent.getBooleanExtra("success", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 /*       if(requestCode == 1001){
            Bitmap bitmap = BitmapFactory.decodeFile(outputfile.getAbsolutePath());

            int iWidth = 480;
            int iHeight = 832;
            float fWidth = bitmap.getWidth();
            float fHeight = bitmap.getHeight();
            if(fWidth > iWidth){
                float mWidth = (float)(fWidth/100);
                float fScale = (float)(iWidth/mWidth);
                fWidth *= (fScale/100);
                fHeight *= (fScale/100);
            } else if(fHeight>iHeight){
                float mHeight = (float)(fHeight/100);
                float fScale = (float)(iHeight/mHeight);
                fWidth *= (fScale/100);
                fHeight *= (fScale/100);
            }

            FileOutputStream fosObj = null;
            try{
                Bitmap resizeBmp = Bitmap.createScaledBitmap(bitmap, (int)fWidth, (int)fHeight, true);
                fosObj = new FileOutputStream(outputfile.getAbsolutePath());
                resizeBmp.compress(Bitmap.CompressFormat.JPEG, 100, fosObj);
            } catch(Exception e){

            }
            imageView.setImageBitmap(bitmap);
            takePicBtn.setText("알약 사진 다시 찍기");
            takePicTxt.setText("이 사진을 사용하시려면 확인을 아니면 다시 사진을 찍어주세요.");
        }
*/
        if(requestCode == 1001){
            takePicBtn.setVisibility(View.GONE);
            searchBtn.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(outputfile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            //takePicBtn.setText("알약 사진 다시 찍기");
           // takePicTxt.setText("이 사진을 사용하시려면 확인을 아니면 다시 사진을 찍어주세요.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_by_camera, menu);
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
