package com.myhalf.controller;

/**
 * Created by CardUser on 10/10/2017.
 */

public class ChatBubble {
    private String content;
    private boolean myMessage;

    public ChatBubble(String content, boolean myMessage) {
        this.content = content;
        this.myMessage = myMessage;
    }

    public String getContent() {
        return content;
    }

    public boolean myMessage() {
        return myMessage;
    }
}
