package com.example.manthankhorwal.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity {
 ArrayList<Integer> answers=new ArrayList<Integer>();
    int score=0;
    int numberofQuestions=0;
    TextView resultTextview;
    int locationofCorrectanswer;
    TextView scoreTextview;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    TextView sumTextview,Timertextview;
    Button playagainbtn;
    CountDownTimer cnt;
boolean running;
    public void playgain(View view)
    {

        score=0;
        numberofQuestions=0;
        scoreTextview.setText(score+"/"+numberofQuestions);
        playagainbtn.setVisibility(View.INVISIBLE);
        resultTextview.setText("");
        newquestion();
       cnt= new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {

                Timertextview.setText(String.valueOf(l/1000)+"s");
                running=true;
            }

            @Override
            public void onFinish() {
                resultTextview.setText("Time's up!");
                playagainbtn.setVisibility(View.VISIBLE);
                running=false;
            }
        }.start();
    }
    public void getanswers(View view)
    {
         if(running)
         {if(view.getTag().toString().equals(Integer.toString(locationofCorrectanswer)) )
        {
            resultTextview.setText("Correct");
            score++;
        }
        else
        {
            resultTextview.setText("Wrong");
        }
        numberofQuestions++;
        scoreTextview.setText(score+"/"+numberofQuestions);
           newquestion();
    }}
 public void newquestion()
 {
     Random rand=new Random();
     int a=rand.nextInt(31);
     int b=rand.nextInt(31);
     sumTextview.setText(Integer.toString(a)+" + "+Integer.toString(b));
     locationofCorrectanswer=rand.nextInt(4);
     answers.clear();
     for(int i=0;i<4;i++)
     {
         if(locationofCorrectanswer==i)
             answers.add(a+b);
         else
         {
             int wronganswers=rand.nextInt(61);
             while(wronganswers==a+b)
                 wronganswers=rand.nextInt(61);
             answers.add(wronganswers);
         }
     }
     button0.setText(Integer.toString(answers.get(0)));
     button1.setText(Integer.toString(answers.get(1)));
     button2.setText(Integer.toString(answers.get(2)));
     button3.setText(Integer.toString(answers.get(3)));
 }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
         sumTextview=(TextView)findViewById(R.id.sumTextview);
        button0=(Button)findViewById(R.id.button0);
         button1=(Button)findViewById(R.id.button1);
         button2=(Button)findViewById(R.id.button2);
         button3=(Button)findViewById(R.id.button3);
        resultTextview  =(TextView)findViewById(R.id.resultTextview);
        scoreTextview=(TextView)findViewById(R.id.ScoreTextview);
        Timertextview=(TextView)findViewById(R.id.Timertextview);
        playagainbtn=(Button)findViewById(R.id.playagainbtn);

        playgain(findViewById(R.id.resultTextview));



    }


}
