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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.R;
import com.base.adapter.PhotoAlbumLVAdapter;
import com.base.config.BPConfig;
import com.base.model.PhotoAlbumLVItem;
import com.base.util.Tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;


public class PhotoAlbumActivity extends Activity {
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_album);
        layout = findViewById(R.id.ll_comm_topbar);
        layout.setBackgroundColor(BPConfig.APP_THEME_COLOR);
        Intent t = getIntent();
        if (!t.hasExtra("latest_count")) {
            return;
        }

        TextView titleTV = findViewById(R.id.topbar_title_tv);
        titleTV.setText(R.string.select_album);

        Button cancelBtn = findViewById(R.id.topbar_right_btn);
        cancelBtn.setText(R.string.main_cancel);
        cancelBtn.setVisibility(View.VISIBLE);

        ListView listView = findViewById(R.id.select_img_listView);

        final ArrayList<PhotoAlbumLVItem> list = new ArrayList<>();
        list.add(
                new PhotoAlbumLVItem(getResources().getString(R.string.latest_image), t.getIntExtra("latest_count", -1), t.getStringExtra("latest_first_img")));
        list.addAll(getImagePathsByContentProvider());

        PhotoAlbumLVAdapter adapter = new PhotoAlbumLVAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PhotoAlbumActivity.this, PhotoWallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                if (position == 0) {
                    intent.putExtra("code", 200);
                } else {
                    intent.putExtra("code", BPConfig.BACK_CODE);
                    intent.putExtra("folderPath", list.get(position).getPathName());
                }
                setResult(BPConfig.PHOTO_ALBUM, intent);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消，回到主页面
                backAction();
            }
        });
    }

    private void backAction() {
        setResult(BPConfig.PHOTO_ALBUM, null);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backAction();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private int getImageCount(File folder) {
        int count = 0;
        File[] files = folder.listFiles();
        for (File file : files) {
            if (Tool.isImage(file.getName())) {
                count++;
            }
        }

        return count;
    }

    private String getFirstImagePath(File folder) {
        File[] files = folder.listFiles();
        for (int i = files.length - 1; i >= 0; i--) {
            File file = files[i];
            if (Tool.isImage(file.getName())) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    private ArrayList<PhotoAlbumLVItem> getImagePathsByContentProvider() {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA}, key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<PhotoAlbumLVItem> list = null;
        if (cursor != null) {
            if (cursor.moveToLast()) {
                HashSet<String> cachePath = new HashSet<String>();
                list = new ArrayList<PhotoAlbumLVItem>();

                while (true) {
                    String imagePath = cursor.getString(0);

                    File parentFile = new File(imagePath).getParentFile();
                    String parentPath = parentFile.getAbsolutePath();

                    if (!cachePath.contains(parentPath)) {
                        list.add(new PhotoAlbumLVItem(parentPath, getImageCount(parentFile), getFirstImagePath(parentFile)));
                        cachePath.add(parentPath);
                    }

                    if (!cursor.moveToPrevious()) {
                        break;
                    }
                }
            }

            cursor.close();
        }

        return list;

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
    }

}
