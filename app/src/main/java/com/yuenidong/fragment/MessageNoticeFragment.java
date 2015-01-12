package com.yuenidong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yuenidong.activity.R;
import com.yuenidong.adapter.NoticeAdapter;
import com.yuenidong.bean.MessageNoticeEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩 消息(通知)
 */
public class MessageNoticeFragment extends Fragment {
    @InjectView(R.id.listview)
    ListView listView;
    private List<MessageNoticeEntity> list;
    public static MessageNoticeFragment newInstance() {
        MessageNoticeFragment fragment = new MessageNoticeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MessageNoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        ButterKnife.inject(this, view);
        list=new ArrayList<MessageNoticeEntity>();
        MessageNoticeEntity entity=new MessageNoticeEntity();
        NoticeAdapter adapter=new NoticeAdapter(getActivity(),list);
        listView.setAdapter(adapter);
        return view;
    }
}
