package com.layren.basedebug;


import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.base.baseClass.BaseActivity;
import com.base.pickphoto.PickPhoto;
import com.base.view.ItemDecoration;
import com.base.view.NestedRecyclerView;
import com.base.view.RefreshRecyclerView;
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

    private PickPhoto pickPhoto;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
        setRecyclerViewAdapter(recyclerView, R.layout.red);
        recyclerView.addItemDecoration(new ItemDecoration(1, Color.BLUE));
        recyclerView.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData(0);
            }

            @Override
            public void onLoadMore() {
                setData(1);
            }
        });
        recyclerView.setRefreshing(true);
       String path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        System.out.println("aaaaaaa"+path);
    }

    int a;
    int i;

    public void setData(int index) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (index == 0)
                i = 0;
            List<Object> list = new ArrayList<>();
            a = i;
            for (; i < a + 10; i++) {
                list.add("Data:" + i);
            }
            System.out.println("===========:"+i);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (index == 0)
                    recyclerView.setData(list);
                else if (i < 50)
                    recyclerView.addData(list, true);
                else recyclerView.addData(list, false);
                recyclerView.setRefreshing(false);
            });
        }).start();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pickPhoto.setResult(requestCode, resultCode, data);
    }

    @Override
    public void setHolder(BaseViewHolder holder, Object item) {
        super.setHolder(holder, item);
        holder.setText(R.id.textRed, item.toString());
    }
}
