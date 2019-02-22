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
import android.widget.AdapterView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_wall);

        titleTV = findViewById(R.id.topbar_title_tv);
        titleTV.setText(R.string.latest_image);
        layout = findViewById(R.id.ll_comm_topbar);
        layout.setBackgroundColor(BPConfig.APP_THEME_COLOR);

        Button backBtn = findViewById(R.id.topbar_left_btn);
        Button confirmBtn = findViewById(R.id.topbar_right_btn);
        backBtn.setText(R.string.photo_album);
        backBtn.setVisibility(View.VISIBLE);
        confirmBtn.setText(R.string.main_confirm);
        confirmBtn.setVisibility(View.VISIBLE);

        mPhotoWall = findViewById(R.id.photo_wall_grid);
        list = getLatestImagePaths(100);
        adapter = new PhotoWallAdapter(this, list);
        mPhotoWall.setAdapter(adapter);
        mPhotoWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (PickPhoto.isSign()) {
                    ArrayList<String> paths = new ArrayList<>();
                    paths.add(list.get(position));
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("code", BPConfig.BACK_CODE);
                    intent.putStringArrayListExtra("paths", paths);
                    // startActivity(intent);
                    setResult(BPConfig.PIC_SELECT, intent);
                    finish();
                }
            }
        });
        if (PickPhoto.isSign()) {
            confirmBtn.setVisibility(View.GONE);
        }
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> paths = getSelectImagePaths();

                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("code", paths != null ? BPConfig.BACK_CODE : 101);
                intent.putStringArrayListExtra("paths", paths);
                // startActivity(intent);
                setResult(BPConfig.PIC_SELECT, intent);
                finish();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
            }
        });

    }

    @SuppressLint("NewApi")
    private void backAction() {
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        if (list != null && list.size() > 0) {
            intent.putExtra("latest_count", getLatestImagePaths(100).size());
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
            return null;
        }
        ArrayList<String> imageFilePaths = new ArrayList<String>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (Tool.isImage(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }
        return imageFilePaths;
    }

    private ArrayList<String> getLatestImagePaths(int maxCount) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA}, key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
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
        if (selects.size() == 0) {
            return null;
        }
        ArrayList<String> selectedImageList = new ArrayList<String>();
        for (int i : selects) {
            selectedImageList.add(list.get(i));
        }
        return selectedImageList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case BPConfig.PHOTO_ALBUM:
                if (intent == null) {
                    setResult(BPConfig.PIC_SELECT, null);
                    finish();
                    break;
                }
                overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                int code = intent.getIntExtra("code", -1);
                switch (code) {
                    case BPConfig.BACK_CODE:
                        String folderPath = intent.getStringExtra("folderPath");
                        if (folderPath != null && !folderPath.equals(currentFolder)) {
                            currentFolder = folderPath;
                            updateView(BPConfig.BACK_CODE, currentFolder);
                        }
                        break;
                    case 200:
                        updateView(200, null);
                        break;
                }
                break;
        }

    }

}
