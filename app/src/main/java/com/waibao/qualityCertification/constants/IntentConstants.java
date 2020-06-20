package com.waibao.qualityCertification.constants;

public class IntentConstants {
    //拍照对应RequestCode
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;//拍摄人脸
    public static final int SELECT_PIC_BY_TACK_PHOTO_SEAL = 2;//印章拍照
    public static final int SELECT_PIC_BY_TACK_PHOTO_IMAGE = 3;//图片拍照
    public static final int SELECT_PIC_BY_TACK_PHOTO_IMAGE_DETAIL = 4;//图片详情二维码
    public static final int RECORD_SYSTEM_VIDEO = 4;//录制视频
    public static final int GO_TO_KEY = 5;//生成私钥
    //裁剪图片
    public static final int CROP_PICTURE = 6;
    public static final int GO_TO_FACE = 7;//人脸验证
    public static final int OPEN_SCAN_PUBLICSEARCH = 8;//公共查询
    public static final int RESULT_OPEN_SCAN_STORECALLIGRAPHY = 9;//存链中扫一扫获取二维码信息,返回结果
    public static final int RESULT_RECTANGLE_CAMERA = 10;//存链中扫一扫获取二维码信息,返回结果
    public static final int OPEN_SCAN_QUERYCERITIFICATE = 11;//证书查询
    public static final int OPEN_SCAN_DATAUPLOAD_ERITIFICATE_APPLICATION = 12;//证书申请资料上传
    public static final int OPEN_SCAN_DATAUPLOAD_DOCUMENT_AUDIT = 13;//文件审核资料上传
    public static final int OPEN_SCAN_DATAUPLOAD_SITE__AUDIT = 14;//现场审核资料上传
    public static final int OPEN_SCAN_DATAUPLOAD_DATA = 15;//证书数据上传
    public static final int OPEN_SCAN_DATAUPLOAD_JIANCE = 16;//检测检验上传
    public static final int OPEN_SCAN_DATAUPLOAD_TRY_RUN = 17;//试运行数据上传
}