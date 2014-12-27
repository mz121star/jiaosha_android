package com.inuoer.fragment;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inuoer.model.Shop;
import com.inuoer.util.CartData;
import com.inuoer.util.Config;
import com.inuoer.util.HttpUtil;
import com.inuoer.util.ShopAdapter;
import com.inuoer.wemall.EditAddressActivity;
import com.inuoer.wemall.OrderActivity;
import com.inuoer.wemall.R;
import com.inuoer.wemall.SettingActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MiaoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MiaoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MiaoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;  //声明一个ListView对象
    private List<Shop> shopList = new ArrayList<Shop>();  //声明一个list，动态存储要显示的信息
    private LayoutInflater inflater;
    private FrameLayout ll;
    private String username , uid;
    private SharedPreferences sharedpreferences;
    public ProgressDialog progressDialog;
    public ArrayList<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
    private OnFragmentInteractionListener mListener;
    private ListView lv;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MiaoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MiaoFragment newInstance(String param1, String param2) {
        MiaoFragment fragment = new MiaoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

   /* public MiaoFragment() {
        // Required empty public constructor
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在加载中...");
        progressDialog.setCancelable(true);
        progressDialog.show();


    }
    public void setInfo(){

        shopList.clear();
        Shop information1 = new Shop();
        information1.setShopName("miao");

        information1.setShopImage("1.jpg");

        shopList.add(information1);
        progressDialog.dismiss();
        return;
       // new Thread(new Runnable() {

          //  @Override
           // public void run() {
                // TODO Auto-generated method stub
          /*      try {


                    String result = HttpUtil.getGetJsonContent(Config.API_GET_SHOPS);

                    if (!result.isEmpty()) {
                        JSONArray jsonObject = JSONObject.parseArray(result);
                        for (int i = 0; i < jsonObject.size(); i++) {
                            JSONObject myjObject = jsonObject.getJSONObject(i);
                            Shop information = new Shop();
                            information.setShopName(myjObject.getString("shop_name").toString());

                            information.setShopImage(myjObject.getString("shop_image").toString());
                            shopList.add(information);



                        }


                    }
                    progressDialog.dismiss();

                } catch (Exception e) {
                    // TODO: handle exception
//					System.out.println(e);
                    handler.sendEmptyMessage(0x124);
                    // System.out.println("请检查网络连接");
                }*/

           // }
       // }).start();

    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {

            }else if(msg.what == 0x124){
                Toast.makeText(getActivity(), "订单提交成功", Toast.LENGTH_SHORT).show();

            }else {

            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
/*
        ll = (FrameLayout) inflater.inflate(R.layout.fragment_miao, null);
        listView=(ListView)ll.findViewById(R.id.listView2);
        setInfo();  //给信息赋值函数，用来测试
        listView.setAdapter(new  ShopAdapter(getActivity(),shopList));*/

        //return inflater.inflate(R.layout.fragment_miao, container, false);

        this.inflater = inflater;

        ll = (FrameLayout) inflater.inflate(R.layout.fragment_miao, null);
        lv=(ListView)ll.findViewById(R.id.listView2);
        //lv = new ListView(getActivity());
        setInfo();  //给信息赋值函数，用来测试

        ShopAdapter MyAdapter = new ShopAdapter(getActivity(), shopList);// 得到一个MyAdapter对象
        lv.setAdapter(MyAdapter);


        return ll;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
