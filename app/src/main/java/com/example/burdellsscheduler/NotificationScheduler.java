package com.example.burdellsscheduler;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class NotificationScheduler {

    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    public static final String NOTIFICATION_TITLE = "notification_title";
    public static final String NOTIFICATION_MESSAGE = "notification_message";
    public static final String NOTIFICATION_ID = "notification_id";

    // Inner BroadcastReceiver class
    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra(NOTIFICATION_TITLE);
            String message = intent.getStringExtra(NOTIFICATION_MESSAGE);
            int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);

            createNotificationChannel(context);
            Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_icon) // Replace with your app's icon
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(context, R.color.tech_gold))
                    .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(notificationId, notification);
        }

        private void createNotificationChannel(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Scheduled Notifications";
                String description = "Channel for Alarm Manager";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static void scheduleNotifications(Context context, @NonNull List<Events> notificationTimes, String title, int message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (int i = 0; i < notificationTimes.size(); i++) {
            LocalDateTime localDateTime = notificationTimes.get(i).getTime();
            long triggerAtMillis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            triggerAtMillis -= 60000*message;
            long currentMillis = System.currentTimeMillis();
            // If the time is in the past, skip scheduling this notification
            if (triggerAtMillis < currentMillis) continue;

            Intent intent = new Intent(context, AlarmReceiver.class);
            Log.v("1", notificationTimes.get(i).getLabel());
            intent.putExtra(NOTIFICATION_TITLE, title.concat(notificationTimes.get(i).getLabel()));
            intent.putExtra(NOTIFICATION_MESSAGE,  notificationTimes.get(i).getLabel() + " is starting in " + message + " minutes.");
            intent.putExtra(NOTIFICATION_ID, (int) ((currentMillis % 2147483647) + 1));

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) ((currentMillis % 2147483647) + 1), intent, PendingIntent.FLAG_IMMUTABLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }
    }
}

