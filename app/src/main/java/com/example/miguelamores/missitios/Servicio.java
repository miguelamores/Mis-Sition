package com.example.miguelamores.missitios;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.security.Principal;

/**
 * Created by miguelamores on 7/25/14.
 */
public class Servicio extends Service {

    NotificationManager nm;
    //private static final int ID_NOTIFICACION_CREAR = 1;
    //int icono = R.drawable.ic_launcher;
    Notification notificacion;
    //private Timer timer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificacion = new Notification(R.drawable.ic_launcher, "Has añadido un nuevo sitio", 0);
        //Notification notificacion = new Notification(R.drawable.ic_launcher, "Servicio de notoficaciones", System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, InsertarSitio.class), 0);
        notificacion.setLatestEventInfo(this, "Has visitado un nuevo lugar", "",pi);

        //timer = new Timer();
        //timer.scheduleAt


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        nm.notify(1, notificacion);

        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nm.cancel(1);
    }

    /*
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            Not
        }
    })*/
}