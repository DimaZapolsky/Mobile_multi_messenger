package ru.sirius.january.mmm;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Dialog {

    private ArrayList<Message> messages = null;
    private String name;
    private String imageKey;

    public Dialog(ArrayList<Message> messages, String name, String imageKey) {
        this.messages = messages;
        this.name = name;
        this.imageKey = imageKey;
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

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }
}
