package com.waibao.qualityCertification.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.util.IDCardUtils;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 实现用户的注册
 */
public class RegisterActivity extends BaseActivity {
    private EditText institutionNumber;
    private String institutionNumberStr;
    private String institutionTypeStr;
    private String institutionNameStr;
    private EditText IDCard;
    private String IDCardStr;
    private EditText realname;
    private String realnameStr;
    private EditText name;
    private String nameStr;
    private EditText passwd;
    private EditText passwd1;
    private String passwdStr;
    private String passwd1Str;
    private TextView goLogin;
    private Button registerButton;
    private Spinner usertypes = null;
    private String usertypeStr = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        institutionNumber = (EditText) findViewById(R.id.institutionNumber);
        usertypes = (Spinner) findViewById(R.id.usertypes);
        IDCard = (EditText) findViewById(R.id.IDCard);
        realname = (EditText) findViewById(R.id.realname);
        name = (EditText) findViewById(R.id.name);
        passwd = (EditText) findViewById(R.id.passwd);
        passwd1 = (EditText) findViewById(R.id.passwd);
        goLogin = (TextView) findViewById(R.id.goLogin);
        registerButton = (Button) findViewById(R.id.register);
        usertypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if (pos == 0) {
                    usertypeStr = "0";
                } else {
                    usertypeStr = "1";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //已经注册过账号直接登陆
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        //注册
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                institutionNumberStr = institutionNumber.getText().toString().trim();
                IDCardStr = IDCard.getText().toString().trim();
                realnameStr = realname.getText().toString().trim();
                nameStr = name.getText().toString().trim();
                passwdStr = passwd.getText().toString().trim();
                passwd1Str = passwd1.getText().toString().trim();
                if (strIsValid(institutionNumberStr, IDCardStr, realnameStr, nameStr, passwdStr, passwd1Str)) {
                    new InsNameTask(RegisterActivity.this,
                            "InsNameTask", institutionNumberStr).execute();
                }
            }
        });
    }

    private boolean strIsValid(String institutionNumberStr, String IDCardStr, String realnameStr, String nameStr, String passwdStr, String passwd1Str) {
        if (TextUtils.isEmpty(institutionNumberStr) || TextUtils.isEmpty(IDCardStr) || TextUtils.isEmpty(realnameStr) || TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(passwdStr) || TextUtils.isEmpty(passwd1Str)) {
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
//        if ((institutionTypeStr == "2" && usertypeStr == "2") || (institutionTypeStr == "3" && usertypeStr == "2")) {
//            UiUtils.show("操作错误，请重试");
//            return false;
//        }
        return true;
    }

    //获取机构信息
    public class InsNameTask extends BaseAsyTask {
        private String status = "-500";
        private String msg = "success";

        public InsNameTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                String[] msgStr = msg.split(",");
                institutionNameStr = msgStr[0];
                institutionTypeStr = msgStr[1];
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("institutionNumber", institutionNumberStr);
                    jsonObject.put("institutionType", institutionTypeStr);
                    jsonObject.put("institutionName", institutionNameStr);
                    jsonObject.put("userType", usertypeStr);
                    jsonObject.put("idCard", IDCardStr + ";" + realnameStr);
                    jsonObject.put("username", nameStr);
                    jsonObject.put("password", passwdStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new RegisterTask(RegisterActivity.this,
                        "RegisterTask", String.valueOf(jsonObject)).execute();
            } else if (TextUtils.equals(s, "500")) {
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

    //注册
    public class RegisterTask extends BaseAsyTask {
        private String status = "-500";
        private String msg = "success";

        public RegisterTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                UiUtils.show(getString(R.string.registerSuccess));
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else if (TextUtils.equals(s, "500")) {
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