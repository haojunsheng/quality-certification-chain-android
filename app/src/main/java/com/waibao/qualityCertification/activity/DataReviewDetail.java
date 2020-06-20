package com.waibao.qualityCertification.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.constants.URLConstants;
import com.waibao.qualityCertification.util.IDCardUtils;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.waibao.qualityCertification.constants.URLConstants.ServerURL;

public class DataReviewDetail extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    // 检测检验数据审核
    private LinearLayout data_review_detail_jiance_line;
    private TextView data_review_detail_jiance_certificateID;
    private TextView data_review_detail_jiance_unitID;
    private TextView data_review_detail_jiance_unitName;
    private TextView data_review_detail_jiance_platformName;
    private TextView data_review_detail_jiance_edition;
    private TextView data_review_detail_jiance_testUnitID;
    private TextView data_review_detail_jiance_testUnitName;
    private TextView data_review_detail_jiance_conclusion;
    private TextView data_review_detail_jiance_testTime_text;
    private TextView data_review_detail_jiance_testTime;
    private TextView data_review_detail_jiance_testingPersonID;
    private TextView data_review_detail_jiance_testingPersonName;
    private TextView data_review_detail_jiance_postPersonID;
    private TextView data_review_detail_jiance_postPersonName;
    private TextView data_review_detail_jiance_checkPersonID;
    private TextView data_review_detail_jiance_checkPersonName;
    private Button data_review_detail_jiance_pass_btn;
    private Button data_review_detail_jiance_reject_btn;
    private String certificateID = "0";
    private String unitID = "0";
    private String unitName = "0";
    private String platformName = "0";
    private String edition = "0";
    private String testUnitID = "0";
    private String testUnitName = "0";
    private String conclusion = "0";
    private String testTime = "0";
    private String testingPersonID = "0";
    private String testingPersonName = "0";
    private String postPersonID = "0";
    private String postPersonName = "0";
    private String deployPlace = "0";
    private String runPlace = "0";
    private String runState = "0";
    private String applyTime = "0";
    private String operatorID;
    private String operatorName = "0";
    private String applicationHash = "0";
    private String legalFileHash = "0";
    private String image1URL = "";
    private String image2URL = "";
    private String hashSummary = "";
    // 证书申请资料审核
    private LinearLayout data_review_detail_applyData_line;
    private TextView data_review_detail_applyData_unitID;
    private TextView data_review_detail_applyData_applicationPrintFileID;
    private TextView data_review_detail_applyData_verificationPrintFileID;
    private ImageView data_review_detail_applyData_applyimg;
    private ImageView data_review_detail_applyData_legalimg;
    private TextView data_review_detail_applyData_unitName;
    private TextView data_review_detail_applyData_platformName;
    private TextView data_review_detail_applyData_edition;
    private TextView data_review_detail_applyData_deployPlace;
    private TextView data_review_detail_applyData_runPlace;
    private TextView data_review_detail_applyData_runState;
    private TextView data_review_detail_applyData_applyTime;
    private TextView data_review_detail_applyData_operatorID;
    private TextView data_review_detail_applyData_operatorName;
    private TextView data_review_detail_applyData_checkPersonID;
    private TextView data_review_detail_applyData_checkPersonName;
    private Button data_review_detail_applyData_pass_btn;
    private Button data_review_detail_applyData_reject_btn;
    // 文件审核和现场资料审核
    private LinearLayout data_review_detail_fileOnsite_line;
    private TextView data_review_detail_fileOnsite_unitID;
    private TextView data_review_detail_fileOnsite_unitName;
    private TextView data_review_detail_fileOnsite_platformName;
    private TextView data_review_detail_fileOnsite_edition;
    private TextView data_review_detail_fileOnsite_certificationUnitID;
    private TextView data_review_detail_fileOnsite_certificationUnitName;
    private TextView data_review_detail_fileOnsite_conclusion;
    private TextView data_review_detail_fileOnsite_auditTime;
    private TextView data_review_detail_fileOnsite_auditorID;
    private TextView data_review_detail_fileOnsite_auditorName;
    private TextView data_review_detail_fileOnsite_postPersonID;
    private TextView data_review_detail_fileOnsite_postPersonName;
    private TextView data_review_detail_fileOnsite_checkPersonID;
    private TextView data_review_detail_fileOnsite_checkPersonName;
    private Button data_review_detail_fileOnsite_pass_btn;
    private Button data_review_detail_fileOnsite_reject_btn;
    private String auditorID = "0";
    private String auditorName = "0";
    private String auditTime = "0";
    private String certificationUnitName = "0";
    private String certificationUnitID = "0";
    // 证书数据审核
    private LinearLayout data_review_detail_upData_line;
    private TextView data_review_detail_upData_certificateID;
    private TextView data_review_detail_upData_unitID;
    private TextView data_review_detail_upData_unitName;
    private TextView data_review_detail_upData_registerAddr;
    private TextView data_review_detail_upData_platformName;
    private TextView data_review_detail_upData_edition;
    private TextView data_review_detail_upData_website;
    private TextView data_review_detail_upData_auditAddr;
    private TextView data_review_detail_upData_authenticationStandard;
    private TextView data_review_detail_upData_certificationMode;
    private TextView data_review_detail_upData_certificationConclusion;
    private TextView data_review_detail_upData_certificationClass;
    private TextView data_review_detail_upData_awardDate;
    private TextView data_review_detail_upData_replaceDate;
    private TextView data_review_detail_upData_validityTerm;
    private TextView data_review_detail_upData_certificationID;
    private TextView data_review_detail_upData_certificationName;
    private TextView data_review_detail_upData_certificateScanHASH;
    private ImageView data_review_detail_upData_certificateimg;
    private TextView data_review_detail_upData_postPersonID;
    private TextView data_review_detail_upData_postPersonName;
    private TextView data_review_detail_upData_checkPersonID;
    private TextView data_review_detail_upData_checkPersonName;
    private Button data_review_detail_upData_pass_btn;
    private Button data_review_detail_upData_reject_btn;
    private String registerAddr;
    private String website;
    private String auditAddr;
    private String certificationID;
    private String authenticationStandard;
    private String certificationMode;
    private String certificationConclusion;
    private String certificationClass;
    private String awardDate;
    private String replaceDate;
    private String validityTerm;
    private String certificationName;
    private String certificateScanHASH;

    private int urlId;
    private String url;
    private int pos1;
    private int pos2;

    private String checkPersonID = "0";
    private String checkPersonName = "0";
    private String blockToken;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_review_detail);
        initToolbar();
        urlId = getIntent().getIntExtra("id", 0);
        pos1 = getIntent().getIntExtra("pos1", 0);
        pos2 = getIntent().getIntExtra("pos2", 0);
        if (sharedPreference != null) {
            String tempId = sharedPreference.getString("userid", "0");
            blockToken = sharedPreference.getString("token", "0");
            if (!TextUtils.equals(tempId, "0")) {
                checkPersonID = tempId.split(";")[0];
                checkPersonName = tempId.split(";")[1];
            }
        }
        if (pos1 == 0) {
            url = "/getApplyDatadetail";
            if (pos2 == 0) {
                initApplyDataView();
            } else if (pos2 == 1) {
                url = "/getFileDatadetail";
                initFileOnsiteView();
            } else if (pos2 == 2) {
                url = "/getOnsiteDatadetail";
                initFileOnsiteView();
            } else {
                url = "/getDatadetail";
                initUpDataView();
            }
        } else {
            url = "/getDataDetails";
            initJianceView();
        }
        new DataDetailsTask(DataReviewDetail.this,
                "DataDetailsTask", url, "?id=" + urlId).execute();
    }

    private void initFileOnsiteView() {
        data_review_detail_fileOnsite_line = (LinearLayout) findViewById(R.id.data_review_detail_fileOnsite_line);
        data_review_detail_fileOnsite_unitID = (TextView) findViewById(R.id.data_review_detail_fileOnsite_unitID);
        data_review_detail_fileOnsite_unitName = (TextView) findViewById(R.id.data_review_detail_fileOnsite_unitName);
        data_review_detail_fileOnsite_platformName = (TextView) findViewById(R.id.data_review_detail_fileOnsite_platformName);
        data_review_detail_fileOnsite_edition = (TextView) findViewById(R.id.data_review_detail_fileOnsite_edition);
        data_review_detail_fileOnsite_certificationUnitID = (TextView) findViewById(R.id.data_review_detail_fileOnsite_certificationUnitID);
        data_review_detail_fileOnsite_certificationUnitName = (TextView) findViewById(R.id.data_review_detail_fileOnsite_certificationUnitName);
        data_review_detail_fileOnsite_conclusion = (TextView) findViewById(R.id.data_review_detail_fileOnsite_conclusion);
        data_review_detail_fileOnsite_auditTime = (TextView) findViewById(R.id.data_review_detail_fileOnsite_auditTime);
        data_review_detail_fileOnsite_auditorID = (TextView) findViewById(R.id.data_review_detail_fileOnsite_auditorID);
        data_review_detail_fileOnsite_auditorName = (TextView) findViewById(R.id.data_review_detail_fileOnsite_auditorName);
        data_review_detail_fileOnsite_postPersonID = (TextView) findViewById(R.id.data_review_detail_fileOnsite_postPersonID);
        data_review_detail_fileOnsite_postPersonName = (TextView) findViewById(R.id.data_review_detail_fileOnsite_postPersonName);
        data_review_detail_fileOnsite_checkPersonID = (TextView) findViewById(R.id.data_review_detail_fileOnsite_checkPersonID);
        data_review_detail_fileOnsite_checkPersonName = (TextView) findViewById(R.id.data_review_detail_fileOnsite_checkPersonName);
        data_review_detail_fileOnsite_pass_btn = (Button) findViewById(R.id.data_review_detail_fileOnsite_pass_btn);
        data_review_detail_fileOnsite_pass_btn.setOnClickListener(this);
        data_review_detail_fileOnsite_reject_btn = (Button) findViewById(R.id.data_review_detail_fileOnsite_reject_btn);
        data_review_detail_fileOnsite_reject_btn.setOnClickListener(this);
    }

    private void initJianceView() {
        data_review_detail_jiance_line = (LinearLayout) findViewById(R.id.data_review_detail_jiance_line);
        data_review_detail_jiance_certificateID = (TextView) findViewById(R.id.data_review_detail_jiance_certificateID);
        data_review_detail_jiance_unitID = (TextView) findViewById(R.id.data_review_detail_jiance_unitID);
        data_review_detail_jiance_unitName = (TextView) findViewById(R.id.data_review_detail_jiance_unitName);
        data_review_detail_jiance_platformName = (TextView) findViewById(R.id.data_review_detail_jiance_platformName);
        data_review_detail_jiance_edition = (TextView) findViewById(R.id.data_review_detail_jiance_edition);
        data_review_detail_jiance_testUnitID = (TextView) findViewById(R.id.data_review_detail_jiance_testUnitID);
        data_review_detail_jiance_testUnitName = (TextView) findViewById(R.id.data_review_detail_jiance_testUnitName);
        data_review_detail_jiance_conclusion = (TextView) findViewById(R.id.data_review_detail_jiance_conclusion);
        data_review_detail_jiance_testTime_text = (TextView) findViewById(R.id.data_review_detail_jiance_testTime_text);
        data_review_detail_jiance_testTime = (TextView) findViewById(R.id.data_review_detail_jiance_testTime);
        data_review_detail_jiance_testingPersonID = (TextView) findViewById(R.id.data_review_detail_jiance_testingPersonID);
        data_review_detail_jiance_testingPersonName = (TextView) findViewById(R.id.data_review_detail_jiance_testingPersonName);
        data_review_detail_jiance_postPersonID = (TextView) findViewById(R.id.data_review_detail_jiance_postPersonID);
        data_review_detail_jiance_postPersonName = (TextView) findViewById(R.id.data_review_detail_jiance_postPersonName);
        data_review_detail_jiance_checkPersonID = (TextView) findViewById(R.id.data_review_detail_jiance_checkPersonID);
        data_review_detail_jiance_checkPersonName = (TextView) findViewById(R.id.data_review_detail_jiance_checkPersonName);
        data_review_detail_jiance_pass_btn = (Button) findViewById(R.id.data_review_detail_jiance_pass_btn);
        data_review_detail_jiance_pass_btn.setOnClickListener(this);
        data_review_detail_jiance_reject_btn = (Button) findViewById(R.id.data_review_detail_jiance_reject_btn);
        data_review_detail_jiance_reject_btn.setOnClickListener(this);
    }

    private void initApplyDataView() {
        data_review_detail_applyData_line = (LinearLayout) findViewById(R.id.data_review_detail_applyData_line);
        data_review_detail_applyData_unitID = (TextView) findViewById(R.id.data_review_detail_applyData_unitID);
        data_review_detail_applyData_applicationPrintFileID = (TextView) findViewById(R.id.data_review_detail_applyData_applicationPrintFileID);
        data_review_detail_applyData_verificationPrintFileID = (TextView) findViewById(R.id.data_review_detail_applyData_verificationPrintFileID);
        data_review_detail_applyData_applyimg = (ImageView) findViewById(R.id.data_review_detail_applyData_applyimg);
        data_review_detail_applyData_legalimg = (ImageView) findViewById(R.id.data_review_detail_applyData_legalimg);
        data_review_detail_applyData_unitName = (TextView) findViewById(R.id.data_review_detail_applyData_unitName);
        data_review_detail_applyData_platformName = (TextView) findViewById(R.id.data_review_detail_applyData_platformName);
        data_review_detail_applyData_edition = (TextView) findViewById(R.id.data_review_detail_applyData_edition);
        data_review_detail_applyData_deployPlace = (TextView) findViewById(R.id.data_review_detail_applyData_deployPlace);
        data_review_detail_applyData_runPlace = (TextView) findViewById(R.id.data_review_detail_applyData_runPlace);
        data_review_detail_applyData_runState = (TextView) findViewById(R.id.data_review_detail_applyData_runState);
        data_review_detail_applyData_applyTime = (TextView) findViewById(R.id.data_review_detail_applyData_applyTime);
        data_review_detail_applyData_operatorID = (TextView) findViewById(R.id.data_review_detail_applyData_operatorID);
        data_review_detail_applyData_operatorName = (TextView) findViewById(R.id.data_review_detail_applyData_operatorName);
        data_review_detail_applyData_checkPersonID = (TextView) findViewById(R.id.data_review_detail_applyData_checkPersonID);
        data_review_detail_applyData_checkPersonName = (TextView) findViewById(R.id.data_review_detail_applyData_checkPersonName);
        data_review_detail_applyData_pass_btn = (Button) findViewById(R.id.data_review_detail_applyData_pass_btn);
        data_review_detail_applyData_pass_btn.setOnClickListener(this);
        data_review_detail_applyData_reject_btn = (Button) findViewById(R.id.data_review_detail_applyData_reject_btn);
        data_review_detail_applyData_reject_btn.setOnClickListener(this);
    }

    private void initUpDataView() {
        data_review_detail_upData_line = (LinearLayout) findViewById(R.id.data_review_detail_upData_line);
        data_review_detail_upData_certificateID = (TextView) findViewById(R.id.data_review_detail_upData_certificateID);
        data_review_detail_upData_unitID = (TextView) findViewById(R.id.data_review_detail_upData_unitID);
        data_review_detail_upData_unitName = (TextView) findViewById(R.id.data_review_detail_upData_unitName);
        data_review_detail_upData_registerAddr = (TextView) findViewById(R.id.data_review_detail_upData_registerAddr);
        data_review_detail_upData_platformName = (TextView) findViewById(R.id.data_review_detail_upData_platformName);
        data_review_detail_upData_edition = (TextView) findViewById(R.id.data_review_detail_upData_edition);
        data_review_detail_upData_website = (TextView) findViewById(R.id.data_review_detail_upData_website);
        data_review_detail_upData_auditAddr = (TextView) findViewById(R.id.data_review_detail_upData_auditAddr);
        data_review_detail_upData_authenticationStandard = (TextView) findViewById(R.id.data_review_detail_upData_authenticationStandard);
        data_review_detail_upData_certificationMode = (TextView) findViewById(R.id.data_review_detail_upData_certificationMode);
        data_review_detail_upData_certificationConclusion = (TextView) findViewById(R.id.data_review_detail_upData_certificationConclusion);
        data_review_detail_upData_certificationClass = (TextView) findViewById(R.id.data_review_detail_upData_certificationClass);
        data_review_detail_upData_awardDate = (TextView) findViewById(R.id.data_review_detail_upData_awardDate);
        data_review_detail_upData_replaceDate = (TextView) findViewById(R.id.data_review_detail_upData_replaceDate);
        data_review_detail_upData_validityTerm = (TextView) findViewById(R.id.data_review_detail_upData_validityTerm);
        data_review_detail_upData_certificationID = (TextView) findViewById(R.id.data_review_detail_upData_certificationID);
        data_review_detail_upData_certificationName = (TextView) findViewById(R.id.data_review_detail_upData_certificationName);
        data_review_detail_upData_certificateScanHASH = (TextView) findViewById(R.id.data_review_detail_upData_certificateScanHASH);
        data_review_detail_upData_certificateimg = (ImageView) findViewById(R.id.data_review_detail_upData_certificateimg);
        data_review_detail_upData_postPersonID = (TextView) findViewById(R.id.data_review_detail_upData_postPersonID);
        data_review_detail_upData_postPersonName = (TextView) findViewById(R.id.data_review_detail_upData_postPersonName);
        data_review_detail_upData_checkPersonID = (TextView) findViewById(R.id.data_review_detail_upData_checkPersonID);
        data_review_detail_upData_checkPersonName = (TextView) findViewById(R.id.data_review_detail_upData_checkPersonName);
        data_review_detail_upData_pass_btn = (Button) findViewById(R.id.data_review_detail_upData_pass_btn);
        data_review_detail_upData_pass_btn.setOnClickListener(this);
        data_review_detail_upData_reject_btn = (Button) findViewById(R.id.data_review_detail_upData_reject_btn);
        data_review_detail_upData_reject_btn.setOnClickListener(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_data_review_detail);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onClick(View view) {
        JSONObject checkdata;
        JSONObject basedata;
        JSONObject data;
        JSONArray peersJSONArray;
        JSONArray argsJSONArray;
        switch (view.getId()) {
            case R.id.data_review_detail_jiance_pass_btn:
                if (!isValidInput(2, testingPersonID, postPersonID, certificateID, unitID, unitName, platformName, edition, testUnitID, testUnitName, conclusion, testTime, testingPersonName, postPersonName)) {
                    UiUtils.show("对不起，您的输入不能为空。");
                } else {
                    basedata = new JSONObject();
                    data = new JSONObject();
                    try {
                        basedata.put("unitName", unitName);
                        basedata.put("platformName", platformName);
                        basedata.put("edition", edition);
                        basedata.put("testUnitID", testUnitID);
                        basedata.put("testUnitName", testUnitName);
                        basedata.put("conclusion", conclusion);
                        if (pos2 == 0) {
                            basedata.put("testTime", testTime);
                        } else {
                            basedata.put("testRunTime", testTime);
                        }
                        basedata.put("testingPersonID", testingPersonID);
                        basedata.put("testingPersonName", testingPersonName);
                        basedata.put("postPersonID", postPersonID);
                        basedata.put("postPersonName", postPersonName);
                        peersJSONArray = new JSONArray();
                        peersJSONArray.put("peer0.org1.example.com").put("peer0.org2.example.com").put("peer0.org3.example.com");
                        data.put("peers", peersJSONArray);
                        data.put("fcn", "testDataUpload");
                        argsJSONArray = new JSONArray();
                        argsJSONArray.put(certificateID).put(unitID).put(basedata.toString()).put("");
                        data.put("args", argsJSONArray);
                        new CheckTransactionTask(DataReviewDetail.this,
                                "CheckTransactionTask", data.toString(), blockToken).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // 同意证书申请审核
            case R.id.data_review_detail_applyData_pass_btn:
                if (!isValidInput(1, operatorID, unitID, unitName, platformName, edition, deployPlace, runPlace, runState, applyTime, operatorID, operatorName, applicationHash, legalFileHash)) {
                    UiUtils.show("对不起，您的输入不能为空。");
                } else {
                    basedata = new JSONObject();
                    data = new JSONObject();
                    try {
                        basedata.put("unitID", unitID);
                        basedata.put("unitName", unitName);
                        basedata.put("platformName", platformName);
                        basedata.put("edition", edition);
                        basedata.put("deployPlace", deployPlace);
                        basedata.put("runPlace", runPlace);
                        basedata.put("runState", runState);
                        basedata.put("applyTime", applyTime);
                        basedata.put("operatorID", operatorID);
                        basedata.put("operatorName", operatorName);
                        peersJSONArray = new JSONArray();
                        peersJSONArray.put("peer0.org1.example.com").put("peer0.org2.example.com").put("peer0.org3.example.com");
                        data.put("peers", peersJSONArray);
                        data.put("fcn", "certApplication");
                        argsJSONArray = new JSONArray();
                        argsJSONArray.put(basedata.toString()).put(applicationHash).put(legalFileHash).put(hashSummary).put("");
                        data.put("args", argsJSONArray);
                        new CheckTransactionTask(DataReviewDetail.this,
                                "CheckTransactionTask", data.toString(), blockToken).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.data_review_detail_fileOnsite_pass_btn:
                if (!isValidInput(2, auditorID, postPersonID, unitName, platformName, edition, deployPlace, certificationUnitID, certificationUnitName, conclusion, auditTime, auditorName, postPersonName)) {
                    UiUtils.show("对不起，您的输入不能为空。");
                } else {
                    basedata = new JSONObject();
                    data = new JSONObject();
                    try {
                        basedata.put("unitID", unitID);
                        basedata.put("unitName", unitName);
                        basedata.put("platformName", platformName);
                        basedata.put("edition", edition);
                        basedata.put("certificationUnitID", certificationUnitID);
                        basedata.put("certificationUnitName", certificationUnitName);
                        basedata.put("conclusion", conclusion);
                        basedata.put("auditTime", auditTime);
                        basedata.put("auditorID", auditorID);
                        basedata.put("auditorName", auditorName);
                        basedata.put("postPersonID", postPersonID);
                        basedata.put("postPersonName", postPersonName);
                        peersJSONArray = new JSONArray();
                        peersJSONArray.put("peer0.org1.example.com").put("peer0.org2.example.com").put("peer0.org3.example.com");
                        data.put("peers", peersJSONArray);
                        data.put("fcn", "docAudit");
                        argsJSONArray = new JSONArray();
                        argsJSONArray.put(basedata.toString()).put("");
                        data.put("args", argsJSONArray);
                        new CheckTransactionTask(DataReviewDetail.this,
                                "CheckTransactionTask", data.toString(), blockToken).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.data_review_detail_upData_pass_btn:
                if (!isValidInput(2, postPersonID, checkPersonID, unitName, unitID, registerAddr, platformName, edition, website, auditAddr, authenticationStandard, certificationClass, certificationMode, certificationConclusion,
                        awardDate, replaceDate, validityTerm, certificationID, certificationName, postPersonName, checkPersonName)) {
                    UiUtils.show("对不起，您的输入不能为空。");
                } else {
                    basedata = new JSONObject();
                    data = new JSONObject();
                    try {
                        basedata.put("certificateID", certificateID);
                        basedata.put("unitName", unitName);
                        basedata.put("registerAddr", registerAddr);
                        basedata.put("platformName", platformName);
                        basedata.put("edition", edition);
                        basedata.put("website", website);
                        basedata.put("auditAddr", auditAddr);
                        basedata.put("authenticationStandard", authenticationStandard);
                        basedata.put("certificationMode", certificationMode);
                        basedata.put("certificationConclusion", certificationConclusion);
                        basedata.put("certificationClass", certificationClass);
                        basedata.put("awardDate", awardDate);
                        basedata.put("replaceDate", replaceDate);
                        basedata.put("validityTerm", validityTerm);
                        basedata.put("certificationID", certificationID);
                        basedata.put("certificationName", certificationName);
                        basedata.put("certificateScanHASH", certificateScanHASH);
                        basedata.put("postPersonID", postPersonID);
                        basedata.put("postPersonName", postPersonName);
                        peersJSONArray = new JSONArray();
                        peersJSONArray.put("peer0.org1.example.com").put("peer0.org2.example.com").put("peer0.org3.example.com");
                        data.put("peers", peersJSONArray);
                        data.put("fcn", "certUpload");
                        argsJSONArray = new JSONArray();
                        argsJSONArray.put(certificateID).put(unitID).put(basedata.toString()).put("");
                        data.put("args", argsJSONArray);
                        new CheckTransactionTask(DataReviewDetail.this,
                                "CheckTransactionTask", data.toString(), blockToken).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.data_review_detail_jiance_reject_btn:
            case R.id.data_review_detail_applyData_reject_btn:
            case R.id.data_review_detail_fileOnsite_reject_btn:
            case R.id.data_review_detail_upData_reject_btn:
                checkdata = new JSONObject();
                try {
                    checkdata.put("id", urlId);
                    checkdata.put("check", "false");
                    checkdata.put("checkPersonID", checkPersonID);
                    checkdata.put("checkPersonName", checkPersonName);
                    new CheckCertificateTask(DataReviewDetail.this,
                            "CheckCertificateTask", checkdata.toString()).execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    // 获取详细数据
    private class DataDetailsTask extends BaseAsyTask {
        private String status = "0";
        private String data;
        private JSONObject tempJSONObject;
        private JSONArray tempJSONArray;

        public DataDetailsTask(Context context, String string, String... params) {
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
                    if (pos1 == 0) {
                        if (pos2 == 0) {
                            initApplyDataJson();
                        } else if (pos2 == 1 || pos2 == 2) {
                            initFileOnsiteJson();
                        } else {
                            initUpDataJson();
                        }
                    } else {
                        initJianceJson();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = "500";
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                if (pos1 == 0) {
                    if (pos2 == 0) {
                        data_review_detail_applyData_line.setVisibility(View.VISIBLE);
                        setApplyDataText();
                    } else if (pos2 == 1 || pos2 == 2) {
                        data_review_detail_fileOnsite_line.setVisibility(View.VISIBLE);
                        setFileOnsiteText();
                    } else {
                        data_review_detail_upData_line.setVisibility(View.VISIBLE);
                        setUpDataText();
                    }
                } else {
                    data_review_detail_jiance_line.setVisibility(View.VISIBLE);
                    if (pos2 == 0) {
                        data_review_detail_jiance_testTime_text.setText("检测时间:");
                    } else {
                        data_review_detail_jiance_testTime_text.setText("试运行时间:");
                    }
                    setJianceText();
                }
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(DataReviewDetail.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("未知错误");
            }
        }

        private void setJianceText() {
            data_review_detail_jiance_certificateID.setText(certificateID);
            data_review_detail_jiance_unitID.setText(unitID);
            data_review_detail_jiance_unitName.setText(unitName);
            data_review_detail_jiance_platformName.setText(platformName);
            data_review_detail_jiance_edition.setText(edition);
            data_review_detail_jiance_testUnitID.setText(testUnitID);
            data_review_detail_jiance_testUnitName.setText(testUnitName);
            data_review_detail_jiance_conclusion.setText(conclusion);
            data_review_detail_jiance_testTime.setText(testTime);
            data_review_detail_jiance_testingPersonID.setText(testingPersonID);
            data_review_detail_jiance_testingPersonName.setText(testingPersonName);
            data_review_detail_jiance_postPersonID.setText(postPersonID);
            data_review_detail_jiance_postPersonName.setText(postPersonName);
            data_review_detail_jiance_checkPersonID.setText(checkPersonID);
            data_review_detail_jiance_checkPersonName.setText(checkPersonName);
        }

        private void initJianceJson() {
            try {
                tempJSONArray = new JSONArray(jsonObject.getString("basedata"));
                certificateID = tempJSONArray.getString(0);
                unitID = tempJSONArray.getString(1);
                data = tempJSONArray.getString(2);
                tempJSONObject = new JSONObject(data);
                unitName = tempJSONObject.optString("unitName");
                platformName = tempJSONObject.optString("platformName");
                edition = tempJSONObject.optString("edition");
                testUnitID = tempJSONObject.optString("testUnitID");
                testUnitName = tempJSONObject.optString("testUnitName");
                conclusion = tempJSONObject.optString("conclusion");
                if (pos2 == 0) {
                    testTime = tempJSONObject.optString("testTime");
                } else {
                    testTime = tempJSONObject.optString("testRunTime");
                }
                testingPersonID = tempJSONObject.optString("testingPersonID");
                testingPersonName = tempJSONObject.optString("testingPersonName");
                postPersonID = tempJSONObject.optString("postPersonID");
                postPersonName = tempJSONObject.optString("postPersonName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void setApplyDataText() {
            data_review_detail_applyData_unitID.setText(unitID);
            data_review_detail_applyData_applicationPrintFileID.setText(applicationHash);
            data_review_detail_applyData_verificationPrintFileID.setText(legalFileHash);
            Glide.with(DataReviewDetail.this).load(URLConstants.ServerURL + URLConstants.WebPort + URLConstants.DOWNLOAD_URL + image1URL).apply(new RequestOptions().error(R.drawable.bg_pic_loading)).into(data_review_detail_applyData_applyimg);
            Glide.with(DataReviewDetail.this).load(URLConstants.ServerURL + URLConstants.WebPort + URLConstants.DOWNLOAD_URL + image2URL).apply(new RequestOptions().error(R.drawable.bg_pic_loading)).into(data_review_detail_applyData_legalimg);
            data_review_detail_applyData_unitName.setText(unitName);
            data_review_detail_applyData_platformName.setText(platformName);
            data_review_detail_applyData_edition.setText(edition);
            data_review_detail_applyData_deployPlace.setText(deployPlace);
            data_review_detail_applyData_runPlace.setText(runPlace);
            data_review_detail_applyData_runState.setText(runState);
            data_review_detail_applyData_applyTime.setText(applyTime);
            data_review_detail_applyData_operatorID.setText(operatorID);
            data_review_detail_applyData_operatorName.setText(operatorName);
            data_review_detail_applyData_checkPersonID.setText(checkPersonID);
            data_review_detail_applyData_checkPersonName.setText(checkPersonName);
        }

        private void initApplyDataJson() {
            unitID = jsonObject.optString("unitID");
            unitName = jsonObject.optString("unitName");
            platformName = jsonObject.optString("platformName");
            edition = jsonObject.optString("edition");
            deployPlace = jsonObject.optString("deployPlace");
            runPlace = jsonObject.optString("runPlace");
            runState = jsonObject.optString("runState");
            applyTime = jsonObject.optString("applyTime");
            operatorID = jsonObject.optString("operatorID");
            operatorName = jsonObject.optString("operatorName");
            applicationHash = jsonObject.optString("applicationHash");
            legalFileHash = jsonObject.optString("legalFileHash");
            image1URL = jsonObject.optString("image1");
            image2URL = jsonObject.optString("imgge2");
        }

        private void initFileOnsiteJson() {
            unitID = jsonObject.optString("unitID");
            unitName = jsonObject.optString("unitName");
            platformName = jsonObject.optString("platformName");
            edition = jsonObject.optString("edition");
            auditorID = jsonObject.optString("auditorID");
            runPlace = jsonObject.optString("runPlace");
            auditorName = jsonObject.optString("auditorName");
            auditTime = jsonObject.optString("auditTime");
            postPersonID = jsonObject.optString("postPersonID");
            postPersonName = jsonObject.optString("postPersonName");
            certificationUnitID = jsonObject.optString("certificationUnitID");
            certificationUnitName = jsonObject.optString("certificationUnitName");
        }

        private void setFileOnsiteText() {
            data_review_detail_fileOnsite_unitID.setText(unitID);
            data_review_detail_fileOnsite_unitName.setText(unitName);
            data_review_detail_fileOnsite_platformName.setText(platformName);
            data_review_detail_fileOnsite_edition.setText(edition);
            data_review_detail_fileOnsite_certificationUnitID.setText(certificationUnitID);
            data_review_detail_fileOnsite_certificationUnitName.setText(certificationUnitName);
            data_review_detail_fileOnsite_conclusion.setText(conclusion);
            data_review_detail_fileOnsite_auditTime.setText(auditTime);
            data_review_detail_fileOnsite_auditorID.setText(auditorID);
            data_review_detail_fileOnsite_auditorName.setText(auditorName);
            data_review_detail_fileOnsite_postPersonID.setText(postPersonID);
            data_review_detail_fileOnsite_postPersonName.setText(postPersonName);
            data_review_detail_fileOnsite_checkPersonID.setText(checkPersonID);
            data_review_detail_fileOnsite_checkPersonName.setText(checkPersonName);
            data_review_detail_fileOnsite_checkPersonID.setText(checkPersonID);
            data_review_detail_fileOnsite_checkPersonName.setText(checkPersonName);
        }

        private void initUpDataJson() {
            certificateID = jsonObject.optString("certificateID");
            unitID = jsonObject.optString("unitId");
            unitName = jsonObject.optString("unitName");
            registerAddr = jsonObject.optString("registerAddr");
            platformName = jsonObject.optString("platformName");
            edition = jsonObject.optString("edition");
            website = jsonObject.optString("website");
            auditAddr = jsonObject.optString("auditAddr");
            certificationID = jsonObject.optString("certificationID");
            authenticationStandard = jsonObject.optString("authenticationStandard");
            certificationMode = jsonObject.optString("certificationMode");
            certificationConclusion = jsonObject.optString("certificationConclusion");
            certificationClass = jsonObject.optString("certificationClass");
            awardDate = jsonObject.optString("awardDate");
            replaceDate = jsonObject.optString("replaceDate");
            validityTerm = jsonObject.optString("validityTerm");
            certificationName = jsonObject.optString("certificationName");
            certificateScanHASH = jsonObject.optString("certificateScanHASH");
            postPersonID = jsonObject.optString("postPersonID");
            postPersonName = jsonObject.optString("postPersonName");
            image1URL = jsonObject.optString("img");
        }

        private void setUpDataText() {
            data_review_detail_upData_certificateID.setText(certificateID);
            data_review_detail_upData_unitID.setText(unitID);
            data_review_detail_upData_unitName.setText(unitName);
            data_review_detail_upData_registerAddr.setText(registerAddr);
            data_review_detail_upData_platformName.setText(platformName);
            data_review_detail_upData_edition.setText(edition);
            data_review_detail_upData_website.setText(website);
            data_review_detail_upData_auditAddr.setText(auditAddr);
            data_review_detail_upData_authenticationStandard.setText(authenticationStandard);
            data_review_detail_upData_certificationMode.setText(certificationMode);
            data_review_detail_upData_certificationConclusion.setText(certificationConclusion);
            data_review_detail_upData_certificationClass.setText(certificationClass);
            data_review_detail_upData_awardDate.setText(awardDate);
            data_review_detail_upData_replaceDate.setText(replaceDate);
            data_review_detail_upData_validityTerm.setText(validityTerm);
            data_review_detail_upData_certificationID.setText(certificationID);
            data_review_detail_upData_certificationName.setText(certificationName);
            data_review_detail_upData_certificateScanHASH.setText(certificateScanHASH);
            Glide.with(DataReviewDetail.this).load(URLConstants.ServerURL + URLConstants.WebPort + URLConstants.DOWNLOAD_URL + image1URL).apply(new RequestOptions().error(R.drawable.bg_pic_loading)).into(data_review_detail_upData_certificateimg);
            data_review_detail_upData_postPersonID.setText(postPersonID);
            data_review_detail_upData_postPersonName.setText(postPersonName);
            data_review_detail_upData_checkPersonID.setText(checkPersonID);
            data_review_detail_upData_checkPersonName.setText(checkPersonName);
        }
    }

    private boolean isValidInput(int cnt, String... args) {
        for (int i = 0; i < cnt; ++i) {
            if (!IDCardUtils.isIDNumber(args[i])) {
                UiUtils.show("身份证号有误");
                return false;
            }
        }
        for (int i = 0; i < args.length; ++i) {
            if (TextUtils.isEmpty(args[i])) {
                return false;
            }
        }
        return true;
    }

    // 同意审核
    private class CheckTransactionTask extends BaseAsyTask {
        private String status = "false";
        private String msg = "未知错误";
        private String transactionId = "";

        public CheckTransactionTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                dialog = new AlertDialog.Builder(DataReviewDetail.this);
                dialog.setMessage("上传成功，请妥善保存该交易id。");
                final TextView transactionTxt = new TextView(DataReviewDetail.this);
                transactionTxt.setText(transactionId);
                dialog.setView(transactionTxt);
                dialog.setPositiveButton("我已保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JSONObject checkdata = new JSONObject();
                        try {
                            checkdata.put("id", urlId);
                            checkdata.put("transaction", transactionId);
                            checkdata.put("check", "true");
                            checkdata.put("checkPersonID", checkPersonID);
                            checkdata.put("checkPersonName", checkPersonName);
                            new CheckCertificateTask(DataReviewDetail.this,
                                    "CheckCertificateTask", checkdata.toString()).execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog.show();
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(DataReviewDetail.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show(msg);
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
                    status = jsonObject.optString("success");
                    msg = jsonObject.optString("message");
                    transactionId = msg.split(":")[1];
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = "500";
            }
            return status;
        }
    }

    private class CheckCertificateTask extends BaseAsyTask {
        private String status = "0";
        private String msg = "未知错误";

        public CheckCertificateTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                UiUtils.show(msg);
                startActivity(new Intent(DataReviewDetail.this, MainActivity.class));
                finish();
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(DataReviewDetail.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show(msg);
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
                e.printStackTrace();
                status = "500";
            }
            return status;
        }
    }
}