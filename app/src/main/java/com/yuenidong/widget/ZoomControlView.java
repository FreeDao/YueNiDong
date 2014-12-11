package com.yuenidong.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;

/**
 * Created by 石岩 on 2014/12/10.
 */
public class ZoomControlView extends RelativeLayout implements View.OnClickListener {
    private Button mButtonZoomin;
    private Button mButtonZoomout;
    private Button mButtonLocation;
    private MapView mapView;
    private int maxZoomLevel;
    private int minZoomLevel;

    public ZoomControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.zoom_controls_layout, null);
        mButtonZoomin = (Button) view.findViewById(R.id.zoomin);
        mButtonZoomout = (Button) view.findViewById(R.id.zoomout);
        mButtonLocation = (Button) view.findViewById(R.id.location);
        mButtonZoomin.setOnClickListener(this);
        mButtonZoomout.setOnClickListener(this);
        mButtonLocation.setOnClickListener(this);
        addView(view);
    }

    @Override
    public void onClick(View v) {
        if (mapView == null) {
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        switch (v.getId()) {
            case R.id.zoomin: {
                mapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            }
            case R.id.zoomout: {
                mapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
            }
            case R.id.location: {
                mapView.getMap().setMapStatus(MapStatusUpdateFactory.newLatLngZoom(new LatLng(30.482576, 114.410068), 12.0f));
                break;
            }
        }
    }

    /**
     * 与MapView设置关联
     *
     * @param mapView
     */
    public void setMapView(MapView mapView) {
        this.mapView = mapView;
        // 获取最大的缩放级别
        maxZoomLevel = (int) mapView.getMap().getMaxZoomLevel();
        // 获取最大的缩放级别
        minZoomLevel = (int) mapView.getMap().getMinZoomLevel();
    }


    /**
     * 根据MapView的缩放级别更新缩放按钮的状态，当达到最大缩放级别，设置mButtonZoomin
     * 为不能点击，反之设置mButtonZoomout
     *
     * @param level
     */
    public void refreshZoomButtonStatus(int level) {
        if (mapView == null) {
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        if (level > minZoomLevel && level < maxZoomLevel) {
            if (!mButtonZoomout.isEnabled()) {
                mButtonZoomout.setEnabled(true);
            }
            if (!mButtonZoomin.isEnabled()) {
                mButtonZoomin.setEnabled(true);
            }
        } else if (level == minZoomLevel) {
            mButtonZoomout.setEnabled(false);
        } else if (level == maxZoomLevel) {
            mButtonZoomin.setEnabled(false);
        }
    }
}
