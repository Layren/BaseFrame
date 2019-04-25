package com.base.pickphoto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.base.R;
import com.base.config.BPConfig;
import com.base.config.RequestCode;
import com.base.model.PhotoItem;
import com.base.model.PhotoSelectRecord;
import com.base.util.ClipViewConfig;
import com.base.util.Tool;
import com.base.view.PopWindows;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;


/**
 * 照片管理器
 */
public class PickPhoto implements PopWindows.PopWindowsViewOnCallk, View.OnClickListener {
    private Activity activity;
    private PopWindows popSetAvatar;

    private String photoName;
    private ArrayList<Object> list = new ArrayList<>();
    private LinkedHashSet<String> urls = new LinkedHashSet<>();
    private PickPhotoCall call;
    private boolean isSign;
    private boolean isTailor;
    private ClipViewConfig config;
    private int maxPage = 9;

    public PickPhoto(Activity activity) {
        this.activity = activity;
        initPickPhoto();
    }

    private void initPickPhoto() {
        popSetAvatar = new PopWindows(activity, R.layout.popwindow_photo_menu);
        popSetAvatar.initPopWindows(this);
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.tv_album_select_pop_window_photo_menu).setOnClickListener(this);
        view.findViewById(R.id.tv_photo_graph_pop_window_photo_menu).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel_pop_window_photo_menu).setOnClickListener(this);
    }

    /**
     * 设置拍照存放路径。默认SD卡/DCIM/Camera/
     *
     * @param path 路径
     */
    public void setImagePath(String path) {
        if (!TextUtils.isEmpty(path))
            BPConfig.cameraImgPath = path;
    }

    /**
     * 拍照并保存
     *
     * @param name 照片名称
     */
    private void photoGraph(String name) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(BPConfig.cameraImgPath, name);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, BPConfig.appplcaitonId + ".fileProvider", f);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            Uri u = Uri.fromFile(f);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
        }

        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        activity.startActivityForResult(intent, BPConfig.PHOTO_GRAPH);

    }

    /**
     * 设置是否单选
     */
    public PickPhoto setSign(boolean isSign) {
        this.isSign = isSign;
        return this;
    }

    /**
     * 相册选择
     */
    public void albumSelect(PickPhotoCall call) {
        Intent intent = new Intent(activity, PhotoWallActivity.class);
        intent.putExtra("sign", isSign);
        activity.startActivityForResult(intent, BPConfig.PIC_SELECT);
        this.call = call;
    }


    /**
     * 显示菜单
     *
     * @param call 选择图片回调
     */
    public void showMenu(PickPhotoCall call) {
        popSetAvatar.show(Gravity.BOTTOM);
        this.call = call;
    }

    /**
     * 显示菜单(单选)
     *
     * @param call 选择图片回调
     */
    public void showMenuSign(PickPhotoCall call) {
        popSetAvatar.show(Gravity.BOTTOM);
        this.call = call;
        this.isSign = true;
    }

    /**
     * 显示菜单(裁剪)
     *
     * @param call 选择图片回调
     */
    public void showMenuTailor(PickPhotoCall call, ClipViewConfig config) {
        popSetAvatar.show(Gravity.BOTTOM);
        this.call = call;
        this.isSign = true;
        this.isTailor = true;
        this.config = config;
    }

    public void setResult(int requestCode, int resultCode, Intent data) {
        list.clear();
        urls.clear();
        switch (requestCode) {
            case BPConfig.PIC_SELECT:
                if (data == null)
                    break;
                int code = data.getIntExtra("code", -1);
                if (code != BPConfig.BACK_CODE) {
                    break;
                }
                // 返回的图片路径 list
                ArrayList<String> paths = data.getStringArrayListExtra("paths");
                if (paths != null) {
                    for (String path : paths) {
                        String imgPath = Tool.getPath(path);
                        addImage(imgPath);
                        if (urls.size() >= maxPage)
                            break;
                    }
                }
                break;
            case BPConfig.PHOTO_GRAPH:
                if (resultCode == Activity.RESULT_OK)
                    addImage(Tool.getPath(BPConfig.cameraImgPath + photoName));
                break;
            case RequestCode.CROP_PHOTO:
                if (resultCode == RequestCode.CROP_PHOTO_RESULT) {
                    addImage(data.getStringExtra("path"));
                }
                break;
            default:
                break;
        }
        if (isTailor && resultCode != RequestCode.CROP_PHOTO_RESULT) {
            Intent intent = new Intent(activity, ClipImageActivity.class);
            if (list.isEmpty()) return;
            intent.putExtra("path", ((PhotoItem) list.get(list.size() - 1)).getUrl());
            intent.putExtra("config", config);
            activity.startActivityForResult(intent, RequestCode.CROP_PHOTO);
            return;
        }
        PhotoSelectRecord psr = new PhotoSelectRecord();
        psr.setItemList(list);
        psr.setUrls(urls);

        if (call != null)
            call.onBack(psr);

    }

    private void addImage(String path) {
        if (!urls.add(path)) return;
        PhotoItem item = new PhotoItem();
        item.setUrl(path);
        list.add(item);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_photo_graph_pop_window_photo_menu) {
            photoName = Tool.getPhotoName();
            photoGraph(photoName);
            popSetAvatar.close();
        } else if (i == R.id.tv_album_select_pop_window_photo_menu) {
            albumSelect(call);
            popSetAvatar.close();
        } else if (i == R.id.tv_cancel_pop_window_photo_menu) {
            popSetAvatar.close();
        }
    }

    public void setMaxPage(int maxPage) {
        if (maxPage < 1)
            maxPage = 9;
        this.maxPage = maxPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public interface PickPhotoCall {
        void onBack(PhotoSelectRecord psr);
    }
}
