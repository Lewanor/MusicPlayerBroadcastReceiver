package com.example.usermusicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "UserInfo.txt";

    EditText username, password;
    Button button, toSignUp;
    count chance;
    AirplaneModeChangeReceiver airplaneModeChangeReceiver;
    IntentFilter filter;
    Intent launchIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);


        filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);


        chance = new count(3);

        username = findViewById(R.id.takeUsername);
        password = findViewById(R.id.takePassword);
        button = findViewById(R.id.buttonOne);
        toSignUp = findViewById(R.id.toSignUp);
        Context context = this;


        File file = new File(getApplicationContext().getFilesDir(), FILE_NAME);
        if (!file.exists()){
            Toast.makeText(context, "damn it doesnt", Toast.LENGTH_SHORT).show();
            String fileContents = "Tahu;123;asdasd;asdasd;asdasd;asdasd;asdasd\n";
            try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
                fos.write(fileContents.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        button.setOnClickListener(v -> {
            if (chance.getX() > 0) {
                try {
                    FileInputStream fis = context.openFileInput(FILE_NAME);
                    InputStreamReader inputStreamReader =
                            new InputStreamReader(fis, StandardCharsets.UTF_8);
                    try {
                        try (BufferedReader in = new BufferedReader(inputStreamReader)) {
                            String line = in.readLine();
                            while (line != null) {
                                String[] check = line.split(";");
                                line = in.readLine();
                                if (check[0].equals(username.getText().toString())) {
                                    if (check[1].equals(password.getText().toString())) {
                                        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(v.getContext(), ListOfSongs.class);
                                        startActivity(intent);
                                    } else
                                        chance.decX();
                                } else
                                    chance.decX();
                            }
                        }
                    } catch (IOException e) {
                        // Error occurred when opening raw file for reading.
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "No File Found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "To the signup", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), SignUpAct.class);
                startActivity(intent);
            }
        });
        toSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), SignUpAct.class);
            startActivity(intent);
        });



        registerReceiver(airplaneModeChangeReceiver, filter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        chance.setX(2);


    }

    class count {

        private int X;

        count(int X) {
            this.X = X;
        }

        public int getX() {
            return X;
        }

        public void decX() {
            X--;
        }
        public void setX(int Y){
            X = Y;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

}

