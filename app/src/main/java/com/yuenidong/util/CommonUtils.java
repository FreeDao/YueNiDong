
package com.yuenidong.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.BaseColumns;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;

import com.yuenidong.common.AppData;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CommonUtils {

    /**
     * Get StatusBar Height
     * 
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Get actionbar height
     */
//    public static int getActionBarHeight(Context context) {
//        try {
//            Class.forName("android.support.v7.app.ActionBarActivity");
//            return (int) context.getResources().getDimension(
//                    R.dimen.abc_action_bar_default_height_material);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        TypedValue typedValue = new TypedValue();
//        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
//            return TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources()
//                    .getDisplayMetrics());
//        }
//        return 0;
//    }

    public static int getTransparentNavigationBarHeight(Context context) {
        // below version 4.4
        if (VERSION.SDK_INT < VERSION_CODES.KITKAT) {
            return 0;
        }
        // has navigation bar
        if (getNavigationBarVisibility(context)) {
            int resourceId = Resources.getSystem().getIdentifier("navigation_bar_height", "dimen",
                    "android");
            if (resourceId > 0) {
                return Resources.getSystem().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean getNavigationBarVisibility(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Point realSize = getDislayRealSize(context);

        int screen_height = Math.max(size.y, size.x);
        int real_screen_height = Math.max(realSize.y, realSize.x);
        return real_screen_height > screen_height;
    }

    /**
     * 判断应用是否已安装
     * 
     * @param context
     * @param pakageName 完整包名
     * @return
     */
    public static boolean isPackageInstalled(Context context, String pakageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pakageName, 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    @SuppressWarnings("deprecation")
    public static void clipText(String text) {
        if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) AppData
                    .getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(android.content.ClipData.newPlainText("Fuubo", text));
        } else {
            ClipboardManager clipboardManager = (ClipboardManager) AppData.getContext()
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(text);
        }
    }

    public static void refreshMedia(String path) {
        if (VERSION.SDK_INT < VERSION_CODES.KITKAT) {
            AppData.getContext().sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + path)));
        } else {
            AppData.getContext().sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        }
    }

    public static String encodeUrl(Map<String, String> param) {
        if (param == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        Set<Entry<String, String>> entries = param.entrySet();
        boolean first = true;

        for (Entry<String, String> entry : entries) {
            String value = entry.getValue();
            String key = entry.getKey();
            if (!TextUtils.isEmpty(value)) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }

                try {
                    sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
                            .append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                try {
                    params.putString(URLDecoder.decode(v[0], "UTF-8"),
                            URLDecoder.decode(v[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return params;
    }

    public static Bundle parseUrl(String url) {
        url = url.replace("weiboconnect", "http");
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }

    /**
     * 创建app快捷方式 需要权限com.android.launcher.permission.INSTALL_SHORTCUT
     * 
     * @param intent 启动activity所需的intent
     * @param shortCutName
     * @param iconResId
     * @param duplicate
     */
    public static void installLauncherShortCut(Intent intent, String shortCutName, int iconResId,
            boolean duplicate) {
        final Context context = AppData.getContext();
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 设置属性
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName);
        ShortcutIconResource iconRes = ShortcutIconResource.fromContext(context, iconResId);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        // 是否允许重复创建
        shortcutIntent.putExtra("duplicate", duplicate);

        intent.setAction(Intent.ACTION_MAIN);

        // 设置启动程序
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        context.sendBroadcast(shortcutIntent);
    }

    /**
     * 创建app快捷方式 需要权限com.android.launcher.permission.INSTALL_SHORTCUT
     * 
     * @param intent 启动activity所需的intent
     * @param shortCutName
     * @param icon
     * @param duplicate
     */
    public static void installLauncherShortCut(Intent intent, String shortCutName, Bitmap icon,
            boolean duplicate) {
        final Context context = AppData.getContext();
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 设置属性
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
        // 是否允许重复创建
        shortcutIntent.putExtra("duplicate", duplicate);

        intent.setAction(Intent.ACTION_MAIN);

        // 设置启动程序
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        context.sendBroadcast(shortcutIntent);
    }

    /**
     * 将dip转换为px
     * 
     * @param dimen
     * @return px
     */
    public static int convertDimenToPix(float dimen) {
        Resources r = Resources.getSystem();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimen,
                r.getDisplayMetrics());
    }

    /**
     * 判断url是否合法
     * 
     * @param strUrl
     * @return
     */
    public static boolean isUrlValid(final String strUrl) {
        if (null == strUrl) {
            return false;
        }
        try {
            new URL(strUrl);
            return true;
        } catch (final MalformedURLException e) {
            return false;
        }
    }

    /**
     * 使用{@link android.os.AsyncTask.THREAD_POOL_EXECUTOR} 执行AsyncTask 这样可以避免android
     * 4.0以上系统 每次只执行一个 asyncTask
     * 
     * @param task
     * @param params
     */
    public static <Params, Progress, Result> void executeAsyncTask(
            AsyncTask<Params, Progress, Result> task, Params... params) {
        if (VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    public static enum SDCardState {
        AVAILABLE, UNAVAILABLE, FULL
    }

    /**
     * 检查sd卡状态
     * 
     * @return {@link com.yuenidong.util.CommonUtils.SDCardState}:AVAILABLE,UNAVAILABLE,FULL
     */
    public static SDCardState checkSDCardState() {
        SDCardState state = SDCardState.AVAILABLE;
        if (isSDCardUnavailable() || isSDCardBusy()) {
            // sd卡卸载了
            state = SDCardState.UNAVAILABLE;
        } else if (isSDCardFull()) {
            // sd卡满了
            state = SDCardState.FULL;
        }
        return state;
    }

    /**
     * 没有检测到SD卡
     * 
     * @return
     */
    public static boolean isSDCardUnavailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED);
    }

    /**
     * @return true 如果SD卡处于不可读写的状态
     */
    public static boolean isSDCardBusy() {
        return !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 检查ＳＤ卡是否已满。如果ＳＤ卡的剩余空间小于１００ｋ，则认为ＳＤ卡已满。
     * 
     * @return
     */
    public static boolean isSDCardFull() {
        return getSDCardAvailableBytes() <= (100 * 1024);
    }

    public static boolean isSDCardUseful() {
        return (!CommonUtils.isSDCardBusy()) && (!CommonUtils.isSDCardFull())
                && (!CommonUtils.isSDCardUnavailable());
    }

    /**
     * 获取ＳＤ卡的剩余字节数。
     * 
     * @return
     */
    public static long getSDCardAvailableBytes() {
        if (isSDCardBusy()) {
            return 0;
        }

        final File path = Environment.getExternalStorageDirectory();
        final StatFs stat = new StatFs(path.getPath());
        final long blockSize = stat.getBlockSize();
        final long availableBlocks = stat.getAvailableBlocks();
        return blockSize * (availableBlocks - 4);
    }

    /**
     * 在EditText 光标之后加入addString
     * 
     * @param editText
     * @param text
     */
    public static void insertTextToEditText(EditText editText, String text) {
        int selectionIndex = editText.getSelectionStart();
        editText.getText().insert(selectionIndex, text);
    }

    public static void createTable(final SQLiteDatabase db, final String tableName,
            final String[] columnsDefinition) {
        String queryStr = "CREATE TABLE " + tableName + "(" + BaseColumns._ID
                + " INTEGER  PRIMARY KEY ,";
        // Add the columns now, Increase by 2
        for (int i = 0; i < (columnsDefinition.length - 1); i += 2) {
            if (i != 0) {
                queryStr += ",";
            }
            queryStr += columnsDefinition[i] + " " + columnsDefinition[i + 1];
        }

        queryStr += ");";

        db.execSQL(queryStr);
    }

    public static void deleteTable(final SQLiteDatabase db, final String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * 
     * @param intent The Intent to check for availability.
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */
    public static boolean isIntentAvailable(final Intent intent) {
        final PackageManager packageManager = AppData.getContext().getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static int getActiveNetworkType() {
        int defaultValue = -1;
        ConnectivityManager cm = (ConnectivityManager) AppData.getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return defaultValue;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null)
            return defaultValue;
        return info.getType();
    }

    public static Point getDislayRealSize(Context context) {
        Point point = new Point();
        WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();

        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(point);
        } else if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                point.x = (Integer) mGetRawW.invoke(display);
                point.y = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                display.getSize(point);
            }
        } else {
            display.getSize(point);
        }
        return point;
    }

    /**
     * @return currentDate
     */
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(year));
        stringBuilder.append(".");
        stringBuilder.append(String.valueOf(month));
        stringBuilder.append(".");
        stringBuilder.append(String.valueOf(day));

        return stringBuilder.toString();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Map转换成Json
     * @param map
     * @return
     */
    public static String hashMapToJson(HashMap map) throws UnsupportedEncodingException {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Entry e = (Entry) it.next();
            string += "'" + e.getKey() + "':";
            string += "'" + e.getValue() + "',";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

}
