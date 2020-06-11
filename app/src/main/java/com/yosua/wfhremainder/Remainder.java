package com.yosua.wfhremainder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.text.style.SuperscriptSpan;
import android.widget.Toast;


import java.util.Calendar;


public class Remainder extends BroadcastReceiver {
    public static final String EXTRA_MESSAGE = "message";
    public static final String ID = "id";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        Integer id = intent.getExtras().getInt(ID);
        showToast(context, message);
        showAlarmNotification(context, message, id);
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, "It's Time to "+message, Toast.LENGTH_SHORT ).show();
    }

    public void SetAlarm (Context context, String time, String message, Integer id){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Remainder.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(ID, id);

        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id, intent, 0);
        if(alarmManager != null){
            alarmManager.setInexactRepeating(alarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, "alarm set", Toast.LENGTH_SHORT).show();
    }

    public void UpdateAlarm (Context context, String time, String message, Integer id){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Remainder.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(ID, id);

        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(alarmManager != null){
            alarmManager.setInexactRepeating(alarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showAlarmNotification(Context context, String message, Integer id){
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "WFH Remainder channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder;
                builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("WFH Remainder")
                .setContentText("It's time to "+ message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if(notificationManagerCompat != null){
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if(notificationManagerCompat != null) {
            notificationManagerCompat.notify(id, notification);
        }
    }

    public void cancelAlarm(Context context, Integer id){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Remainder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();

        if(alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }

    public boolean checkIfAlarmIsRegistred(Context context, int id){
        Intent intent = new Intent(context, Remainder.class);
        return (PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_NO_CREATE) != null);
    }
}
