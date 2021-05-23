package com.waibao.qualityCertification.activity;

import android.content.Context;
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
import android.widget.GridView;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.util.GridViewUtils;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// 机构管理
public class InstitutionReview extends BaseActivity {
    private Toolbar toolbar;
    private GridView institution_review_grid;
    private ArrayList<String> institutionReviewListData = new ArrayList<String>();
    private ArrayAdapter<String> institution_review_grid_adapter;
    private String session = "0";
    private int gridPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_review);
        initToolbar();
        initView();
    }

    private void initView() {
        institution_review_grid = (GridView) findViewById(R.id.institution_review_grid);
        institution_review_grid_adapter = new ArrayAdapter<String>(InstitutionReview.this, R.layout.gridview_text, institutionReviewListData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(InstitutionReview.this, view, institutionReviewListData, position);
            }
        };
        institution_review_grid.setAdapter(institution_review_grid_adapter);
        initArrayList(institutionReviewListData, "机构编号", "机构名", "机构类型", "当前状态", "操作");
        if (sharedPreference != null) {
            session = sharedPreference.getString("session", "0");
        }
        if (!TextUtils.equals(session, "0")) {
            new InstitutionInfoTask(InstitutionReview.this, "InstitutionReview", session).execute();
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_institution_review);
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

    // 机构信息
    public class InstitutionInfoTask extends BaseAsyTask {
        private String status = "true";

        public InstitutionInfoTask(Context context, String string, String... params) {
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
                    initArrayList(institutionReviewListData, "机构编号", "机构名", "机构类型", "当前状态", "操作");
                    jsonArray = new JSONArray(string);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        institutionReviewListData.add(jsonObject.getString("institutionNumber"));
                        institutionReviewListData.add(jsonObject.getString("institutionName"));
                        switch (jsonObject.getString("institutionType")) {
                            case "1":
                                institutionReviewListData.add("认证机构");
                                break;
                            case "2":
                                institutionReviewListData.add("检测机构");
                                break;
                            case "3":
                                institutionReviewListData.add("政府机构");
                                break;
                            default:
                                institutionReviewListData.add("未知机构");
                                break;
                        }
                        institutionReviewListData.add(TextUtils.equals("0", jsonObject.getString("isInvalid")) ? "合法" : "非法");
                        institutionReviewListData.add("变更状态");
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
                institution_review_grid_adapter.notifyDataSetChanged();
                institution_review_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int gridi, long l) {
                        gridPos = gridi;
                        if (gridi > 5 && gridi % 5 == 4) {
                            String institutionNumber = institutionReviewListData.get(gridi - 4);
                            String toBeIsValid = TextUtils.equals("合法", institutionReviewListData.get(gridi - 1)) ? "1" : "0";
//                            JSONObject jsonObject = new JSONObject();
//                            try {
//                                jsonObject.put("institution_number", institutionNumber);
//                                jsonObject.put("isInvalid", toBeIsValid);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                            new UpdateInstitutionStatusTask(InstitutionReview.this, "UpdateInstitutionStatusTask", institutionNumber, toBeIsValid).execute();
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
                startActivity(new Intent(InstitutionReview.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("未知错误");
            }
        }
    }

    // 更新机构状态
    public class UpdateInstitutionStatusTask extends BaseAsyTask {
        private String status = "-500";
        private String msg = "success";

        public UpdateInstitutionStatusTask(Context context, String string, String... params) {
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
                    if (TextUtils.equals(status, "0")) {
                        // 修改状态
                        String isValid = TextUtils.equals("合法", institutionReviewListData.get(gridPos - 1)) ? "非法" : "合法";
                        institutionReviewListData.set(gridPos - 1, isValid);
                    }
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
                UiUtils.show(msg);
                institution_review_grid_adapter.notifyDataSetChanged();
            } else if (TextUtils.equals(s, "-200")) {
                UiUtils.show("发生错误，请稍候重试。");
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(InstitutionReview.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("未知错误");
            }
        }
    }
}
