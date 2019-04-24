package com.base.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.base.util.Tool;


/**
 * 头像上传原图裁剪容器
 */
public class ClipViewLayout extends RelativeLayout {
    private Context context;
    //裁剪原图
    private ImageView imageView;
    //裁剪框
    private ClipView clipView;
    //裁剪框水平方向间距
    private float horizontalPadding;
    //垂直方向间距
    private float verticalPadding;
    //图片缩放、移动操作矩阵
    private Matrix matrix = new Matrix();
    //图片原来已经缩放、移动过的操作矩阵
    private Matrix savedMatrix = new Matrix();
    //动作标志：无
    private static final int NONE = 0;
    //动作标志：拖动
    private static final int DRAG = 1;
    //动作标志：缩放
    private static final int ZOOM = 2;
    //初始化动作标志
    private int mode = NONE;
    //记录起始坐标
    private PointF start = new PointF();
    //记录缩放时两指中间点坐标
    private PointF mid = new PointF();
    private float oldDist = 1f;
    //用于存放矩阵的9个值
    private final float[] matrixValues = new float[9];
    //最小缩放比例
    private float minScale;
    //最大缩放比例
    private float maxScale = 4;
    int clipType;//裁剪框类型(圆或者正方形或者矩形)
    private float ratio;

    public ClipViewLayout(Context context) {
        this(context, null);
    }

    public ClipViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public ClipViewLayout setHorizontalPadding(float horizontalPadding) {
        this.horizontalPadding = horizontalPadding;
        return this;
    }

    public ClipViewLayout setClipType(int clipType) {
        this.clipType = clipType;
        return this;
    }

    public ClipViewLayout setRatio(float ratio) {
        this.ratio = ratio;
        return this;
    }

    //初始化控件自定义的属性
    public void init(String path) {
        if (clipView == null)
            clipView = new ClipView(context);
        if (clipType != 3)
            ratio = 1;
        clipView.setRatio(ratio);
        //设置裁剪框类型
        if (clipType == 1)
            clipView.setClipType(ClipView.ClipType.CIRCLE);
        else if (clipType == 2)
            clipView.setClipType(ClipView.ClipType.SQUARE);
        else
            clipView.setClipType(ClipView.ClipType.RECTANGLE);
        //设置剪切框边框
        clipView.setClipBorderWidth(1);
        //设置剪切框水平间距
        clipView.setmHorizontalPadding(horizontalPadding);
        imageView = new ImageView(context);
        //相对布局布局参数
        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(imageView, lp);
        this.addView(clipView, lp);
        post(() -> initSrcPic(path));

    }

    /**
     * 初始化图片
     */
    private void initSrcPic(String path) {

        Bitmap bitmap = Tool.zoomImage(path);
        //图片的缩放比
        float scale;

        double widths = bitmap.getWidth() * ratio;
        Rect rect = clipView.getClipRect(ratio);

        if (widths >= bitmap.getHeight()) {//宽图
            scale = (float) imageView.getWidth() / bitmap.getWidth();
            //高的最小缩放比
            minScale = (float) rect.height() / bitmap.getHeight();
            if (scale < minScale) {
                scale = minScale;
            }
        } else {//高图
            scale = (float) imageView.getHeight() * ratio / bitmap.getHeight();
            //宽的最小缩放比
            minScale = rect.width() / (float) bitmap.getWidth();
            if (scale < minScale) {
                scale = minScale;
            }
        }
        int midY;

        // 缩放
        matrix.postScale(scale, scale);
        // 平移,将缩放后的图片平移到imageview的中心
        //imageView的中心x
        int midX = imageView.getWidth() / 2;
        //imageView的中心y
        midY = (int) (imageView.getHeight() / 2);
        //bitmap的中心x
        int imageMidX = (int) (bitmap.getWidth() * scale / 2);
        //bitmap的中心y
        int imageMidY = (int) (bitmap.getHeight() * scale / 2);
        matrix.postTranslate(midX - imageMidX, midY - imageMidY);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setImageMatrix(matrix);
        imageView.setImageBitmap(bitmap);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                //设置开始点位置
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //开始放下时候两手指间的距离
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) { //拖动
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    verticalPadding = clipView.getClipRect(ratio).top;
                    matrix.postTranslate(dx, dy);
                    //检查边界
                    checkBorder();
                } else if (mode == ZOOM) { //缩放
                    //缩放后两手指间的距离
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        //手势缩放比例
                        float scale = newDist / oldDist;
                        if (scale < 1) { //缩小
                            if (getScale() > minScale) {
                                matrix.set(savedMatrix);
                                verticalPadding = clipView.getClipRect(ratio).top;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                                //缩放到最小范围下面去了，则返回到最小范围大小
                                while (getScale() < minScale) {
                                    //返回到最小范围的放大比例
                                    scale = 1 + 0.01F;
                                    matrix.postScale(scale, scale, mid.x, mid.y);
                                }
                            }
                            //边界检查
                            checkBorder();
                        } else { //放大
                            if (getScale() <= maxScale) {
                                matrix.set(savedMatrix);
                                verticalPadding = clipView.getClipRect(ratio).top;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                        }
                    }
                }
                imageView.setImageMatrix(matrix);
                break;
        }
        return true;
    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     */
    private RectF getMatrixRectF(Matrix matrix) {
        RectF rect = new RectF();
        Drawable d = imageView.getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    /**
     * 边界检测
     */
    private void checkBorder() {
        RectF rect = getMatrixRectF(matrix);
        float deltaX = 0;
        float deltaY = 0;
        int width = imageView.getWidth();
        int height = (int) (imageView.getHeight());
        // 如果宽或高大于屏幕，则控制范围 ; 这里的0.001是因为精度丢失会产生问题，但是误差一般很小，所以我们直接加了一个0.01
        if (rect.width() >= width - 2 * horizontalPadding) {
            if (rect.left > horizontalPadding) {
                deltaX = -rect.left + horizontalPadding;
            }
            if (rect.right < width - horizontalPadding) {
                deltaX = width - horizontalPadding - rect.right;
            }
        }
        if (rect.height() >= height - 2 * verticalPadding) {
            if (rect.top > verticalPadding) {
                deltaY = (float) (-rect.top + verticalPadding);
            }
            if (rect.bottom < height - verticalPadding) {
                deltaY = (float) (height - verticalPadding - rect.bottom);
            }
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 获得当前的缩放比例
     */
    public final float getScale() {
        matrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }


    /**
     * 多点触控时，计算最先放下的两指距离
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 多点触控时，计算最先放下的两指中心坐标
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


    /**
     * 获取剪切图
     */
    public Bitmap clip() {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Rect rect = clipView.getClipRect(ratio);

        Bitmap cropBitmap = null;
        try {
            cropBitmap = Bitmap.createBitmap(imageView.getDrawingCache(), rect.left, rect.top, rect.width(), rect.height());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 释放资源
        imageView.destroyDrawingCache();
        return cropBitmap;
    }

}
