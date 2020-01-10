package com.example.manthankhorwal.connect3game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    boolean gameactive = true;
     int activeplayer=0;
      int[] gamestate={2,2,2,2,2,2,2,2,2};
      int[][] winningpositions ={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};

    public void dropit(View view) {

        ImageView counter = (ImageView) view;
        int tappedcounter = Integer.parseInt(counter.getTag().toString());

        if (gameactive==true && gamestate[tappedcounter] == 2) {
            gamestate[tappedcounter] = activeplayer;
            counter.setTranslationY(-1500);

            if (activeplayer == 0) {

                activeplayer = 1;

                counter.setImageResource(R.drawable.yellow);

            } else {

                activeplayer = 0;
                counter.setImageResource(R.drawable.red);

            }
            counter.animate().rotation(3600).translationYBy(1500).setDuration(400);

            for (int i = 0; i < winningpositions.length; i++) {
                if (gamestate[winningpositions[i][0]] == gamestate[winningpositions[i][1]] && gamestate[winningpositions[i][1]] == gamestate[winningpositions[i][2]] && gamestate[winningpositions[i][0]] != 2)
                {
                    gameactive=false;
                    String winner = "";
                    if (activeplayer == 1) {
                        winner = "Yellow";

                    } else {
                        winner = "Red";

                    }
                    Button btn=(Button)findViewById(R.id.buttonplay);
                    TextView message=(TextView)findViewById(R.id.message);
                    message.setText(winner+" has Won");

                    btn.setVisibility(View.VISIBLE);
                    message.setVisibility(View.VISIBLE);
                }
            }
            int i=0;
            for( i=0;i<gamestate.length;i++)
            {
                if(gamestate[i]==2)
                    break;
            }
            if(i==gamestate.length)
            {
                Button btn=(Button)findViewById(R.id.buttonplay);
                TextView message=(TextView)findViewById(R.id.message);
                message.setText("Better Luck next Time");
                btn.setVisibility(View.VISIBLE);
                message.setVisibility(View.VISIBLE);
            }

        }
    }
   public void again(View view)
    {
        Button btn=(Button)findViewById(R.id.buttonplay);
        TextView message=(TextView)findViewById(R.id.message);
       btn.setVisibility(View.INVISIBLE);
        message.setVisibility(View.INVISIBLE);
        GridLayout grid=(GridLayout)findViewById(R.id.gridLayout);
      //  Toast.makeText(this,grid.getChildCount(), Toast.LENGTH_SHORT).show();
        for(int i=0;i<grid.getChildCount();i++)
        {
            ImageView counter=(ImageView)grid.getChildAt(i);
            counter.setImageDrawable(null);
        }

        activeplayer=0;
         for(int i=0;i<gamestate.length;i++)
             gamestate[i]=2;
        gameactive=true;

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
