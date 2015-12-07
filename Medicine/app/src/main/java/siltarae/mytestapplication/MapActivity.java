package siltarae.mytestapplication;

import android.content.Context;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.NMapView.OnMapViewTouchEventListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.NMapCalloutBasicOverlay;
import com.nhn.android.mapviewer.NMapPOIflagType;
import com.nhn.android.mapviewer.NMapViewerResourceProvider;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-11-16.
 */
public class MapActivity extends NMapActivity
implements OnMapStateChangeListener, OnMapViewTouchEventListener, NMapOverlayManager.OnCalloutOverlayListener {

    public static final String API_KEY = "4aa39d4a0093a9b0bb38d4cbd0e5b5fb";
    NMapView mMapView = null;
    NMapController mMapController = null;
    LinearLayout MapContainer;
    NMapViewerResourceProvider mMapViewerResourceProvider = null;
    NMapOverlayManager mOverlayManager;
    NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = null;

    LocationManager mLM;
    ArrayList<String> xList = new ArrayList<String>();
    ArrayList<String> yList = new ArrayList<String>();
    ArrayList<String> nameList = new ArrayList<String>();

    double longitude;
    double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapContainer = (LinearLayout)findViewById(R.id.map);

        // create map view
        mMapView = new NMapView(this);

// set a registered API key for Open MapViewer Library
        mMapView.setApiKey(API_KEY);

// set the activity content to the map view
        setContentView(mMapView);

// initialize map view
        mMapView.setClickable(true);

// register listener for map state changes
        mMapView.setOnMapStateChangeListener(this);
        mMapView.setOnMapViewTouchEventListener(this);

        mMapView.setBuiltInZoomControls(true, null);

// use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();

        //
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getMapInfo();
    }

    @Override
    public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
        if (errorInfo == null) { // success
            //mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);

        } else { // fail
            android.util.Log.e("NMAP", "onMapInitHandler: error=" + errorInfo.toString());
        }
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    @Override
    public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onLongPressCanceled(NMapView nMapView) {

    }

    @Override
    public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {

    }

    @Override
    public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {

    }

    public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
        // [[TEMP]] handle a click event of the callout
        Toast.makeText(MapActivity.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
    }

    public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
        if (item != null) {
            Log.i("NAMP", "onFocusChanged: " + item.toString());
        } else {
            Log.i("NMAP", "onFocusChanged: ");
        }
    }

    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0, NMapOverlayItem arg1, Rect arg2) {
        // set your callout overlay
        return new NMapCalloutBasicOverlay(arg0, arg1, arg2);
    }

    public void getMapInfo() {
        try {
            LocationListener mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    try {
                        mLM.removeUpdates(this);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        float accuracy = location.getAccuracy();
                        String url = "type=HOSPITAL_PHARMACY&boundary="+(longitude-0.02)+"%3B"+(latitude-0.01)
                                +"%3B"+(longitude+0.02)+"%3B"+(latitude+0.01)+"&pageSize=100";

                        MyHttpClient.get(url, null, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, Header[] headers, byte[] response) {
                                try {
                                    JSONObject obj = new JSONObject(new String(response));
                                    JSONObject obj2 = obj.getJSONObject("result");
                                    JSONArray arr = obj2.getJSONArray("site");

                                    for (int num = 0; num < arr.length(); num++) {
                                        JSONObject data = new JSONObject(arr.getString(num));
                                        xList.add(data.getString("x"));
                                        yList.add(data.getString("y"));
                                        nameList.add(data.getString("name"));
                                    }
                                    drawMap();
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
                        }, 1);
                    } catch(SecurityException e) {
                    }
                }

                @Override
                public void onProviderDisabled(String provider) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }
            };
            mLM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            mLM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        }
        catch(SecurityException e) {

        }
    }

    public void drawMap() {
        int markerId = NMapPOIflagType.PIN;
        int markerId2 = NMapPOIflagType.FROM;
        NMapPOIdata poiData = new NMapPOIdata (2, mMapViewerResourceProvider);
        poiData.beginPOIdata(xList.size());

        for(int num = 0; num<xList.size(); num++)
            poiData.addPOIitem(Double.parseDouble(xList.get(num)), Double.parseDouble(yList.get(num)), nameList.get(num), markerId, 0);

        poiData.addPOIitem(longitude, latitude, "내 위치", markerId2, 0);

        poiData.endPOIdata();
        NMapPOIdataOverlay poIdataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poIdataOverlay.showAllPOIdata(0);
        poIdataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        mOverlayManager.setOnCalloutOverlayListener((NMapOverlayManager.OnCalloutOverlayListener) this);
    }
}
