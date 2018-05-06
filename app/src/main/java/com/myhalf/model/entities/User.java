package com.myhalf.model.entities;

import android.app.Notification;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String firebaseToken;
    protected String password;
    protected String emailAdress;
    protected String cellPhone;
    protected List<Notification> notifications;
    protected List<String> blockedIds;
    @ServerTimestamp protected Date serverTimestamp;



//-----------------------------------------methods-----------------------------------------------------



//-----------------c-tors--------------------------------
    public User() {
        notifications = new ArrayList<>();
        blockedIds = new ArrayList<>();
    }

    public User( String password, String emailAdress, String cellPhone,
                List<Notification> notifications,
                List<String> blockedIds) {


        this.password = password;
        this.emailAdress = emailAdress;
        this.notifications = new ArrayList<>(notifications);
        this.blockedIds = new ArrayList<>(blockedIds);
    }

    public User(User other) {
        this.id = other.id;
        this.password = other.password;
        this.emailAdress = other.emailAdress;
        this.cellPhone = other.cellPhone;
        this.notifications = new ArrayList<>(other.notifications);
        this.blockedIds = new ArrayList<>(other.blockedIds);
    }

//-----------------Getters & Setters----------------------

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAdress() {
        return emailAdress;
    }
    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public String getCellPhone() {
        return cellPhone;
    }
    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public List<Notification> getNotifications() {
       return notifications;
    }
    public void setNotifications(List<Notification> notifications) {
        this.notifications = new ArrayList<>(notifications);}

    public List<String> getBlockedIds() {
        return new ArrayList<>(blockedIds);
    }
    public void setBlockedIds(List<String> blockedIds) {
        this.blockedIds = new ArrayList<>(blockedIds);}

    public String getFirebaseToken() {
        return firebaseToken;
    }
    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Date getServerTimestamp() {
        return serverTimestamp;
    }
    public void setServerTimestamp(Date serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }

//----------------------------------------implemented methods -------------------------------------------
    public boolean isConfirmed(String email, String password) {
        String thisName = this.getEmailAdress().toString();
        String thisPassword = this.password.toString();
        boolean flagName = (thisName.equals(email)),
                flagPassword = (thisPassword.equals(password));


        if (flagName && flagPassword) return true;
        else return false;
    }
}
