package com.av.ddimchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements SocketListener {

    private  static final String URL = "dev-indelebil.com:8090";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        try {
            dialog = ProgressDialog.show(this, "","Connexion en cours", true);
            SocketConnector socketConnector = new SocketConnector(URL, this, this);
        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
        }
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onNewMessage(JSONObject data) {

    }

    @Override
    public void onNewPrivateMessage(JSONObject data) {

    }

    @Override
    public void onUserList(ArrayList<User> userList) {

    }

    @Override
    public void onUserLocations(JSONObject data) {

    }

    @Override
    public void onConnectError() {
        dialog.dismiss();
        finish();
        Toast.makeText(ChatActivity.this, "Erreur de connexion", Toast.LENGTH_LONG).show();
    }
}
