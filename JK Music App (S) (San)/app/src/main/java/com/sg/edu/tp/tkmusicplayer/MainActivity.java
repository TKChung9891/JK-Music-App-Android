package com.sg.edu.tp.tkmusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Initialisation of song data, ie name and singer as global constants for all activities
    // songName and singer are string arrays with 3 values, and index from 0 to 2
    public static class Config {
        public static final String[] songName = {"영원의군주 연", "아라딘 한국어버전","時の流れに身をまかせ"};
        public static final String[] singer = {"김종완 Kim Jong Wan", "John and Lena Park", "テレサテン 邓丽君"};
    }

    public void SelectSong (View v){
        // Capture Id of the selected song
        String idString = v.getResources().getResourceEntryName(v.getId());

        // Assign the selected song number to variable index
        int index = 0;
        if (idString.equals("song0")) {index = 0;}
        if (idString.equals("song1")) {index = 1;}
        if (idString.equals("song2")) {index = 2;}

        // Pass on index of selected song to PlaySong activity
        Intent myIntent = new Intent(getBaseContext(),PlaySong.class);
        myIntent.putExtra("transferIndex", index);
        startActivity(myIntent);
    }
}