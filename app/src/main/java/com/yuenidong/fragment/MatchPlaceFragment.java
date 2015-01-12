package com.yuenidong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MatchPlaceFragment extends Fragment {
    private Button btn_sure;
    @InjectView(R.id.autoCompleteTextView)
    AutoCompleteTextView textView;

    public static MatchPlaceFragment newInstance() {
        MatchPlaceFragment fragment = new MatchPlaceFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MatchPlaceFragment() {
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
        View view = inflater.inflate(R.layout.fragment_match_place, container, false);
        ButterKnife.inject(this, view);
        btn_sure = (Button) getActivity().findViewById(R.id.actionbar_rightbutton);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = textView.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("place", place);
                getActivity().setResult(1, intent);
                getActivity().finish();
            }
        });
        List<String> list = new ArrayList<String>();
        list.add("123");
        list.add("121212");
        Object[] str = list.toArray();
        String[] obj = new String[str.length];
        for (int i = 0; i < str.length; i++) {
            obj[i] = str[i].toString();
            DsncLog.e("sss", obj[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.autocompletetext, obj);
        textView.setAdapter(adapter);
        return view;
    }

}
