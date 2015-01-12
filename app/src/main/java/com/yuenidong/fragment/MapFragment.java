package com.yuenidong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.common.AppData;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩  周边
 */
public class MapFragment extends Fragment {
    private boolean isMap=false;

    private int status = 0;

    private static final int FRIEND_SELECTED = 1 << 0;

    private static final int COACH_SELECTED = 1 << 1;

    private static final int VENUES_SELECTED = 1 << 2;

    @InjectView(R.id.actionbar_text)
    TextView tv_title;
    @InjectView(R.id.actionbar_rightbutton)
    Button btn_right;
    @InjectView(R.id.tv_friend)
    TextView tv_friend;
    @InjectView(R.id.tv_coach)
    TextView tv_coach;
    @InjectView(R.id.tv_venues)
    TextView tv_venues;
    @InjectView(R.id.view_freiend)
    View view_friend;
    @InjectView(R.id.view_coach)
    View view_coach;
    @InjectView(R.id.view_venues)
    View view_venues;
    @InjectView(R.id.view_freiend_select)
    View view_friend_select;
    @InjectView(R.id.view_coach_select)
    View view_coach_select;
    @InjectView(R.id.view_venues_select)
    View view_venues_select;


    @OnClick(R.id.rl_friend)
    void tv_friend() {
        refreshTab(FRIEND_SELECTED);
        hideView();
        status = 0;
        view_friend_select.setVisibility(View.VISIBLE);
        view_venues.setVisibility(View.VISIBLE);
        view_coach.setVisibility(View.VISIBLE);
        if(!isMap) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = FriendFragment.newInstance();
            fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
            fragmentTransaction.commit();
        }else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = FriendMapFragment.newInstance();
            fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    @OnClick(R.id.rl_coach)
    void tv_coach() {
        refreshTab(COACH_SELECTED);
        hideView();
        status = 1;
        view_coach_select.setVisibility(View.VISIBLE);
        view_friend.setVisibility(View.VISIBLE);
        view_venues.setVisibility(View.VISIBLE);
        if(!isMap) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = CoachFragment.newInstance();
            fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
            fragmentTransaction.commit();
        }else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = CoachMapFragment.newInstance();
            fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
            fragmentTransaction.commit();
        }

    }

    @OnClick(R.id.rl_venues)
    void tv_venues() {
        refreshTab(VENUES_SELECTED);
        hideView();
        status = 2;
        view_venues_select.setVisibility(View.VISIBLE);
        view_friend.setVisibility(View.VISIBLE);
        view_coach.setVisibility(View.VISIBLE);
        if(!isMap) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = VenuesFragment.newInstance();
            fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
            fragmentTransaction.commit();
        }else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = VenuesMapFragment.newInstance();
            fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Button btn_showMap = (Button) getActivity().findViewById(R.id.actionbar_rightbutton);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isMap){
                    isMap=true;
                }else{
                    isMap=false;
                }
                switch (status) {
                    case 0:
                        if(isMap) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Fragment fragment = FriendMapFragment.newInstance();
                            fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
                            fragmentTransaction.commit();
                        }else{
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Fragment fragment = FriendFragment.newInstance();
                            fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
                            fragmentTransaction.commit();
                        }
                        break;
                    case 1:
                        if(isMap) {
                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                            Fragment fragment2 = CoachMapFragment.newInstance();
                            fragmentTransaction2.replace(R.id.layout_fragment_container, fragment2);
                            fragmentTransaction2.commit();
                        }else{
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Fragment fragment = CoachFragment.newInstance();
                            fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
                            fragmentTransaction.commit();
                        }
                        break;
                    case 2:
                        if(isMap) {
                            FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                            Fragment fragment3 = VenuesMapFragment.newInstance();
                            fragmentTransaction3.replace(R.id.layout_fragment_container, fragment3);
                            fragmentTransaction3.commit();
                        }else{
                            FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                            Fragment fragment3 = VenuesFragment.newInstance();
                            fragmentTransaction3.replace(R.id.layout_fragment_container, fragment3);
                            fragmentTransaction3.commit();
                        }
                        break;

//                    //------------------------------------地图与交互界面进行切换--------------------------------
//                    case 3:
//                        status = 0;
//                        DsncLog.e("status", status + "");
//                        FragmentTransaction fragmentTransaction4 = fragmentManager.beginTransaction();
//                        Fragment fragment4 = FriendFragment.newInstance();
//                        fragmentTransaction4.replace(R.id.layout_fragment_container, fragment4);
//                        fragmentTransaction4.commit();
//                        break;
//                    case 4:
//                        status = 1;
//                        DsncLog.e("status", status + "");
//                        FragmentTransaction fragmentTransaction5 = fragmentManager.beginTransaction();
//                        Fragment fragment5 = CoachFragment.newInstance();
//                        fragmentTransaction5.replace(R.id.layout_fragment_container, fragment5);
//                        fragmentTransaction5.commit();
//                        break;
//                    case 5:
//                        status = 2;
//                        DsncLog.e("status", status + "");
//                        FragmentTransaction fragmentTransaction6 = fragmentManager.beginTransaction();
//                        Fragment fragment6 = VenuesFragment.newInstance();
//                        fragmentTransaction6.replace(R.id.layout_fragment_container, fragment6);
//                        fragmentTransaction6.commit();
//                        break;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.inject(this, view);
        btn_right.setBackgroundResource(R.drawable.button_map);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(AppData.getString(R.string.main_map));
        btn_right.setVisibility(View.VISIBLE);
        refreshTab(FRIEND_SELECTED);
        hideView();
        view_friend_select.setVisibility(View.VISIBLE);
        view_coach.setVisibility(View.VISIBLE);
        view_venues.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = FriendFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
        fragmentTransaction.commit();

        return view;
    }

    private void refreshTab(final int whichSelected) {
        tv_friend.setTextColor(AppData.getColor(R.color.view));
        tv_coach.setTextColor(AppData.getColor(R.color.view));
        tv_venues.setTextColor(AppData.getColor(R.color.view));

        view_friend.setBackgroundColor(AppData.getColor(R.color.view));
        view_coach.setBackgroundColor(AppData.getColor(R.color.view));
        view_venues.setBackgroundColor(AppData.getColor(R.color.view));

        switch (whichSelected) {
            case FRIEND_SELECTED:
                tv_friend.setTextColor(AppData.getColor(R.color.green));
                view_friend.setBackgroundColor(AppData.getColor(R.color.green));
                break;
            case COACH_SELECTED:
                tv_coach.setTextColor(AppData.getColor(R.color.green));
                view_coach.setBackgroundColor(AppData.getColor(R.color.green));
                break;
            case VENUES_SELECTED:
                tv_venues.setTextColor(AppData.getColor(R.color.green));
                view_venues.setBackgroundColor(AppData.getColor(R.color.green));
                break;
        }
    }

    private void hideView() {
        view_friend.setVisibility(View.GONE);
        view_friend_select.setVisibility(View.GONE);
        view_coach.setVisibility(View.GONE);
        view_coach_select.setVisibility(View.GONE);
        view_venues.setVisibility(View.GONE);
        view_venues_select.setVisibility(View.GONE);

    }

}
