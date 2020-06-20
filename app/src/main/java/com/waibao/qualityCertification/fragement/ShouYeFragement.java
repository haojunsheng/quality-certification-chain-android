package com.waibao.qualityCertification.fragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.activity.DataReview;
import com.waibao.qualityCertification.activity.DataUpload;
import com.waibao.qualityCertification.activity.InstitutionReview;
import com.waibao.qualityCertification.activity.LoginActivity;
import com.waibao.qualityCertification.activity.PlatformMonitor;
import com.waibao.qualityCertification.activity.QueryCertificate;
import com.waibao.qualityCertification.activity.UserReview;
import com.waibao.qualityCertification.adapter.HomeCarouselAdapter;
import com.waibao.qualityCertification.adapter.MyHomeListAdapter;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.base.BaseFragement;
import com.waibao.qualityCertification.bean.HomeCarousel;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页的Fragement 上面是一个ViewPager，下面是一个ListView
 */
public class ShouYeFragement extends BaseFragement implements AdapterView.OnItemClickListener {
    private ViewPager vpHomeTitle;//图片轮播条ViewPager
    LinearLayout pointGroup;//轮播条指示点
    private View view;//整个界面的view
    private List<HomeCarousel> picDatas;//图片地址和信息的集合
    private HomeCarouselAdapter homeCarouselAdapter;
    private ListView main_list;
    protected int lastPosition = 0;//上一个页面的位置
    private boolean isRunning = false;//判断是否自动滚动
    private AuToRunTask runTask;

    private MyHomeListAdapter adapter;

    private String usertype;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragement_shouye, container, false);
        main_list = (ListView) view.findViewById(R.id.main_list);
        vpHomeTitle = (ViewPager) view.findViewById(R.id.vp_home_title);
        pointGroup = (LinearLayout) view.findViewById(R.id.point_group);
        initListView();

        if (picDatas == null) {
            //如果没有数据，就从网络中获取
            initImageUrl();
        } else {
            //如果有数据，去除掉所有的指示点，再次初始化原点坐标
            pointGroup.removeAllViews();
            initIndicator();
        }
        if (sharedPreference != null) {
            usertype = sharedPreference.getString("usertype", "0");
        }
        new UserTokenTask(getActivity(),
                "UserTokenTask", "Admin", "Org1").execute();
        return view;
    }

    @Override
    public void onPause() {
        if (runTask != null) {
            runTask.stop();
        }
        isRunning = false;
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (runTask != null) {
            runTask = null;
        }
        isRunning = false;
        super.onDestroyView();
    }

    /**
     * 初始化原点指示器
     */
    private void initIndicator() {
        UiUtils.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < picDatas.size(); i++) {
                    ImageView point = new ImageView(getActivity());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = 20;
                    params.bottomMargin = 10;
                    point.setLayoutParams(params);
                    point.setBackgroundResource(R.drawable.point_bg);
                    if (lastPosition == i) {
                        point.setEnabled(true);
                    } else {
                        point.setEnabled(false);
                    }
                    pointGroup.addView(point);
                }
                homeCarouselAdapter = new HomeCarouselAdapter(picDatas, getActivity());
                vpHomeTitle.setAdapter(homeCarouselAdapter);
                vpHomeTitle.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        position = position % picDatas.size();
                        //改变指示点的状态
                        //把当前点enbale 为true
                        pointGroup.getChildAt(position).setEnabled(true);
                        //把上一个点设为false
                        pointGroup.getChildAt(lastPosition).setEnabled(false);
                        lastPosition = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                vpHomeTitle.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                runTask.stop();
                                break;
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                runTask.start();
                                break;
                        }
                        return false;//true的话消费掉了
                    }
                });
                runTask = new AuToRunTask();
                runTask.start();
            }
        });

    }

    private void initImageUrl() {
        picDatas = new ArrayList<>();
        HomeCarousel homeCarousel;
        homeCarousel = new HomeCarousel(R.mipmap.eth, "交易信息统计", 0);
        picDatas.add(homeCarousel);
        homeCarousel = new HomeCarousel(R.mipmap.xrp, "节点状态监控", 0);
        picDatas.add(homeCarousel);
        homeCarousel = new HomeCarousel(R.mipmap.btc, "节点在线人数", 0);
        picDatas.add(homeCarousel);
        initIndicator();
    }

    private void initListView() {
        adapter = new MyHomeListAdapter(getActivity());
        main_list.setAdapter(adapter);
        main_list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                if (TextUtils.equals(usertype, "3")) {
                    startActivity(new Intent(getActivity(), UserReview.class));//用户审核
                } else {
                    UiUtils.show("对不起，您无权限进行此项操作。");
                }
                break;
            case 1:
                if (TextUtils.equals(usertype, "3")) {
                    startActivity(new Intent(getActivity(), InstitutionReview.class));//机构管理
                } else {
                    UiUtils.show("对不起，您无权限进行此项操作。");
                }
                break;
            case 2:
                startActivity(new Intent(getActivity(), QueryCertificate.class));//查询证书
                break;
            case 3:
                if (TextUtils.equals(usertype, "2")) {
                    startActivity(new Intent(getActivity(), DataReview.class));//数据审核
                } else {
                    UiUtils.show("对不起，您无权限进行此项操作。");
                }
                break;
            case 4:
                if (TextUtils.equals(usertype, "1")) {
                    startActivity(new Intent(getActivity(), DataUpload.class));//数据上传
                } else {
                    UiUtils.show("对不起，您无权限进行此项操作。");
                }
                break;
            default:
                startActivity(new Intent(getActivity(), PlatformMonitor.class));//
                break;
        }
    }

    /**
     * viewpager自动轮询的任务
     */
    public class AuToRunTask implements Runnable {

        @Override
        public void run() {
            if (isRunning) {
                int currentItem = vpHomeTitle.getCurrentItem();
                currentItem++;
                if (currentItem > picDatas.size() - 1) {
                    currentItem %= picDatas.size();
                }
                vpHomeTitle.setCurrentItem(currentItem);
                //延迟执行当前任务，递归调用
                UiUtils.postDelayed(this, 3000);
            } else {
                //如果标示变化，取消这个任务
                UiUtils.cancel(this);
            }
        }

        /**
         * 判断之前是否正在轮播，如果是，就不用再开始了
         * 如果不是，就再次开始执行任务的run方法
         */
        public void start() {
            if (!isRunning) {
                UiUtils.cancel(this);//先取消之前的任务
                isRunning = true;
                UiUtils.postDelayed(this, 3000);
            }
        }

        public void stop() {
            if (isRunning) {
                isRunning = false;
                UiUtils.cancel(this);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class UserTokenTask extends BaseAsyTask {
        private String status = "false";

        public UserTokenTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                if (!TextUtils.isEmpty(token)) {
                    new TransactionMonitorTask(getActivity(), "TransactionMonitorTask", token).execute();
                } else {
                    UiUtils.show("出现未知错误");
                }
            } else {
                UiUtils.show(getString(R.string.netWorkError));
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (okHttpClient != null) {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    jsonObject = new JSONObject(string);
                    status = jsonObject.optString("success");
                    token = jsonObject.optString("token");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }
    }

    // 交易监控
    public class TransactionMonitorTask extends BaseAsyTask {
        private String status = "true";
        private int count = 0;

        public TransactionMonitorTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                picDatas.get(0).setCount(count);
                homeCarouselAdapter.notifyDataSetChanged();
                new StatusMonitorTask(getActivity(), "StatusMonitorTask", "?peer=peer0.org1.example.com", token).execute();
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            } else {
                UiUtils.show("查询失败请稍后再试");
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (okHttpClient != null) {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    if (string.contains("\"status\":500")) {
                        status = "401";
                        return status;
                    }
                    jsonArray = new JSONArray(string);
                    count = jsonArray.length();
                }
            } catch (Exception e) {
                status = "false";
                e.printStackTrace();
            }
            return status;
        }
    }

    // 区块链状态监控
    public class StatusMonitorTask extends BaseAsyTask {
        private String status = "true";
        private String status_monitor_num_str;

        public StatusMonitorTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (okHttpClient != null) {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    if (string.contains("\"status\":500")) {
                        status = "401";
                        return status;
                    }
                    jsonObject = new JSONObject(string);
                    status_monitor_num_str = jsonObject.getJSONObject("height").getString("low");
                }
            } catch (Exception e) {
                status = "false";
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                picDatas.get(1).setCount(Integer.valueOf(status_monitor_num_str));
                homeCarouselAdapter.notifyDataSetChanged();
                new QueryAllUsersTask(getActivity(),
                        "QueryAllUsersTask", "/getAllUser").execute();
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            } else {
                UiUtils.show("查询失败请稍后再试");
            }
        }
    }

    //查询人数
    public class QueryAllUsersTask extends BaseAsyTask {
        private String status = "true";
        private int count = 0;

        public QueryAllUsersTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (okHttpClient != null) {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    jsonArray = new JSONArray(string);
                    count = jsonArray.length();
                }
            } catch (Exception e) {
                status = "false";
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                picDatas.get(2).setCount(count);
                homeCarouselAdapter.notifyDataSetChanged();
            } else {
                UiUtils.show(getString(R.string.netWorkError));
            }
        }
    }
}