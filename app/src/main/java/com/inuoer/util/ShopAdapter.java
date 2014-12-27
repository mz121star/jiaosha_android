/*http://blog.csdn.net/lanjianhun/article/details/8198108*/
package com.inuoer.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.inuoer.model.Shop;
import com.inuoer.wemall.MainActivity;
import com.inuoer.wemall.R;

import java.util.List;

/**
 * Created by Administrator on 2014/12/26.
 */

    public class ShopAdapter extends BaseAdapter {
        View[] itemViews;
        private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
        private Context mContext;
    private List<Shop> shopDate;
        public ShopAdapter(Context context ,List<Shop> mlistInfo) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
            this.shopDate=mlistInfo;

            itemViews = new View[mlistInfo.size()];
            for(int i=0;i<mlistInfo.size();i++){
                Shop getInfo=(Shop)mlistInfo.get(i);    //获取第i个对象
                //调用makeItemView，实例化一个Item
                itemViews[i]=makeItemView(
                        getInfo.getShopName(), getInfo.getShopImage()
                );
            }
        }

        public int getCount() {
            return itemViews.length;
        }

        public View getItem(int position) {
            return itemViews[position];
        }

        public long getItemId(int position) {
            return position;
        }


         private View makeItemView(String strTitle, String strText ) {


                // 使用View的对象itemView与R.layout.item关联
                View itemView = mInflater.inflate(R.layout.shoplist_shop, null);

                // 通过findViewById()方法实例R.layout.item内各组件
                TextView title = (TextView) itemView.findViewById(R.id.title);
                title.setText(strTitle);    //填入相应的值
                TextView text = (TextView) itemView.findViewById(R.id.info);
                text.setText(strText);


                return itemView;
    }




    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            return itemViews[position];
        return convertView;
        }
    }


