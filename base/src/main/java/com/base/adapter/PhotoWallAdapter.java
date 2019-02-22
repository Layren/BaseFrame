package com.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.base.R;
import com.base.config.BPConfig;
import com.base.pickphoto.PickPhoto;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoWallAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePathList = null;
    private List<Integer> selects = new ArrayList<>();

    public PhotoWallAdapter(Context context, ArrayList<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;
    }

    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String filePath = (String) getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_wall_item, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.photo_wall_item_photo);
            holder.checkBox = convertView.findViewById(R.id.photo_wall_item_cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (PickPhoto.isSign()) {
            holder.checkBox.setVisibility(View.GONE);
        } else {
            if (selects.contains(position)) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked())
                    selects.add(position);
                else
                    selects.remove((Integer) position);
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.imageView.setColorFilter(0x80000000);
                } else {
                    holder.imageView.setColorFilter(null);
                }
            }
        });
        int size = BPConfig.SCREEN_WIDTH / 3;
        Picasso.get().load(new File(filePath))
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.empty_photo)
                .resize(size, size)
                .centerCrop()
                .into(holder.imageView);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }

    public List<Integer> getSelects() {
        return selects;
    }

    public void clearSelects() {
        selects.clear();
    }

}
