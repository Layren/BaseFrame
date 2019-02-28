package com.base.pickphoto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.R;
import com.base.adapter.PhotoWallAdapter;
import com.base.config.BPConfig;
import com.base.util.Tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoWallActivity extends Activity {
    private TextView titleTV;

    private ArrayList<String> list;
    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;

    private String currentFolder = null;
    private RelativeLayout layout;
    private boolean isSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_wall);
        isSign = getIntent().getBooleanExtra("sign", isSign);
        titleTV = findViewById(R.id.topbar_title_tv);
        titleTV.setText(R.string.latest_image);
        layout = findViewById(R.id.ll_comm_topbar);
        layout.setBackgroundColor(BPConfig.appThemeColor);

        Button backBtn = findViewById(R.id.topbar_left_btn);
        Button confirmBtn = findViewById(R.id.topbar_right_btn);
        backBtn.setText(R.string.photo_album);
        backBtn.setVisibility(View.VISIBLE);
        confirmBtn.setText(R.string.main_confirm);
        confirmBtn.setVisibility(View.VISIBLE);

        mPhotoWall = findViewById(R.id.photo_wall_grid);
        list = getLatestImagePaths(100);
        adapter = new PhotoWallAdapter(this, list);
        adapter.setSign(isSign);
        mPhotoWall.setAdapter(adapter);
        mPhotoWall.setOnItemClickListener((parent, view, position, id) -> {
            if (isSign) {
                ArrayList<String> paths = new ArrayList<>();
                paths.add(list.get(position));
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("code", BPConfig.BACK_CODE);
                intent.putStringArrayListExtra("paths", paths);
                setResult(BPConfig.PIC_SELECT, intent);
                finish();
            }
        });
        if (isSign) {
            confirmBtn.setVisibility(View.GONE);
        }
        confirmBtn.setOnClickListener(v -> {
            ArrayList<String> paths = getSelectImagePaths();

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("code", paths != null ? BPConfig.BACK_CODE : 101);
            intent.putStringArrayListExtra("paths", paths);
            setResult(BPConfig.PIC_SELECT, intent);
            finish();
        });
        backBtn.setOnClickListener(v -> backAction());

    }

    @SuppressLint("NewApi")
    private void backAction() {
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        if (list != null && !list.isEmpty()) {
            ArrayList<String> lists = getLatestImagePaths(100);
            if (lists != null)
                intent.putExtra("latest_count", lists.size());
            else
                intent.putExtra("latest_count", 0);
            intent.putExtra("latest_first_img", list.get(0));
        }

        startActivityForResult(intent, BPConfig.PHOTO_ALBUM);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backAction();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void updateView(int code, String folderPath) {
        list.clear();
        adapter.clearSelects();
        if (code == BPConfig.BACK_CODE) {
            int lastSeparator = folderPath.lastIndexOf(File.separator);
            String folderName = folderPath.substring(lastSeparator + 1);
            titleTV.setText(folderName);
            list.addAll(getAllImagePathsByFolder(folderPath));
        } else {
            list.addAll(getLatestImagePaths(100));
        }
        adapter.notifyDataSetChanged();
    }

    private ArrayList<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return new ArrayList<>();
        }
        ArrayList<String> imageFilePaths = new ArrayList<>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (Tool.isImage(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }
        return imageFilePaths;
    }

    private ArrayList<String> getLatestImagePaths(int maxCount) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String keyMimeType = MediaStore.Images.Media.MIME_TYPE;
        String keyData = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{keyData}, keyMimeType + "=? or " + keyMimeType + "=? or " + keyMimeType + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<String> latestImagePaths = null;
        if (cursor != null) {
            if (cursor.moveToLast()) {
                latestImagePaths = new ArrayList<>();
                while (true) {
                    String path = cursor.getString(0);
                    latestImagePaths.add(path);
                    if (latestImagePaths.size() >= maxCount || !cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestImagePaths;
    }

    private ArrayList<String> getSelectImagePaths() {
        List<Integer> selects = adapter.getSelects();
        if (selects.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<String> selectedImageList = new ArrayList<>();
        for (int i : selects) {
            selectedImageList.add(list.get(i));
        }
        return selectedImageList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == BPConfig.PHOTO_ALBUM) {
            if (intent == null) {
                setResult(BPConfig.PIC_SELECT, null);
                finish();
                return;
            }
            overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
            int code = intent.getIntExtra("code", -1);
            if (code == BPConfig.BACK_CODE) {
                String folderPath = intent.getStringExtra("folderPath");
                if (folderPath != null && !folderPath.equals(currentFolder)) {
                    currentFolder = folderPath;
                    updateView(BPConfig.BACK_CODE, currentFolder);
                }
            } else if (code == 200) {
                updateView(200, "");
            }

        }

    }

}
