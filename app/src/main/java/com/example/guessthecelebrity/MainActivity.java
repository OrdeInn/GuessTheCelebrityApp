package com.example.guessthecelebrity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static TextView loadingText;
    static ImageView celebrityImage;
    Button button1;
    Button button2;
    Button button3;
    Button button4;

    private static Handler handler;
    GuessApp guessApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guessApp = new GuessApp();

        loadingText = findViewById(R.id.loadingTextView);
        celebrityImage = findViewById(R.id.imageView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);


        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                /*
                    0: If ImageDownloader downloads the image
                    1: If GuessApp sets answers for each buttons
                    2: If HtmlDocumentDownloader finishes all downloading and adding operations
                 */

                if(msg.what == 0){
                   celebrityImage.setImageBitmap((Bitmap) msg.obj);
                }else if(msg.what == 1 ){
                    ArrayList<String> answers = (ArrayList<String>) msg.obj;
                    button1.setText(answers.get(0));
                    button2.setText(answers.get(1));
                    button3.setText(answers.get(2));
                    button4.setText(answers.get(3));

                }else if(msg.what == 2){
                    getContents();
                    guessApp.start();
                }
            }
        };

    }


    public void guessCelebrity(View view){


        String tag = view.getTag().toString();
        int userAnswerIndex = Integer.parseInt(tag);
        int correctAnswerIndex = guessApp.getCorrectAnswerIndex();


        if(userAnswerIndex == correctAnswerIndex){
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Wrong," + " It was " + guessApp.getCelebrityName(guessApp.getCorrectImageIndex()), Toast.LENGTH_SHORT).show();
        }

        guessApp.run();

    }


    public static Handler getHandler(){
        return handler;
    }


    //When all documents are downloaded, removes the "Loading..." text and gets the question.
    private void getContents(){
        loadingText.setVisibility(View.INVISIBLE);
        celebrityImage.setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        button4.setVisibility(View.VISIBLE);


    }

}