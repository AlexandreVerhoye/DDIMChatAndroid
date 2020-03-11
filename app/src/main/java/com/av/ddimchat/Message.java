package com.av.ddimchat;

public class Message {

    private User user;
    private String message;
    private int typeMessage;

    public Message(User user, String message, int typeMessage) {
        this.user = user;
        this.message = message;
        this.typeMessage = typeMessage;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public int getTypeMessage() {
        return typeMessage;
    }
}
