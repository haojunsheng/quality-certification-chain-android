package com.waibao.qualityCertification.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.interfaceMy.PermissionListener;
import com.waibao.qualityCertification.util.FileUtils;
import com.waibao.qualityCertification.util.GridViewUtils;
import com.waibao.qualityCertification.util.MipcaActivityCapture;
import com.waibao.qualityCertification.util.OCRManagerUtil;
import com.waibao.qualityCertification.util.ToastUtils;
import com.waibao.qualityCertification.util.UiUtils;
import com.waibao.qualityCertification.view.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.waibao.qualityCertification.constants.IntentConstants.OPEN_SCAN_QUERYCERITIFICATE;
import static com.waibao.qualityCertification.constants.IntentConstants.RESULT_RECTANGLE_CAMERA;

// 证书查询
public class QueryCertificate extends BaseActivity {
    private Toolbar toolbar;
    private Spinner query_certificate_spinner;
    private GridView query_certificate_grid1;
    private MyGridView query_certificate_grid2;
    private MyGridView query_certificate_grid3;
    private MyGridView query_certificate_grid4;
    private MyGridView query_certificate_grid5;
    private MyGridView query_certificate_grid6;
    private MyGridView query_certificate_grid7;
    private LinearLayout query_certificate_linear;
    // 综合查询
    private ArrayList<String> mulQueryListData = new ArrayList<String>();
    // 条件查询
    private ArrayList<String> conListData = new ArrayList<String>();
    private ArrayList<String> conListData3 = new ArrayList<String>();
    private ArrayList<String> conListData4 = new ArrayList<String>();
    private ArrayList<String> conListData5 = new ArrayList<String>();
    private ArrayList<String> conListData6 = new ArrayList<String>();
    private ArrayList<String> conListData7 = new ArrayList<String>();
    private ArrayAdapter<String> mulQueryListAdapter;
    private ArrayAdapter<String> conQueryListAdapter;
    private ArrayAdapter<String> conQueryListAdapter3;
    private ArrayAdapter<String> conQueryListAdapter4;
    private ArrayAdapter<String> conQueryListAdapter5;
    private ArrayAdapter<String> conQueryListAdapter6;
    private ArrayAdapter<String> conQueryListAdapter7;
    private TextView query_certificate_text2;
    private TextView query_certificate_text3;
    private TextView query_certificate_text4;
    private TextView query_certificate_text5;
    private TextView query_certificate_text6;
    private TextView query_certificate_text7;
    private int position = 0;
    private String userToken = "";
    private String userSession = "";
    private ArrayList<String> nameArr = new ArrayList<>();
    private JSONArray tempJsonArray = null;
    private JSONObject tempJSONObject = null;
    private Button query_certificate_Btn;
    private Button qr_code_query_certificate_Btn;
    private Button img_ocr_query_certificate_Btn;
    private EditText query_certificate_num_Edt;
    private EditText query_certificate_unitID_Edt;
    private String query_certificate_num_Str;
    private String query_certificate_unitID_Str;
    private String data_upload_ocr_ImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_certificate);
        initToolbar();
        initView();
    }

    private void initView() {
        query_certificate_spinner = (Spinner) findViewById(R.id.query_certificate_spinner);
        query_certificate_grid1 = (GridView) findViewById(R.id.query_certificate_grid1);
        query_certificate_grid2 = (MyGridView) findViewById(R.id.query_certificate_grid2);
        query_certificate_grid3 = (MyGridView) findViewById(R.id.query_certificate_grid3);
        query_certificate_grid4 = (MyGridView) findViewById(R.id.query_certificate_grid4);
        query_certificate_grid5 = (MyGridView) findViewById(R.id.query_certificate_grid5);
        query_certificate_grid6 = (MyGridView) findViewById(R.id.query_certificate_grid6);
        query_certificate_grid7 = (MyGridView) findViewById(R.id.query_certificate_grid7);
        query_certificate_linear = (LinearLayout) findViewById(R.id.query_certificate_linear);
        query_certificate_Btn = (Button) findViewById(R.id.query_certificate_Btn);
        qr_code_query_certificate_Btn = (Button) findViewById(R.id.qr_code_query_certificate_Btn);
        img_ocr_query_certificate_Btn = (Button) findViewById(R.id.img_ocr_query_certificate_Btn);
        query_certificate_num_Edt = (EditText) findViewById(R.id.query_certificate_num_Edt);
        query_certificate_unitID_Edt = (EditText) findViewById(R.id.query_certificate_unitID_Edt);
        query_certificate_text2 = (TextView) findViewById(R.id.query_certificate_text2);
        query_certificate_text3 = (TextView) findViewById(R.id.query_certificate_text3);
        query_certificate_text4 = (TextView) findViewById(R.id.query_certificate_text4);
        query_certificate_text5 = (TextView) findViewById(R.id.query_certificate_text5);
        query_certificate_text6 = (TextView) findViewById(R.id.query_certificate_text6);
        query_certificate_text7 = (TextView) findViewById(R.id.query_certificate_text7);
        if (sharedPreference != null) {
            userSession = sharedPreference.getString("session", "");
        }
        mulQueryListAdapter = new ArrayAdapter<String>(QueryCertificate.this, android.R.layout.simple_list_item_1, mulQueryListData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(QueryCertificate.this, view, mulQueryListData, position);
            }
        };
        conQueryListAdapter = new ArrayAdapter<String>(QueryCertificate.this, android.R.layout.simple_list_item_1, conListData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(QueryCertificate.this, view, conListData, position);
            }
        };
        conQueryListAdapter3 = new ArrayAdapter<String>(QueryCertificate.this, android.R.layout.simple_list_item_1, conListData3) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(QueryCertificate.this, view, conListData3, position);
            }
        };
        conQueryListAdapter4 = new ArrayAdapter<String>(QueryCertificate.this, android.R.layout.simple_list_item_1, conListData4) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(QueryCertificate.this, view, conListData4, position);
            }
        };
        conQueryListAdapter5 = new ArrayAdapter<String>(QueryCertificate.this, android.R.layout.simple_list_item_1, conListData5) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(QueryCertificate.this, view, conListData5, position);
            }
        };
        conQueryListAdapter6 = new ArrayAdapter<String>(QueryCertificate.this, android.R.layout.simple_list_item_1, conListData6) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(QueryCertificate.this, view, conListData6, position);
            }
        };
        conQueryListAdapter7 = new ArrayAdapter<String>(QueryCertificate.this, android.R.layout.simple_list_item_1, conListData7) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(QueryCertificate.this, view, conListData7, position);
            }
        };
        query_certificate_grid1.setAdapter(mulQueryListAdapter);
        query_certificate_grid2.setAdapter(conQueryListAdapter);
        query_certificate_grid3.setAdapter(conQueryListAdapter3);
        query_certificate_grid4.setAdapter(conQueryListAdapter4);
        query_certificate_grid5.setAdapter(conQueryListAdapter5);
        query_certificate_grid6.setAdapter(conQueryListAdapter6);
        query_certificate_grid7.setAdapter(conQueryListAdapter7);
        query_certificate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                query_certificate_grid1.setVisibility(View.GONE);
                query_certificate_linear.setVerticalGravity(View.GONE);
                position = i;

                new UserTokenTask(QueryCertificate.this,
                        "UserTokenTask", "Admin", "Org1").execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        query_certificate_text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.show("恭喜您，追溯成功。");
                query_certificate_text3.setText("检测数据");
                query_certificate_grid3.setVisibility(View.VISIBLE);
                query_certificate_text4.setVisibility(View.VISIBLE);
            }
        });
        query_certificate_text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.show("恭喜您，追溯成功。");
                query_certificate_text4.setText("试运行数据");
                query_certificate_grid4.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_query_certificate);
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

    public void openScan() {
        //启动扫一扫
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("TAG", "QueryCertificate");
        intent.setClass(QueryCertificate.this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        startActivityForResult(intent, OPEN_SCAN_QUERYCERITIFICATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                data_upload_ocr_ImagePath = FileUtils.getPath(QueryCertificate.this, uri);
                OCRManagerUtil.recognizeAccurateBasic(this, data_upload_ocr_ImagePath, new OCRManagerUtil.OCRCallBack<GeneralResult>() {
                    @Override
                    public void succeed(GeneralResult data) {
                        // 调用成功，返回GeneralResult对象
                        String content = OCRManagerUtil.getResult(data);
                        Log.e("ocrRes", content + "");
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            int count = jsonObject.getInt("words_result_num");
                            for (int i = 0; i < count; ++i) {
                                JSONArray jsonArray = jsonObject.getJSONArray("words_result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String str = jsonObject1.getString("words");
                                if (str.contains("证书编号")) {
                                    query_certificate_num_Str = str.substring(str.indexOf(":") + 1).trim().toUpperCase();
                                    query_certificate_num_Edt.setText(query_certificate_num_Str);
                                    break;
                                }
                            }
                            if (TextUtils.isEmpty(query_certificate_num_Str)) {
                                UiUtils.show("非法图片，请重试。");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            UiUtils.show("非法图片，请重试。");
                        }
                    }

                    @Override
                    public void failed(OCRError error) {
                        // 调用失败，返回OCRError对象
                        Log.e("ocrErr", "错误信息：" + error.getMessage());
                    }
                });
            }
        } else if (resultCode == RESULT_RECTANGLE_CAMERA) {
            if (requestCode == OPEN_SCAN_QUERYCERITIFICATE) {
                String resultStr = data.getStringExtra("resultString");
                ToastUtils.showToast(QueryCertificate.this, resultStr);
                try {
                    int mid = resultStr.indexOf("营业执照编号：");
                    String firMidStr = resultStr.substring(0, mid).trim();
                    String secMidStr = resultStr.substring(mid).trim();
                    query_certificate_num_Str = firMidStr.substring(firMidStr.indexOf("：") + 1).trim();
                    query_certificate_num_Edt.setText(query_certificate_num_Str);
                    query_certificate_unitID_Str = secMidStr.substring(secMidStr.indexOf("：") + 1).trim();
                    query_certificate_unitID_Edt.setText(query_certificate_unitID_Str);
                    UiUtils.show("扫描成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    UiUtils.show("对不起，您的二维码的信息不符合我们的要求，请重试.");
                }
            }
        }
    }

    public class QueryAllCertsTask extends BaseAsyTask {
        private String status = "true";

        public QueryAllCertsTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (okHttpClient != null) {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    if (position == 0) {
                        jsonArray = new JSONArray(string);
                        tempJsonArray = jsonArray;
                    } else {
                        jsonObject = new JSONObject(string);
                        tempJSONObject = jsonObject;
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
                if (!TextUtils.isEmpty(userToken) && !TextUtils.isEmpty(userSession)) {
                    new GetUnitUserTask(QueryCertificate.this,
                            "GetUnitUserTask", userSession, userToken).execute();
                } else {
                    UiUtils.show("出现未知错误");
                }
            } else {
                UiUtils.show(getString(R.string.netWorkError));
            }
        }
    }

    public class GetUnitUserTask extends BaseAsyTask {
        private String status = "-200";
        private String msg = "";
        String type = "-1";
        String loginname = "";

        public GetUnitUserTask(Context context, String string, String... params) {
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
                    jsonArray = jsonObject.getJSONArray("nameList");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        nameArr.add(jsonArray.getJSONObject(i).optString("idCard").split(";")[1]);
                    }
                    if (sharedPreference != null) {
                        type = sharedPreference.getString("unittype", "-1");
                        loginname = sharedPreference.getString("loginname", "");
                    }
                    if (position == 0) {
                        // 综合查询
                        for (int i = 0; i < tempJsonArray.length(); ++i) {
                            JSONObject tempJsonObject = null;
                            tempJsonObject = tempJsonArray.getJSONObject(i);
                            String xdata = "";
                            String pID = "";
                            String pName = "";
                            JSONObject vobj = null;
                            JSONObject recordJsonObject = tempJsonObject.getJSONObject("Record");
                            String vobjJson = null;
                            if (!TextUtils.equals(recordJsonObject.optString("testDataUpload"), "null") && TextUtils.equals(type, "2")) {
                                vobjJson = recordJsonObject.getJSONObject("testDataUpload").getString("baseData");
                            }
                            if (!TextUtils.equals(recordJsonObject.optString("trialRunDataUpload"), "null") && TextUtils.equals(type, "2")) {
                                vobjJson = recordJsonObject.getJSONObject("trialRunDataUpload").getString("baseData");

                            }
                            if (!TextUtils.equals(recordJsonObject.optString("certUpload"), "null") && TextUtils.equals(type, "1")) {
                                vobjJson = recordJsonObject.getJSONObject("certUpload").getString("baseData");
                            }
                            if (vobjJson != null) {
                                vobj = new JSONObject(vobjJson);
                                xdata = vobj.optString("unitName");
                                pID = vobj.optString("postPersonID");
                                pName = vobj.optString("postPersonName");
                            }
                            int sign = 0;
                            for (int j = 0; j < nameArr.size(); ++j) {
                                if (TextUtils.equals(nameArr.get(j), pName)) {
                                    sign = 1;
                                    break;
                                }
                            }
                            if (sign == 1 || TextUtils.equals(loginname, "admin1") || TextUtils.equals(loginname, "admin2") || TextUtils.equals(loginname, "admin3")) {
                                mulQueryListData.add(tempJsonObject.optString("Key").split(",")[0]);
                                mulQueryListData.add(tempJsonObject.optString("Key").split(",")[1]);
                                mulQueryListData.add(TextUtils.isEmpty(xdata) ? "未找到" : xdata);
                                mulQueryListData.add(TextUtils.isEmpty(pID) ? "未找到" : pID);
                                mulQueryListData.add(TextUtils.isEmpty(pName) ? "未找到" : pName);
                            }
                        }
                    } else {
                        conListData.clear();
                        conListData3.clear();
                        conListData4.clear();
                        conListData5.clear();
                        conListData6.clear();
                        conListData7.clear();
                        //条件查询
                        JSONObject vobj = null;
                        String vobjJson = null;
                        // 代码中根本没检验type
//                        if (!TextUtils.equals(tempJSONObject.optString("testDataUpload"), "null") && TextUtils.equals(type, "2")) {
                        if (!TextUtils.equals(tempJSONObject.optString("testDataUpload"), "null")) {
                            vobjJson = tempJSONObject.getJSONObject("testDataUpload").getString("baseData");
                            if (isValid(vobjJson)){
                                vobj = new JSONObject(vobjJson);
                                conListData3.add("委托人单位编号");
                                conListData3.add(tempJSONObject.optString("unitID"));
                                conListData3.add("委托人单位名称");
                                conListData3.add(TextUtils.isEmpty(vobj.optString("unitName")) ? "未找到" : vobj.optString("unitName"));
                                conListData3.add("交易平台名称");
                                conListData3.add(TextUtils.isEmpty(vobj.optString("platformName")) ? "未找到" : vobj.optString("platformName"));
                                conListData3.add("交易平台版本");
                                conListData3.add(TextUtils.isEmpty(vobj.optString("edition")) ? "未找到" : vobj.optString("edition"));
                                conListData3.add("检测机构名称");
                                conListData3.add(TextUtils.isEmpty(vobj.optString("testUnitName")) ? "未找到" : vobj.optString("testUnitName"));
                                conListData3.add("检测报告结论");
                                conListData3.add(TextUtils.isEmpty(vobj.optString("conclusion")) ? "未找到" : vobj.optString("conclusion"));
                                conListData3.add("检测时间");
                                String timeTemp="未找到";
                                if(!TextUtils.isEmpty(vobj.optString("testRunTime"))){
                                    timeTemp=vobj.optString("testRunTime");
                                }else if(!TextUtils.isEmpty(vobj.optString("testTime"))){
                                    timeTemp=vobj.optString("testTime");
                                }
                                conListData3.add(timeTemp);
                                conListData3.add("检测人证件号码");
                                conListData3.add(TextUtils.isEmpty(vobj.optString("testingPersonID")) ? "未找到" : vobj.optString("testingPersonID"));
                                conListData3.add("检测人姓名");
                                conListData3.add(TextUtils.isEmpty(vobj.optString("testingPersonName")) ? "未找到" : vobj.optString("testingPersonName"));
                                conListData3.add("数据上传人证件号码");
                                conListData3.add(TextUtils.isEmpty(vobj.optString("postPersonID")) ? "未找到" : vobj.optString("postPersonID"));
                                conListData3.add("数据上传人证件姓名");
                                conListData3.add(TextUtils.isEmpty(vobj.optString("postPersonName")) ? "未找到" : vobj.optString("postPersonName"));
                            }
                        }
                        if (!TextUtils.equals(tempJSONObject.optString("trialRunDataUpload"), "null")) {
                            vobjJson = tempJSONObject.getJSONObject("trialRunDataUpload").getString("baseData");
                            if(isValid(vobjJson)){
                                vobj = new JSONObject(vobjJson);
                                conListData4.add("证书编号");
                                conListData4.add(tempJSONObject.optString("certificateID"));
                                conListData4.add("委托人单位编号");
                                conListData4.add(tempJSONObject.optString("unitID"));
                                conListData4.add("委托人单位名称");
                                conListData4.add(TextUtils.isEmpty(vobj.optString("unitName")) ? "未找到" : vobj.optString("unitName"));
                                conListData4.add("交易平台名称");
                                conListData4.add(TextUtils.isEmpty(vobj.optString("platformName")) ? "未找到" : vobj.optString("platformName"));
                                conListData4.add("交易平台版本");
                                conListData4.add(TextUtils.isEmpty(vobj.optString("edition")) ? "未找到" : vobj.optString("edition"));
                                conListData4.add("检测机构编号");
                                conListData4.add(TextUtils.isEmpty(vobj.optString("testUnitName")) ? "未找到" : vobj.optString("testUnitName"));
                                conListData4.add("检测机构名称");
                                conListData4.add(TextUtils.isEmpty(vobj.optString("testUnitName")) ? "未找到" : vobj.optString("testUnitName"));
                                conListData4.add("检测报告结论");
                                conListData4.add(TextUtils.isEmpty(vobj.optString("conclusion")) ? "未找到" : vobj.optString("conclusion"));
                                conListData4.add("试运行时间");
                                String timeTemp="未找到";
                                if(!TextUtils.isEmpty(vobj.optString("testRunTime"))){
                                    timeTemp=vobj.optString("testRunTime");
                                }else if(!TextUtils.isEmpty(vobj.optString("testTime"))){
                                    timeTemp=vobj.optString("testTime");
                                }
                                conListData4.add(timeTemp);
                                conListData4.add("检测人证件号码");
                                conListData4.add(TextUtils.isEmpty(vobj.optString("testingPersonID")) ? "未找到" : vobj.optString("testingPersonID"));
                                conListData4.add("检测人姓名");
                                conListData4.add(TextUtils.isEmpty(vobj.optString("testingPersonName")) ? "未找到" : vobj.optString("testingPersonName"));
                                conListData4.add("数据上传人证件号码");
                                conListData4.add(TextUtils.isEmpty(vobj.optString("postPersonID")) ? "未找到" : vobj.optString("postPersonID"));
                                conListData4.add("数据上传人证件姓名");
                                conListData4.add(TextUtils.isEmpty(vobj.optString("postPersonName")) ? "未找到" : vobj.optString("postPersonName"));
                            }

                        }
                        if (!TextUtils.equals(tempJSONObject.optString("certUpload"), "null")) {
                            vobjJson = tempJSONObject.getJSONObject("certUpload").getString("baseData");
                            if(isValid(vobjJson)){
                                vobj = new JSONObject(vobjJson);
                                conListData.add("证书编号");
                                conListData.add(tempJSONObject.optString("certificateID"));
                                conListData.add("获证企业单位编号");
                                conListData.add(tempJSONObject.optString("unitID"));
                                conListData.add("获证企业单位名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("unitName")) ? "未找到" : vobj.optString("unitName"));
                                conListData.add("注册地址");
                                conListData.add(TextUtils.isEmpty(vobj.optString("registerAddr")) ? "未找到" : vobj.optString("registerAddr"));
                                conListData.add("交易平台名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("platformName")) ? "未找到" : vobj.optString("platformName"));
                                conListData.add("交易平台版本");
                                conListData.add(TextUtils.isEmpty(vobj.optString("edition")) ? "未找到" : vobj.optString("edition"));
                                conListData.add("网址");
                                conListData.add(TextUtils.isEmpty(vobj.optString("website")) ? "未找到" : vobj.optString("website"));
                                conListData.add("受审核地址");
                                conListData.add(TextUtils.isEmpty(vobj.optString("auditAddr")) ? "未找到" : vobj.optString("auditAddr"));
                                conListData.add("标准和技术要求");
                                conListData.add(TextUtils.isEmpty(vobj.optString("authenticationStandard")) ? "未找到" : vobj.optString("authenticationStandard"));
                                conListData.add("认证模式");
                                conListData.add(TextUtils.isEmpty(vobj.optString("certificationMode")) ? "未找到" : vobj.optString("certificationMode"));
                                conListData.add("认证级别");
                                conListData.add(TextUtils.isEmpty(vobj.optString("certificationClass")) ? "未找到" : vobj.optString("certificationClass"));
                                conListData.add("认证结论");
                                conListData.add(TextUtils.isEmpty(vobj.optString("certificationConclusion")) ? "未找到" : vobj.optString("certificationConclusion"));
                                conListData.add("颁证日期");
                                conListData.add(TextUtils.isEmpty(vobj.optString("awardDate")) ? "未找到" : vobj.optString("awardDate"));
                                conListData.add("换证日期");
                                conListData.add(TextUtils.isEmpty(vobj.optString("replaceDate")) ? "未找到" : vobj.optString("replaceDate"));
                                conListData.add("有效期至");
                                conListData.add(TextUtils.isEmpty(vobj.optString("validityTerm")) ? "未找到" : vobj.optString("validityTerm"));
                                conListData.add("认证机构编号");
                                conListData.add(TextUtils.isEmpty(vobj.optString("certificationID")) ? "未找到" : vobj.optString("certificationID"));
                                conListData.add("认证机构名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("certificationName")) ? "未找到" : vobj.optString("certificationName"));
                                conListData.add("提交人");
                                conListData.add(TextUtils.isEmpty(vobj.optString("postPersonName")) ? "未找到" : vobj.optString("postPersonName"));
                            }
                        }
                        // 证书申请
                        if (!TextUtils.equals(tempJSONObject.optString("certApplication"), "null")) {
                            vobjJson = tempJSONObject.getJSONObject("certApplication").getString("baseData");
                            if(isValid(vobjJson)){
                                vobj = new JSONObject(vobjJson);
                                conListData5.add("获证企业单位编号");
                                conListData5.add(tempJSONObject.optString("unitID"));
                                conListData5.add("获证企业单位名称");
                                conListData5.add(TextUtils.isEmpty(vobj.optString("unitName")) ? "未找到" : vobj.optString("unitName"));
                                conListData5.add("交易平台名称");
                                conListData5.add(TextUtils.isEmpty(vobj.optString("platformName")) ? "未找到" : vobj.optString("platformName"));
                                conListData5.add("交易平台版本");
                                conListData5.add(TextUtils.isEmpty(vobj.optString("edition")) ? "未找到" : vobj.optString("edition"));
                                conListData5.add("部署场所");
                                conListData5.add(TextUtils.isEmpty(vobj.optString("deployPlace")) ? "未找到" : vobj.optString("deployPlace"));
                                conListData5.add("运营场所");
                                conListData5.add(TextUtils.isEmpty(vobj.optString("runPlace")) ? "未找到" : vobj.optString("runPlace"));
                                conListData5.add("运营情况");
                                conListData5.add(TextUtils.isEmpty(vobj.optString("runState")) ? "未找到" : vobj.optString("runState"));
                                conListData5.add("申请时间");
                                conListData5.add(TextUtils.isEmpty(vobj.optString("applyTime")) ? "未找到" : vobj.optString("applyTime"));
                                conListData5.add("经办人证件号码");
                                conListData5.add(TextUtils.isEmpty(vobj.optString("operatorID")) ? "未找到" : vobj.optString("operatorID"));
                                conListData5.add("经办人姓名");
                                conListData5.add(TextUtils.isEmpty(vobj.optString("operatorName")) ? "未找到" : vobj.optString("operatorName"));
                            }
                        }
                        // 文件审核
                        if (!TextUtils.equals(tempJSONObject.optString("docAudit"), "null")) {
                            vobjJson = tempJSONObject.getJSONObject("docAudit").getString("baseData");
                            if(isValid(vobjJson)){
                                vobj = new JSONObject(vobjJson);
                                conListData6.add("获证企业单位编号");
                                conListData6.add(tempJSONObject.optString("unitID"));
                                conListData6.add("委托人单位名称");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("unitName")) ? "未找到" : vobj.optString("unitName"));
                                conListData6.add("交易平台名称");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("platformName")) ? "未找到" : vobj.optString("platformName"));
                                conListData6.add("交易平台版本");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("edition")) ? "未找到" : vobj.optString("edition"));
                                conListData6.add("认证机构编号");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("certificationUnitID")) ? "未找到" : vobj.optString("certificationID"));
                                conListData6.add("认证机构名称");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("certificationUnitName")) ? "未找到" : vobj.optString("certificationName"));
                                conListData6.add("审核报告结论");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("conclusion")) ? "未找到" : vobj.optString("conclusion"));
                                conListData6.add("审核时间");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("auditTime")) ? "未找到" : vobj.optString("auditTime"));
                                conListData6.add("审核人证件号码");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("auditorID")) ? "未找到" : vobj.optString("auditorID"));
                                conListData6.add("审核人姓名");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("auditorName")) ? "未找到" : vobj.optString("auditorName"));
                                conListData6.add("数据上传人证件号码");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("postPersonID")) ? "未找到" : vobj.optString("postPersonID"));
                                conListData6.add("数据上传人证件姓名");
                                conListData6.add(TextUtils.isEmpty(vobj.optString("postPersonName")) ? "未找到" : vobj.optString("postPersonName"));
                            }
                        }
                        // 现场审核
                        if (!TextUtils.equals(tempJSONObject.optString("onsiteAudit"), "null")) {
                            vobjJson = tempJSONObject.getJSONObject("onsiteAudit").getString("baseData");
                            if(isValid(vobjJson)){
                                vobj = new JSONObject(vobjJson);
                                conListData7.add("获证企业单位编号");
                                conListData7.add(tempJSONObject.optString("unitID"));
                                conListData7.add("委托人单位名称");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("unitName")) ? "未找到" : vobj.optString("unitName"));
                                conListData7.add("交易平台名称");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("platformName")) ? "未找到" : vobj.optString("platformName"));
                                conListData7.add("交易平台版本");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("edition")) ? "未找到" : vobj.optString("edition"));
                                conListData7.add("认证机构编号");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("certificationUnitID")) ? "未找到" : vobj.optString("certificationID"));
                                conListData7.add("认证机构名称");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("certificationUnitName")) ? "未找到" : vobj.optString("certificationName"));
                                conListData7.add("审核报告结论");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("conclusion")) ? "未找到" : vobj.optString("conclusion"));
                                conListData7.add("审核时间");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("auditTime")) ? "未找到" : vobj.optString("auditTime"));
                                conListData7.add("审核人证件号码");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("auditorID")) ? "未找到" : vobj.optString("auditorID"));
                                conListData7.add("审核人姓名");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("auditorName")) ? "未找到" : vobj.optString("auditorName"));
                                conListData7.add("数据上传人证件号码");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("postPersonID")) ? "未找到" : vobj.optString("postPersonID"));
                                conListData7.add("数据上传人证件姓名");
                                conListData7.add(TextUtils.isEmpty(vobj.optString("postPersonName")) ? "未找到" : vobj.optString("postPersonName"));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                if (position == 0) {
                    mulQueryListAdapter.notifyDataSetChanged();
                } else {
                    conQueryListAdapter.notifyDataSetChanged();
                    conQueryListAdapter3.notifyDataSetChanged();
                    conQueryListAdapter4.notifyDataSetChanged();
                    conQueryListAdapter5.notifyDataSetChanged();
                    conQueryListAdapter6.notifyDataSetChanged();
                    conQueryListAdapter7.notifyDataSetChanged();

                    query_certificate_text2.setVisibility(View.VISIBLE);
                    query_certificate_grid2.setVisibility(View.VISIBLE);
                    query_certificate_text3.setVisibility(View.VISIBLE);
                    query_certificate_text5.setVisibility(View.VISIBLE);
                    query_certificate_grid5.setVisibility(View.VISIBLE);
                    query_certificate_text7.setVisibility(View.VISIBLE);
                }
            } else if (TextUtils.equals(s, "500")) {
                UiUtils.show(msg);
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(QueryCertificate.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show(getString(R.string.netWorkError));
            }
        }

        boolean isValid(String vobjJson) {
            String pName = "";
            int sign = 0;
            if (vobjJson != null) {
                JSONObject vobj = null;
                try {
                    vobj = new JSONObject(vobjJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pName = vobj.optString("postPersonName");
            }
            for (int j = 0; j < nameArr.size(); ++j) {
                if (TextUtils.equals(nameArr.get(j), pName)) {
                    sign = 1;
                    break;
                }
            }
            if (sign == 1 || TextUtils.equals(loginname, "admin1") || TextUtils.equals(loginname, "admin2") || TextUtils.equals(loginname, "admin3")) {
                return true;
            }
            return false;
        }
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
                    if (position == 0) {
                        initArrayList(mulQueryListData, "证书编号", "营业执照编号", "获证企业名称", "提交人身份证号", "提交人姓名");
                        query_certificate_grid1.setVisibility(View.VISIBLE);
                        new QueryAllCertsTask(QueryCertificate.this,
                                "QueryAllCertsTask", "?peer=peer0.org1.example.com&fcn=queryAllCerts&args=['']", token).execute();
                    } else {
                        query_certificate_linear.setVisibility(View.VISIBLE);
                        qr_code_query_certificate_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                                    @Override
                                    public void onGranted() {
                                        openScan();
                                    }

                                    @Override
                                    public void onDenied(List<String> deniedPermission) {
                                        dialog(QueryCertificate.this, "识别二维码需要该权限，拒绝后将不能正常使用，是否重新开启此权限？");
                                    }
                                });
                            }
                        });
                        img_ocr_query_certificate_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                                    @Override
                                    public void onGranted() {
                                        Intent imageIntent = new Intent();
                                        imageIntent.setType("image/*");
                                        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(imageIntent, 1);
                                    }

                                    @Override
                                    public void onDenied(List<String> deniedPermission) {
                                        dialog(QueryCertificate.this, "OCR识别需要该权限，拒绝后将不能正常使用，是否重新开启此权限？");
                                    }
                                });
                            }
                        });

                        query_certificate_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                query_certificate_num_Str = query_certificate_num_Edt.getText().toString().trim();
                                query_certificate_unitID_Str = query_certificate_unitID_Edt.getText().toString().trim();
                                if (!TextUtils.isEmpty(query_certificate_num_Str) && !TextUtils.isEmpty(query_certificate_unitID_Str)) {
                                    new QueryAllCertsTask(QueryCertificate.this,
                                            "QueryAllCertsTask",
                                            "?peer=peer0.org1.example.com&fcn=queryCert&args=[\"" + query_certificate_num_Str + "\",\"" + query_certificate_unitID_Str + "\"]", token).execute();
                                } else {
                                    UiUtils.show("输入不能为空");

                                }
                            }
                        });
                    }
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
                    userToken = token;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }
    }
}