package com.waibao.qualityCertification.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.waibao.qualityCertification.activity.LoginActivity;
import com.waibao.qualityCertification.activity.UserReview;
import com.waibao.qualityCertification.constants.URLConstants;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * author: anapodoton
 * created on: 2018/3/27 9:38
 * description: AsyncTask的基类
 */
public class BaseAsyTask extends AsyncTask<String, String, String> {
    private Context context = null;

    private String TAG;//表示哪个方法需要调用AsyTask
    private String URL;
    private String dialogInfo;
    protected static OkHttpClient okHttpClient;
    protected static ProgressDialog pDialog = null;
    protected static FormBody.Builder builder = new FormBody.Builder();
    protected static Request request = null;
    protected static RequestBody requestBody = null;
    protected static Response response = null;
    protected static String token = null;
    protected String string;//响应体
    protected JSONObject jsonObject;
    protected JSONArray jsonArray;
    protected MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private String userName;
    private String password;
    private String institutionNumberStr;
    private String session;
    private String jsondata;

    public BaseAsyTask() {

    }

    public BaseAsyTask(Context context, String TAG) {
        this.context = context;
        this.TAG = TAG;
    }

    public BaseAsyTask(Context context, String TAG, String... params) {
        this.context = context;
        this.TAG = TAG;
        switch (TAG) {
            case "LoginTask":
                userName = params[0];
                password = params[1];
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.LOGIN_URL;
                builder = new FormBody.Builder();
                dialogInfo = "登陆中，请稍候...";
                builder.add("username", userName);
                builder.add("password", password);
                break;
            case "InsNameTask":
                institutionNumberStr = params[0];
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.INSNAME_URL;
                builder = new FormBody.Builder();
                dialogInfo = "获取机构信息中，请稍候...";
                builder.add("institutionNumber", institutionNumberStr);
                break;
            case "DataUpdateTask":
                URL = URLConstants.ServerURL + URLConstants.WebPort + params[0];
                jsondata = params[1];
                dialogInfo = "上传中，请稍候...";
                break;
            case "RegisterTask":
                jsondata = params[0];
                dialogInfo = "用户注册中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.USER_REGISTER_URL;
                break;
            case "RecoverPassword1Task":
                jsondata = params[0];
                dialogInfo = "重置密码中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.RECOVER_PASSWORD_URL;
                break;
            case "UpdatePasswordTask":
                institutionNumberStr = params[0];
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.UPDATE_PASSWORD_URL;
                builder = new FormBody.Builder();
                dialogInfo = "密码更新中，请稍候...";
                builder.add("password", params[0]);
                builder.add("newpassword", params[1]);
                break;
            case "TransactionMonitorTask":
                token = params[0];
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.TRANSACTION_INFO_MONITOR_URL;
                dialogInfo = "交易信息查询中，请稍候...";
                break;
            case "OkRevListTask":
                session = params[0];
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.OK_REV_LIST_URL;
                dialogInfo = "已审核用户列表加载中，请稍候...";
                break;
            case "RevListTask":
                session = params[0];
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.REV_LIST_URL;
                dialogInfo = "待审核用户列表加载中，请稍候...";
                break;
            case "InstitutionReview":
                session = params[0];
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.INSTITUTION_INFO_URL;
                dialogInfo = "机构查询中，请稍候...";
                break;
            case "UpdateInstitutionStatusTask":
                builder = new FormBody.Builder();
                builder.add("institution_number", params[0]);
                builder.add("isInvalid", params[1]);
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.UPDATE_INSTITUTION_STATUS_URL;
                dialogInfo = "变更机构状态中，请稍候...";
                break;
            case "ReviewSuccessTask":
                session = params[0];
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.REVIEW_SUCCESS_URL + params[1];
                dialogInfo = "审核用户成功中，请稍候...";
                break;
            case "ReviewFailTask":
                session = params[0];
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.REVIEW_FAIL_URL + params[1];
                dialogInfo = "审核用户拒绝中，请稍候...";
                break;
            case "GetUnitUserTask":
                session = params[0];
                token = params[1];
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.UNIT_USER_URL;
                dialogInfo = "用户列表获取中，请稍候...";
                break;
            case "UserTokenTask":
                URL = URLConstants.ServerURL + URLConstants.BlockPort + URLConstants.USER_TOKEN_URL;
                builder = new FormBody.Builder();
                builder.add("username", params[0]);
                builder.add("orgName", params[1]);
                break;
            case "PublicSearchTask":
                token = params[1];
                dialogInfo = "查询中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.BlockPort + URLConstants.PUBLIC_SEARCH_URL + params[0];
                break;
            case "CheckTransactionTask":
                jsondata = params[0];
                token = params[1];
                dialogInfo = "查询中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.BlockPort + URLConstants.PUBLIC_SEARCH_URL;
                break;
            case "CheckCertificateTask":
                jsondata = params[0];
                dialogInfo = "处理中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.CHECK_CERTIFICATE_URL;
                break;
            case "StatusMonitorTask":
                token = params[1];
                dialogInfo = "查询中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.BlockPort + URLConstants.BLOCK_STATUS_MONITOR_URL + params[0];
                break;
            case "BlockMonitorTask":
                token = params[1];
                dialogInfo = "查询中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.BlockPort + URLConstants.BLOCK_INFO_MONITOR_URL + params[2] + params[0];
                break;
            case "QueryAllCertsTask":
                token = params[1];
                dialogInfo = "查询中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.BlockPort + URLConstants.QUERY_ALL_CERTS_URL + params[0];
                break;
            case "QueryAllUsersTask":
//                token = params[1];
                dialogInfo = "查询中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.PEER_PEOPLE_COUNT_URL;
                break;
            case "GetCheckListTask":
                dialogInfo = "查询中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.WebPort + URLConstants.CHECK_LIST_URL + params[0];
                break;
            case "GetUploadListTask":
                session = params[1];
                dialogInfo = "查询中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.WebPort + params[0];
                break;
            case "DataDetailsTask":
                dialogInfo = "查询中，请稍候...";
                URL = URLConstants.ServerURL + URLConstants.WebPort + params[0] + params[1];
                break;
            default:
                break;
        }
        okHttpClient = new OkHttpClient.Builder()//实例化okhttp
                .connectTimeout(5 * 1000, TimeUnit.MILLISECONDS)//链接超时;
                .readTimeout(10 * 1000, TimeUnit.MILLISECONDS) //读取超时
                .writeTimeout(10 * 1000, TimeUnit.MILLISECONDS) //写入超时
                .addInterceptor(new HttpLoggingInterceptor())
                .retryOnConnectionFailure(false)
                .build();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);//实例化ProgressDialog
        pDialog.setMessage(dialogInfo);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(false);
        pDialog.show();

        switch (TAG) {
            case "RegisterTask"://注册
            case "RecoverPassword1Task"://重置密码
            case "DataUpdateTask"://上传检测数据
            case "CheckCertificateTask"://
                requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsondata);
                request = new Request.Builder().url(URL).post(requestBody).addHeader("Connection", "close").build();
                break;
            case "CheckTransactionTask":
                requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsondata);
                request = new Request.Builder().url(URL).post(requestBody).addHeader("Connection", "close").header("Authorization", "Bearer " + token).build();
                break;
            case "LoginTask"://用户登陆
            case "InsNameTask"://获取机构信息
            case "UserTokenTask"://获取区块链的用户token
            case "UpdatePasswordTask"://更新密码
            case "DataDetailsTask"://获取详细审核数据
            case "UpdateInstitutionStatusTask":
                request = new Request.Builder()
                        .url(URL)
                        .post(builder.build())
                        .addHeader("Connection", "close")
                        .build();
                break;
            case "GetCheckListTask":
            case "QueryAllUsersTask":
                request = new Request.Builder()
                        .url(URL)
                        .get()
                        .addHeader("Connection", "close")
                        .build();
                break;
            case "PublicSearchTask":
            case "StatusMonitorTask":
            case "BlockMonitorTask":
            case "TransactionMonitorTask":
            case "QueryAllCertsTask":
                request = new Request.Builder()
                        .header("Authorization", "Bearer " + token)
                        .url(URL)
                        .get()
                        .addHeader("Connection", "close")
                        .build();
                break;
            case "OkRevListTask":
            case "RevListTask":
            case "ReviewSuccessTask":
            case "ReviewFailTask":
            case "GetUnitUserTask":
            case "GetUploadListTask":
            case "InstitutionReview":
                request = new Request.Builder()
                        .header("Authorization", "Bearer " + token)
                        .header("cookie", session)
                        .url(URL)
                        .get()
                        .addHeader("Connection", "close")
                        .build();
                break;
            default:
                break;
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pDialog.dismiss();
    }
}