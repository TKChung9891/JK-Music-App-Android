package com.sg.edu.tp.tkmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import static com.sg.edu.tp.tkmusicplayer.MainActivity.Config.songName;

public class DisplayInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_info);

        int savedIndex = getIntent().getIntExtra("transferIndex", 0);

        // Display Title of selected song
        setTitle("Info for Song " + songName[savedIndex]);

        // Display Song Info and Lyrics of selected song
        ImageView iInfoArt = findViewById(R.id.imgInfoArt);
        if (savedIndex == 0) {iInfoArt.setImageDrawable(getResources().getDrawable(R.drawable.info0));}
        if (savedIndex == 1) {iInfoArt.setImageDrawable(getResources().getDrawable(R.drawable.info1));}
        if (savedIndex == 2) {iInfoArt.setImageDrawable(getResources().getDrawable(R.drawable.info2));}
    }
}