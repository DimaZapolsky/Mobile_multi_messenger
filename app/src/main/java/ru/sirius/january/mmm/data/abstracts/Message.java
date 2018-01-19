package ru.sirius.january.mmm.data.abstracts;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public abstract class Message implements Comparable<Message> {
    private final int uniqueID;
    private String text = null;
    private Date dateTime = null;
    private User owner = null;
    private boolean isRead = true;
    protected boolean isMine = false;

    public void setRead(boolean read) {
        isRead = read;
    }

    public Message(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    private ArrayList attachments = new ArrayList();

    public boolean isRead() {
        return isRead;
    }

    public abstract void markAsRead();

    public ArrayList getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList attachments) {
        this.attachments = attachments;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public boolean isMine() {
        return isMine;
    }

    @Override
    public int compareTo(@NonNull Message o) {
        return o.getDateTime().compareTo(getDateTime());
    }
}
