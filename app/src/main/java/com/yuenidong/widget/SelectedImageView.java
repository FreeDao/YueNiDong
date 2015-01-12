package com.yuenidong.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;

/**
 * TODO
 *
 * @author 石岩
 *         2014-12-29下午3:29:49
 */
public class SelectedImageView extends ImageView {
    private int count = 0;
    private boolean isSelected = false;
    private int count_select = 0;
    private boolean isFirst;
    // 图片数组
    private Integer[] imgs2 = {R.drawable.label_billiards_grey,
            R.drawable.label_basketball_grey, R.drawable.label_running_grey,
            R.drawable.label_tennis_grey, R.drawable.label_badminton_grey,
            R.drawable.label_football_grey, R.drawable.label_riding_grey,
            R.drawable.label_tabletennis_grey, R.drawable.label_swimming_grey,
            R.drawable.label_bodybuilding_grey,
            R.drawable.label_slidingplate_grey, R.drawable.label_skidding_grey};
    private Integer[] imgs1 = {
            R.drawable.label_billiards_filled_round,
            R.drawable.label_basketball_filled_round, R.drawable.label_running_filled_round,
            R.drawable.label_tennis_filled_round, R.drawable.label_badminton_filled_round,
            R.drawable.label_football_filled_round, R.drawable.label_riding_filled_round,
            R.drawable.label_tabletennis_filled_round, R.drawable.label_swimming_filled_round,
            R.drawable.label_bodybuilding_filled_round,
            R.drawable.label_slidingplate_filled_round, R.drawable.label_skidding_filled_round
    };
    private String[] sports = {"台球", "篮球", "跑步", "网球", "羽毛球", "足球", "骑行",
            "乒乓球", "游泳", "健身", "滑板", "轮滑"};

    private ImageView imageView;

    public SelectedImageView(Context context, boolean isFirst,String str) {
        super(context);
        this.isFirst = isFirst;
        // TODO Auto-generated constructor stub
        imageView = (ImageView) getRootView();
        // unselected();

//        for (int i = 0; i < sports.length; i++) {
//            if (str.equals(sports[i]) && isFirst) {
//                imageView.setBackgroundResource(imgs2[i]);
//                isSelected = false;
//            }
//        }
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isSelected) {
                    unselected(sports[count]);
                    System.out.println("未选中");
                    Toast.makeText(getContext(), "未选中", Toast.LENGTH_SHORT).show();
                } else {
                    selected(sports[count]);
                    System.out.println("已选中");
                    Toast.makeText(getContext(), "已选中", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public SelectedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
//		imageView = (ImageView) getRootView();
//		// unselected();
//		imageView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				if (isSelected) {
//					unselected(sports[count]);
//					System.out.println("未选中");
//					Toast.makeText(getContext(), "未选中", Toast.LENGTH_SHORT).show();
//				} else {
//					selected(sports[count]);
//					System.out.println("已选中");
//					Toast.makeText(getContext(), "已选中", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
    }

    public void selected(String str) {
        for (int i = 0; i < sports.length; i++) {
            if (str.equals(sports[i])) {
                YueNiDongConstants.count += 1;
                DsncLog.e("count1",YueNiDongConstants.count+"");
//                isFirst = false;
//                if (YueNiDongConstants.count < 4) {
//                    switch (YueNiDongConstants.count){
//                        case 1:
//                            PreferenceUtil.setPreString("label_one",str);
//                            break;
//                        case 2:
//                            PreferenceUtil.setPreString("label_two",str);
//                            break;
//                        case 3:
//                            PreferenceUtil.setPreString("label_three",str);
//                            break;
//                    }
                    imageView.setBackgroundResource(imgs1[i]);
                    count = i;
                    isSelected = true;
                }
//                if(YueNiDongConstants.count==4){
//                    YueNiDongConstants.count=3;
//                }
//            }
        }
    }

    public void unselected(String str) {
        for (int i = 0; i < sports.length; i++) {
            if (str.equals(sports[i]) && isFirst) {
                imageView.setBackgroundResource(imgs2[i]);
                count = i;
                isSelected = false;
            }
//            if (str.equals(sports[i]) && !isFirst) {
//                switch(YueNiDongConstants.count){
//                    case 1:
//                        PreferenceUtil.setPreString("label_one","");
//                        break;
//                    case 2:
//                        PreferenceUtil.setPreString("label_two","");
//                        break;
//                    case 3:
//                        PreferenceUtil.setPreString("label_three","");
//                        break;
//                }
//                YueNiDongConstants.count-=1;
//                DsncLog.e("count",YueNiDongConstants.count+"");
//                imageView.setBackgroundResource(imgs2[i]);
//                count = i;
                isSelected = false;
//            }
        }
    }

}
