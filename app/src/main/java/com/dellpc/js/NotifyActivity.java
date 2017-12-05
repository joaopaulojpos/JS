package com.dellpc.js;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NotifyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);


    }

    public void gerarNotificacao(){

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, NotificationActivity.class), 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setTicker("Ticker Texto");
            builder.setContentTitle("Jaime App");
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notifi));
            builder.setContentIntent(p);

            NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
            String [] descs = new String[]{"Atenção para o clima no dia","situacao" ,"O clima será:"};
            for(int i = 0; i < descs.length; i++){
                style.addLine(descs[i]);
            }
            builder.setStyle(style);

            Notification n = builder.build();
            n.flags = Notification.FLAG_AUTO_CANCEL;
            nm.notify(R.drawable.notifi, n);

            try{
                Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone toque = RingtoneManager.getRingtone(this, som);
                toque.play();
            }
            catch(Exception e){}
        }

    }