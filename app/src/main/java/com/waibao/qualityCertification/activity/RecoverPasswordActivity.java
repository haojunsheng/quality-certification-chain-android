package com.waibao.qualityCertification.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.util.IDCardUtils;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 实现忘记密码重置的功能
 */
public class RecoverPasswordActivity extends BaseActivity {
    private Toolbar toolbar;
    private EditText institutionNumber;
    private String institutionNumberStr;
    private EditText IDCard;
    private String IDCardStr;
    private EditText passwd;
    private EditText passwd1;
    private String passwdStr;
    private String passwd1Str;
    private Button recoverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        initToolbar();
        IDCard = (EditText) findViewById(R.id.ID);
        institutionNumber = (EditText) findViewById(R.id.unitNo);
        passwd = (EditText) findViewById(R.id.newpassword);
        passwd1 = (EditText) findViewById(R.id.newpassword1);
        recoverButton = (Button) findViewById(R.id.recover);
        recoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDCardStr = IDCard.getText().toString().trim();
                institutionNumberStr = institutionNumber.getText().toString().trim();
                passwdStr = passwd.getText().toString().trim();
                passwd1Str = passwd1.getText().toString().trim();
                if (strIsValid(IDCardStr, institutionNumberStr, passwdStr, passwd1Str)) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("institutionNumber", institutionNumberStr);
                        jsonObject.put("idCard", IDCardStr);
                        jsonObject.put("password", passwdStr);
                        jsonObject.put("password1", passwd1Str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new RecoverPassword1Task(RecoverPasswordActivity.this,
                            "RecoverPassword1Task", String.valueOf(jsonObject)).execute();
                }
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_recover_pass);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private boolean strIsValid(String IDCardStr, String institutionNumberStr, String passwdStr, String passwd1Str) {
        if (TextUtils.isEmpty(institutionNumberStr) || TextUtils.isEmpty(IDCardStr) || TextUtils.isEmpty(passwdStr) || TextUtils.isEmpty(passwd1Str)) {
            UiUtils.show("输入不能为空");
            return false;
        }
        if (!IDCardUtils.isIDNumber(IDCardStr)) {
            UiUtils.show("身份证号格式不正确");
            return false;
        }
        if (!IDCardUtils.isUnifiedCreditCode(institutionNumberStr)) {
            UiUtils.show("机构编号不正确");
            return false;
        }
        if (!TextUtils.equals(passwdStr, passwd1Str)) {
            UiUtils.show("两次输入密码不一致");
            return false;
        }
        return true;
    }

    public class RecoverPassword1Task extends BaseAsyTask {
        private String status = "-500";
        private String msg = "success";

        public RecoverPassword1Task(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                UiUtils.show("密码重置成功");
                Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else if (TextUtils.equals(s, "-1") || TextUtils.equals(s, "-2")) {
                UiUtils.show(msg);
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
                    status = jsonObject.optString("code");
                    msg = jsonObject.optString("msg");
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