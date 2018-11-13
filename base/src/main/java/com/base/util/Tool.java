package com.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.base.config.BPConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;
import static android.view.Gravity.TOP;

public class Tool {

    private static final int SIZETYPE_B = 1;//
    private static final int SIZETYPE_KB = 2;//
    private static final int SIZETYPE_MB = 3;//
    private static final int SIZETYPE_GB = 4;//

    /**
     * 随机整数
     *
     * @param scope 取值范围
     * @return
     */
    public static int randomInt(int scope) {
        return (int) (Math.random() * 10000 % scope);
    }

    /**
     * 判断是否图片
     *
     * @param fileName 文件名
     * @return
     */
    public static boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

    /**
     * 获取图片名
     *
     * @return 已当前时间命名
     */
    public static String getPhotoName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");
        Date date = new Date(System.currentTimeMillis());
        return "IMG_" + formatter.format(date) + ".jpg";
    }

    /**
     * 保存图片到sd卡
     */
    public static void saveCahceBitmapToFile(Bitmap bitmap, String _file) {
        BufferedOutputStream os = null;
        try {
            File filePath = new File(BPConfig.CACHE_IMG_PATH);

            if (!filePath.exists()) {
                filePath.mkdir();
            }
            File file = new File(filePath + File.separator + _file);
            if (!file.exists()) {
                file.createNewFile();
                os = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
            }
        } catch (IOException e) {
            Log.e("Tools", "saveCahceBitmapToFile_Error:");
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e("Error", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 保存图片到相机相册
     */
    public static void saveCahceBitmapToDCIM(Bitmap bitmap, String _file) {
        BufferedOutputStream os = null;
        try {
            File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera");

            if (!filePath.exists()) {
                filePath.mkdir();
            }
            File file = new File(filePath + File.separator + _file);
            if (file.exists())
                file.delete();
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
        } catch (Exception e) {
            Log.e("Tools", "saveCahceBitmapToDCIM_Error:");
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e("Error", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 获取map 所有的Key
     *
     * @param map map
     * @return key集合
     */
    public static List<String> getIdFromMap(Map<String, String> map) {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            keys.add(entry.getKey().toString());
        }
        return keys;
    }


    /**
     * 调用电话
     */
    public static void call(Context ctx, String phoneno) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneno));
        ctx.startActivity(intent);
    }

    /**
     * 将drawable转化为bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 字符串转换到时间格式
     *
     * @param dateStr   需要转换的字符串
     * @param formatStr 需要格式的目标字符串 举例 yyyy-MM-dd
     * @return Date 返回转换后的时间
     * @throws ParseException 转换异常
     */
    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 计算时间差
     *
     * @param time
     */
    public static String getTimeDiff(long time) {
        long m = (time / 60000l) % 60;
        long h = (time / 3600000l) % 24;
        long d = time / 86400000l;
        return d + "天" + h + "小时" + m + "分";

    }

    /**
     * 获取当前时间
     *
     * @return yyyy年MM月dd日 HH:mm:ss
     */
    public static String getCurTime(String format) {
        SimpleDateFormat formatter;
        if (format == null || format.equals("")) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            formatter = new SimpleDateFormat(format);
        }
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取当前时间
     *
     * @param offset 偏移量（分钟）
     * @return yyyy年MM月dd日 HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurTimeDeviation(String format, int offset) {
        SimpleDateFormat formatter;
        if (format == null || format.equals("")) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            formatter = new SimpleDateFormat(format);
        }
        Date curDate = new Date(System.currentTimeMillis() + offset * 60000);// 获取当前时间（添加偏移量 ）

        return formatter.format(curDate);
    }

    /**
     * 获取当前网络时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurInternetTime(String format) {
        SimpleDateFormat formatter;
        if (format == null || format.equals("")) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            formatter = new SimpleDateFormat(format);
        }
        URL url;
        URLConnection uc;
        long ld = 0;
        try {
            url = new URL("http://open.baidu.com/special/time/");
            uc = url.openConnection();// 生成连接对象
            uc.connect(); // 发出连接
            ld = uc.getDate(); // 取得网站日期时间
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        Date curDate = new Date(ld); // 转换为标准时间对象
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取固定格式的毫秒数
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static long getTime(String format, String time) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        long millionSeconds = 0;
        try {
            millionSeconds = formatter.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        } // 毫秒
        return millionSeconds;
    }

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取随机数
     */
    public static String getRandom(int length) {

        String hexDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String ranStr = "";
        for (int i = 0; i < length; i++) {
            int x = (int) (Math.random() * hexDigits.length());
            ranStr += hexDigits.substring(x, x + 1);
        }
        return ranStr;

    }

    /**
     * 获取数字随机数
     */
    public static String getRandomNum(int length) {

        String hexDigits = "0123456789";
        String ranStr = "";
        for (int i = 0; i < length; i++) {
            int x = (int) (Math.random() * hexDigits.length());
            ranStr += hexDigits.substring(x, x + 1);
        }
        return ranStr;

    }


    /**
     * 写在/mnt/sdcard/目录下面的文件
     */
    public static void writeFileSdcard(String fileName, String message) {
        try {
            // FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读在sdcard/目录下面的文件
     */
    public static String readFileSdcard(String fileName) {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 获取文件路径
     *
     * @return
     */
    public static String getFilePath(String fileName) {
        String dirPath = BPConfig.CACHE_FILE_PATH;
        File folder = new File(dirPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(dirPath + "/" + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();

    }

    /**
     * 返回String像素长度
     *
     * @param view
     * @return
     */
    public static int getTextLenght(TextView view) {
        return (int) view.getPaint().measureText(view.getText().toString());
    }

    /**
     * 创建压缩文件做调用（不影响原文件）
     *
     * @param path
     * @return
     */
    public static String getPath(String path) {
        String imgPath = "";
        String fileName = "";
        if (path.contains(".") && path.lastIndexOf(".") > 0
                && path.lastIndexOf("/") + 1 < path.lastIndexOf("."))
            fileName = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".")) + "temp";
        else
            fileName = path.substring(path.lastIndexOf("/") + 1) + "temp";
        imgPath = BPConfig.CACHE_IMG_PATH + File.separator + fileName;
        File file = new File(imgPath);
        if (file.exists())
            return imgPath;
        saveCahceBitmapToFile(zoomImage(path), fileName);
        return imgPath;
    }

    /**
     * 图片按宽高比例缩放到 4096X4096（硬件加速可以支持的最大尺寸）
     *
     * @param bgimage ：源图片资源
     * @return
     */
    private static Bitmap zoomImage(@NonNull Bitmap bgimage) {
        int w = 4096, h = 4096;
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        if (width < w && height < h)
            return bgimage;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = w / width;
        float scaleHeight = h / height;
        float scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
        // 缩放图片动作
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
    }

    /**
     * 图片按宽高比例缩放到 4096X4096（硬件加速可以支持的最大尺寸）
     *
     * @param path ：源图片路径
     * @return
     */
    private static Bitmap zoomImage(String path) {
        try {
            return zoomImage(BitmapFactory.decodeFile(path));
        } catch (Exception e) {
            // 当图片过大，无法创建Bitmap时，压缩至硬件加速支持范围内的最大尺寸
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            float scaleWidth = opts.outWidth / 4096;
            float scaleHeight = opts.outHeight / 4096;
            double scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
            opts.inSampleSize = (int) Math.ceil(scale);
            opts.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(path, opts);
        }

    }

    /**
     * 日期格式转换
     */
    public static String getMilliToDate(String format, long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String getTextViewContent(TextView tv) {
        if (tv.getText() != null && !"".equals(tv.getText().toString().trim())) {
            return tv.getText().toString().trim();
        } else
            return null;
    }

    public static int getCheckedRadioButtonIndex(RadioGroup group) {
        return group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    public static void dismissSoftInputFromWindow(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int[] getTimer(int time) {
        int[] ints = new int[4];
        ints[3] = time % 1000;
        ints[2] = time / 1000 % 60;
        ints[1] = time / 60000 % 60;
        ints[0] = time / 3600000 % 24;
        //9148564    2:32:28:564;
        return ints;
    }

    //获取文件夹大小
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }

    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }


    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 数字校正
     *
     * @param str    输入字符
     * @param length 小数点位数限制，0为不限制
     * @return 校正后数字
     */
    private static String NumberCorrect(String str, int length) {
        String result = str;
        if (str.length() > 1 && str.startsWith("0") && !str.substring(1, 2).equals(".")) {
            result = NumberCorrect(str.substring(1), length);
        } else if (str.startsWith(".")) {
            result = "0.";
        } else if (str.contains(".") && str.indexOf(".") != str.lastIndexOf(".")) {
            result = str.substring(0, str.lastIndexOf("."));
        }
        if (length > 0
                && result.contains(".")
                && result.indexOf(".") + length < result.length()) {
            result = result.substring(0, result.lastIndexOf(".") + length + 1);
        }
        return result;
    }

    /**
     * 为输入框添加数字矫正
     *
     * @param editText
     * @param length   小数点后位数限制长度
     */
    public static void addNumberCorrect(final EditText editText, final int length) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.removeTextChangedListener(this);
                String content = Tool.NumberCorrect(s.toString(), length);
                editText.setText(content);
                editText.setSelection(content.length());
                editText.addTextChangedListener(this);
            }
        });
    }

    /**
     * 为输入框添加数字矫正
     *
     * @param editText
     */
    public static void addNumberCorrect(EditText editText) {
        addNumberCorrect(editText, 0);
    }

    public static void deleteAllFiles(String path) {
        File root = new File(path);
        if (!root.exists()) {
            return;
        }
        deletFiles(root);
    }

    /**
     * 递归删除子文件夹下的所有文件
     */
    private static void deletFiles(File root) {
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory())
                    deletFiles(f);
                if (f.exists()) { // 判断是否存在
                    f.delete();
                }
            }
        }
    }

    /**
     * 给TextView设置Drawable
     *
     * @param view      TextView
     * @param resourId  图片资源ID
     * @param color     字体颜色
     * @param direction 方向
     */
    private static void setTextViewDrawable(TextView view, int resourId, int color, int direction) {
        switch (direction) {
            case LEFT:
                view.setCompoundDrawablesWithIntrinsicBounds(resourId, 0, 0, 0);
                break;
            case TOP:
                view.setCompoundDrawablesWithIntrinsicBounds(0, resourId, 0, 0);
                break;
            case RIGHT:
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, resourId, 0);
                break;
            case BOTTOM:
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, resourId);
                break;
        }
        view.setTextColor(color);
    }

    /**
     * 设置TextView 左侧Drawable
     *
     * @param view
     * @param resourId
     * @param color
     */
    public static void setTextViewLeftDrawable(TextView view, int resourId, int color) {
        setTextViewDrawable(view, resourId, color, Gravity.LEFT);
    }

    /**
     * 设置TextView 顶部Drawable
     *
     * @param view
     * @param resourId
     * @param color
     */
    public static void setTextViewTopDrawable(TextView view, int resourId, int color) {
        setTextViewDrawable(view, resourId, color, Gravity.TOP);
    }

    /**
     * 设置TextView 右侧Drawable
     *
     * @param view
     * @param resourId
     * @param color
     */
    public static void setTextViewRightDrawable(TextView view, int resourId, int color) {
        setTextViewDrawable(view, resourId, color, Gravity.RIGHT);
    }

    /**
     * 设置TextView 底部Drawable
     *
     * @param view
     * @param resourId
     * @param color
     */
    public static void setTextViewBottomDrawable(TextView view, int resourId, int color) {
        setTextViewDrawable(view, resourId, color, Gravity.BOTTOM);
    }

    /**
     * 隐藏Edittext 光标
     *
     * @param mEditText
     */
    public static void hideCursor(final EditText... mEditText) {//编辑框光标
        for (int i = 0; i < mEditText.length; i++) {
            mEditText[i].setCursorVisible(false);// 内容清空后将编辑框1的光标隐藏，提升用户的体验度
            // 编辑框设置触摸监听
            final int finalI = i;
            mEditText[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                        mEditText[finalI].setCursorVisible(true);// 再次点击显示光标
                    }

                    return false;
                }

            });
        }

    }
}
