package com.base.pickphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BPApplication;
import com.base.baseClass.BaseActivity;
import com.base.config.RequestCode;
import com.base.galleryview.GalleryView;
import com.base.model.PhotoItem;
import com.base.model.PhotoSelectRecord;
import com.base.net.MCBaseAPI;
import com.base.R;
import com.base.view.ItemDecoration;
import com.base.view.RefreshRecyclerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * 类：添加图片界面
 */
public class AddPhotoActivity extends BaseActivity implements OnClickListener {

    private RefreshRecyclerView recyclerView;
    private ArrayList<Object> list = new ArrayList<>();
    private LinkedHashSet<String> urls = new LinkedHashSet<>();
    private ArrayList<InputStream> streams = new ArrayList<>();
    private int maxPage = 9;
    private PickPhoto pickPhoto;
    private PickPhoto.PickPhotoCall call;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addphoto;
    }

    @Override
    protected void initView() {
        initTop();
        init();
        initPop();
    }

    private void initTop() {
        title.setText("添加图片");
        back.setOnClickListener(this);
        rightTv.setText("保存");
        rightTv.setOnClickListener(this);
    }

    private void init() {
        TextView hint = findViewById(R.id.tv_hint_addphoto);
        PhotoSelectRecord photoSR = (PhotoSelectRecord) getIntent().getSerializableExtra("PhotoSelectRecord");
        maxPage = getIntent().getIntExtra("maxPage", 9);
        hint.setText(getIntent().getStringExtra("hint"));
        if (photoSR != null) {
            list.clear();
            list.addAll(photoSR.getItemList());
            urls.clear();
            urls.addAll(photoSR.getUrls());
        } else {
            list.add(new PhotoItem());
        }
        final GalleryView galleryView = new GalleryView(this);
        recyclerView = findViewById(R.id.recycler_add_photo);
        recyclerView.addItemDecoration(new ItemDecoration(1, Color.GRAY));
        setRecyclerViewAdapter(recyclerView, R.layout.item_addphoto);
        recyclerView.setData(list);
        recyclerView.getAdapter().setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.item_event_photo_delete) {
                list.remove(position);
                urls.remove(((PhotoItem) list.get(position - 1)).getUrl());
                notifyData();
            } else if (view.getId() == R.id.item_event_photo_img) {
                if (position == list.size() - 1) {
                    if (urls.size() >= maxPage) {
                        showToast("最多选择" + maxPage + "张");
                        return;
                    }
                    pickPhoto.showMenu(call);
                    return;
                }
                ArrayList<PhotoItem> lists = new ArrayList<>();
                for (int i = 0; i < list.size() - 1; i++) {
                    PhotoItem item = (PhotoItem) list.get(i);
                    PhotoItem item2 = new PhotoItem();
                    item2.setNet(item.isNet());
                    item2.setUrl(MCBaseAPI.API_FILES + item.getUrl());
                    lists.add(item2);
                }
                galleryView.onClickImage(lists, position);
            }
        });
    }

    private void notifyData() {
        streams.clear();
        recyclerView.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_back_iclude_header) {
            finishAnim();
        } else if (i == R.id.tv_right_include_header) {
            save();
        }
    }

    private void initPop() {
        pickPhoto = new PickPhoto(this);
        pickPhoto.setImagePath(BPApplication.getCachePath());
        call = psr -> {
            urls.clear();
            list.clear();
            urls.addAll(psr.getUrls());
            list.addAll(psr.getItemList());
            notifyData();
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        pickPhoto.setResult(requestCode, resultCode, data);
    }

    private void save() {
        PhotoSelectRecord psr = new PhotoSelectRecord();
        psr.setItemList(list);
        psr.setUrls(urls);
        psr.setStreams(streams);
        Intent data = new Intent();
        data.putExtra("PhotoSelectRecord", psr);
        setResult(RequestCode.ADD_PHOTO_RESULT, data);
        finishAnim();

    }

    @Override
    public void setHolder(final BaseViewHolder holder, Object item) {
        super.setHolder(holder, item);
        int size = (screenWidth - 6) / 3;
        final ImageView img = holder.getView(R.id.item_event_photo_img);
        holder.addOnClickListener(R.id.item_event_photo_delete);
        holder.addOnClickListener(R.id.item_event_photo_img);
        if (item.equals(recyclerView.getLastItem())) {
            Picasso.get().load(R.drawable.img_select).resize(size, size).centerCrop().into(img);
            holder.setVisible(R.id.item_event_photo_delete, false);
        } else {
            holder.setVisible(R.id.item_event_photo_delete, true);
            PhotoItem photoItem = (PhotoItem) item;
            if (photoItem.getUrl() == null)
                return;
            if (photoItem.isNet()) {
                String url = MCBaseAPI.API_FILES + photoItem.getUrl();
                Picasso.get().load(url).resize(size, size).centerCrop().into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bm = ((BitmapDrawable) img.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                        streams.add(isBm);
                        bm.recycle();
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
            } else {
                streams.add(null);
                File image = new File(photoItem.getUrl());
                Picasso.get().load(image).resize(size, size).centerCrop().into(img);
            }
        }
    }
}
