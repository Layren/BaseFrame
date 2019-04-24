package com.base.pickphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.base.R;
import com.base.baseClass.BaseActivity;
import com.base.config.BPConfig;
import com.base.config.RequestCode;
import com.base.util.ClipViewConfig;
import com.base.util.Tool;
import com.base.view.ClipViewLayout;

//头像上传
public class ClipImageActivity extends BaseActivity implements View.OnClickListener {

    private ClipViewLayout clipViewLayout;
    private TextView btnCancel;
    private TextView btnOk;
    private String path;
    private ClipViewConfig config;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_clip_image;
    }

    @Override
    protected void initView() {
        if (BPConfig.appThemeColor == 0xffffffff) {
            setWhiteHeaderView();
        }
        title.setText("移动和缩放");
        layout.setBackgroundColor(BPConfig.appThemeColor);
        path = getIntent().getStringExtra("path");
        config = (ClipViewConfig) getIntent().getSerializableExtra("config");
        clipViewLayout = findViewById(R.id.clipViewLayout);
        clipViewLayout.setClipType(config.getType())
                .setHorizontalPadding(config.getPadding())
                .setRatio(config.getRatio())
                .init(path);
        btnCancel = findViewById(R.id.btn_cancel);
        btnOk = findViewById(R.id.bt_ok);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            finish();
        } else if (i == R.id.bt_ok) {
            result();
        }
    }


    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private void result() {
        //调用返回剪切图
        Bitmap zoomedCropBitmap = clipViewLayout.clip();
        if (zoomedCropBitmap == null) {
            Log.e("ZoomedCropBitmap", "bitmap == null");
            return;
        }
        String oldPath = path.substring(path.lastIndexOf("/") + 1);
        if (oldPath.contains("."))
            oldPath = oldPath.substring(0, oldPath.lastIndexOf("."));
        String filePath = BPConfig.CACHE_IMG_PATH + "/" + oldPath + ".crop";
        Tool.saveCacheBitmapToFile(zoomedCropBitmap, oldPath + ".crop");
        Intent intent = new Intent();
        intent.putExtra("path", filePath);
        setResult(RequestCode.CROP_PHOTO_RESULT, intent);
        finishAnim();
    }
}
