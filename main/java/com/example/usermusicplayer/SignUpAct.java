package com.example.usermusicplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SignUpAct extends AppCompatActivity {

    private static final String FILE_NAME = "UserInfo.txt";

    Uri imUri;
    private ImageView image;
    EditText username, password, passCheck, name, surname, phone, email;
    Button signUpBtn;
    String u, p, p2, n, sn, ph, em, line;
    boolean exists, same;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up); //create signinactivitylayout

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passCheck = findViewById(R.id.password2);
        name = findViewById(R.id.takeName);
        surname = findViewById(R.id.takeSurname);
        phone = findViewById(R.id.takePhone);
        email = findViewById(R.id.takeEmail);
        image = findViewById(R.id.takeImage);

        signUpBtn = findViewById(R.id.BtnSignUp);   //sign in button


        signUpBtn.setOnClickListener(v -> {

            u = username.getText().toString();
            p = password.getText().toString();
            p2 = passCheck.getText().toString();
            n = name.getText().toString();
            sn = surname.getText().toString();
            ph = phone.getText().toString();
            em = email.getText().toString();

            exists = true;
            same = false;

            if (TextUtils.isEmpty(u)){
                username.setError("Can't be empty");
                exists = false;
            } else username.setError(null);
            if (TextUtils.isEmpty(p)){
                password.setError("Can't be empty");
                exists = false;
            } else password.setError(null);
            if (TextUtils.isEmpty(p2)){
                passCheck.setError("Can't be empty");
                exists = false;
            } else passCheck.setError(null);
            if (TextUtils.isEmpty(n)){
                name.setError("Can't be empty");
                exists = false;
            } else name.setError(null);
            if (TextUtils.isEmpty(sn)){
                surname.setError("Can't be empty");
                exists = false;
            } else surname.setError(null);
            if (TextUtils.isEmpty(ph)){
                phone.setError("Can't be empty");
                exists = false;
            } else phone.setError(null);

            if (TextUtils.isEmpty(em)){
                email.setError("Can't be empty");
                exists = false;
            }
            else if (!em.contains("@")){
                email.setError("Email syntax not valid.");
                exists = false;
            }

            if (imUri == null){
                exists = false;
                Toast.makeText(this, "Please add an image.", Toast.LENGTH_SHORT).show();
            }


            if (!exists){
                Toast.makeText(this, "Please fill all the information correctly.",
                        Toast.LENGTH_SHORT).show();
            }
            else if (!p.equals(p2)){
                Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
                password.setError("Passwords must match.");
                passCheck.setError("Passwords must match.");
                exists = false;
            }
            else{
                try {
                    FileInputStream fis = getApplicationContext().openFileInput(FILE_NAME);
                    InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                    try (BufferedReader in = new BufferedReader(inputStreamReader)) {
                        String line = in.readLine();
                        while (line != null && !exists) {
                            String[] check = line.split(";");
                            line = in.readLine();
                            if (check[0].equals(u)){
                                same = true;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

            if (exists && !same){
                try (FileOutputStream fos = getApplicationContext().openFileOutput(FILE_NAME,
                        MODE_APPEND)) {
                    line =  u + ";" +
                            p + ";" +
                            n + ";" +
                            sn + ";" +
                            ph + ";" +
                            em + "\n";
                    fos.write(line.getBytes(StandardCharsets.UTF_8));

                    String[] adress = {em};
                    composeEmail(adress, "Personal Information",
                            u + "\n" + p + "\n" + n + "\n" + sn + "\n" + ph, imUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        image.setOnClickListener(view -> {
            Intent getIm = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            someActivityResultLauncher.launch(getIm);
        });

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    assert result.getData() != null;
                    imUri = result.getData().getData();
                    image.setImageURI(imUri);
                }
            });

    @SuppressLint("IntentReset")
    public void composeEmail(String[] addresses, String subject, String maintext, Uri image) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, maintext);
        intent.putExtra(Intent.EXTRA_STREAM, image);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}

