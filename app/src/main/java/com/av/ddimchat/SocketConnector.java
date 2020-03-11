package com.av.ddimchat;

import android.app.Activity;
import android.location.Location;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.transports.WebSocket;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

public class SocketConnector
{
    private SocketListener listener;
    private Socket socket;
    private Activity activity;
    private String userName;
    
    
    /**
     * Constructeur : appelé lorsqu'on instancie la classe SocketConnector.
     * @param url           L'adresse URL du serveur de socket
     * @param activity      Une référence à l'activité dans laquelle on est (=> this)
     * @param listener      Un objet qui implémente l'interface SocketListener pour récupérer
     *                      les événements du socket (cela peut être l'activité elle-même => this)
     * @example
     * SocketConnector socket = new SocketConnector("http://...", this, this);
     */
    public SocketConnector(String url, Activity activity, SocketListener listener)
    {
        this.activity = activity;
        this.listener = listener;
    
        try
        {
            IO.Options opts = new IO.Options();
            opts.transports = new String[]{WebSocket.NAME};

            socket = IO.socket(url, opts);
            socket.on("connect", onConnect);
            socket.on("disconnect", onDisconnect);
            socket.on("new_message", onNewMessage);
            socket.on("new_private_message", onNewPrivateMessage);
            socket.on("load_users_list", onUserList);
            socket.on("user_locations", onUserLocations);
            socket.on("error", onError);
            socket.on(Manager.EVENT_CONNECT_ERROR, onConnectError);
            socket.connect();
    
            Console.log("SocketConnector : connected");
        }
        catch (URISyntaxException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Méthode à appeler impérativement lorsque l'activité est détruite.
     */
    public void destroy()
    {
        socket.disconnect();
        
        socket.off("connect", onConnect);
        socket.off("disconnect", onDisconnect);
        socket.off("new_message");
        socket.off("new_private_message");
        socket.off("load_users_list");
        socket.off("user_locations");
        socket.off("error");
        
        // Si vous ajoutez d'autres écouteurs d'événements il faut les déréférencer ici...
        
        this.activity = null;
        
        Console.log("SocketConnector : disconnected");
    }
    
    /**
     * Méthode à appeler pour envoyer le nom d'utilisateur au serveur
     * @param name  Le nom de l'utilisateur
     */
    public void setUserName(String name)
    {
        this.userName = name;
        
        socket.emit("user_enter", name);
        
        Console.log("SocketConnector : emit user enter");
    }
    
    /**
     * Méthode à appeler pour envoyer un message au serveur
     * @param message   Le message à envoyer
     */
    public void sendMessage(String message)
    {
        try
        {
            JSONObject data = new JSONObject();
            data.put("name", userName);
            data.put("message", message);
            
            socket.emit("message", message);
            
            Console.log("SocketConnector : emit message " + data.toString());
        }
        catch(JSONException ex){}
    }
    
    public void sendPrivateMessage(String userId, String message)
    {
        try
        {
            JSONObject data = new JSONObject();
            data.put("userId", userId);
            data.put("message", message);
        
            socket.emit("private_message", data);
            
            Console.log("SocketConnector : emit private message " + data.toString());
        }
        catch(JSONException ex){}
    }
    
    /**
     * Méthode à appeler pour envoyer un avatar au serveur
     * @param data  Les données "brutes" compressées en JPG ou PNG
     */
    public void sendAvatar(byte[] data)
    {
        socket.emit("new_avatar", data);
    }
    
    /**
     * Méthode à appeler pour envoyer une localisation au serveur
     * @param location  Un objet Location contenant la position GPS (latitude/longitude/altitude)
     */
    public void sendLocation(Location location)
    {
        try
        {
            JSONObject data = new JSONObject();
            data.put("latitude", location.getLatitude());   // latitude (degrés)
            data.put("longitude", location.getLongitude()); // longitude (degrés)
            data.put("altitude", location.getAltitude());   // altitude (mètres)
            data.put("accuracy", location.getAccuracy());   // précision de la localisation (mètres)

            Console.log("SocketConnector : send location " + data.toString());

            socket.emit("geolocation", data);
        }
        catch(JSONException ex){}
    }
    
    
    /**
     * Pour récupérer l'instance de socket.io, pour pouvoir communiquer directement avec le socket
     * @exemple
     * socket.getSocket().emit("un_evenement", "donnees à envoyer...");
     */
    public Socket getSocket()
    {
        return socket;
    }
    
    
    /**********************************************************************************************/
    /***  ÉCOUTEURS D'ÉVÉNEMENTS  *****************************************************************/
    /**********************************************************************************************/
    
    /**
     * Écouteur d'événement "new_message"
     */
    private Emitter.Listener onNewMessage = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            final JSONObject data = (JSONObject) args[0];

            Console.log("SocketConnector : new message " + data.toString());

            //-- repasse sur le thread UI (user interface)
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    //-- délègue au listener
                    listener.onNewMessage(data);
                }
            });
        }
    };
    
    /**
     * Écouteur d'événement "new_private_message"
     */
    private Emitter.Listener onNewPrivateMessage = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            final JSONObject data = (JSONObject) args[0];

            Console.log("SocketConnector : new private message " + data.toString());

            //-- repasse sur le thread UI (user interface)
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    //-- délègue au listener
                    listener.onNewPrivateMessage(data);
                }
            });
        }
    };
    
    /**
     * Écouteur d'événement "user_list"
     */
    private Emitter.Listener onUserList = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            //-- parse les données
            final JSONObject data = (JSONObject) args[0];

            Console.log("SocketConnector : user list " + data.toString());

            Iterator<String> it = data.keys();

            final ArrayList<User> userList = new ArrayList();

            while (it.hasNext())
            {
                String key = it.next();

                JSONObject user = data.optJSONObject(key);

                userList.add(new User(key, user.optString("name")));
            }

            Console.log(userList);
            
            //-- repasse sur le thread UI (user interface)
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    //-- délègue au listener
                    listener.onUserList(userList);
                }
            });
        }
    };
    
    /**
     * Écouteur d'événement "user_locations"
     */
    private Emitter.Listener onUserLocations = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            //-- parse les données
            final JSONObject data = (JSONObject) args[0];
            
            //-- repasse sur le thread UI (user interface)
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    //-- délègue au listener
                    listener.onUserLocations(data);
                }
            });
        }
    };
    
    /**
     * Écouteur d'événement "connect"
     */
    private Emitter.Listener onConnect = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            //-- repasse sur le thread UI (user interface)
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    //-- délègue au listener
                    listener.onConnect();
                }
            });
        }
    };
    
    /**
     * Écouteur d'événement "disconnect"
     */
    private Emitter.Listener onDisconnect = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            //-- repasse sur le thread UI (user interface)
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    //-- délègue au listener
                    listener.onDisconnect();
                }
            });
        }
    };
    
    /**
     * Écouteur d'événement "connectError"
     */
    private Emitter.Listener onConnectError = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            Console.log("SocketConnector : onConnectError");
            Console.log(args[0]);

            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    //-- délègue au listener
                    listener.onConnectError();
                }
            });
        }
    };
    
    /**
     * Écouteur d'événement "error"
     */
    private Emitter.Listener onError = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            Console.log("SocketConnector : onError");
            Console.log(args[0]);
        }
    };
}
