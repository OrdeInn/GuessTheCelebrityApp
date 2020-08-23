package com.example.guessthecelebrity;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;


//General application structure lies in that class.
public class GuessApp extends Thread {

    private static ArrayList<String> celebrityNames;
    private static ArrayList<String> imageLinks;

    private static int correctImageIndex;
    private static int correctAnswerIndex;

    public GuessApp(){
        celebrityNames = new ArrayList<String>();
        imageLinks = new ArrayList<String>();
        correctImageIndex = 0;
        correctAnswerIndex = 0;
        HtmlDocumentDownloader htmlDocumentDownloader = new HtmlDocumentDownloader("https://www.imdb.com/list/ls052283250/");
        new Thread(htmlDocumentDownloader).start();
    }

    @Override
    public void run(){

        Handler mainHandler = MainActivity.getHandler();

        ArrayList<String> answers = new ArrayList<String>();

        int min = 0;
        int max = imageLinks.size();
        Log.i("MAX", Integer.toString(max));
        Random random = new Random();

        //Image that will display in question will choose from imageLinks[correctIndex]
        correctImageIndex = random.nextInt(max);

        int tempCounter=0;
        boolean isDone = false;
        while(!isDone){
            int wrongAnswer = random.nextInt(max);
            if(wrongAnswer != correctImageIndex){
                answers.add(celebrityNames.get(wrongAnswer));
                tempCounter++;
            }
            if(tempCounter == 3){
                isDone = true;
            }
        }
       correctAnswerIndex = random.nextInt(4);
        if(correctAnswerIndex == 4){
            answers.add(celebrityNames.get(correctImageIndex));
        }else{
            answers.add(correctAnswerIndex, celebrityNames.get(correctImageIndex));
        }


        String imageLink = imageLinks.get(correctImageIndex);

        ImageDownloader imageDownloader = new ImageDownloader(imageLink);
        new Thread(imageDownloader).start();

        //Sending answers to set them on buttons to main thread.
        Message message = MainActivity.getHandler().obtainMessage(1);
        message.obj = answers;
        message.sendToTarget();



    }


    public int getCorrectAnswerIndex(){ return correctAnswerIndex; }
    public int getCorrectImageIndex (){ return correctImageIndex; }
    public String getCelebrityName(int index){ return celebrityNames.get(index); }


    //To use in HtmlDocumentDownloader class' thread.
    public static void addCelebrityName(String celebrityName){
        celebrityNames.add(celebrityName);
    }
    public static void addImageLink(String imageLink){
        imageLinks.add(imageLink);
    }


}
