package com.base.galleryview.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.base.config.BPConfig;
import com.base.galleryview.photoview.PhotoViewAttacher;
import com.base.R;
import com.base.galleryview.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * 图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mType;
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String imageUrl, String type) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putString("type", type);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        mType = getArguments() != null ? getArguments().getString("type") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        if (BPConfig.HTTP_URL.equals(mType))
            Picasso.get().load(mImageUrl).resize(BPConfig.SCREEN_WIDTH, BPConfig.SCREEN_HEIGHT).centerInside().into(mImageView, new Callback() {

                @Override
                public void onSuccess() {
                    mAttacher.update();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }


            });
        else if (BPConfig.LOCAL_FILE.equals(mType))
            Picasso.get().load(new File(mImageUrl)).resize(BPConfig.SCREEN_WIDTH, BPConfig.SCREEN_HEIGHT).centerInside().into(mImageView,
                    new Callback() {

                        @Override
                        public void onSuccess() {
                            mAttacher.update();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });
    }

}
