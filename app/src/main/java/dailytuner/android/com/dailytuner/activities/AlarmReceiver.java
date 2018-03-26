package dailytuner.android.com.dailytuner.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import dailytuner.android.com.dailytuner.R;

/**
 * Created by akhil on 21/3/18.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    Context context;

    NotificationManager notificationManager;

    Notification myNotification;

    @Override
    public void onReceive(Context arg0, Intent intent) {

        this.context = arg0;

        String activityName = intent.getStringExtra("activityName");
        long time = intent.getLongExtra("time",0);
        //String pillImage = intent.getStringExtra("pillImage");*/
        int MY_NOTIFICATION_ID = intent.getIntExtra("time", 0);
        Log.i("id",""+MY_NOTIFICATION_ID);
        int id = intent.getIntExtra("_id", 0);


        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_app);
//        Log.e("alarm", pillImage);

        Intent myIntent = new Intent(context, LoginActivity.class);
        //myIntent.putExtra("ACTIVITYID", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                arg0,
                MY_NOTIFICATION_ID,
                myIntent,
                0);
        myNotification = new NotificationCompat.Builder(arg0)
                .setSmallIcon(R.drawable.ic_app)
                .setContentTitle("Its time to complete the Activity")
                .setContentText(activityName)
                .setLargeIcon(icon)
                .setTicker("Activity Remainder!")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .extend(new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                .build();
        myNotification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        notificationManager =
                (NotificationManager)arg0.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(5000,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            v.vibrate(500);
        }

    }


}
