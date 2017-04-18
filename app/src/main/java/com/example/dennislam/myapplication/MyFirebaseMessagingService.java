package com.example.dennislam.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.dennislam.myapplication.activity.MainPageActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by shaunlau on 18/4/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.v("message recevied","~");
           if (remoteMessage.getNotification()!= null){
               String title = remoteMessage.getNotification().getTitle();
               String message = remoteMessage.getNotification().getBody();
               Log.v("title " +title,"~");
               sendNotification(title,message);
           }
    }

    public void sendNotification(String title, String message){
        Intent intent = new Intent(getApplicationContext(),MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("JobLibrary")
                .setContentText(message)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.appicon)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(14,notificationBuilder.build());
    }
}
