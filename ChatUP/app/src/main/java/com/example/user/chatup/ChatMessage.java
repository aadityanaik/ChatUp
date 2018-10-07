package com.example.user.chatup;

public class ChatMessage {
    private String message;
    private long timestamp;
    private boolean user;

    public ChatMessage(String message, long timestamp, boolean user){
        this.message = message;
        this.timestamp = timestamp;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isUser() {
        return user;
    }
}
