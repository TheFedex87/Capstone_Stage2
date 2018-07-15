package com.udacity.thefedex87.takemyorder.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.ui.activities.LoginMapsActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.WaiterMainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feder on 15/07/2018.
 */

public class WaiterRequestAttentionService extends Service {
    private final String CALLS_ROOT = "waiters_calls";
    private static int NOTIFICATION_ID = 1;

    private boolean serviceIsRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServiceWithNotification(intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        serviceIsRunning = false;
        super.onDestroy();
    }

    private void startServiceWithNotification(Intent intent){
        if(serviceIsRunning) return;
        serviceIsRunning = true;
        final String restaurantId = intent.getStringExtra(LoginMapsActivity.RESTAURANTS_INFO_KEY);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference waiterCallReference = db.getReference(CALLS_ROOT + "/" +restaurantId);
        waiterCallReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO: manage if restaurant does not exists
                if(dataSnapshot != null){
                    List<WaiterCall> waiterCalls = new ArrayList<>();
                    for(DataSnapshot waiterCallSnapshot : dataSnapshot.getChildren()) {
                        WaiterCall waiterCall = waiterCallSnapshot.getValue(WaiterCall.class);
                        waiterCall.setId(waiterCallSnapshot.getKey());
                        waiterCalls.add(waiterCall);
                    }
                    //calls.setValue(waiterCalls);
                    createNotification(getApplicationContext().getString(R.string.waiter_call_text, waiterCalls.get(waiterCalls.size() - 1).getTableId()), restaurantId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createNotification(String notificationText, String restaurantId){
        Intent intent = new Intent(getApplicationContext(), WaiterMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(LoginMapsActivity.RESTAURANTS_INFO_KEY, restaurantId);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), WaiterMainActivity.NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.ic_waiter_call_black))
                .setSmallIcon(R.drawable.ic_waiter_call)
                .setContentTitle(getString(R.string.waiter_new_waiter_call))
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[] {1000})
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build());


    }
}
