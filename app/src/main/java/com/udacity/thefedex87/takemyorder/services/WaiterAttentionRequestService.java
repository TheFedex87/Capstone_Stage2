package com.udacity.thefedex87.takemyorder.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Vibrator;
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
import com.udacity.thefedex87.takemyorder.models.WaiterReadyOrder;
import com.udacity.thefedex87.takemyorder.ui.activities.LoginMapsActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.WaiterMainActivity;

import java.util.ArrayList;

/**
 * Created by feder on 14/07/2018.
 */
/*This is the class service user for Waiter. It is a service which is started when a waiter logs into the system. It observe for new call request and for new ready orders.
When a new request attnetion (waiter call or ready order) arrives, this service send a notification to the device, and send a Broadcast message, which contains the new request.
The message is capture by a Broadcast receiver which take care to fill the UI for waiter app section, with the received data
*/
public class WaiterAttentionRequestService extends IntentService {

    private final String CALLS_ROOT = "waiters_calls";
    private final String READY_ORDERS = "orders";
    private static int NOTIFICATION_ID = 1;

    private static boolean isExecutingTask = false;

    private String restaurantId;
    private boolean callsFirstDownload = true;
    private boolean readyOrdersFirstDownload = true;

    private static ArrayList<WaiterCall> currentCalls;
    private static ArrayList<WaiterReadyOrder> currentReadyOrders;

    private FirebaseDatabase db;
    private DatabaseReference waiterCallReference;
    private DatabaseReference waiterReadyOrderReference;

    public WaiterAttentionRequestService() {
        super(WaiterAttentionRequestService.class.getSimpleName());

        db = FirebaseDatabase.getInstance();

    }

    public static void setIsExecutingTask(boolean isExecutingTask){
        WaiterAttentionRequestService.isExecutingTask = isExecutingTask;
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        //If the service has already been executed (and we are here again because for example a screen rotation is occurred), this means we do not create another valueEventListener (which already exists and is waiting to receive new request), we just send
        //the previous data through a broadcast message
        if(isExecutingTask) {
            sendCallsBroadcast(currentCalls);
            sendReadyOrdersBroadcast(currentReadyOrders);
            return;
        }
        isExecutingTask = true;

        restaurantId = intent.getStringExtra(LoginMapsActivity.RESTAURANTS_INFO_KEY);

        //Select the correct reference for the restaurant where the waiter is logged
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

                //If this is not the first download after app is started and there is a new ready order request send the notification
                if (!readyOrdersFirstDownload && readyOrders.size() > currentReadyOrders.size()) {
                    createNotification(getApplicationContext().getString(R.string.waiter_order_ready_text, readyOrders.get(readyOrders.size() - 1).getTableId()),
                            getApplicationContext().getString(R.string.waiter_new_order_ready),
                            restaurantId,
                            R.drawable.ic_waiter_order_ready_black,
                            R.drawable.ic_waiter_order_ready);
                }

                currentReadyOrders = readyOrders;
                readyOrdersFirstDownload = false;

                sendReadyOrdersBroadcast(readyOrders);
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

                //If this is not the first download after app is started and there is a new waiter call request send the notification
                if (!callsFirstDownload && waiterCalls.size() > currentCalls.size()) {
                    createNotification(getApplicationContext().getString(R.string.waiter_call_text, waiterCalls.get(waiterCalls.size() - 1).getTableId()),
                            getApplicationContext().getString(R.string.waiter_new_waiter_call),
                            restaurantId,
                            R.drawable.ic_waiter_call_black,
                            R.drawable.ic_waiter_call);
                }

                currentCalls = waiterCalls;
                callsFirstDownload = false;

                sendCallsBroadcast(waiterCalls);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void sendCallsBroadcast(ArrayList<WaiterCall> waiterCalls){
        //Send the broadcast message with the waiter calls list
        Intent callsIntent = new Intent();
        callsIntent.setAction(WaiterMainActivity.ACTION_WAITER_CALLS);
        callsIntent.putParcelableArrayListExtra(WaiterMainActivity.WAITER_CALLS_LIST, waiterCalls);
        sendBroadcast(callsIntent);
    }

    private void sendReadyOrdersBroadcast(ArrayList<WaiterReadyOrder> readyOrders){
        //Send the broadcast message with the ready orders list
        Intent readyOrdersIntent = new Intent();
        readyOrdersIntent.setAction(WaiterMainActivity.ACTION_WAITER_READY_ORDERS);
        readyOrdersIntent.putParcelableArrayListExtra(WaiterMainActivity.WAITER_READY_ORDERS_LIST, readyOrders);
        sendBroadcast(readyOrdersIntent);
    }

    private void createNotification(String notificationText, String notificationTitle, String restaurantId, int imageResourceSmall, int imageResourceLarge){
        Intent intent = new Intent(getApplicationContext(), WaiterMainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(LoginMapsActivity.WAITER_RESTAURANT_KEY, restaurantId);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), WaiterMainActivity.NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), imageResourceLarge))
                .setSmallIcon(imageResourceSmall)
                .setContentTitle(notificationTitle)
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
