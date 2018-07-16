package com.udacity.thefedex87.takemyorder.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.ui.activities.LoginMapsActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.WaiterMainActivity;

import timber.log.Timber;

/**
 * Created by federico.creti on 16/07/2018.
 */

public class TakeMyOrderNotificationService extends FirebaseMessagingService {
    private static int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Timber.d("MESSAGE RECEIVED: " + remoteMessage.getData().get("message"));

        createNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("restaurantId"));
    }

    private void createNotification(String notificationText, String restaurantId){
        Intent intent = new Intent(getApplicationContext(), WaiterMainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(LoginMapsActivity.WAITER_RESTAURANT_KEY, restaurantId);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), WaiterMainActivity.NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.ic_waiter_call_black))
                .setSmallIcon(R.drawable.ic_waiter_call)
                .setContentTitle(getString(R.string.waiter_new_waiter_call))
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000 })
                .setLights(Color.BLUE, 1000, 500)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build());

        Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1000);
    }
}
