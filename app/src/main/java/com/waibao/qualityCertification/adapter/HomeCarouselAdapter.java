package com.waibao.qualityCertification.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.bean.HomeCarousel;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页viewPager的适配器
 */
public class HomeCarouselAdapter extends PagerAdapter {
    private List<HomeCarousel> picDatas;
    private Context context;
    private ArrayList<String> colors = new ArrayList<>();
    private ArrayList<String> info = new ArrayList<>();

    public HomeCarouselAdapter(List<HomeCarousel> picDatas, Context context) {
        this.picDatas = picDatas;
        this.context = context;
        colors.add("#7d909b");
        colors.add("#4a9fec");
        colors.add("#f19b59");
        info.add(" 条交易信息");
        info.add(" 个区块高度");
        info.add(" 名在线人数");
    }

    @Override
    public int getCount() {
        return picDatas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater mLi = LayoutInflater.from(context);
        View view = mLi.inflate(R.layout.viewpager, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.viewpager_line);
        linearLayout.setBackgroundColor(Color.parseColor(colors.get(position % 3)));
        ImageView imageView = (ImageView) view.findViewById(R.id.viewpager_image);
        Glide.with(context).load(picDatas.get(position).getImageView()).into(imageView);
        TextView textViewTitle = (TextView) view.findViewById(R.id.viewpager_title);
        textViewTitle.setTextSize(25);
        textViewTitle.setText(picDatas.get(position).getTitle());
        TextView textViewCount = (TextView) view.findViewById(R.id.viewpager_count);
        textViewCount.setTextSize(25);
        if (position % 3 == 2) {
            textViewCount.setText("我们共为您找到了： 7个区块链节点");
        }else {
            textViewCount.setText("我们共为您找到了： " + picDatas.get(position).getCount() + info.get(position % 3));
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    // 重载此方法解决第二个数据无法加载的问题
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}