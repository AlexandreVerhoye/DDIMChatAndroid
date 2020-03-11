package com.av.ddimchat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public interface SocketListener
{
    /**
     * Méthode appelée lorsque le serveur envoie l'événement "connect"
     */
    public void onConnect();
    
    /**
     * Méthode appelée lorsque le serveur envoie l'événement "disconnect"
     */
    public void onDisconnect();
    
    /**
     * Méthode appelée lorsque le serveur envoie l'événement "new_message"
     * @param data  Les données envoyées par le serveur
     */
    public void onNewMessage(JSONObject data);
    
    /**
     * Méthode appelée lorsque le serveur envoie l'événement "new_private_message"
     * @param data  Les données envoyées par le serveur
     */
    public void onNewPrivateMessage(JSONObject data);
    
    /**
     * Méthode appelée lorsque le serveur envoie l'événement "user_list"
     * @param userList  Les données envoyées par le serveur (tableau des utilisateurs)
     */
    public void onUserList(ArrayList<User> userList);
    
    /**
     * Méthode appelée lorsque le serveur envoie l'événement "user_locations" (positions GPS)
     * @param data Les données envoyées par le serveur
     */
    public void onUserLocations(JSONObject data);

    public void onConnectError();
}
