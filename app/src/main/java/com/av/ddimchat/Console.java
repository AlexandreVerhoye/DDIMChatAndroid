package com.av.ddimchat;

import android.util.Log;

public class Console
{
    public static final String TAG = "DDIM";
    
    public static void log(boolean message)
    {
        Log.i(TAG, String.valueOf(message));
    }
    
    public static void log(int message)
    {
        Log.i(TAG, String.valueOf(message));
    }
    
    public static void log(long message)
    {
        Log.i(TAG, String.valueOf(message));
    }
    
    public static void log(float message)
    {
        Log.i(TAG, String.valueOf(message));
    }
    
    public static void log(double message)
    {
        Log.i(TAG, String.valueOf(message));
    }
    
    public static void log(Object message) { Log.i(TAG, message.toString()); }
}
