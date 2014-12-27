package com.inuoer.wemall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inuoer.fragment.CartFragment;
import com.inuoer.fragment.MainFragment;
import com.inuoer.fragment.MiaoFragment;
import com.inuoer.fragment.WoFragment;
import com.inuoer.util.CartData;
import com.inuoer.util.Config;
import com.inuoer.util.HttpUtil;
import com.inuoer.util.MainAdapter;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;
import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;
import com.baidu.location.GeofenceClient.OnRemoveBDGeofencesResultListener;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements OnClickListener{
	public String jsonString;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	public TextView title;
    public Button currentLocation;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	public List<RadioButton> Tabs = new ArrayList<RadioButton>();
	public ArrayList<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
	public MainAdapter MyAdapter;
	public ProgressDialog progressDialog;
//	public LinearLayout popmenull;

    public LocationClient locationClient = null;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				MyAdapter.notifyDataSetChanged();
			} else if (msg.what == 0x124) {
				Toast.makeText(MainActivity.this, "请检查网络连接!", Toast.LENGTH_LONG)
						.show();
			}
		}
	};

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        RelativeLayout actionbar = (RelativeLayout) this.getLayoutInflater()
                .inflate(R.layout.fragment_actionbar, null);
        title = (TextView) actionbar
                .findViewById(R.id.fragment_actionbar_title);
//		popmenull = (LinearLayout) actionbar.findViewById(R.id.fragment_actionbar_menu);
//		popmenull.setOnClickListener(this);
        currentLocation = (Button) actionbar.findViewById(R.id.current_location);

        LinearLayout ll = (LinearLayout) findViewById(R.id.fragment_actionbar_linearlayout);
        ll.addView(actionbar);


        MyAdapter = new MainAdapter(this, listItem);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在加载中...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        initData();

        Tabs.add((RadioButton) findViewById(R.id.shop));
        Tabs.add((RadioButton) findViewById(R.id.cart));
        Tabs.add((RadioButton) findViewById(R.id.wode));

        Tabs.get(0).setOnClickListener(this);
        Tabs.get(1).setOnClickListener(this);
        Tabs.get(2).setOnClickListener(this);

        fragments.add(new MainFragment(listItem, MyAdapter));
        fragments.add(new CartFragment());
        fragments.add(new WoFragment());
        fragments.add(new MiaoFragment());

        fragmentManager = getSupportFragmentManager();
        Tabs.get(0).callOnClick();

        findViewById(R.id.current_location).setOnClickListener(this);
        locationClient = new LocationClient(this);
        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //是否打开GPS
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。

        option.setProdName("jiaoshawang"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(2000);    //设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);

        //注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {

                if (location == null) {
                    return;
                }

                currentLocation.setText(location.getAddrStr());
            }

            public void onReceivePoi(BDLocation location) {
            }

        });
        locationClient.start();
        if (locationClient != null )
            locationClient.requestLocation();
        else
            Toast.makeText(MainActivity.this, "尚未发起定位!", Toast.LENGTH_LONG)
                    .show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shop:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.framelayout_content, fragments.get(0));
			title.setText("叫啥网");
			fragmentTransaction.commit();

			break;
		case R.id.cart:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.framelayout_content, fragments.get(1));
			title.setText("提交订单");
			fragmentTransaction.commit();
			
			break;
		case R.id.wode:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.framelayout_content, fragments.get(2));
			title.setText("我的");
			fragmentTransaction.commit();
			
			break;
       case R.id.current_location:
           fragmentTransaction = fragmentManager.beginTransaction();
           fragmentTransaction.replace(R.id.framelayout_content, fragments.get(3));
           title.setText("map");
           fragmentTransaction.commit();
           break;
//		case R.id.fragment_actionbar_menu:
//			Toast.makeText(this, "11111111", Toast.LENGTH_SHORT).show();
//			
//			break;
		}
	}
	
	public void initData() {
		new Thread(new Runnable() {
			public void run() {
				try {
					jsonString = HttpUtil
							.getGetJsonContent(Config.API_GET_GOODS);
					
					JSONArray jsonArr = JSON.parseArray(jsonString);
					for (int i = 0; i < jsonArr.size(); i++) {
						// 获取每一个JsonObject对象
						JSONObject myjObject = jsonArr.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", i);
						//new GetBitmapUtil().getBitmapByUrl(Config.API_UPLOADS+myjObject.getString("image"));
						map.put("image", Config.API_UPLOADS+myjObject.getString("image"));
						map.put("name", myjObject.getString("name"));
						map.put("price", myjObject.getString("price"));
						map.put("num", CartData.findCart(i));
						listItem.add(map);
					}
//
					handler.sendEmptyMessage(0x123);
					progressDialog.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
//					System.out.println(e);
					handler.sendEmptyMessage(0x124);
					// System.out.println("请检查网络连接");
				}

			}
		}).start();

	}
}
