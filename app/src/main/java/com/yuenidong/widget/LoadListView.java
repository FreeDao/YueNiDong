package com.yuenidong.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.yuenidong.activity.R;

public class LoadListView extends ListView implements OnScrollListener {
	// 底部布局
	View footer;
	// 总数量
	int totalItemCount;
	// 最后一个可见的item
	int lastVisibleItem;
	// 正在加载
	boolean isLoading;
	
	ILoadListener iLoadListener;

	public LoadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public LoadListView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 添加底部加载提示布局到listview
	 */
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.listview_footer, null);
		this.addFooterView(footer);
		footer.findViewById(R.id.layout_load).setVisibility(View.GONE);
		this.setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (lastVisibleItem == totalItemCount
				&& scrollState == SCROLL_STATE_IDLE) {
			if (!isLoading) {
				isLoading = true;
				footer.findViewById(R.id.layout_load).setVisibility(
						View.VISIBLE);
				// 加载更多数据
				iLoadListener.onLoad();
			}

		}
	}
	/**
	 * 加载完毕
	 */
	public void loadComplete(){
		isLoading=false;
		footer.findViewById(R.id.layout_load).setVisibility(
				View.GONE);
	}
	
	public void setInterface(ILoadListener iLoadListener){
		this.iLoadListener=iLoadListener;
	}
	//加载更多数据的回调接口
	public interface ILoadListener{
		public void onLoad();
	}

}
