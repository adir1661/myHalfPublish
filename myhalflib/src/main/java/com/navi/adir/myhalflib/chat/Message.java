package com.navi.adir.myhalflib.chat;


import java.util.Date;


public class Message<T extends HasId> {

    private String id;
    private String text;
    private boolean myMessage;
    private Date messageTime =null;
    private T sender = null;



    public Message(String id_,String content, boolean myMessage, Date messageTime,T userSender) {
        id=id_;
        this.text = content;
        this.myMessage = myMessage;
        this.messageTime = messageTime;
        sender = userSender;
    }

    public boolean isMyMessage() {
        return myMessage;
    }

    public void setMyMessage(boolean myMessage) {
        this.myMessage = myMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public T getSender() {
        return sender;
    }

    public void setSender(T sender) {
        this.sender = sender;
    }
}
