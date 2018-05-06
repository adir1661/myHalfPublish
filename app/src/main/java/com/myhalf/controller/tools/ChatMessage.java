package com.myhalf.controller.tools;

import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by User on 11/10/2017.
 */

public class ChatMessage {
    private String messageUser;
    private String messageText;
    private long messageTime;
    private String messegeImageUri;

    public ChatMessage(String messageText, String messageUser, long messageTime, @Nullable String messegeImageUri) {
        this.messageUser = messageUser;
        this.messageText = messageText;
        if (messageTime == 0)
            this.messageTime = new Date().getTime();
        else
            this.messageTime = messageTime;
        this.messegeImageUri = messegeImageUri;
    }

    public ChatMessage(String messageText, String messageUser, @Nullable String messegeImageUri) {
        this.messageUser = messageUser;
        this.messageText = messageText;
        this.messageTime = new Date().getTime();
        this.messegeImageUri = messegeImageUri;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessegeImageUri() {
        return messegeImageUri;
    }

    public void setMessegeImageUri(String messegeImageUri) {
        this.messegeImageUri = messegeImageUri;
    }
}
