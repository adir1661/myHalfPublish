package com.myhalf.controller.activities;

import android.widget.ImageView;

import com.myhalf.model.entities.UserSeeker;

import java.sql.Time;
import java.util.Date;

/**
 * Created by CardUser on 10/10/2017.
 */

public class ChatBubble {
    private String content;
    private boolean myMessage;
    private Date messageTime =null;
    UserSeeker sender = null;

    public ChatBubble(String content, boolean myMessage, Date messageTime,UserSeeker userSeeker) {
        this.content = content;
        this.myMessage = myMessage;
        this.messageTime = messageTime;
        sender = userSeeker;
    }

    public String getContent() {
        return content;
    }

    public boolean isMyMessage() {
        return myMessage;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public UserSeeker getSender() {
        return sender;
    }
}
