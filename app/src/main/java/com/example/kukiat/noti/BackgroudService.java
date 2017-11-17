package com.example.kukiat.noti;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;

/**
 * Created by kukiat on 11/17/2017 AD.
 */

public class BackgroudService extends Service{
    private Microgear microgear = new Microgear(this);
    private String appid = "noti";
    private String key = "6xeLdlHHWBuM49O";
    private String secret = "tzTRtxJbuejASaIBHWD3snUa3";
    private String alias = "client";

    Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service", "onBlind BackgroudService");
        return null;
    }

    @Override
    public void onCreate() {
        MicrogearCallBack callback = new MicrogearCallBack();

        microgear.connect(appid, key, secret, alias);
        microgear.setCallback(callback);
        microgear.subscribe("message");
        Log.i("service", "create BackgroudService");
    }

    @SuppressLint("HandlerLeak")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.i("service", "onStartCommand BackgroudService");

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String message = bundle.getString("myKey");
                Log.i("service", message);

            }
        };
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("service", "destroy BackgroudService");
        super.onDestroy();
    }
    class MicrogearCallBack implements MicrogearEventListener {
        @Override
        public void onConnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Now I'm connected with netpie message");
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        public void onMessage(String topic, String message) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", topic+" : "+message);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        public void onPresent(String token) {
//            Message msg = handler.obtainMessage();
//            Bundle bundle = new Bundle();

//            bundle.putString("myKey", "New friend Connect :"+ token);

//            msg.setData(bundle);
//            handler.sendMessage(msg);
//            Log.i("present","New friend Connect :"+ token);
        }

        @Override
        public void onAbsent(String token) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Friend lost :"+token);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("service","Friend lost :"+token);
        }

        @Override
        public void onDisconnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Disconnected");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("service","Disconnected");
        }

        @Override
        public void onError(String error) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Exception : "+error);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("service","Exception : "+error);
        }

        @Override
        public void onInfo(String info) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Exception : "+info);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("service","Info : "+info);
        }
    }
}
