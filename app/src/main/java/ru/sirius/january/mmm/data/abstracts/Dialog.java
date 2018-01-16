package ru.sirius.january.mmm.data.abstracts;


import android.support.annotation.NonNull;

import java.net.URI;
import java.util.ArrayList;

public abstract class Dialog implements Comparable<Dialog> {
    public final int uniqueID;
    private ArrayList<Message> messages = null;
    private String name = null;
    private String imageKey = null;
    private Message last = null;
    private int unread = 0;
    public abstract void update();
    public abstract void loadOld();

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public Dialog(int uniqueID, ArrayList<Message> messages, String name, String imageKey, Message last) {
        this.uniqueID = uniqueID;
        this.messages = messages;
        this.name = name;
        this.imageKey = imageKey;
        this.last = last;
    }

    public Message getLast() {
        return last;
    }

    public void setLast(Message last) {
        this.last = last;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    @Override
    public int compareTo(@NonNull Dialog o) {
        return getLast().getDateTime().compareTo(o.getLast().getDateTime());
    }
}
