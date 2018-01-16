package ru.sirius.january.mmm.data.vk;

import java.util.Date;

import ru.sirius.january.mmm.data.UniqueIDBuilder;
import ru.sirius.january.mmm.data.abstracts.Message;
import ru.sirius.january.mmm.data.abstracts.User;


public final class VKMessage extends Message {
    private int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VKMessage(int id, String text, Date date_time, User owner) {
        super(UniqueIDBuilder.VK(id));
        this.id = id;
        this.setText(text);
        this.setDateTime(date_time);
        this.setOwner(owner);
    }

    @Override
    public void markAsRead() {
        //TODO: mark as read
    }
}
