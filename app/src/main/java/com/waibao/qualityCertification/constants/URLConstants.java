package com.waibao.qualityCertification.constants;

/**
 * author: anapodoton
 * created on: 2018/3/26 19:16
 * description:
 */
public class URLConstants {
    public static final String ServerURL = "http://47.105.220.31";//服务器的地址
    public static final String WebPort = ":8081";
    public static final String BlockPort = ":4000";
    public static final String LOGIN_URL = "/userLogin";//登陆
    public static final String INSNAME_URL = "/getinsName";//返回相对应的机构名称
    public static final String USER_REGISTER_URL = "/userRegister";//注册
    public static final String RECOVER_PASSWORD_URL = "/userRecoverPassword1";//重置密码
    public static final String UPDATE_PASSWORD_URL = "/updatePassword";//更新密码
    public static final String USER_TOKEN_URL = "/users";//获取区块链的用户token
    public static final String PUBLIC_SEARCH_URL = "/channels/mychannel/chaincodes/mycc";//公众查询
    public static final String QUERY_ALL_CERTS_URL = "/channels/mychannel/chaincodes/mycc";//查询所有证书
    public static final String BLOCK_STATUS_MONITOR_URL = "/channels/mychannel";//区块链状态监控
    public static final String PEER_PEOPLE_COUNT_URL = "/getAllUser";//节点在线人数
    public static final String BLOCK_INFO_MONITOR_URL = "/channels/mychannel/blocks/";//区块信息监控列表
    public static final String TRANSACTION_INFO_MONITOR_URL = "/getTransaData";//交易信息实时监控列表
    public static final String OK_REV_LIST_URL = "/getOKRevList";//已审核用户列表
    public static final String REV_LIST_URL = "/getRevList";//待审核用户列表
    public static final String REVIEW_SUCCESS_URL = "/reviewSuccess";//待审核用户列表
    public static final String REVIEW_FAIL_URL = "/reviewFail";//待审核用户列表
    public static final String UNIT_USER_URL = "/getUnitUser";//获取用户列表
    public static final String INSTITUTION_INFO_URL = "/getInstitutions";//查询机构
    public static final String UPDATE_INSTITUTION_STATUS_URL = "/updateInstitutionStatus";//变更机构状态
    public static final String CHECK_LIST_URL = "/getCheckList";//获取审核列表
    public static final String DATA_DETAILS_URL = "/getDataDetails";//审核数据详细列表
    public static final String CHECK_CERTIFICATE_URL = "/checkCertificate";//审核数据
    public static final String DOWNLOAD_URL = "/getImage?imageUrl=";//下载图片
}