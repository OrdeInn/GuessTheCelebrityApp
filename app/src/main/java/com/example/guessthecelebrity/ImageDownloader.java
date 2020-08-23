package com.example.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.widget.ImageView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;

public class ImageDownloader implements Runnable{

    String urlString;

    public ImageDownloader(String url){
        urlString = url;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream in = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(in);

            Handler mainHandler = MainActivity.getHandler();

            Message message = mainHandler.obtainMessage();
            message.what = 0;
            message.obj = bitmap;

            message.sendToTarget();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
