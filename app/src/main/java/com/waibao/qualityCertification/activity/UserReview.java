package com.waibao.qualityCertification.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.util.GridViewUtils;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// 用户审核
public class UserReview extends BaseActivity {
    private Toolbar toolbar;
    private Spinner user_review_spinner;
    private GridView user_review_grid1;
    private GridView user_review_grid2;
    private ArrayList<String> okRevListData = new ArrayList<String>();
    private ArrayList<String> revListData = new ArrayList<String>();
    private ArrayList<String> idData = new ArrayList<>();
    private String usertype = "0";
    private String session = "0";
    private ArrayAdapter<String> user_review_adapter1;
    private ArrayAdapter<String> user_review_adapter2;
    private AlertDialog.Builder dialog;
    private int gridPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review);
        initToolbar();
        initView();
    }

    private void initView() {
        user_review_spinner = (Spinner) findViewById(R.id.user_review_spinner);
        user_review_grid1 = (GridView) findViewById(R.id.user_review_grid1);
        user_review_grid2 = (GridView) findViewById(R.id.user_review_grid2);
        if (sharedPreference != null) {
            usertype = sharedPreference.getString("usertype", "0");
            session = sharedPreference.getString("session", "0");
        }
        user_review_adapter1 = new ArrayAdapter<String>(UserReview.this,R.layout.gridview_text , okRevListData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView;
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(UserReview.this, view, okRevListData, position);
            }
        };
        user_review_adapter2 = new ArrayAdapter<String>(UserReview.this, android.R.layout.simple_list_item_1, revListData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(UserReview.this, view, revListData, position);
            }
        };
        user_review_grid1.setAdapter(user_review_adapter1);
        user_review_grid2.setAdapter(user_review_adapter2);
        user_review_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                TextView tv = (TextView)view;
                tv.setTextSize(20);
                user_review_grid1.setVisibility(View.GONE);
                user_review_grid2.setVisibility(View.GONE);
                if (TextUtils.equals(usertype, "3") && !TextUtils.equals(session, "0")) {
                    if (pos == 0) {
                        user_review_grid1.setVisibility(View.VISIBLE);
                        new OkRevListTask(UserReview.this,
                                "OkRevListTask", session).execute();
                    } else {
                        user_review_grid2.setVisibility(View.VISIBLE);
                        new RevListTask(UserReview.this,
                                "RevListTask", session).execute();
                    }
                } else {
                    UiUtils.show("对不起，您不是管理员，无权限查看。");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_user_review);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initArrayList(ArrayList<String> temp, String... parmas) {
        temp.clear();
        for (int i = 0; i < parmas.length; ++i) {
            temp.add(parmas[i]);
        }
    }

    // 已审核
    public class OkRevListTask extends BaseAsyTask {
        private String status = "true";

        public OkRevListTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                user_review_adapter1.notifyDataSetChanged();
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(UserReview.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("发生错误，请稍候重试。");
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
                    initArrayList(okRevListData, "用户名", "机构名称", "姓名", "身份证号");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        okRevListData.add(jsonObject.getString("institutionNumber"));
                        okRevListData.add(jsonObject.getString("institutionName"));
                        okRevListData.add(jsonObject.getString("username"));
                        okRevListData.add(jsonObject.getString("idCard").split(";")[0]);
                    }
                }
            } catch (Exception e) {
                status = "false";
                e.printStackTrace();
            }
            return status;
        }
    }

    // 待审核
    public class RevListTask extends BaseAsyTask {
        private String status = "true";

        public RevListTask(Context context, String string, String... params) {
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
                    initArrayList(revListData, "用户名", "身份证号", "机构编号", "机构名称", "操作");
                    initArrayList(idData, "0");
                    jsonArray = new JSONArray(string);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        revListData.add(jsonObject.getString("idCard").split(";")[1]);
                        revListData.add(jsonObject.getString("idCard").split(";")[0]);
                        revListData.add(jsonObject.getString("institutionNumber"));
                        revListData.add(jsonObject.getString("institutionName"));
                        revListData.add("同意/拒绝");
                        idData.add(jsonObject.getString("id"));
                    }
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
                user_review_adapter2.notifyDataSetChanged();
                user_review_grid2.setVisibility(View.VISIBLE);
                user_review_grid2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int gridi, long l) {
                        if (gridi > 5 && gridi % 5 == 4) {
                            gridPos = gridi;
                            final EditText reasonEdt = new EditText(UserReview.this);
                            reasonEdt.setHint("拒绝，请填写理由。同意，不需要填写。");
                            dialog = new AlertDialog.Builder(UserReview.this);
                            dialog.setMessage("是否通过该用户审核？");
                            dialog.setView(reasonEdt);
                            dialog.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // idData.get(gridi / 5)取得id
                                    new ReviewSuccessTask(UserReview.this,
                                            "ReviewSuccessTask", session, "?id=" + idData.get(gridi / 5)).execute();
                                }
                            });
                            dialog.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String remark = reasonEdt.getText().toString().trim();
                                    if (!TextUtils.isEmpty(remark)) {
                                        new ReviewFailTask(UserReview.this,
                                                "ReviewFailTask", session, "?id=" + idData.get(gridi / 5) + "&remark=" + remark).execute();
                                    } else {
                                        UiUtils.show("拒绝原因不能为空");
                                    }
                                }
                            });
                            dialog.show();
                        }
                    }
                });
            } else if (TextUtils.equals(s, "false")) {
                UiUtils.show("发生错误，请稍候重试。");
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(UserReview.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("未知错误");
            }
        }
    }

    // 审核成功
    public class ReviewSuccessTask extends BaseAsyTask {
        private String status = "-200";
        private String msg = "未知错误";

        public ReviewSuccessTask(Context context, String string, String... params) {
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
                    status = jsonObject.optString("code");
                    msg = jsonObject.optString("msg");
                }
            } catch (Exception e) {
                status = "-200";
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                UiUtils.show("审核成功。");
                for (int i = 0; i < 5; ++i) {
                    revListData.remove(gridPos--);
                }
                idData.remove(gridPos / 5);
                user_review_adapter2.notifyDataSetChanged();
            } else if (TextUtils.equals(s, "500")) {
                UiUtils.show(msg);
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(UserReview.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(UserReview.this, LoginActivity.class));
                finish();
            }
        }
    }

    //审核失败
    public class ReviewFailTask extends BaseAsyTask {
        private String status = "-200";
        private String msg = "未知错误";

        public ReviewFailTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                UiUtils.show("该用户未通过审核");
                for (int i = 0; i < 5; ++i) {
                    revListData.remove(gridPos--);
                }
                idData.remove(gridPos / 5);
                user_review_adapter2.notifyDataSetChanged();
            } else if (TextUtils.equals(s, "500")) {
                UiUtils.show(msg);
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(UserReview.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(UserReview.this, LoginActivity.class));
                finish();
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
                    jsonObject = new JSONObject(string);
                    status = jsonObject.optString("code");
                    msg = jsonObject.optString("msg");
                }
            } catch (Exception e) {
                status = "-200";
                e.printStackTrace();
            }
            return status;
        }
    }
}