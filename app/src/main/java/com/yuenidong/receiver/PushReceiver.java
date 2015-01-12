package com.yuenidong.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.AppManager;
import com.yuenidong.app.DsncLog;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;

/**
 * Created by 石岩 on 2014/12/26.
 */
public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {

            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                if (result) {
                    DsncLog.e("个推透传功能", "成功!");
                } else {
                    DsncLog.e("个推透传功能", "失败!");
                }

                if (payload != null) {
                    String data = new String(payload);
                    DsncLog.e("个推透传数据", data);
                }

                //自定义通知栏
                NotificationManager mNotificationManager = (NotificationManager) AppData.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification(R.drawable.ic_launcher, "通知", System.currentTimeMillis());
                Intent intent2 = new Intent(AppData.getContext(), MainActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(AppData.getContext(), 0, intent2, 0);
                notification.setLatestEventInfo(AppData.getContext(), "约你动", "您收到了一条活动邀请!", pendingIntent);

                notification.flags = Notification.FLAG_AUTO_CANCEL;//点击后自动消失

                notification.defaults = Notification.DEFAULT_SOUND;//声音默认
                PreferenceUtil.setPreBoolean("notice", true);
                mNotificationManager.notify(0, notification);
                break;
//            case PushConsts.GET_CLIENTID:
//                // 获取ClientID(CID)
//                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
//                String cid = bundle.getString("clientid");
//                if (GetuiSdkDemoActivity.tView != null)
//                    GetuiSdkDemoActivity.tView.setText(cid);
//                break;
//            case PushConsts.THIRDPART_FEEDBACK:
//			/*String appid = bundle.getString("appid");
//			String taskid = bundle.getString("taskid");
//			String actionid = bundle.getString("actionid");
//			String result = bundle.getString("result");
//			long timestamp = bundle.getLong("timestamp");
//
//			Log.d("GetuiSdkDemo", "appid = " + appid);
//			Log.d("GetuiSdkDemo", "taskid = " + taskid);
//			Log.d("GetuiSdkDemo", "actionid = " + actionid);
//			Log.d("GetuiSdkDemo", "result = " + result);
//			Log.d("GetuiSdkDemo", "timestamp = " + timestamp);*/
//                break;
            default:
                break;
        }
    }
}
