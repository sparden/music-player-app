package support.android.com.exp1;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //setContentView(android.R.layout.activity_main);
        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        //media player
        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        //position Bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mp.seekTo(progress);
                    positionBar.setProgress(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //volume Bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNum = progress / 100f;
                mp.setVolume(volumeNum, volumeNum);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //thread (update positionBar & timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }

            }
        }).start();
    }

     private Handler handler = new Handler(){
         @Override
         public void handleMessage(Message msg) {
             int currentPosition = msg.what;
             //update positionBar
             positionBar.setProgress(currentPosition);
             //update label
             String elapsedTime = createTimeLabel(currentPosition);
             elapsedTimeLabel.setText(elapsedTime);

             String remainingTime = createTimeLabel(totalTime-currentPosition);
             remainingTimeLabel.setText("-"+remainingTime);
         }
     };

     public String createTimeLabel(int time){
         String timeLabel="";
         int min=time/1000/60;
         int sec=time/1000%60;


         timeLabel =min + ":";
         if(sec <10) timeLabel +="0";
         timeLabel +=sec;
         return timeLabel;
    }

    public void playBtnClick(View view)
    {
        if(!mp.isPlaying())
        {
            //stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);
        }else
        {
            //playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }
}
