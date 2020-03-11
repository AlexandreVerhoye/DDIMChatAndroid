package com.av.ddimchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Login page";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText login_username_value = findViewById(R.id.login_username_value);
        Button login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: login_button clicked.");

              String username_entered = login_username_value.getText().toString();

                if(username_entered.matches("")){
                    Toast.makeText(MainActivity.this, "Le nom d'utilisateur ne doit pas etre vide.", Toast.LENGTH_LONG).show();
                } else {
                    Intent chatIntent = new Intent(MainActivity.this, ChatActivity.class);
                    chatIntent.putExtra("username", username_entered);
                    startActivity(chatIntent);
                }



            }
        });


    }
}
