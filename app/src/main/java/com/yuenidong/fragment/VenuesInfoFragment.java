package com.yuenidong.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuenidong.activity.BookingQuicklyActivity;
import com.yuenidong.activity.R;
import com.yuenidong.activity.VenuesAddSignActivity;
import com.yuenidong.activity.VenuesCommentActivity;
import com.yuenidong.activity.VenuesSignActivity;
import com.yuenidong.app.DsncLog;
import com.yuenidong.common.AppData;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.widget.ImageCycleView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩  场馆信息
 */
public class VenuesInfoFragment extends Fragment {
    @InjectView(R.id.tv_firstprice)
    TextView tv_firstPrice;
    @InjectView(R.id.ad_view)
    ImageCycleView mAdView;

    private ArrayList<String> mImageUrl = null;

    private String imageUrl1 = "http://app.yuenidong.com/rrydBack1.1/img/2.jpg";

    private String imageUrl2 = "http://app.yuenidong.com/rrydBack1.1/img/3.jpg";

    private String imageUrl3 = "http://app.yuenidong.com/rrydBack1.1/img/4.jpg";

    private String imageUrl4 = "http://i1.s1.dpfile.com/pc/mc/3d8f08f18b0ee3dd757e536c84d34cc8(450c280)/aD0yODAmaz0vcGMvbWMvM2Q4ZjA4ZjE4YjBlZTNkZDc1N2U1MzZjODRkMzRjYzgmbG9nbz0wJm09YyZ3PTQ1MA.38cfc7cfb7bb928514511ee191cef45b/thumb.jpg";

    private String imageUrl5 = "http://i2.s1.dpfile.com/pc/mc/71493652f06b9f316e3c2543e1a6b685(450c280)/aD0yODAmaz0vcGMvbWMvNzE0OTM2NTJmMDZiOWYzMTZlM2MyNTQzZTFhNmI2ODUmbG9nbz0wJm09YyZ3PTQ1MA.ace4a1a0bf436fcfce5c9ba250a50475/thumb.jpg";

    //打电话
    @OnClick(R.id.rl_phone)
    void phone() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:13850734494"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppData.getContext().startActivity(intent);
    }

    //场馆评价
    @OnClick(R.id.rl_venues_comment)
    void comment() {
        Intent intent = new Intent(getActivity(), VenuesCommentActivity.class);
        startActivity(intent);
    }

    //添加签到
    @OnClick(R.id.rl_sign)
    void sign() {
        Intent intent = new Intent(getActivity(), VenuesSignActivity.class);
        startActivity(intent);
    }

    //立即预定
    @OnClick(R.id.btn_predetermine)
    void predetermine() {
        Intent intent = new Intent(getActivity(), BookingQuicklyActivity.class);
        startActivity(intent);
    }

    public static VenuesInfoFragment newInstance() {
        VenuesInfoFragment fragment = new VenuesInfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public VenuesInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venues_info, container, false);
        ButterKnife.inject(this, view);
        tv_firstPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中划线
//        //获取手机屏幕宽度和高度
//        DisplayMetrics dm = new DisplayMetrics();
//        //取得窗口属性
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        //窗口的宽度
//        int screenWidth = dm.widthPixels;
//
//        //窗口高度
//        int screenHeight = dm.heightPixels;
//
//        int screenWidthDp = CommonUtils.px2dip(getActivity(), screenWidth);
////        int screenHeightDp = CommonUtils.px2dip(getActivity(), screenHeight);
//        int height = (int) (screenWidthDp);
//        ViewGroup.LayoutParams lp = mAdView.getLayoutParams();
//        lp.height = screenWidthDp * 9 / 16;
////        lp.height = screenHeight;
//        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        DsncLog.e("heightDp", height + "");
//        DsncLog.e("widthDp", screenWidthDp + "");
//        DsncLog.e("heightPx", screenHeight + "");
//        DsncLog.e("widthPx", screenWidth + "");
//        DsncLog.e("heightPx/1.4", screenHeight / 1.4 + "");
//        DsncLog.e("widthPx/1.4", screenWidth / 1.4 + "");
//        mAdView.setLayoutParams(lp);
        mImageUrl = new ArrayList<String>();
        mImageUrl.add(imageUrl1);
        mImageUrl.add(imageUrl2);
        mImageUrl.add(imageUrl3);
        mImageUrl.add(imageUrl4);
        mImageUrl.add(imageUrl5);
        mAdView.setImageResources(mImageUrl, mAdCycleViewListener);
        return view;
    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View imageView) {
            // TODO 单击图片处理事件
            Toast.makeText(getActivity(), "position->" + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 此处本人使用了ImageLoader对图片进行加装！
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        mAdView.startImageCycle();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdView.pushImageCycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdView.pushImageCycle();
    }

}

