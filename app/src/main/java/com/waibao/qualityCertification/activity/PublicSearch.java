package com.waibao.qualityCertification.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.interfaceMy.PermissionListener;
import com.waibao.qualityCertification.util.FileUtils;
import com.waibao.qualityCertification.util.MipcaActivityCapture;
import com.waibao.qualityCertification.util.OCRManagerUtil;
import com.waibao.qualityCertification.util.ToastUtils;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.waibao.qualityCertification.constants.IntentConstants.OPEN_SCAN_PUBLICSEARCH;
import static com.waibao.qualityCertification.constants.IntentConstants.RESULT_RECTANGLE_CAMERA;

// 公众查询
public class PublicSearch extends BaseActivity {
    private EditText certificateId;
    private EditText unitName;
    private EditText platformName;
    private Button queryCerts;
    private Button img_ocr_queryCerts;
    private Button qr_code_queryCerts;
    private TextView certificationClass;
    private TextView validityTerm;
    private LinearLayout certificationClassLine;
    private LinearLayout validityTermLine;
    private String certificateIdStr;
    private String unitNameStr;
    private String platformNameStr;
    private String publicQuery = "";
    private String certificationClassStr;
    private String validityTermStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_search);
        certificateId = (EditText) findViewById(R.id.certificateId);
        unitName = (EditText) findViewById(R.id.unitName);
        platformName = (EditText) findViewById(R.id.platformName);
        queryCerts = (Button) findViewById(R.id.queryCerts);
        img_ocr_queryCerts = (Button) findViewById(R.id.img_ocr_queryCerts);
        qr_code_queryCerts = (Button) findViewById(R.id.qr_code_queryCerts);
        certificationClass = (TextView) findViewById(R.id.certificationClass);
        validityTerm = (TextView) findViewById(R.id.validityTerm);
        certificationClassLine = (LinearLayout) findViewById(R.id.certificationClassLine);
        validityTermLine = (LinearLayout) findViewById(R.id.validityTermLine);
        queryCerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                certificateIdStr = certificateId.getText().toString().trim();
                unitNameStr = unitName.getText().toString().trim();
                platformNameStr = platformName.getText().toString().trim();
                publicQuery = "?peer=peer0.org1.example.com&fcn=publicQuery&args=[\"" + certificateIdStr + "\",\"" + unitNameStr + "\",\"" + platformNameStr + "\"]";
                if (TextUtils.isEmpty(certificateIdStr) || TextUtils.isEmpty(unitNameStr) || TextUtils.isEmpty(platformNameStr)) {
                    UiUtils.show("字段不能为空");
                } else {
                    new UserTokenTask(PublicSearch.this,
                            "UserTokenTask", "Admin", "Org1").execute();
                }
            }
        });
        qr_code_queryCerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {
                        openScan();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        dialog(PublicSearch.this, "识别二维码需要该权限，拒绝后将不能正常使用，是否重新开启此权限？");
                    }
                });
            }
        });
        img_ocr_queryCerts.setOnClickListener(new View.OnClickListener() {
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
                        dialog(PublicSearch.this, "OCR识别需要该权限，拒绝后将不能正常使用，是否重新开启此权限？");
                    }
                });
            }
        });
    }

    public void openScan() {
        //启动扫一扫
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("TAG", "PublicSearch");
        intent.setClass(PublicSearch.this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        startActivityForResult(intent, OPEN_SCAN_PUBLICSEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String data_upload_ocr_ImagePath = FileUtils.getPath(PublicSearch.this, uri);
                OCRManagerUtil.recognizeAccurateBasic(this, data_upload_ocr_ImagePath, new OCRManagerUtil.OCRCallBack<GeneralResult>() {
                    @Override
                    public void succeed(GeneralResult data) {
                        // 调用成功，返回GeneralResult对象
                        String content = OCRManagerUtil.getResult(data);
                        Log.e("ocrRes", content + "");
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            JSONArray jsonArray = jsonObject.getJSONArray("words_result");
                            int words_result_num = jsonObject.getInt("words_result_num");
                            JSONObject jsonObjectTemp = jsonArray.getJSONObject(0);
                            String stringTemp = jsonObjectTemp.getString("words").trim();
                            boolean isVaild = true;
                            if (!TextUtils.equals(stringTemp, "CCRC")) {
                                isVaild = false;
                            }
                            jsonObjectTemp = jsonArray.getJSONObject(2);
                            stringTemp = jsonObjectTemp.getString("words").trim();
                            if (!stringTemp.contains("证书编号")) {
                                isVaild = false;
                            }
                            String[] strArr = stringTemp.split("-");
                            StringBuilder stringBuilder = new StringBuilder("CCRC-ERS");
                            for (int i = 2; i < strArr.length; ++i) {
                                stringBuilder.append("-" + strArr[i]);
                            }
                            certificateIdStr=stringBuilder.toString();
//                            certificateIdStr = stringTemp.substring(stringTemp.indexOf(":") + 1).trim().toUpperCase();

                            jsonObjectTemp = jsonArray.getJSONObject(8);
                            stringTemp = jsonObjectTemp.getString("words").trim();
                            if (stringTemp.contains("V")) {
                                platformNameStr = stringTemp.substring(0, stringTemp.indexOf("V")).trim();
                            } else {
                                isVaild = false;
                                platformNameStr = stringTemp.trim();
                            }

                            jsonObjectTemp = jsonArray.getJSONObject(words_result_num - 4);
                            stringTemp = jsonObjectTemp.getString("words").trim();
                            unitNameStr = stringTemp.trim();

                            if (isVaild == false || TextUtils.isEmpty(certificateIdStr) || TextUtils.isEmpty(unitNameStr) || TextUtils.isEmpty(platformNameStr)) {
                                UiUtils.show("非法图片，请重试。");
                            } else {
                                certificateId.setText(certificateIdStr);
                                unitName.setText(unitNameStr);
                                platformName.setText(platformNameStr);
                                UiUtils.show("扫描成功");
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
            if (requestCode == OPEN_SCAN_PUBLICSEARCH) {
                String resultStr = data.getStringExtra("resultString");
                ToastUtils.showToast(PublicSearch.this, resultStr);
                try {
                    int firIndex = resultStr.indexOf("机构名称");
                    String firMidStr = resultStr.substring(0, firIndex).trim();
                    int secIndex = resultStr.indexOf("交易平台名称");
                    String secMidStr = resultStr.substring(firIndex, secIndex).trim();
                    String thirdMidStr = resultStr.substring(secIndex).trim();
                    certificateIdStr = firMidStr.substring(firMidStr.indexOf("：") + 1).trim();
                    certificateId.setText(certificateIdStr);
                    unitNameStr = secMidStr.substring(secMidStr.indexOf("：") + 1).trim();//出厂日期
                    unitName.setText(unitNameStr);
                    platformNameStr = thirdMidStr.substring(thirdMidStr.indexOf("：") + 1).trim();
                    platformName.setText(platformNameStr);
                    UiUtils.show("扫描成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    UiUtils.show("对不起，您的二维码的信息不符合我们的要求，请重试.");
                }
            }
        } else {
            UiUtils.show("未知失败，请重试");
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
                new PublicSearchTask(PublicSearch.this,
                        "PublicSearchTask", publicQuery, token).execute();
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

    public class PublicSearchTask extends BaseAsyTask {
        private String status = "true";

        public PublicSearchTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                UiUtils.show("查询成功");
                certificationClassLine.setVisibility(View.VISIBLE);
                validityTermLine.setVisibility(View.VISIBLE);
                certificationClass.setText(certificationClassStr);
                validityTerm.setText(validityTermStr);
            } else {
                UiUtils.show("查询不到该条信息：" + string);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (okHttpClient != null) {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    jsonObject = new JSONObject(string);
                    certificationClassStr = jsonObject.optString("certificationClass");
                    validityTermStr = jsonObject.optString("validityTerm");
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = "false";
            }
            return status;
        }
    }
}