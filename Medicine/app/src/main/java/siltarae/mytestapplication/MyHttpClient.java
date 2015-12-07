package siltarae.mytestapplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Created by Mansu on 2015-11-28.
 */
public class MyHttpClient {
    // 163.180.116.114:3000
    private static final String[] BASE_URL = {"http://163.180.117.180:12334/", "http://map.naver.com/search2/interestSpot.nhn?"};

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static AsyncHttpClient getInstance(){
        return MyHttpClient.client;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, int type){
        client.get(getAbsoluteUrl(url, type), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, int type){
        client.setResponseTimeout(10000);
        client.post(getAbsoluteUrl(url, type), params, responseHandler);
    }

    public static void post(String url, StringEntity entity, AsyncHttpResponseHandler responseHandler, int type){
        client.setResponseTimeout(10000);
        client.post(null, getAbsoluteUrl(url, type), entity, "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl, int type){
        return BASE_URL[type] + relativeUrl;
    }
}
