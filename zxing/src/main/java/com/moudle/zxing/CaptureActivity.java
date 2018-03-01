/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moudle.zxing;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.zxing.Result;
import com.moudle.zxing.camera.CameraManager;
import com.moudle.zxing.decode.DecodeThread;
import com.moudle.zxing.utils.BeepManager;
import com.moudle.zxing.utils.CaptureActivityHandler;
import com.moudle.zxing.utils.DimenUtil;
import com.moudle.zxing.utils.InactivityTimer;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by lizhaotailang on 2017/2/13.
 */

public class CaptureActivity extends AppCompatActivity
        implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private FrameLayout fullScreen;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private Rect mCropRect = null;
    private ImageView capture_mask_bottom;
    public Handler getHandler() {
        return handler;
    }
    public CameraManager getCameraManager() {
        return cameraManager;
    }
    private boolean isHasSurface = false;
    private boolean isLighting;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        initView();
        initDimen();
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
        capture_mask_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraManager.isFlashlightOn(isLighting)) {
                    cameraManager.Closeshoudian();
                    capture_mask_bottom.setImageResource(R.mipmap.light_select);
                } else {
                    cameraManager.Openshoudian();
                    capture_mask_bottom.setImageResource(R.mipmap.light_normal);
                }
            }
        });

    }

    public void yes(){
        cameraManager = new CameraManager(getApplication());
        handler = null;
        if (isHasSurface) {
            initCamera(scanPreview.getHolder());
        } else {
            scanPreview.getHolder().addCallback(this);
        }
        inactivityTimer.onResume();
    }




    @Override
    protected void onResume() {
        super.onResume();
        yes();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        if (null!=cameraManager)
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        Intent resultIntent = new Intent();
        bundle.putInt("width", mCropRect.width());
        bundle.putInt("height", mCropRect.height());
        bundle.putString("result", rawResult.getText());
        resultIntent.putExtras(bundle);

        this.setResult(RESULT_OK, resultIntent);
        this.finish();
    }

    /**
     * Init the camera.
     *
     * @param surfaceHolder The surface holder.
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(getString(R.string.unable_to_open_camera));
        dialog.setTitle(getString(R.string.error));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * Init the interception rectangle area
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        int x = cropLeft * cameraWidth / containerWidth;
        int y = cropTop * cameraHeight / containerHeight;

        int width = cropWidth * cameraWidth / containerWidth;
        int height = cropHeight * cameraHeight / containerHeight;

        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 适配
     */
    protected void initDimen() {
        String flag = getIntent().getStringExtra(CameraManager.FLAG);
        DimenUtil instance = DimenUtil.getInstance(this);
        int imageSize = instance.getDimen(30);
        int viewSize = instance.getDimen(150);
        int textSize = instance.getDimen(120);
        fullScreen = findViewById(R.id.full_screen);
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        TextView capture_mask_top = findViewById(R.id.capture_mask_top);
        capture_mask_bottom = (ImageView) findViewById(R.id.capture_mask_bottom);
        View left_top = findViewById(R.id.left_top);
        View left_bottom = findViewById(R.id.left_bottom);
        View right_top = findViewById(R.id.right_top);
        View right_bottom = findViewById(R.id.right_bottom);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageSize, imageSize);
        left_top.setLayoutParams(layoutParams);
        layoutParams = new RelativeLayout.LayoutParams(imageSize, imageSize);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        right_top.setLayoutParams(layoutParams);
        layoutParams = new RelativeLayout.LayoutParams(imageSize, imageSize);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        right_bottom.setLayoutParams(layoutParams);
        layoutParams = new RelativeLayout.LayoutParams(imageSize, imageSize);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        left_bottom.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(viewSize, viewSize);
        layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.addRule(RelativeLayout.BELOW,R.id.capture_mask_top);
        scanCropView.setLayoutParams(layout);
        RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, textSize);
        capture_mask_top.setLayoutParams(textParam);
        flag = TextUtils.isEmpty(flag)?"请扫描二维码":flag;
        capture_mask_top.setText(flag);
        DimenUtil.getInstance(this).setTextSize(14,capture_mask_top);
    }

    protected void initView() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scan);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.full_screen) finish();
    }
}
