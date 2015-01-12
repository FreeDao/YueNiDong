package com.yuenidong.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.igexin.sdk.PushManager;
import com.yuenidong.activity.FirstActivity;
import com.yuenidong.activity.LoginActivity;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.AppManager;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.MyUserEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.UpdateManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import http.AsyncHttpClient;
import http.AsyncHttpResponseHandler;
import http.RequestParams;

/**
 * 石岩 我的信息修改
 */
public class MyInfoUpdateFragment extends Fragment {
    @InjectView(R.id.iv_image)
    ImageView iv_image;
    @InjectView(R.id.tv_name)
    TextView tv_userName;
    @InjectView(R.id.iv_gender)
    ImageView iv_gender;
    @InjectView(R.id.tv_id)
    TextView tv_id;
    @InjectView(R.id.iv_label_one)
    ImageView iv_label_one;
    @InjectView(R.id.iv_lable_two)
    ImageView iv_label_two;
    @InjectView(R.id.iv_label_three)
    ImageView iv_label_three;
    @InjectView(R.id.tv_care_count)
    TextView tv_care;
    @InjectView(R.id.tv_fans_count)
    TextView tv_fans;
    @InjectView(R.id.tv_candy_count)
    TextView tv_candy;
    @InjectView(R.id.et_userName)
    EditText et_userName;
    @InjectView(R.id.iv_sex_man)
    ImageView iv_sex_man;
    @InjectView(R.id.iv_sex_woman)
    ImageView iv_sex_woman;
    @InjectView(R.id.et_sign)
    EditText et_sign;


    private String[] items = new String[]{"从相机获取照片", "从相册获取照片"};
    private final String IMAGE_TYPE = "image/*";
    private String SD_CARD_TEMP_DIR;
    private Bitmap myBitmap;
    private Handler handler;
    private byte[] mContent;

    private MyUserEntity user;
    boolean isMan = false;

    //修改头像
    @OnClick(R.id.iv_image)
    void image() {
        showDialog();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    iv_image.setImageBitmap(myBitmap);
                }
            }
        };

    }

    //完成
    @OnClick(R.id.btn_finish)
    void finish() {
        String userName = et_userName.getText().toString().trim();
        String sign = et_sign.getText().toString().trim();
        String sex = "";
        if (isMan) {
            sex = "m";
        } else {
            sex = "f";
        }
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(getActivity(), "昵称不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        //向服务器发送请求
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userName", userName);
        map.put("signature", sign);
        map.put("gender", sex);
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);

            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.UPDATE_INFO, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("success", response.toString());
                        if (!TextUtils.isEmpty(SD_CARD_TEMP_DIR)) {
                            uploadByAsyncHttpClient();
                        } else {
                            exitApp();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        RequestManager.addRequest(jsonRequest, this);
    }

    //检查版本
    @OnClick(R.id.rl_checkVersion)
    void checkVersion() {
        //这里来检测版本是否需要更新
        UpdateManager mUpdateManager = new UpdateManager(getActivity());
        mUpdateManager.checkUpdateInfo();

    }

    //登出
    @OnClick(R.id.rl_logout)
    void logout() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //修改性别
    @OnClick(R.id.rl_updatesex)
    void updatesex() {
        if (isMan) {
            iv_sex_woman.setImageResource(R.drawable.ic_woman_update);
            iv_sex_man.setImageResource(R.drawable.ic_man_no_update);
            isMan = false;
        } else {
            iv_sex_man.setImageResource(R.drawable.ic_man_update);
            iv_sex_woman.setImageResource(R.drawable.ic_woman_no_update);
            isMan = true;
        }
    }

    private void showDialog() {
        Dialog dialog = new AlertDialog.Builder(getActivity()).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int position) {
                // 拍照获取图片功能
                if (position == 0) {
                    // 设置拍照的意图
                    Intent takePictureFromCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureFromCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SD_CARD_TEMP_DIR)));
                    startActivityForResult(takePictureFromCameraIntent, 1);
                }
                // 相册获取图片功能
                else {
                    Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                    getAlbum.setType(IMAGE_TYPE);
                    startActivityForResult(getAlbum, 2);
                }
            }
        }).create();
        dialog.show();
    }


    public static MyInfoUpdateFragment newInstance(MyUserEntity user) {
        MyInfoUpdateFragment fragment = new MyInfoUpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    public MyInfoUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (MyUserEntity) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info_update, container, false);
        ButterKnife.inject(this, view);
        loadImageVolley(user.getImage_path(), iv_image);
        tv_userName.setText(user.getUserName());
        if (user.getGender().equals("m")) {
            iv_gender.setImageResource(R.drawable.ic_man);
            iv_sex_man.setImageResource(R.drawable.ic_man_update);
            iv_sex_woman.setImageResource(R.drawable.ic_woman_no_update);
            isMan = true;
        } else {
            iv_gender.setImageResource(R.drawable.ic_woman);
            iv_sex_woman.setImageResource(R.drawable.ic_woman_update);
            iv_sex_man.setImageResource(R.drawable.ic_man_no_update);
            isMan = false;
        }
        tv_id.setText("ID:" + user.getID());
        if (!TextUtils.isEmpty(user.getLabel1())) {
            iv_label_one.setVisibility(View.VISIBLE);
            showImage(iv_label_one, user.getLabel1());
        } else {
            iv_label_one.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(user.getLabel2())) {
            iv_label_two.setVisibility(View.VISIBLE);
            showImage(iv_label_two, user.getLabel2());
        } else {
            iv_label_two.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(user.getLabel3())) {
            iv_label_three.setVisibility(View.VISIBLE);
            showImage(iv_label_three, user.getLabel3());
        } else {
            iv_label_three.setVisibility(View.GONE);
        }
        tv_care.setText(user.getCare());
        tv_fans.setText(user.getFans());
        tv_candy.setText(user.getCandy());
        et_userName.setText(user.getUserName());
        if ("null".equals(user.getSign())) {
            et_sign.setText("");
        } else {
            et_sign.setText(user.getSign());
        }
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        ContentResolver resolver = getActivity().getContentResolver();
        // 通过相机获取照片返回的结果
        if (requestCode == 1) {
            try {
                File f = new File(SD_CARD_TEMP_DIR);
                Uri capturedImage = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(resolver, f.getAbsolutePath(), null, null));
                Toast toast = Toast.makeText(getActivity(), capturedImage.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                // 将图片内容解析成字节数组
                mContent = readStream(resolver.openInputStream(Uri.parse(capturedImage.toString())));
                // 将字节数组转换为ImageView可调用的Bitmap对象
                myBitmap = getPicFromBytes(mContent, null);
                handler.sendEmptyMessage(0);
            } catch (Exception e) {
            }
        }
        if (requestCode == 2) {
            try {

                if (data != null) {
                    Uri originalUri = data.getData(); // 获得图片的uri
                    DsncLog.e("uri", originalUri + "");
                    myBitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片
                    handler.sendEmptyMessage(0);
                    String[] proj = {MediaStore.Images.Media.DATA};
                    // 好像是android多媒体数据库的封装接口，具体的看Android文档
                    Cursor cursor = getActivity().managedQuery(originalUri, proj, null, null, null);
                    // 按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    // 最后根据索引值获取图片路径
                    SD_CARD_TEMP_DIR = cursor.getString(column_index);
                }
            } catch (IOException e) {
                Toast.makeText(getActivity(), "获取相册图片失败!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }


    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public void uploadByAsyncHttpClient() {
        DsncLog.e("SD_CARD_TEMP_DIR", SD_CARD_TEMP_DIR);
        final String url = YueNiDongConstants.IMAGE_UPLOAD;
        final File file = new File(SD_CARD_TEMP_DIR);
        DsncLog.e("file", file.getName());
        // 先判断文件是否存在
        if (file.exists() && file.length() > 0) {

            // 1. 创建AsyncHttpClient对象
            AsyncHttpClient client = new AsyncHttpClient();

            // 2.设置参数体
            RequestParams params = new RequestParams();
            try {
                // 其实这里的异常不可能出现，因为上边已经做了判断
                params.put("fileForward", file);
                params.put("userId", PreferenceUtil.getPreString("userId", ""));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // 3.上传文件
            client.post(url, params, new AsyncHttpResponseHandler() {
                // 上传成功时回调的方法
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                    exitApp();
                }

                // 上传失败时回调的方法
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    Toast.makeText(getActivity(), "上传失败！错误码：" + statusCode,
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "文件不存在！", Toast.LENGTH_SHORT).show();
        }
    }

    //加载网络图片
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void loadImageVolley(String imageurl, ImageView imageView) {
//        String imageurl = "http://10.0.0.52/lesson-img.png";
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lurcache = new LruCache<String, Bitmap>(
                20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lurcache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lurcache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(RequestManager.sRequestQueue, imageCache);
        ImageLoader.ImageListener listener = imageLoader.getImageListener(imageView,
                R.drawable.ic_launcher, R.drawable.ic_launcher);
        imageLoader.get(imageurl, listener);
    }

    private void showImage(ImageView iv, String label1) {
        if (label1.equals("台球")) {
            iv.setImageResource(R.drawable.label_billiards_filled);
        }
        if (label1.equals("篮球")) {
            iv.setImageResource(R.drawable.label_basketball_filled);
        }
        if (label1.equals("跑步")) {
            iv.setImageResource(R.drawable.label_running_filled);
        }
        if (label1.equals("网球")) {
            iv.setImageResource(R.drawable.label_tennis_filled);
        }
        if (label1.equals("羽毛球")) {
            iv.setImageResource(R.drawable.label_badminton_filled);
        }
        if (label1.equals("足球")) {
            iv.setImageResource(R.drawable.label_football_filled);
        }
        if (label1.equals("骑行")) {
            iv.setImageResource(R.drawable.label_riding_filled);
        }
        if (label1.equals("乒乓球")) {
            iv.setImageResource(R.drawable.label_tabletennis_filled);
        }
        if (label1.equals("游泳")) {
            iv.setImageResource(R.drawable.label_swimming_filled);
        }
        if (label1.equals("健身")) {
            iv.setImageResource(R.drawable.label_bodybuilding_filled);
        }
        if (label1.equals("滑板")) {
            iv.setImageResource(R.drawable.label_slidingplate_filled);
        }
        if (label1.equals("轮滑")) {
            iv.setImageResource(R.drawable.label_skidding_filled);
        }
    }

    public void exitApp() {
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        DsncLog.e("onResume2", "OnResume2");
    }
}
