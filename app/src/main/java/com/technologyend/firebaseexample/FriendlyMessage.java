/**
 * Copyright JNM *
 */
package com.technologyend.firebaseexample;

public class FriendlyMessage {

    private String text;
    private String name;
    private String photoUrl;
    private String dateandtime;
    private String dateandtimewithoutss;
    private String sentByUID;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name, String photoUrl, String dateandtime, String dateandtimewithoutss, String sentByUID) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.dateandtime = dateandtime;
        this.dateandtimewithoutss = dateandtimewithoutss;
        this.sentByUID = sentByUID;

    }

    public String getDateandtimewithoutss() {
        return dateandtimewithoutss;
    }

    public void setDateandtimewithoutss(String dateandtimewithoutss) {
        this.dateandtimewithoutss = dateandtimewithoutss;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        this.dateandtime = dateandtime;
    }


    public String getSentByUID() {
        return sentByUID;
    }

    public void setSentByUID(String sentByUID) {
        this.sentByUID = sentByUID;
    }
}
