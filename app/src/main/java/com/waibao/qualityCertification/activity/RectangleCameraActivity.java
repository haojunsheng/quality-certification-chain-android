package com.waibao.qualityCertification.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.camera.CameraManager;
import com.waibao.qualityCertification.decoding.InactivityTimer;
import com.waibao.qualityCertification.util.FileProvider7Util;
import com.waibao.qualityCertification.util.ToasUtils;
import com.waibao.qualityCertification.view.RectangleCameraView;

import java.io.IOException;

import static com.waibao.qualityCertification.constants.IntentConstants.RESULT_RECTANGLE_CAMERA;

public class RectangleCameraActivity extends BaseActivity implements SurfaceHolder.Callback {

    //Toolbar
    private Toolbar toolbar;

    private RectangleCameraView viewfinderView;
    private boolean hasSurface;

    private InactivityTimer inactivityTimer;
    private ImageView camera_close;
    private ImageView camera_take;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectangle_camera);
        CameraManager.init(getApplication());
        initView();
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        initToolbar();

    }

    private void initView() {
        viewfinderView = (RectangleCameraView) findViewById(R.id.RectangleCameraView);
        viewfinderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focus();
            }
        });
        camera_close = (ImageView) findViewById(R.id.camera_close);
        camera_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        camera_take = (ImageView) findViewById(R.id.camera_take);
        camera_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(final byte[] data, final Camera camera) {
//                            ToasUtils.showToast(RectangleCameraActivity.this,data.toString());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (data.length < 1) {//拍摄有误
                                        camera.stopPreview();
                                        ToasUtils.showToast(RectangleCameraActivity.this, "对不起，拍摄错误");
                                        finish();
                                    }
                                    String picPath = FileProvider7Util.createFileWithByte(data);
                                    if (picPath == null) {
                                        ToasUtils.showToast(RectangleCameraActivity.this, "对不起，图片处理错误");
                                    } else {
                                        Intent intent = new Intent();
                                        intent.putExtra("picPath", picPath);
                                        setResult(RESULT_RECTANGLE_CAMERA, intent);
                                        finish();
                                    }
                                }
                            }).start();
                        }
                    });
                }
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_rectangleCamera);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.previewRectangleCamera_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CameraManager.get().stopPreview();

        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

//    public void handleDecode(Result result, Bitmap barcode) {
//        inactivityTimer.onActivity();
//        //playBeepSoundAndVibrate();
//        //得到二维码的信息
//        // { "goodsName": "火腿", "goodsDate": "20181001", "goodsOwner": "金华", "goodsBatch":"第二批", "goodsId":"10" }
//        resultString = result.getText();
//        if (!TextUtils.isEmpty(resultString)) {
//            Intent intent =new Intent();
//            intent.putExtra("resultString",resultString);
//            setResult(RESULT_OPEN_SCAN_STORECALLIGRAPHY,intent);
//            finish();
////            switch (FLAG) {
////                //微信扫码收银
////                case "StoreCalligraph":
////                    //new MipcaSaoAsyTask(MipcaActivityCapture.this, "WeiXinSao", resultString, jinEr).execute();
////                    break;
//////                case "TuiKuan":
//////                    new MipcaSaoAsyTask(MipcaActivityCapture.this, "TuiKuan", resultString).execute();
//////                    break;
//////                case "HeXiao":
//////                    new MipcaSaoAsyTask(MipcaActivityCapture.this, "HeXiao", resultString).execute();
////                    //break;
////                default:
////                    break;
////            }
//        } else {
//            ToasUtils.showToast(RectangleCameraActivity.this, "未获取到二维码/条形码信息，请稍候再试");
//        }
//    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            mCamera = CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        CameraManager.get().startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public RectangleCameraView getViewfinderView() {
        return viewfinderView;
    }

    /**
     * 对焦，在CameraActivity中触摸对焦
     */
    public void focus() {
        if (mCamera != null) {
            mCamera.autoFocus(null);
        }
    }

    /**
     * 拍摄照片
     *
     * @param pictureCallback 在pictureCallback处理拍照回调
     */
    public void takePhoto(Camera.PictureCallback pictureCallback) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, pictureCallback);
        }
    }
}