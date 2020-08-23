package com.example.guessthecelebrity;

import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class HtmlDocumentDownloader implements Runnable{

    String urlString;
    private String result;
    public HtmlDocumentDownloader(String urlString){
        this.urlString = urlString;
        result = "";
    }


    @Override
    public void run() {

        try {
            URL url = new URL(urlString);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            /*
            *Reader reads line by line
            * On html document that I get from imdb.com, contents that I looking for starts with new line.
             */
            String data = reader.readLine();

            Log.i("Download Html Content", "On Process");


            //Regular Expressions for Celebrity's name or link for image
            Pattern celebrityNamePattern;
            Pattern imageLinkPattern;
            Matcher nameMatcher;
            Matcher linkMatcher;


            //Reads the HTML line and gets celebrity's name and image url.
            while(data != null){

                celebrityNamePattern = Pattern.compile("^(> <img alt=\")(.*)(\")");
                imageLinkPattern = Pattern.compile("^(src=\")(.*)(\")");

                nameMatcher = celebrityNamePattern.matcher(data);
                linkMatcher = imageLinkPattern.matcher(data);

                if(nameMatcher.find()){
                    GuessApp.addCelebrityName(nameMatcher.group(2));
                }else if(linkMatcher.find()){
                    GuessApp.addImageLink(linkMatcher.group(2));
                }
                result += data;

                data = reader.readLine();
            }

            //Message for main thread to say everything is ready.
            Message message = MainActivity.getHandler().obtainMessage();
            message.what = 2;
            message.sendToTarget();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
