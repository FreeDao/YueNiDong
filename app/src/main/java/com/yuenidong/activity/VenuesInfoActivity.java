package com.yuenidong.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.fragment.VenuesInfoFragment;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩 场馆信息
 */
public class VenuesInfoActivity extends BaseActivity {
    @InjectView(R.id.actionbar_rightbutton)
    Button actionbar_rightbutton;
    VenuesEntity entity;

    //收藏
    @OnClick(R.id.actionbar_rightbutton)
    void collent() {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        map.put("venderId", entity.getVenderId());
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.VENUESCOLLECTION, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("场馆收藏success", response.toString());
                        Commvert commvert=new Commvert(response);
                        if(commvert.getString("status").equals("1")){
                            Toast.makeText(AppData.getContext(),"收藏场馆成功!",Toast.LENGTH_SHORT).show();
                        }
                        if(commvert.getString("status").equals("11")){
                            Toast.makeText(AppData.getContext(),"取消收藏场馆成功!",Toast.LENGTH_SHORT).show();
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

    //分享
    @OnClick(R.id.actionbar_rightbutton2)
    void share() {
        Toast.makeText(this, "分享成功!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entity = (VenuesEntity) getIntent().getSerializableExtra("venues");
        showActionBarText(AppData.getString(R.string.venues));
        showActionBarRightButton2(R.drawable.button_share_venues);
        showActionBarRightButton();
        actionbar_rightbutton.setBackgroundResource(R.drawable.button_collect_venues);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = VenuesInfoFragment.newInstance(entity);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();
    }

}
