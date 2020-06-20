package com.waibao.qualityCertification.util;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.camera.CameraManager;
import com.waibao.qualityCertification.decoding.CaptureActivityHandler;
import com.waibao.qualityCertification.decoding.InactivityTimer;
import com.waibao.qualityCertification.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

import static com.waibao.qualityCertification.constants.IntentConstants.RESULT_RECTANGLE_CAMERA;

public class MipcaActivityCapture extends BaseActivity implements SurfaceHolder.Callback {
    //Toolbar
    private Toolbar toolbar;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private Button question;
    //扫描结果
    private String resultString;
    private String status = "-1";
    AlertDialog.Builder builderDialog;
    //标志从哪个Activity进来
    private String FLAG;
    private Bundle bundle;
    private String jinEr;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mipca_capture);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        initToolbar();
        //取出Bundle里面的数据
        initDatas();
        builderDialog = new AlertDialog.Builder(MipcaActivityCapture.this);
        question = (Button) findViewById(R.id.question);
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderDialog.setTitle("提示");
                builderDialog.setCancelable(false);
                builderDialog.setMessage("1.如果较长时间未扫描出二维码，尝试调整手机与二维码的距离\n2.如果您扫描的是条形码，请将条形码横放");
                builderDialog.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builderDialog.create().show();
            }
        });
    }

    private void initDatas() {
        bundle = getIntent().getExtras();
        FLAG = bundle.getString("TAG");
        switch (FLAG) {
            case "PublicSearch":
                break;
            default:
                break;
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_erweima);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        //得到二维码的信息
        // { "goodsName": "火腿", "goodsDate": "20181001", "goodsOwner": "金华", "goodsBatch":"第二批", "goodsId":"10" }
        resultString = result.getText();
        UiUtils.show(resultString);
        if (!TextUtils.isEmpty(resultString)) {
            Intent intent = new Intent();
            intent.putExtra("resultString", resultString);
            setResult(RESULT_RECTANGLE_CAMERA, intent);
            finish();
        } else {
            ToasUtils.showToast(MipcaActivityCapture.this, "未获取到二维码/条形码信息，请稍候再试");
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
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

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

//    class MipcaSaoAsyTask extends BaseAsyTask {
//        public MipcaSaoAsyTask() {
//
//        }
//
//        public MipcaSaoAsyTask(Context context, String string, String... params) {
//            super(context, string, params);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String returnResult = "err";
//            //加载付款或者收款状态
//            String status1 = loadingStatusInfo();
//            // 0代表收款或退款成功
//            switch (FLAG) {
//                //微信扫码收银
//                case "WeiXinShouYin":
//                    if (status1.equals("0")) {
//                        returnResult = "ok";
//                    } else if (status1.equals("1")) {
//                        returnResult = "err";
//                    } else {
//                        returnResult = "loading";
//                    }
//                    break;
//                case "TuiKuan":
//                    if (status1.equals("0")) {
//                        returnResult = "ok";
//                    } else if (status1.equals("1")) {
//                        returnResult = "ban";
//                    } else if (status1.equals("4")) {
//                        returnResult = "failure";
//                    } else if (status1.equals("5")) {
//                        returnResult = "notExist";
//                    } else {
//                        returnResult = "err";
//                    }
//                    break;
//                case "HeXiao":
//                    if (status1.equals("0")) {
//                        returnResult = "ok";
//                    } else if (status1.equals("1")) {
//                        returnResult = "not_authority";
//                    } else if (status1.equals("2")) {
//                        returnResult = "hasbeen";
//                    }else {
//                        returnResult = "err";
//                    }
//                    break;
//                default:
//                    break;
//            }
//            return returnResult;
//        }
//
//        //加载用户付款状态
//        private String loadingStatusInfo() {
//            try {
//                response = okHttpClient.newCall(request).execute();
//                String string = response.body().string();
//                JSONObject jsonObject = new JSONObject(string);
//                status = jsonObject.optString("error");
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return status;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            builderDialog.setCancelable(false);
//            switch (FLAG) {
//                case "WeiXinShouYin":
//                    builderDialog.setTitle("收款状态");
//                    if (s.equals("ok")) {
//                        builderDialog.setMessage("收款成功");
//                    } else if (s.equals("err")) {
//                        builderDialog.setMessage("收款失败");
//                    } else if (s.equals("loading")) {
//                        builderDialog.setMessage("请提示用户输入支付密码，具体支付结果以微信通知的为准，如果较长时间未接收到微信的通知，您可以重新发起收款");
//                    }
//                    break;
//                case "TuiKuan":
//                    builderDialog.setTitle("退款状态");
//                    switch (s) {
//                        case "ok":
//                            builderDialog.setMessage("退款成功");
//                            //Toast.makeText(getActivity(), "退款成功", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "ban":
//                            builderDialog.setMessage("已经对过的账单禁止退款");
//                            //Toast.makeText(getActivity(), "已经对过的账单禁止退款", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "failure":
//                            builderDialog.setMessage("退款失败");
//                            //Toast.makeText(getActivity(), "退款失败", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "notExist":
//                            builderDialog.setMessage("该订单不存在");
//                            //Toast.makeText(getActivity(), "该订单不存在", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "err":
//                            builderDialog.setMessage("该订单已经退过款，不可以重新退款。");
//                            //Toast.makeText(getActivity(), "该订单已经退过款，不可以重新退款。", Toast.LENGTH_SHORT).show();
//                            break;
//                        default:
//                            break;
//                    }
//                    break;
//                case "HeXiao":
//                    builderDialog.setTitle("核销结果");
//                    switch (s){
//                        case "ok":
//                            builderDialog.setMessage("核销成功");
//                            break;
//                        case "not_authority":
//                            builderDialog.setMessage("此卡券不属于你的门店，你没权限核销");
//                            break;
//                        case "hasbeen":
//                            builderDialog.setMessage("此核销码不可以再核销");
//                            break;
//                        case "err":
//                            builderDialog.setMessage("出现未知错误");
//                            break;
//                    }
//                    break;
//                default:
//                    break;
//            }
//            builderDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    MipcaActivityCapture.this.finish();
//                }
//            });
//            builderDialog.create().show();
//        }
//    }
}