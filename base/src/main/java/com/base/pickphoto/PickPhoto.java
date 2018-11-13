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
import com.base.model.PhotoItem;
import com.base.model.PhotoSelectRecord;
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
    private LinkedHashSet<String> Urls = new LinkedHashSet<>();
    private PickPhotoCall call;
    private static boolean isSign;
    private int MaxPage = 9;

    public PickPhoto(Activity activity) {
        this.activity = activity;
        initPickPhoto();
        list.add(new PhotoItem());
    }

    public static boolean isSign() {
        return isSign;
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
            BPConfig.CAMERA_IMG_PATH = path;
    }

    /**
     * 拍照并保存
     *
     * @param name 照片名称
     */
    private void PhotoGraph(String name) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(BPConfig.CAMERA_IMG_PATH, name);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, BPConfig.APPLICATION_ID + ".fileProvider", f);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            Uri u = Uri.fromFile(f);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
        }

        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        activity.startActivityForResult(intent, BPConfig.PHONTO_GRAPH);

    }

    /**
     * 相册选择
     */
    private void AlbumSelect() {
        Intent intent = new Intent(activity, PhotoWallActivity.class);
        activity.startActivityForResult(intent, BPConfig.PIC_SELECT);
    }


    /**
     * 显示菜单
     *
     * @param call 选择图片回调
     */
    public void showMenu(PickPhotoCall call) {
        popSetAvatar.show(Gravity.BOTTOM);
        this.call = call;
        this.isSign = false;
    }

    /**
     * 显示菜单
     *
     * @param isSign 是否单选
     * @param call   选择图片回调
     */
    public void showMenu(boolean isSign, PickPhotoCall call) {
        popSetAvatar.show(Gravity.BOTTOM);
        this.call = call;
        this.isSign = isSign;
    }

    public void setResult(int requestCode, int resultCode, Intent data) {
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
                if (paths != null && paths.size() > 0) {
                    // 添加，去重
                    for (String path : paths) {
                        // 去除重复
                        String imgPath = Tool.getPath(path);
                        AddImage(imgPath);
                        if (Urls.size() >= MaxPage)
                            break;
                    }
                }
                break;
            case BPConfig.PHONTO_GRAPH:
                if (resultCode == Activity.RESULT_OK)
                    AddImage(Tool.getPath(BPConfig.CAMERA_IMG_PATH + photoName));
                break;
        }

        PhotoSelectRecord psr = new PhotoSelectRecord();
        psr.setItemList(list);
        psr.setUrls(Urls);

        if (call != null)
            call.onBack(psr);

    }

    private void AddImage(String path) {
        if (!Urls.add(path)) return;
        PhotoItem item = new PhotoItem();
        item.setUrl(path);
        list.add(list.size() - 1, item);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_photo_graph_pop_window_photo_menu) {
            photoName = Tool.getPhotoName();
            PhotoGraph(photoName);
            popSetAvatar.close();
        } else if (i == R.id.tv_album_select_pop_window_photo_menu) {
            AlbumSelect();
            popSetAvatar.close();
        } else if (i == R.id.tv_cancel_pop_window_photo_menu) {
            popSetAvatar.close();
        }
    }

    public void setMaxPage(int maxPage) {
        if (maxPage < 1)
            maxPage = 9;
        this.MaxPage = maxPage;
    }

    public int getMaxPage() {
        return MaxPage;
    }

    public interface PickPhotoCall {
        void onBack(PhotoSelectRecord psr);
    }
}
