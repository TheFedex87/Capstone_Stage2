package com.udacity.thefedex87.takemyorder.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Vibrator;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.models.WaiterReadyOrder;
import com.udacity.thefedex87.takemyorder.ui.activities.LoginMapsActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.WaiterMainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feder on 14/07/2018.
 */

public class WaiterAttentionRequest extends IntentService {
    private final String CALLS_ROOT = "waiters_calls";
    private final String READY_ORDERS = "orders";
    private static int NOTIFICATION_ID = 1;

    private static boolean isExecutingTask = false;

    private String restaurantId;
    private boolean callsFirstDownload = true;
    private boolean readyOrdersFirstDownload = true;
    private int currentCallSize = 0;
    private int currentReadyOrdersSize = 0;

    private FirebaseDatabase db;
    private DatabaseReference waiterCallReference;
    private DatabaseReference waiterReadyOrderReference;

    public WaiterAttentionRequest() {
        super(WaiterAttentionRequest.class.getSimpleName());

        db = FirebaseDatabase.getInstance();

    }

//    @Override
//    public void onDestroy() {
//        isExecutingTask = false;
//        waiterCallReference.removeEventListener(callsEventListener);
//        super.onDestroy();
//    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if(isExecutingTask) return;
        isExecutingTask = true;

        restaurantId = intent.getStringExtra(LoginMapsActivity.RESTAURANTS_INFO_KEY);

        waiterCallReference = db.getReference(CALLS_ROOT + "/" +restaurantId);
        waiterReadyOrderReference = db.getReference(READY_ORDERS + "/" +restaurantId);

        waiterCallReference.addValueEventListener(callsEventListener);
        waiterReadyOrderReference.addValueEventListener(readyOrdersEventListener);
    }

    private ValueEventListener readyOrdersEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot != null){
                ArrayList<WaiterReadyOrder> readyOrders = new ArrayList<>();
                for(DataSnapshot waiterReadyOrderSnapshot : dataSnapshot.getChildren()) {
                    WaiterReadyOrder readyOrder = waiterReadyOrderSnapshot.getValue(WaiterReadyOrder.class);
                    readyOrder.setId(waiterReadyOrderSnapshot.getKey());
                    readyOrders.add(readyOrder);
                }

                if (!readyOrdersFirstDownload && readyOrders.size() > currentReadyOrdersSize) {
                    createNotification(getApplicationContext().getString(R.string.waiter_order_ready_text, readyOrders.get(readyOrders.size() - 1).getTableId()), restaurantId);
                }

                currentReadyOrdersSize = readyOrders.size();
                readyOrdersFirstDownload = false;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ValueEventListener callsEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //TODO: manage if restaurant does not exists
            if(dataSnapshot != null){
                ArrayList<WaiterCall> waiterCalls = new ArrayList<>();
                for(DataSnapshot waiterCallSnapshot : dataSnapshot.getChildren()) {
                    WaiterCall waiterCall = waiterCallSnapshot.getValue(WaiterCall.class);
                    waiterCall.setId(waiterCallSnapshot.getKey());
                    waiterCalls.add(waiterCall);
                }

                if (!callsFirstDownload && waiterCalls.size() > currentCallSize) {
                    createNotification(getApplicationContext().getString(R.string.waiter_call_text, waiterCalls.get(waiterCalls.size() - 1).getTableId()), restaurantId);
                }

                currentCallSize = waiterCalls.size();
                callsFirstDownload = false;


//                    Intent callsIntent = new Intent();
//                    callsIntent.setAction(WaiterMainActivity.ACTION_WAITER_CALLS);
//                    //callsIntent.putParcelableArrayListExtra(WaiterMainActivity.WAITER_CALLS_LIST, waiterCalls);
//                    sendBroadcast(intent);
//                    //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };



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
