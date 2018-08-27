package com.infinity_coder.infinity.airtop.service.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.ui.chat.OnMessageListener;
import com.infinity_coder.infinity.airtop.ui.main.MainActivity;

public class MessageNotification implements OnMessageListener{

    private Notification.Builder getNotification(String nickname, Message message) {
        Context context = App.getInstance().getBaseContext();
        Intent actionIntent = new Intent(context, MainActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, actionIntent, PendingIntent.FLAG_ONE_SHOT);
        String text = message.text;
        return new Notification.Builder(context)
                .setContentTitle(nickname)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_logo_rounded)
                .setContentIntent(actionPendingIntent);
    }

    @Override
    public void onMessage(String nickname, Message message) {
        Context context = App.getInstance().getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = getNotification(nickname, message);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = String.valueOf(1);
                builder.setChannelId(channelId);
                NotificationChannel channel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(1, builder.build());
        }
    }
}
