package ru.sirius.january.mmm.data.abstracts;


import java.net.URI;
import java.util.Date;

public abstract class User {
    public final int uniqueID;
    private String name = null;
    private String imageKey = null;
    private boolean isOnline = false;
    private Date lastSeen = null;
    public abstract void updateOnline();
    public abstract void updateAll();

    public User(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        this.isOnline = online;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int getUniqueID() {
        return uniqueID;
    }
}
