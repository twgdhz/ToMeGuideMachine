package com.uuzuche.lib_zxing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.uuzuche.lib_zxing.R;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.decoding.CaptureActivityHandler;
import com.uuzuche.lib_zxing.decoding.InactivityTimer;
import com.uuzuche.lib_zxing.decoding.RGBLuminanceSource;
import com.uuzuche.lib_zxing.event.EventSweepMessage;
import com.uuzuche.lib_zxing.view.ViewfinderView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 自定义实现的扫描Fragment
 */
public class CaptureFragment extends Fragment implements SurfaceHolder.Callback {

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
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private CodeUtils.AnalyzeCallback analyzeCallback;
    private Camera camera;
    private RadioGroup rgBottom;
    private RadioButton rbSweepCode;
    private RadioButton rbInput;
    private ImageView imgBack;
    private TextView tvAlbum;
    private EditText etCodeNumber;
    private LinearLayout llInput;
    private TextView tvHint;
    private Button btnEnsure;
    private ImageView imgTorch;
    private boolean isOn = true;


    private final int REQUEST_CODE_TAKE = 6;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;
    private String picLocalPath;
    private static final int PARSE_BARCODE_FAIL = 303;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CameraManager.init(getActivity().getApplication());

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this.getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        View view = null;
        if (bundle != null) {
            int layoutId = bundle.getInt(CodeUtils.LAYOUT_ID);
            if (layoutId != -1) {
                view = inflater.inflate(layoutId, null);
            }
        }

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_capture, null);
        }

        viewfinderView = (ViewfinderView) view.findViewById(R.id.viewfinder_view);
        surfaceView = (SurfaceView) view.findViewById(R.id.preview_view);
        surfaceHolder = surfaceView.getHolder();
        rbSweepCode = (RadioButton) view.findViewById(R.id.rb_sweep_code);
        rbInput = (RadioButton) view.findViewById(R.id.rb_sweep_input);
        imgBack = (ImageView) view.findViewById(R.id.img_back);
        tvAlbum = (TextView) view.findViewById(R.id.tv_album);
        llInput = (LinearLayout) view.findViewById(R.id.ll_input);
        rgBottom = (RadioGroup) view.findViewById(R.id.rg_bottom);
        tvHint = (TextView) view.findViewById(R.id.tv_hint);
        etCodeNumber = (EditText) view.findViewById(R.id.et_code_number);
        btnEnsure = (Button) view.findViewById(R.id.btn_ensure);
        imgTorch = (ImageView) view.findViewById(R.id.img_torch);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rgBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radiobutton = (RadioButton) getActivity().findViewById(group.getCheckedRadioButtonId());
                if (radiobutton.getText().toString().equals("扫码")) {
                    llInput.setVisibility(View.GONE);
                    viewfinderView.setVisibility(View.VISIBLE);
                    tvHint.setVisibility(View.VISIBLE);
                    imgTorch.setVisibility(View.VISIBLE);
                } else if (radiobutton.getText().toString().equals("设备号")) {
                    rbSweepCode.setChecked(false);
                    llInput.setVisibility(View.VISIBLE);
                    viewfinderView.setVisibility(View.GONE);
                    tvHint.setVisibility(View.GONE);
                    imgTorch.setVisibility(View.GONE);
//                    llInput.getVisibility()==View.VISIBLE;
                }
            }
        });
        tvAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadAvatarBySendLocalPic();
            }
        });
        btnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etCodeNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "订单号：" + etCodeNumber.getText().toString(), Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new EventSweepMessage("1", etCodeNumber.getText().toString()));
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "请输入订单号", Toast.LENGTH_LONG).show();
                }
            }
        });
        imgTorch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOn) {
                    isOn = false;
//                    imgTorch.setBackground(getResources().getDrawable(R.drawable.torch_pressed));
                    imgTorch.setBackgroundResource(R.drawable.torch_pressed);
//                    try {
//                        CameraManager.get().openDriver(surfaceHolder);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    CameraManager.get().openLight();   //开闪光灯
                } else {
                    isOn = true;
//                    imgTorch.setBackground(getResources().getDrawable(R.drawable.torch_default));
                    imgTorch.setBackgroundResource(R.drawable.torch_default);
//                    CameraManager.get().closeDriver();
                    CameraManager.get().closeLight();   //开闪光灯
                }
            }
        });
        int scale = (int) getContext().getResources().getDisplayMetrics().density;// 屏幕密度
//        px = (int)(dp*scale+0.5f);//px转dp

        Drawable top = getResources().getDrawable(R.drawable.sweep_code);
        top.setBounds(0, 0, (int) (35 * scale + 0.5f), (int) (35 * scale + 0.5f));
        rbSweepCode.setCompoundDrawables(null, top, null, null);
        Drawable top1 = getResources().getDrawable(R.drawable.input);
        top1.setBounds(0, 0, (int) (35 * scale + 0.5f), (int) (35 * scale + 0.5f));
        rbInput.setCompoundDrawables(null, top1, null, null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        if (result == null || TextUtils.isEmpty(result.getText())) {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeFailed();
            }
        } else {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeSuccess(barcode, result.getText());
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            camera = CameraManager.get().getCamera();
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet, viewfinderView);
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
        if (camera != null) {
            if (camera != null && CameraManager.get().isPreviewing()) {
                if (!CameraManager.get().isUseOneShotPreviewCallback()) {
                    camera.setPreviewCallback(null);
                }
                camera.stopPreview();
                CameraManager.get().getPreviewCallback().setHandler(null, 0);
                CameraManager.get().getAutoFocusCallback().setHandler(null, 0);
                CameraManager.get().setPreviewing(false);
            }
        }
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
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
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
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
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

    public CodeUtils.AnalyzeCallback getAnalyzeCallback() {
        return analyzeCallback;
    }

    public void setAnalyzeCallback(CodeUtils.AnalyzeCallback analyzeCallback) {
        this.analyzeCallback = analyzeCallback;
    }

    /**
     * 打开相册
     */
    private void upLoadAvatarBySendLocalPic() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        }
        {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_TAKE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_TAKE:
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        upLoadByUri(selectedImage);
                    }
                }
                break;
        }

        photo_path = picLocalPath;

        System.out.println("-----" + photo_path);

        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("正在扫描...");
        mProgress.setCancelable(false);
        mProgress.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Result result = scanningImage(photo_path);
                if (result != null) {
                    dealWithMessage(result);
                    mProgress.dismiss();
                    getActivity().finish();
                } else {
                    mProgress.dismiss();
                    Message m = mHandler.obtainMessage();
                    m.what = PARSE_BARCODE_FAIL;
                    mHandler.sendMessage(m);
                }
            }
        }).start();
        //}
    }

    protected void upLoadByUri(Uri selectedImage) {
        File file = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picLocalPath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picLocalPath == null || picLocalPath.equals("null")) {
                Toast toast = Toast.makeText(getContext(), "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }

        } else {
            file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getContext(), "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            picLocalPath = file.getAbsolutePath();
        }

        if (picLocalPath == null) {
            Toast toast = Toast.makeText(getContext(), "失败", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private void dealWithMessage(Result obj) {//处理扫描二维码得到的信息
        String Barcode = obj.getBarcodeFormat().toString();
        String Text = obj.getText().toString();
        Log.d("处理扫描二维码得到的信息", Text);
        EventBus.getDefault().post(new EventSweepMessage("1", Text));

//        if ("QR_CODE".equals(Barcode) || "DATA_MATRIX".equals(Barcode)) {
//            if (Text.length() > 7) {
//                String s = Text.substring(0, 7);
//                if ("http://".equals(s)) {
//                    Intent viewIntent = new Intent(
//                            "android.intent.action.VIEW", Uri.parse(Text));
//                    startActivity(viewIntent);
//
//                }
//            }

//        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mProgress.dismiss();
            switch (msg.what) {
                case PARSE_BARCODE_FAIL:
                    Toast.makeText(getContext(), "无法识别", Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };
    /**
     * 扫描二维码图片的方法
     *
     * @param path
     * @return
     */
    MultiFormatReader multiFormatReader;

    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        multiFormatReader = new MultiFormatReader();

        //BufferedImage image =null;
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        multiFormatReader.setHints(hints);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        int width = scanBitmap.getWidth();
        int height = scanBitmap.getHeight();
//		PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(Bitmap2Bytes(scanBitmap), width, height, height, height, height, height);
//	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            return multiFormatReader.decodeWithState(bitmap1);
        } catch (ReaderException re) {
            // continue
        } finally {
            multiFormatReader.reset();
        }
//		QRCodeReader reader = new QRCodeReader();
//		try {
//			//return new MultiFormatReader().decode(bitmap1,hints);
//			return reader.decode(bitmap1, hints);
//
//		} catch (NotFoundException e) {
//			e.printStackTrace();
//		} catch (ChecksumException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        return null;
    }


}
