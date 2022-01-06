package com.sg.edu.tp.tkmusicplayer;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import static com.sg.edu.tp.tkmusicplayer.MainActivity.Config.singer;
import static com.sg.edu.tp.tkmusicplayer.MainActivity.Config.songName;

public class PlaySong extends AppCompatActivity {

    //Declaring various entities
    Button playBtn;
    SeekBar positionBar, volumeBar;
    TextView elapsedTimeLabel, remainingTimeLabel, artistLabel;
    ImageView iCoverArt;
    MediaPlayer mp;
    int totalTime;
    private int transferIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        //Assigning appropriate values/link up to TextViews to entities
        //Receive index for selected song passed on from Main Activity
        int savedIndex = getIntent().getIntExtra("transferIndex", 0);
        transferIndex = savedIndex;

        playBtn = findViewById(R.id.playBtn);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);
        artistLabel = findViewById((R.id.Artist));
        iCoverArt = findViewById(R.id.imgCoverArt);

        // Set Song Title at Play Song Activity, songName is global constant initialised at MainActivity
        // savedIndex is passed on from Main Activity containing index of selected song, 0 to 2
        setTitle(songName[savedIndex]);

        // Set Textview for Artist, singer is global constant initialised at MainActivity
        artistLabel.setText("Artist : "+ singer[savedIndex]);

        // Set Image Cover Art as per index of selected song
        if (savedIndex == 0) {iCoverArt.setImageDrawable(getResources().getDrawable(R.drawable.cover0));}
        if (savedIndex == 1) {iCoverArt.setImageDrawable(getResources().getDrawable(R.drawable.cover1));}
        if (savedIndex == 2) {iCoverArt.setImageDrawable(getResources().getDrawable(R.drawable.cover2));}

        // Initialise Media Player with selected song
        if (savedIndex == 0) {mp = MediaPlayer.create(this, R.raw.music0);}
        if (savedIndex == 1) {mp = MediaPlayer.create(this, R.raw.music1);}
        if (savedIndex == 2) {mp = MediaPlayer.create(this, R.raw.music2);}

        // Other initialisation of media player
        mp.setLooping(true); // media player set to looping as default mode
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f); // left and right volume from 0.0 to 1.0f
        totalTime = mp.getDuration(); // returns duration of song in milliseconds

        // Position Bar implementation with seekBar function
        positionBar = findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //seekBar function implemented through typical 3 override methods
                    //First method tracks how much the user has dragged the positionBar as progress
                    // positionBar has default of 0 to 100, but set to totalTime of song in milliseconds using setMax()
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            // fromUser is true if the user initiates the change in the positionBar
                            // As the positionBar is constantly changing as the song plays,
                            // if this if (fromUser) is not present, the seekTo command is constantly executed,
                            // thus causing significantly drag in the playing of the song.
                            // In comparison, there is no need for this If statement in the volumeBar implementation,
                            // as the volumeBar is always static unless changed by the user
                            mp.seekTo(progress);
                        }
                    }
                    // Remaining two override methods (detects start & stop of user sliding action) typically left blank
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        // Volume Bar implemented through seekBar function
        volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                // Same three typical override methods for volume bar
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f; // progress default value from 0 to 100, need to convert to 0 to 1
                        mp.setVolume(volumeNum, volumeNum); // left and right volume from 0.0 to 1.0f
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        // Thread (Update positionBar & timeLabel as the song is playing)
        // A thread has three parts: start, run some work and terminates
        // The work is performed through a handler which it conveys messages
        // The runnable is continuously performed with delay of 1 second
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition(); //current playback position in milliseconds store to message
                        handler.sendMessage(msg); //transmit message to handler
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar
            positionBar.setProgress(currentPosition);

            // Update Labels
            String elapsedTime = createTimeLabel(currentPosition); // calls createTimeLabel method
            elapsedTimeLabel.setText(elapsedTime); // elasped time is basically current position of song

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText(remainingTime); // remaining time is total song length minus current position

            return true;
        }
    });

    // Converts parameter (in milliseconds) into minutes and seconds and returns this as a string for display
    public String createTimeLabel(int time) {
        String timeLabel;
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        timeLabel = min + ":" + sec;
        return timeLabel;
    }

    public void playBtnClick(View view) {
        if (!mp.isPlaying()) {
            // If media player is not playing then start playing and display stop icon
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);
        } else {
            // If media player is playing then stop playing and display play icon
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }

    // If Loop or See More Info button is clicked
    public void LoopInfoBtn(View v) {
        // Capture Id of the selected song
        String idBtn = v.getResources().getResourceEntryName(v.getId());

        // If Loop button is clicked
        if (idBtn.equals("btnLoop")) {
            TextView myText = (TextView) findViewById(R.id.btnLoop);
            if (mp.isLooping()) {
                // Toggle off looping if currently already looping
                mp.setLooping(false);
                myText.setText("Loop Off");}
            else {
                // Toggle on looping if currently not looping
                mp.setLooping(true);
                myText.setText("Loop On");}
            }
        // If See More info button is clicked, pass on index of song to Display Info Activity
        else {
            Intent myIntent = new Intent(getBaseContext(), DisplayInfo.class);
            myIntent.putExtra("transferIndex", transferIndex);
            startActivity(myIntent);}
        }

        // Stop & Reset Media Player when Android back button is clicked, then pass back to main activity
        // Once back in Main Activity, user is presented with the music library as when app is started
        @Override
        public void onBackPressed() {
            mp.stop();
            mp.reset();
            Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(myIntent);
        }
    }