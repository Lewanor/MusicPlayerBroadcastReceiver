package com.example.usermusicplayer;

import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListOfSongs extends AppCompatActivity {

    RecyclerView recycle;
    ArrayList<String> songList;
    ArrayList<String> artList;
    ArrayList<String> idList;
    ArrayList<String> durList;
    ArrayList<String> dataList;
    MusicAdapter musicAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_songs);

        recycle = findViewById(R.id.recycleSongs);

        songList = new ArrayList<>();
        artList = new ArrayList<>();
        idList = new ArrayList<>();
        durList = new ArrayList<>();
        dataList = new ArrayList<>();


        String[] proj = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
        };




        Cursor curse = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj,
                null, null, MediaStore.Audio.Media.TITLE);
        if(curse != null){
            if(curse.moveToFirst()){
                do{
                    int titleIdx = curse.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                    int artistIdx = curse.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                    int idIdx = curse.getColumnIndex(MediaStore.Audio.Media._ID);
                    int durIdx = curse.getColumnIndex(MediaStore.Audio.Media.DURATION);
                    int dataIdx = curse.getColumnIndex(MediaStore.Audio.Media.DATA);

                    songList.add(curse.getString(titleIdx));
                    artList.add(curse.getString(artistIdx));
                    idList.add(curse.getString(idIdx));
                    durList.add(curse.getString(durIdx));
                    dataList.add(curse.getString(dataIdx));
                }while(curse.moveToNext());
            }
        }
        assert curse != null;
        curse.close();

        musicAdapt = new MusicAdapter(this, songList,artList,durList,idList,dataList);
        recycle.setAdapter(musicAdapt);
        recycle.setLayoutManager(new LinearLayoutManager(this));
    }



}