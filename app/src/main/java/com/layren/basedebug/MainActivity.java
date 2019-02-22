package com.layren.basedebug;


import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.adapter.RefreshViewAdapter;
import com.base.baseClass.BaseActivity;
import com.base.interfaces.RefreshViewAdapterListener;
import com.base.model.MultiModel;
import com.base.pickphoto.AddPhotoActivity;
import com.base.util.PermissionManager;
import com.base.view.ItemDecoration;
import com.base.view.NestedRecyclerView;
import com.base.view.RefreshRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by GaoTing on 2018/9/26.
 *
 * @TODO :
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.recycler)
    RefreshRecyclerView recyclerView;
    @BindView(R.id.recyclers)
    NestedRecyclerView recyclerViews;
    private SoundPool soundPool;
    private int soundId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
        boolean isStorage = PermissionManager.Query(this, PermissionManager.STORAGE);
        if (!isStorage) {
            PermissionManager.Granted(this, PermissionManager.STORAGE, 1);
        }
        findViewById(R.id.text_v).setOnClickListener(v -> goActivity(AddPhotoActivity.class));
        recyclerView.setMultiAdapter(((holder, item, itemType) -> {
            MultiItemMudle mudle = (MultiItemMudle) item;
            switch (itemType) {
                case 0:
                    holder.setText(R.id.textRed, mudle.getText());
                    break;
                case 1:
                    holder.setText(R.id.textBlue, mudle.getText());
                    break;
            }
        }), R.layout.red, R.layout.blue);
        recyclerView.addItemDecoration(new ItemDecoration(1, Color.BLUE));
        List<Object> list = new ArrayList<>();
        list.add("11111111");
        list.add("12222222");
        list.add("12333333");
        list.add("12344444");
        list.add("12345555");
        list.add("12345666");
        list.add("12345677 ");
        List<MultiItemMudle> list2 = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            MultiItemMudle mudle = new MultiItemMudle();
            if (i < 1 || i == 5) {
                mudle.setItemType(0);
                mudle.setSpanSize(3);
            } else
                mudle.setItemType(1);
            mudle.setText("====" + i + "====");
            list2.add(mudle);
        }
        recyclerView.setMulitData(list2);
        recyclerView.setLoadMoreEnd();
        RefreshViewAdapter adapter = new RefreshViewAdapter(R.layout.blue, new RefreshViewAdapterListener() {
            @Override
            public void setHolder(int layoutResId, BaseViewHolder holder, Object item) {
                holder.setText(R.id.textBlue, (String) item);
                holder.setBackgroundColor(R.id.textBlue, Color.WHITE);
                holder.setTextColor(R.id.textBlue, Color.BLUE);
            }
        });
        recyclerViews.setAdapter(adapter);
        recyclerViews.addItemDecoration(new ItemDecoration(1, Color.RED));
        adapter.setNewData(list);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setHolder(BaseViewHolder holder, MultiModel item, int itemType) {
        super.setHolder(holder, item, itemType);
        MultiItemMudle mudle = (MultiItemMudle) item;
        switch (itemType) {
            case 0:
                holder.setText(R.id.textRed, mudle.getText());
                break;
            case 1:
                holder.setText(R.id.textBlue, mudle.getText());
                break;
        }
    }


}
