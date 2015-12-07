package siltarae.mytestapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
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

/**
 * Created by Mansu on 2015-11-28.
 */
public class HistoryActivity extends Activity {
    String id = null;
    private ArrayList<String> pillNameList = new ArrayList<String>();
    private ArrayList<String> pillSrcList = new ArrayList<String>();
    private ArrayList<String> imgSrcList = new ArrayList<String>();
    private ArrayList<ImageView> imgViewList = new ArrayList<ImageView>();
    private ArrayList<TextView> txtViewList = new ArrayList<TextView>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        id = intent.getExtras().getString("userid");
        getSearchedList();
    }

    public void getSearchedList() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("userid", id);
        } catch(JSONException je) {

        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        MyHttpClient.post("searchedList", entity, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] response) {
                //Log.d("json", response);
                JSONArray arr;
                try {
                    arr = new JSONArray(new String(response));
                    for (int num = 0; num < arr.length(); num++) {
                        JSONObject obj = new JSONObject(arr.getString(num));
                        pillNameList.add(obj.getString("pillname"));
                        pillSrcList.add(obj.getString("pillsrc"));
                        imgSrcList.add(obj.getString("imgsrc"));
                    }
                    drawTable();
//                            "success", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                    Toast toast = Toast.makeText(getApplicationContext(),
                } catch (JSONException e) {

                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] response, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "fail", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }, 0);
    }

    public void drawTable() {
        TableLayout table = new TableLayout(this);

        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);

        TableRow rowTitle = new TableRow(this);

        // 제목 타이틀 바 생성
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setText("이약 뭐약");
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 6; //6만큼의 차이를둠

        rowTitle.addView(title, params); // 첫줄에 추가함.
        table.addView(rowTitle);
        //한줄에 사진 두개씩 출력할 예정임
        //나중에 포문처리해야됨.

        TableRow tr = null; // 사진 데이터 받을거
        TableRow tr2 = null; // 알약 이름 받을 거

        for (int i = 0; i < imgSrcList.size(); i++) {
            ImageView imageView = new ImageView(this);
            TextView textView = new TextView(this);

            if (i % 2 == 0) {
                tr = new TableRow(this);
                tr2 = new TableRow(this);
            }

            TableRow.LayoutParams layout = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
            layout.weight = 1;
            layout.height = 300;
            layout.topMargin = 50;
            imageView.setLayoutParams(layout);

            View.OnClickListener clickListener = new ImgListener(pillSrcList.get(i));
            imageView.setOnClickListener(clickListener);
//            imageView.setImageResource(R.mipmap.ic_launcher);

            TableRow.LayoutParams layout2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
            layout2.weight = 1;
            layout2.height = 300;
            textView.setLayoutParams(layout2);
            textView.setText(pillNameList.get(i));
            textView.setGravity(Gravity.CENTER);

            tr.addView(imageView);
            tr2.addView(textView);

            if (i % 2 == 0) {
                table.addView(tr);
                table.addView(tr2);
            }

            imgViewList.add(imageView);
            txtViewList.add(textView);
        }
        setContentView(table);
        for (int i = 0; i < imgViewList.size(); i++)
            new DownloadImageTask(imgViewList.get(i)).execute(imgSrcList.get(i));
    }

    public class ImgListener implements View.OnClickListener {
        private String url;

        public ImgListener(String _url) {
            url = _url;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("http://terms.naver.com/" + url));
            startActivity(intent);
        }
    }
}
